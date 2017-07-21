package csm;

import java.awt.Point;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import csm.exceptions.*;
import csm.statetree.*;


/**
 * Worker-Klasse, die eine CSM laedt
 * <p>
 * nur fuer package-internen Gebrauch
 */
class CSMLoader implements FileTagNames {

	// die Wurzel des XML-Baumes, wird vom Konstruktor gesetzt
	private Element rootElement;

	private Element outermostRegion;

	// Hier werden beim ersten Durchlauf des XML-Documents alle Zustände
	// gespeichert.
	private HashMap<Integer, CSMComponent> componentHash;

	// die zu erstellende CSM
	private CoreStateMachine csm;

	static CoreStateMachine loadCSM(Reader reader) throws IOException,
			ErrLoadFailed {
		return new CSMLoader(reader).decodeXML();
	}

	/**
	 * erzeugt einen XML-Baum aus dem angegebenen Reader und trägt ihn
	 * als rootElement ein
	 * <p>
	 * wird nur von loadCSM benutzt, keine öffentlichen Instanzen
	 * erlaubt
	 */
	private CSMLoader(Reader reader) throws ErrLoadFailed, IOException {
		assert reader != null;
		try {
			this.rootElement = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new InputSource(reader))
					.getDocumentElement();
		} catch (final ParserConfigurationException e1) {
			// should never happen
			throw new RuntimeException("internal error");
		} catch (final SAXException e) {
			throw new ErrLoadFailed(-1, new ErrSyntaxError(
					"SAX Exception: " + e.getMessage()));
		}
	}

	/**
	 * erzeugt aus dem XML-Baum rootElement eine CSM, dabei wird der
	 * componentHash erneuert
	 */
	private CoreStateMachine decodeXML() throws ErrLoadFailed {

		// CSM und Hash initialisieren

		this.componentHash = new HashMap<Integer, CSMComponent>();
		this.csm = new CoreStateMachine();
		this.outermostRegion = null;

		// the root's name must be equal TAG_CSM
		if (!FileTagNames.TAG_CSM.equals(this.rootElement.getTagName()))
			CSMLoader.syntaxError(this.rootElement, "root element is not a"
				+ FileTagNames.TAG_CSM);

		final NodeList nodes = this.rootElement.getChildNodes();

		// Events und Variablen suchen
		extractEventsAndVars(nodes);

		// die outermostRegion suchen
		extractOutermostRegionOrphan(this.rootElement, null);

		// Komponentenbaum erzeugen
		extractChildren(this.outermostRegion, this.csm.region);

		// der zweite Durchlauf: Transitionen erzeugen
		extractTransitions(this.rootElement);

		extractGuiMetadata(nodes);

		return this.csm;
	}

	// Fehlerfälle werden noch nicht erschöpfend behandelt.
	private void extractGuiMetadata(NodeList outerNodes)
			throws ErrLoadFailed {
		for (int i = 0; i < outerNodes.getLength(); i++) {
			if (!(outerNodes.item(i) instanceof Element))
				continue;
			final Element outerElement = (Element) outerNodes.item(i);
			final String outerTagName = outerElement.getTagName();
			if (outerTagName.equals(FileTagNames.TAG_GUIMETADATA)) {
				final NodeList nodes = outerElement.getChildNodes();
				final GuiMetadata metadata = this.csm
						.getGuiMetadata(CSMLoader.loadMandatoryAttr(
								outerElement, FileTagNames.ATTR_GUIID));
				for (final int j = 0; i < nodes.getLength(); i++) {
					final Element element = (Element) nodes.item(j);
					final String tagName = element.getTagName();
					if (tagName.equals(FileTagNames.TAG_CDATA)) {
						metadata.data.put(this.componentHash.get(CSMLoader
								.loadUniqueId(element)), CSMLoader
								.loadMandatoryAttr(element,
										FileTagNames.ATTR_VALUE));

					}
				}
			}
		}
	}

	/**
	 * Alle Variablen und Events des Documentes extrahiern,
	 * 
	 * @param nodes
	 * @throws ErrLoadFailed
	 * @throws
	 */
	private void extractEventsAndVars(NodeList nodes) throws ErrLoadFailed {
		for (int i = 0; i < nodes.getLength(); i++) {
			if (!(nodes.item(i) instanceof Element))
				continue;
			final Element element = (Element) nodes.item(i);
			final String tagName = element.getTagName();

			if (tagName.equals(FileTagNames.TAG_EVENT)) {
				try {
					new Event(this.csm.events, CSMLoader.loadMandatoryAttr(
							element, FileTagNames.ATTR_UNIQUENAME));
				} catch (final ErrAlreadyDefinedElement e) {
					throw new ErrLoadFailed(-1,
							"duplicate definition of event " + e.name);
				} catch (ErrSyntaxError e) {
					throw new ErrLoadFailed(-1,
							"syntax error in name of event: "
								+ e.getMessage());
				}
			}

			else if (tagName.equals(FileTagNames.TAG_VARIABLE)) {
				final String name = CSMLoader.loadMandatoryAttr(element,
						FileTagNames.ATTR_UNIQUENAME);
				try {
					final int init = CSMLoader.loadDefaultingIntAttr(
							element, FileTagNames.ATTR_INITIAL, 0);
					final int min = CSMLoader.loadDefaultingIntAttr(
							element, FileTagNames.ATTR_MINIMUM, Math.min(
									init, 0));
					final int max = CSMLoader.loadDefaultingIntAttr(
							element, FileTagNames.ATTR_MAXIMUM, Math.max(
									init, min));
					new Variable(this.csm.variables, name, init, min, max);
				} catch (final ErrAlreadyDefinedElement e) {
					throw new ErrLoadFailed(-1,
							"duplicate definition of variable " + e.name);
				} catch (final ErrValueOutOfBounds e) {
					throw new ErrLoadFailed(-1, e);
				} catch (ErrSyntaxError e) {
					throw new ErrLoadFailed(-1,
							"syntax error in name of variable: "
								+ e.getMessage());
				}

			} else if (!tagName.equals(FileTagNames.TAG_REGION)) {
				// bei sonstigen Tags nichts tun
				assert true;
			}
		}
	}

	/**
	 * durchläuft die Liste aller Subelemente "transition" des Elementes
	 * e und erzeugt dabei jedes mal eine neue Transition
	 * 
	 * @param e die OutermostRegion
	 * @throws ErrSyntaxError
	 */
	private void extractTransitions(Element e) throws ErrLoadFailed {
		// getElementsByTagName durchsucht den gesamten Unterbaum
		final NodeList list = e
				.getElementsByTagName(FileTagNames.TAG_TRANSITION);
		for (int i = 0; i < list.getLength(); i++) {
			final Element elem = (Element) list.item(i);

			final Point location = CSMLoader.loadPosition(elem);
			final State source = loadHashedRef(elem,
					FileTagNames.ATTR_SOURCE);
			final State target = loadHashedRef(elem,
					FileTagNames.ATTR_TARGET);

			// Eine Transition wird erzeugt und vom Konstruktor in den
			// Komponentenbaum eingetragen
			Transition transition;
			try {
				transition = new Transition(location, source, target);
			} catch (final ErrMayNotConnect e1) {
				final int sourceID = CSMLoader.loadMandatoryIntAttr(elem,
						FileTagNames.ATTR_SOURCE);
				final int targetID = CSMLoader.loadMandatoryIntAttr(elem,
						FileTagNames.ATTR_TARGET);
				throw CSMLoader.syntaxError(elem,
						"illegal transition from state #" + sourceID
							+ " to state #" + targetID);
			}
			try {
				transition.setAction(CSMLoader.loadDefaultingAttr(elem,
						FileTagNames.ATTR_ACTION, "skip"));
			} catch (final ErrSyntaxError e1) {
				throw CSMLoader.syntaxError(elem, e1.getMessage());
			} catch (final ErrUndefinedElement e1) {
				throw CSMLoader.syntaxError(elem,
						"action refers to undefined variable " + e1.name);
			}
			try {
				transition.setGuard(CSMLoader.loadDefaultingAttr(elem,
						FileTagNames.ATTR_GUARD, "true"));
			} catch (final ErrSyntaxError e1) {
				throw CSMLoader.syntaxError(elem, e1.getMessage());
			} catch (final ErrUndefinedElement e1) {
				throw CSMLoader.syntaxError(elem,
						"guard refers to undefined variable " + e1.name);
			}
			try {
				transition.setEvent(CSMLoader.loadDefaultingAttr(elem,
						FileTagNames.ATTR_EVENT, null));
			} catch (final ErrUndefinedElement e1) {
				throw CSMLoader.syntaxError(elem, "undefined event "
					+ e1.name);
			}

			transition.setName(CSMLoader.loadDefaultingAttr(elem,
					FileTagNames.ATTR_NAMECOMMENT, null));
			addToComponentHash(elem, transition);
		}
	}

	/**
	 * @param nodes
	 * @return
	 * @throws ErrSyntaxError wenn mehr oder weniger als eine
	 *             OutermostRegion gefunden wird
	 */
	private void extractOutermostRegionOrphan(Element parent,
			CSMComponent unused) throws ErrLoadFailed {

		final NodeList nodes = parent.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (!(nodes.item(i) instanceof Element))
				continue;
			final Element element = (Element) nodes.item(i);
			if (element.getTagName().equals(FileTagNames.TAG_REGION)) {
				if (this.outermostRegion != null)
					throw CSMLoader.syntaxError(element,
							"more than one outermost "
								+ FileTagNames.TAG_REGION);
				this.outermostRegion = element;
				this.csm.region.setName(CSMLoader.loadDefaultingAttr(
						this.outermostRegion,
						FileTagNames.ATTR_NAMECOMMENT, null));
				// was, wenn Position != (0,0)?
				addToComponentHash(this.outermostRegion, this.csm.region);
			}
		}

		if (this.outermostRegion == null)
			throw new ErrLoadFailed(-1, "missing outermost "
				+ FileTagNames.TAG_REGION);
	}

	/**
	 * geht die Liste der Subelemente des Elementes e durch, erzeugt
	 * alle in der Liste vorkommenden Regionen und trägt sie beim
	 * Zustand parent ein. Im componentHash wird dieses SubElement
	 * seiner UniqueId zugeordnet. Für alle SubElemente wird diese
	 * Funktion rekursiv aufgerufen.
	 * 
	 * @param e ist ein CompositeState d.h. e.getTagName()==
	 *            "compositestate"
	 * @param parent der Parent-State
	 * @throws ErrSyntaxError
	 */

	private void extractChildren(Element e, CSMComponent parent)
			throws ErrLoadFailed {
		final NodeList nodes = e.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (!(nodes.item(i) instanceof Element))
				continue;
			final Element elem = (Element) nodes.item(i);
			CSMComponent child;
			try {
				if (elem.getTagName().equals(FileTagNames.TAG_REGION)) {
					child = new SubRegion(CSMLoader.loadPosition(elem));
					parent.dropHere(child);
				} else if (elem.getTagName().equals(
						FileTagNames.TAG_COMPOSITESTATE)) {
					child = new CompositeState(CSMLoader.loadPosition(elem));
					parent.dropHere(child);
					extractDeferredEvents(elem, child);
					extractDoAction(elem, child);
				} else if (elem.getTagName().equals(
						FileTagNames.TAG_EXITSTATE)) {
					child = new ExitState(CSMLoader.loadPosition(elem));
					parent.dropHere(child);
					final String kind = CSMLoader.loadMandatoryAttr(elem,
							FileTagNames.ATTR_KIND);
					((ExitState) child)
							.setKindOfExitstate(ExitState.KindOfExitstate
									.valueOf(kind));
				} else if (elem.getTagName().equals(
						FileTagNames.TAG_CHOICESTATE)) {
					child = new ChoiceState(CSMLoader.loadPosition(elem));
					parent.dropHere(child);
				} else if (elem.getTagName().equals(
						FileTagNames.TAG_FINALSTATE)) {
					child = new FinalState(CSMLoader.loadPosition(elem));
					parent.dropHere(child);
				} else if (elem.getTagName().equals(
						FileTagNames.TAG_ENTRYSTATE)) {
					child = new EntryState(CSMLoader.loadPosition(elem));
					parent.dropHere(child);
				} else
					continue;
				addToComponentHash(elem, child);
				child.setName(CSMLoader.loadDefaultingAttr(elem,
						FileTagNames.ATTR_NAMECOMMENT, null));

			} catch (final ErrTreeNotChanged e1) {
				e1.printStackTrace();

				throw CSMLoader.syntaxError(elem,
						"may not insert element here");
			}
			extractChildren(elem, child);
		}
	}

	/**
	 * @param elem
	 * @param child
	 * @throws ErrLoadFailed
	 */
	private void extractDoAction(final Element elem, CSMComponent child) throws ErrLoadFailed {
		try {
			((CompositeState) child).setDoAction(CSMLoader
					.loadDefaultingAttr(elem,
							FileTagNames.ATTR_ACTION, "skip"));
		} catch (final ErrSyntaxError e1) {
			throw CSMLoader.syntaxError(elem, e1.getMessage());
		} catch (final ErrUndefinedElement e1) {
			throw CSMLoader.syntaxError(elem,
					"do-action refers to undefined element "
						+ e1.name);
		}
	}

	/**
	 * @param elem
	 * @param child
	 * @throws ErrLoadFailed
	 */
	private void extractDeferredEvents(final Element elem, CSMComponent child) throws ErrLoadFailed {
		try { // deferredEvents laden
			final NodeList defevlist = elem.getChildNodes();
			final LinkedList<String> events = new LinkedList<String>();
			for (int j = 0; j < defevlist.getLength(); j++) {
				if (!(defevlist.item(j) instanceof Element))
					continue;
				final Element evelem = (Element) defevlist
						.item(j);
				if (evelem.getTagName().equals(
						FileTagNames.TAG_DEFERREDEVENT))
					events.add(CSMLoader.loadMandatoryAttr(
							evelem,
							FileTagNames.ATTR_UNIQUENAME));
			}
			((CompositeState) child)
					.setDeferredEventNames(events);
		} catch (final ErrUndefinedElement e2) {
			throw CSMLoader.syntaxError(elem,
					"list of deferred events refers to undefined event "
						+ e2.name);
		}
	}

	private void addToComponentHash(Element element, CSMComponent component)
			throws ErrLoadFailed {
		final int id = CSMLoader.loadUniqueId(element);
		if (this.componentHash.containsKey(id))
			throw CSMLoader.syntaxError(element, " id " + id
				+ " is not unique");
		this.componentHash.put(id, component);
	}

	/**
	 * Liest die Referenz attr des Elementes elem und sucht im
	 * componentHash nach dem zugehörigen State.
	 * 
	 * @throws ErrSyntaxError wenn die Referenz nicht gelesen werden
	 *             konnte, oder wenn sie auf etwas anderes als einen
	 *             State verweist
	 */
	private State loadHashedRef(Element elem, String attr)
			throws ErrLoadFailed {
		final int targetID = CSMLoader.loadMandatoryIntAttr(elem, attr);
		if (!this.componentHash.containsKey(targetID))
			CSMLoader.syntaxError(elem, "referred component " + attr + '='
				+ targetID + " does not exist");
		final CSMComponent target = this.componentHash.get(targetID);
		if (target instanceof State)
			return (State) target;
		throw CSMLoader.syntaxError(elem,
				"component referred by attribute " + attr + '=' + targetID
					+ " is not a state");
	}

	private static Point loadPosition(Element n) throws ErrLoadFailed {
		return new Point(CSMLoader.loadDefaultingIntAttr(n,
				FileTagNames.ATTR_X, 0), CSMLoader.loadDefaultingIntAttr(n,
				FileTagNames.ATTR_Y, 0));
	}

	private static int loadDefaultingIntAttr(Element element, String attr,
			int defaultValue) throws ErrLoadFailed {
		if (!element.hasAttribute(attr))
			return defaultValue;
		final String attrValue = element.getAttribute(attr);
		try {
			return Integer.parseInt(attrValue);
		} catch (final NumberFormatException e) {
			throw CSMLoader.syntaxError(element,
					"NumberFormatException reading " + attr + "=\""
						+ attrValue + "\" (" + e.getMessage() + ')');
		}
	}

	private static int loadMandatoryIntAttr(Element element, String attr)
			throws ErrLoadFailed {
		final String attrValue = CSMLoader.loadMandatoryAttr(element, attr);
		try {
			return Integer.parseInt(attrValue);
		} catch (final NumberFormatException e) {
			throw CSMLoader.syntaxError(element,
					"NumberFormatException reading " + attr + "=\""
						+ attrValue + "\" (" + e.getMessage() + ')');
		}
	}

	/**
	 * @param element
	 * @param attr
	 * @param defaultValue
	 * @return
	 * @throws ErrSyntaxError
	 */
	private static String loadDefaultingAttr(Element element, String attr,
			String defaultValue) {
		if (!element.hasAttribute(attr))
			return defaultValue;
		return getEscapedAttribute(element, attr);
	}

	/**
	 * @param element
	 * @param attr
	 * @return
	 * @throws ErrSyntaxError
	 */
	private static String loadMandatoryAttr(Element element, String attr)
			throws ErrLoadFailed {
		if (!element.hasAttribute(attr))
			throw CSMLoader.syntaxError(element, "missing attribute "
				+ attr);
		return getEscapedAttribute(element, attr);
	}

	/**
	 * Erzeugt Syntax-Error-Message
	 * <p>
	 * Voraussetzung: loadUniqueId(elem) ist aufrufbar
	 * 
	 * @param das Element, dessen uniqueID fehlerfrei auslesbar ist
	 * @param msg
	 * @throws ErrSyntaxError
	 */
	private static ErrLoadFailed syntaxError(Element elem, String msg) {
		try {
			return new ErrLoadFailed(CSMLoader.loadUniqueId(elem), msg);
		} catch (final ErrLoadFailed e) {
			return e;
		}
	}

	/*
	 * muss alle Exceptions selbst erzeugen, weil syntaxError selbst
	 * versucht, die uniqueId auszulesen
	 */
	private static int loadUniqueId(Element element) throws ErrLoadFailed {
		if (!element.hasAttribute(FileTagNames.ATTR_UNIQUE_ID))
			throw new ErrLoadFailed(-1, "missing attribute "
				+ FileTagNames.ATTR_UNIQUE_ID + " in tag "
				+ element.getTagName());
		try {
			return Integer.parseInt(element
					.getAttribute(FileTagNames.ATTR_UNIQUE_ID));
		} catch (final NumberFormatException e) {
			throw new ErrLoadFailed(-1, "NumberFormatException in tag "
				+ element.getTagName() + " parsing attribute "
				+ FileTagNames.ATTR_UNIQUE_ID + ": " + e.getMessage());
		}
	}

	/**
	 * @param element
	 * @param attr
	 * @return
	 */
	private static String getEscapedAttribute(Element element, String attr) {
		String value = element.getAttribute(attr);
		value = value.replaceAll("&quot;", "\"");
		value = value.replaceAll("&amp;", "&");
		return value;
	}

}

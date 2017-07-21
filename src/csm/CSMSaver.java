/**
 * 
 */
package csm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;

import csm.statetree.CSMComponent;
import csm.statetree.ChoiceState;
import csm.statetree.CompositeState;
import csm.statetree.EntryState;
import csm.statetree.ExitState;
import csm.statetree.FinalState;
import csm.statetree.Region;
import csm.statetree.Transition;
import csm.statetree.Visitor;


/**
 * Worker-Klasse, die einen StringBuilder mit der XML-Darstellung eines
 * Komponenten-Baums fuellt.
 * <p>
 * (Nur fuer package-internen Gebrauch.)
 * <p>
 * (nebenbei ist CSMSaver ein Beispiel dafuer, wie man die
 * CSMVisitor-Klasse verwendet)
 * 
 * @author hsi
 */
class CSMSaver extends Visitor implements FileTagNames {

	private BufferedWriter bwr;
	private PrintWriter pwr;

	public static void saveCSM(Writer writer, CoreStateMachine csm)
			throws IOException {
		assert writer != null;
		assert csm != null;
		new CSMSaver().printCSM(writer, csm);
	}

	private CSMSaver() {
		// der Konstruktor soll nur verhindern, dassoeffentliche
		// Instanzen erzeugt werden
	}

	private void printCSM(Writer writer, CoreStateMachine csm)
			throws IOException {
		assert writer != null;
		assert csm != null;

		this.bwr = new BufferedWriter(writer);
		this.pwr = new PrintWriter(this.bwr);
		printXMLHeader();
		csm.region.enumerateStates();
		printComponent(csm.region, FileTagNames.TAG_REGION);
		printEvents(csm.events);
		printVariables(csm.variables);
		for (final GuiMetadata m : csm.guiMetadata.values())
			printGuiMetadata(m);
		printXMLFooter();
		this.pwr.close();
		this.bwr.close();
	}

	void printXMLHeader() {
		tag("?xml version =\"1.0\"?");
		tag(FileTagNames.TAG_CSM);
	}

	void printXMLFooter() {
		closeTag(FileTagNames.TAG_CSM);
	}

	/**
	 * @param events das Dictionary, das die auszugebenden Events
	 *            enthaelt
	 */
	private void printEvents(Dictionary<Event> events) {
		final Collection<Event> eventlist = events.getContents();
		for (final Event e : eventlist)
			singleTag(FileTagNames.TAG_EVENT, CSMSaver.attr(
					FileTagNames.ATTR_UNIQUENAME, e.getName()));
	}

	/**
	 * @param variables das Dictionary, das die auszugebenden Variablen
	 *            enthaelt
	 */
	private void printVariables(Dictionary<Variable> variables) {
		final Collection<Variable> varlist = variables.getContents();
		for (final Variable v : varlist) {
			String attrs = CSMSaver.attr(FileTagNames.ATTR_UNIQUENAME, v
					.getName());
			attrs += CSMSaver.attr(FileTagNames.ATTR_INITIAL, v
					.getInitialValue());
			attrs += CSMSaver.attr(FileTagNames.ATTR_MINIMUM, v
					.getMinValue());
			attrs += CSMSaver.attr(FileTagNames.ATTR_MAXIMUM, v
					.getMaxValue());
			singleTag(FileTagNames.TAG_VARIABLE, attrs);
		}
	}

	private void printGuiMetadata(GuiMetadata m) {
		tag(FileTagNames.TAG_GUIMETADATA
			+ CSMSaver.attr(FileTagNames.ATTR_GUIID, m.guiId));
		for (final CSMComponent c : m.data.keySet()) {
			String attr = CSMSaver.attr(FileTagNames.ATTR_UNIQUE_ID, c
					.getUniqueId());
			attr += CSMSaver.attr(FileTagNames.ATTR_VALUE, m.data.get(c));
			singleTag(FileTagNames.TAG_CDATA, attr);
		}
		closeTag(FileTagNames.TAG_GUIMETADATA);
	}

	// Hilfsfunktionen

	private static String attr(String name, String value) {
		if (value == null)
			return "";
		String escapedValue = value.replaceAll("&", "&amp;");
		escapedValue = escapedValue.replaceAll("\"", "&quot;");
		return ' ' + name + "=\"" + escapedValue + "\"";
	}

	private static String attr(String name, int value) {
		return CSMSaver.attr(name, String.valueOf(value));
	}

	private void tag(String contents) {
		this.pwr.print('<');
		this.pwr.print(contents);
		this.pwr.println('>');
	}

	private void openTag(String tag, CSMComponent component, String attr) {
		String attrs = attr;
		attrs += CSMSaver.attr(FileTagNames.ATTR_UNIQUE_ID, component
				.getUniqueId());
		attrs += CSMSaver.attr(FileTagNames.ATTR_X,
				component.getPosition().x);
		attrs += CSMSaver.attr(FileTagNames.ATTR_Y,
				component.getPosition().y);
		attrs += CSMSaver.attr(FileTagNames.ATTR_NAMECOMMENT, component
				.getName());
		tag(tag + attrs);
	}

	private void closeTag(String tag) {
		tag('/' + tag);
	}

	private void singleTag(String tag, String attr) {
		tag(tag + attr + '/');
	}

	private void printComponent(CSMComponent component, String tag,
			String attributes) {
		openTag(tag, component, attributes);
		visitChildren(component);
		closeTag(tag);
	}

	private void printComponent(CSMComponent component, String tag) {
		printComponent(component, tag, "");
	}

	@Override
	final protected void visitTransition(Transition transition) {
		String attrs = CSMSaver.attr(FileTagNames.ATTR_SOURCE, transition
				.getSource().getUniqueId());
		attrs += CSMSaver.attr(FileTagNames.ATTR_TARGET, transition
				.getTarget().getUniqueId());
		if (transition.hasEvent())
			attrs += CSMSaver.attr(FileTagNames.ATTR_EVENT, transition
					.getEventName());
		attrs += CSMSaver.attr(FileTagNames.ATTR_GUARD, transition
				.getGuard().prettyprint());
		attrs += CSMSaver.attr(FileTagNames.ATTR_ACTION, transition
				.getAction().prettyprint());
		printComponent(transition, FileTagNames.TAG_TRANSITION, attrs);
	}

	@Override
	final protected void visitRegion(Region region) {
		printComponent(region, FileTagNames.TAG_REGION);
	}

	@Override
	final protected void visitEntryState(EntryState state) {
		printComponent(state, FileTagNames.TAG_ENTRYSTATE);
	}

	@Override
	final protected void visitExitState(ExitState state) {
		final String attrs = CSMSaver.attr(FileTagNames.ATTR_KIND, state
				.getKindOfExitstate().toString());
		printComponent(state, FileTagNames.TAG_EXITSTATE, attrs);
	}

	@Override
	final protected void visitCompositeState(CompositeState state) {
		final String attrs = CSMSaver.attr(FileTagNames.ATTR_ACTION, state
				.getDoAction().prettyprint());
		openTag(FileTagNames.TAG_COMPOSITESTATE, state, attrs);
		visitChildren(state);
		for (final String d : state.getDeferredEventNames()) {
			singleTag(FileTagNames.TAG_DEFERREDEVENT, CSMSaver.attr(
					FileTagNames.ATTR_UNIQUENAME, d));
		}
		closeTag(FileTagNames.TAG_COMPOSITESTATE);
	}

	@Override
	final protected void visitFinalState(FinalState state) {
		printComponent(state, FileTagNames.TAG_FINALSTATE);
	}

	@Override
	final protected void visitChoiceState(ChoiceState state) {
		printComponent(state, FileTagNames.TAG_CHOICESTATE);
	}
}

/**
 * 
 */
package csm;

import java.awt.Point;

import csm.statetree.CSMComponent;
import csm.statetree.CSMVisitor;
import csm.statetree.ChoiceState;
import csm.statetree.CompositeState;
import csm.statetree.EntryState;
import csm.statetree.ExitState;
import csm.statetree.FinalState;
import csm.statetree.OutermostRegion;
import csm.statetree.Region;
import csm.statetree.State;


/**
 * Worker-Klasse, die einen StringBuilder mit der XML-Darstellung eines
 * Komponenten-Baums füllt.
 * <p>
 * (und nebenbei ist sie ein Beispiel dafür, wie man die CSMVisitor-Klasse
 * verwendet)
 * 
 * @author hs
 */
public class StatetreeSaver extends CSMVisitor {

	private static final String TAG_POSITION = "position";
	private static final String ATTR_X = "x";
	private static final String ATTR_Y = "y";

	private static final String ATTR_KIND = "kind";
	private static final String ATTR_UNIQUE_ID = "uniqueId";

	private static final String TAG_OUTERMOSTREGION = "outermostregion";
	private static final String TAG_REGION = "region";
	private static final String TAG_ENTRYSTATE = "entrystate";
	private static final String TAG_EXITSTATE = "exitstate";
	private static final String TAG_COMPOSITESTATE = "compositestate";
	private static final String TAG_FINALSTATE = "finalstate";
	private static final String TAG_CHOICESTATE = "choicestate";

	private StringBuilder xml;

	/**
	 * es dürfen keine öffentlichen Instanzen erzeugt werden
	 */
	private StatetreeSaver(StringBuilder xml) {
		this.xml = xml;
	}

	public static void statetreeToXML(StringBuilder xmlstring,
			OutermostRegion outermostRegion) {
		outermostRegion.enumerateStates();
		final StatetreeSaver saver = new StatetreeSaver(xmlstring);
		saver.printComponent(outermostRegion,
				StatetreeSaver.TAG_OUTERMOSTREGION);
	}

	// Hilfsfunktionen

	private static String attr(String name, String value) {
		return ' ' + name + "=\"" + value + "\"";
	}

	private static String attr(String name, int value) {
		return StatetreeSaver.attr(name, String.valueOf(value));
	}

	private void tag(String contents) {
		this.xml.append('<');
		this.xml.append(contents);
		this.xml.append(">\n");
	}

	private void openTag(String tag, String attr) {
		tag(tag + attr);
	}

	void closeTag(String tag) {
		tag('/' + tag);
	}

	private void singleTag(String tag, String attr) {
		tag(tag + attr + '/');
	}

	void positionElement(CSMComponent component) {
		final Point p = component.getPosition();
		final String attrs =
				StatetreeSaver.attr(StatetreeSaver.ATTR_X, p.x)
						+ StatetreeSaver.attr(StatetreeSaver.ATTR_Y,
								p.y);
		singleTag(StatetreeSaver.TAG_POSITION, attrs);
	}

	private void printComponent(CSMComponent component, String tag,
			String attributes) {
		openTag(tag, attributes);
		positionElement(component);
		visitChildren(component);
		closeTag(tag);
	}

	private void printComponent(CSMComponent component, String tag) {
		printComponent(component, tag, "");
	}

	private void printState(State state, String tag) {
		final String attrs =
				StatetreeSaver.attr(StatetreeSaver.ATTR_UNIQUE_ID,
						state.getUniqueId());
		printComponent(state, tag, attrs);
	}

	// visitor pattern ****************************

	@Override
	final public void visitRegion(Region region) {
		printComponent(region, StatetreeSaver.TAG_REGION);
	}

	@Override
	final public void visitEntryState(EntryState state) {
		printState(state, StatetreeSaver.TAG_ENTRYSTATE);
	}

	@Override
	final public void visitExitState(ExitState state) {
		final String attrs =
				StatetreeSaver.attr(StatetreeSaver.ATTR_UNIQUE_ID,
						state.getUniqueId())
						+ StatetreeSaver.attr(StatetreeSaver.ATTR_KIND,
								state.kindOf.toString());
		printComponent(state, StatetreeSaver.TAG_EXITSTATE, attrs);
	}

	@Override
	final public void visitCompositeState(CompositeState state) {
		printState(state, StatetreeSaver.TAG_COMPOSITESTATE);
	}

	@Override
	final public void visitFinalState(FinalState state) {
		printState(state, StatetreeSaver.TAG_FINALSTATE);
	}

	@Override
	final public void visitChoiceState(ChoiceState state) {
		printState(state, StatetreeSaver.TAG_CHOICESTATE);
	}
}

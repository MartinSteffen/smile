/**
 * 
 */
package csm;

import java.awt.Point;
import java.io.Writer;

import csm.statetree.CSMComponent;
import csm.statetree.ChoiceState;
import csm.statetree.CompositeState;
import csm.statetree.EntryState;
import csm.statetree.ExitState;
import csm.statetree.FinalState;
import csm.statetree.OutermostRegion;
import csm.statetree.Region;
import csm.statetree.State;
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

	private StringBuilder xml;

	public static void saveCSM(Writer writer, CoreStateMachine csm) {
		// TODO implementieren
	}

	/**
	 * es duerfen keine oeffentlichen Instanzen erzeugt werden
	 */
	private CSMSaver(StringBuilder xml) {
		this.xml = xml;
	}

	private static void statetreeToXML(StringBuilder xmlstring,
			OutermostRegion outermostRegion) {
		outermostRegion.enumerateStates();
		final CSMSaver saver = new CSMSaver(xmlstring);
		saver.printComponent(outermostRegion,
				FileTagNames.TAG_OUTERMOSTREGION);
	}

	// Hilfsfunktionen

	private static String attr(String name, String value) {
		return ' ' + name + "=\"" + value + "\"";
	}

	private static String attr(String name, int value) {
		return CSMSaver.attr(name, String.valueOf(value));
	}

	private void tag(String contents) {
		this.xml.append('<');
		this.xml.append(contents);
		this.xml.append(">\n");
	}

	private void openTag(String tag, String attr) {
		tag(tag + attr);
	}

	private void closeTag(String tag) {
		tag('/' + tag);
	}

	private void singleTag(String tag, String attr) {
		tag(tag + attr + '/');
	}

	private void positionElement(CSMComponent component) {
		final Point p = component.getPosition();
		final String attrs = CSMSaver.attr(FileTagNames.ATTR_X, p.x)
				+ CSMSaver.attr(FileTagNames.ATTR_Y, p.y);
		singleTag(FileTagNames.TAG_POSITION, attrs);
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
		final String attrs = CSMSaver.attr(FileTagNames.ATTR_UNIQUE_ID,
				state.getUniqueId());
		printComponent(state, tag, attrs);
	}

	// visitor pattern ****************************
	// TODO visitTransition
	@Override
	final protected void visitRegion(Region region) {
		printComponent(region, FileTagNames.TAG_REGION);
	}

	@Override
	final protected void visitEntryState(EntryState state) {
		printState(state, FileTagNames.TAG_ENTRYSTATE);
	}

	@Override
	final protected void visitExitState(ExitState state) {
		final String attrs = CSMSaver.attr(FileTagNames.ATTR_UNIQUE_ID,
				state.getUniqueId())
				+ CSMSaver.attr(FileTagNames.ATTR_KIND, state
						.getKindOfExitstate().toString());
		printComponent(state, FileTagNames.TAG_EXITSTATE, attrs);
	}

	@Override
	final protected void visitCompositeState(CompositeState state) {
		printState(state, FileTagNames.TAG_COMPOSITESTATE);
	}

	@Override
	final protected void visitFinalState(FinalState state) {
		printState(state, FileTagNames.TAG_FINALSTATE);
	}

	@Override
	final protected void visitChoiceState(ChoiceState state) {
		printState(state, FileTagNames.TAG_CHOICESTATE);
	}
}

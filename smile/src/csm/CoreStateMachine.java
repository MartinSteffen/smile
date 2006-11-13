/**
 * 
 */
package csm;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Observable;

import csm.statetree.OutermostRegion;


/**
 * @author hs
 */
public final class CoreStateMachine extends Observable {

	/**
	 * die Komponenten, Regionen und Transitionen der CSM als
	 * Baumstruktur
	 */
	public final OutermostRegion region = new OutermostRegion(this);

	/**
	 * eine Liste aller Events, die von der CSM verwendet werden
	 */
	public final Dictionary<Event> events = new Dictionary<Event>();

	/**
	 * die Liste aller Variablen, die von der CSM verwendet werden
	 */
	public final Dictionary<Variable> variables = new Dictionary<Variable>();

	public CoreStateMachine() {
	}

	/**
	 * eine CSM im XML-Format laden
	 * 
	 * @throws IOException auftretende IO-Exceptions werden nicht
	 *             behandelt, sondern an den Aufrufer weitergegeben
	 * @throws csm.exceptions.CSMEditException wenn die Datei eine fehlerhafte Maschine
	 *             beschreibt
	 */
	public final CoreStateMachine loadCSM(Reader reader) {
		// TODO zu CSMLoader delegieren
		return null;
	}

	/**
	 * die CSM im XML-Format speichern
	 * 
	 * @throws IOException auftretende IO-Exceptions werden nicht
	 *             behandelt, sondern an den Aufrufer weitergegeben
	 */
	public void saveCSM(Writer writer) {
		// TODO zu CSMSaver delegieren
	}
}

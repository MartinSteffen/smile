package csm;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import csm.exceptions.ErrLoadFailed;
import csm.statetree.OutermostRegion;


/**
 * die CoreStateMachine
 * 
 * @author hs
 */
public final class CoreStateMachine extends ModelNode<CoreStateMachine> {

	/**
	 * die Komponenten, Regionen und Transitionen der CSM als
	 * Baumstruktur
	 */
	public final OutermostRegion region;

	/**
	 * eine Liste aller Events, die von der CSM verwendet werden
	 */
	public final Dictionary<Event> events;

	/**
	 * die Liste aller Variablen, die von der CSM verwendet werden
	 */
	public final Dictionary<Variable> variables;

	/**
	 * die Metadaten fuer die verschiedenen GUIs
	 */
	final Map<String, GuiMetadata> guiMetadata;

	public CoreStateMachine() {
		this.region = new OutermostRegion(this);
		this.events = new Dictionary<Event>(this);
		this.variables = new Dictionary<Variable>(this);
		this.guiMetadata = new HashMap<String, GuiMetadata>();
	}

	@Override
	public final CoreStateMachine getCSM() {
		return this;
	}

	/**
	 * eine CSM im XML-Format laden
	 * 
	 * @throws IOException auftretende IO-Exceptions werden nicht
	 *             behandelt, sondern an den Aufrufer weitergegeben
	 * @throws csm.exceptions.CSMEditException wenn die Datei eine
	 *             fehlerhafte Maschine beschreibt
	 */
	public final static CoreStateMachine loadCSM(Reader reader)
			throws ErrLoadFailed, IOException {
		return CSMLoader.loadCSM(reader);
	}

	/**
	 * die CSM im XML-Format speichern
	 * 
	 * @throws IOException
	 * @throws IOException auftretende IO-Exceptions werden nicht
	 *             behandelt, sondern an den Aufrufer weitergegeben
	 */
	public void saveCSM(Writer writer) throws IOException {
		CSMSaver.saveCSM(writer, this);
	}

	/**
	 * gibt die Metadaten einer gegeben GUI zurueck (oder null, wenn
	 * fuer diese GUI-Id keine Metadaten existieren)
	 */
	public final GuiMetadata getGuiMetadata(String guiId) {
		assert guiId != null;
		return this.guiMetadata.get(guiId);
	}

	/**
	 * setzt die Metadaten fuer eine GUI
	 */
	public final void setGuiMetadata(GuiMetadata guiData) {
		assert guiData != null;
		this.guiMetadata.put(guiData.guiId, guiData);
	}

}

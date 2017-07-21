package nua;

/**
 * Ein State eines \nu-Automaten
 */
public class NuState {

	final public NuAutomaton nua;

	/**
	 * Erzeugt einen neuen State und traegt ihn im \nu-Automaten ein.
	 */
	public NuState(NuAutomaton nua) {
		this.nua = nua;
	}

	/**
	 * Erzeugt einen neuen State und traegt ihn im \nu-Automaten ein.
	 * Ist das Argument isRootState true, dann wird er auch in der Liste
	 * der Rootstates des \nu-Automaten eingetragen.
	 * 
	 * @param nua der \nu-Automat, von dem dieser State ein Teil ist
	 * @param isRootState gibt an, ob der State als Root-State im
	 *            \nu-Automaten eingetragen werden soll.
	 */
	public NuState(NuAutomaton nua, boolean isRootState) {
		this(nua);
		if (isRootState)
			nua.rootStates.add(this);
	}
}

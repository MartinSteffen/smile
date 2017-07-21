package nua;

import java.util.HashSet;
import java.util.Set;


/**
 * Ein State eines \nu-Automaten
 */
public class NuState {

	final public NuAutomaton nua;

	final Set<NuTransition> transitions;

	public final boolean isRootState;

	/**
	 * kann bei der Erzeugung des Nu-Automaten gesetzt werden
	 */
	final public String debugInfo;

	int uniqueId;

	/**
	 * graphentheoretisches Tag für den einen oder anderen Algorithmus
	 */
	int tag;

	/**
	 * Erzeugt einen neuen State und traegt ihn im \nu-Automaten ein.
	 */
	public NuState(NuAutomaton nua, String debugInfo) {
		this(nua, false, debugInfo);
	}

	/**
	 * Erzeugt einen neuen State und traegt ihn im \nu-Automaten ein.
	 */
	public NuState(NuAutomaton nua) {
		this(nua, false, "");
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
		this(nua, isRootState, "");
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
	public NuState(NuAutomaton nua, boolean isRootState, String debugInfo) {
		this.nua = nua;
		this.isRootState = isRootState;
		this.transitions = new HashSet<NuTransition>();
		this.debugInfo = debugInfo;
		nua.registerState(this);
		if (isRootState)
			nua.rootStates.add(this);
	}

	/**
	 * Liefert die Menge der aus diesem State herausführenden
	 * Transitionen. Auf dieser Methode basiert das Modelchecking. Daher
	 * muss sie effizient implementiert sein.
	 * 
	 * @return Menge der Transitionen, deren Source-State gleich diesem
	 *         State ist
	 */
	public Set<NuTransition> outgoingTransitions() {
		return this.transitions;
	}

	void registerOutgoingTransition(NuTransition t) {
		this.transitions.add(t);
	}

	/**
	 * checkt, ob 1. alle von diesem State ausgehenden Transitionen
	 * unterschiedliche Labels haben und ob es 2. zu jedem Labels aus
	 * expected eine von diesem State ausgehende Transition gibt
	 */
	boolean isConcreteState(Set<String> expected) {
		Set<String> labels = new HashSet<String>();
		for (NuTransition t : this.transitions) {
			if (labels.contains(t.action))
				return false;
			labels.add(t.action);
		}
		return labels.containsAll(expected);
	}
}

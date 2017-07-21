package nua;

import java.util.Set;


/**
 * eine Transition des \nu-Automaten
 */
public class NuTransition {

	final public NuAutomaton nua;

	public final NuState source;

	public final String action;

	public final Set<NuState> targets;

	/**
	 * kann bei der Erzeugung des Nu-Automaten gesetzt werden
	 */
	public final String debugInfo;

	int uniqueId;

	/**
	 * erzeugt eine neue Transition und traegt sie im \nu-Automaten ein
	 * 
	 * @param targets ein Set, das die Targets dieser Transition
	 *            enthält. Damit NuAutomaton#reduceToIllegalPaths
	 *            funktioniert, darf dieses Set nur beir jeweils einer
	 *            Transition eingetragen werden.
	 */
	public NuTransition(NuAutomaton nua, NuState source, String action,
			Set<NuState> targets) {
		this(nua, source, action, targets, null);
	}

	/**
	 * erzeugt eine neue Transition und traegt sie im \nu-Automaten ein
	 * 
	 * @param targets ein Set, das die Targets dieser Transition
	 *            enthält. Damit NuAutomaton#reduceToIllegalPaths
	 *            funktioniert, darf dieses Set nur beir jeweils einer
	 *            Transition eingetragen werden.
	 */
	public NuTransition(NuAutomaton nua, NuState source, String action,
			Set<NuState> targets, String debugInfo) {
		assert source.nua == nua;
		assert NuTransition.allMembersArePartOf(targets, nua);

		this.nua = nua;
		this.source = source;
		this.action = action;
		this.targets = targets;
		this.debugInfo = debugInfo;
		this.nua.registerTransition(this);
	}

	/**
	 * testet, ob alle States, die in der Menge members enthalten sind,
	 * Elemente des \nu-Automaten nua sind
	 */
	static boolean allMembersArePartOf(Set<NuState> members, NuAutomaton nua) {
		for (final NuState member : members)
			if (member.nua != nua)
				return false;
		return true;
	}
}

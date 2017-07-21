package nua;

import java.util.Set;

import csm.Event;


/**
 * eine Transition des \nu-Automaten
 */
public class NuTransition {

	final public NuAutomaton nua;

	public final NuState source;

	public final Event action;

	public final Set<NuState> targets;

	/**
	 * erzeugt eine neue Transition und traegt sie im \nu-Automaten ein
	 */
	public NuTransition(NuAutomaton nua, NuState source, Event action,
			Set<NuState> targets) {
		assert source.nua == nua;
		assert allMembersArePartOf(targets, nua);

		this.nua = nua;
		this.source = source;
		this.action = action;
		this.targets = targets;

		this.nua.transitions.add(this);
	}

	/**
	 * testet, ob alle States, die in der Liste list enthalten sind,
	 * Elemente des \nu-Automaten nua sind
	 */
	boolean allMembersArePartOf(Set<NuState> list, NuAutomaton nua) {
		for (final NuState member : list)
			if (member.nua != nua)
				return false;
		return true;
	}
}

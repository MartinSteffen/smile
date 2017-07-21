package semantics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import csm.CoreStateMachine;
import csm.ExpressionEnvironment;
import csm.expression.Action;
import csm.expression.DoSkip;
import csm.statetree.CompositeState;
import csm.statetree.InternalState;
import csm.statetree.Region;
import csm.statetree.State;


public class CSMConfiguration {

	ExpressionEnvironment varAssignment;

	Set<InternalState> active;

	// null bedeutet _|_
	Map<Region, InternalState> history;

	Map<CompositeState, Action> doActions;

	Action alpha;

	// das s mit den komischen Pünktchen
	State completionState; // oder null

	List<Tuple<Action, State>> beta;

	// T
	CompoundTransition currentCoTr; // oder null

	// "T
	List<CompoundTransition> remainingCoTr;

	CSMConfiguration(CoreStateMachine csm,
			Map<CompositeState, Action> doActions) {
		this.varAssignment = new ExpressionEnvironment(csm.variables);

		this.active = new HashSet<InternalState>();
		this.active.add(csm.region.getStartState());

		this.history = new HashMap<Region, InternalState>();
		this.history.put(csm.region, csm.region.getStartState());

		this.doActions = doActions;
		this.alpha = DoSkip.skipAction;
		this.completionState = null;
		this.beta = new LinkedList<Tuple<Action, State>>();
		this.currentCoTr = new CompoundTransition();
		this.remainingCoTr = new LinkedList<CompoundTransition>();
	}

	public CSMConfiguration(CSMConfiguration cfg) {
		this.varAssignment = new ExpressionEnvironment(cfg.varAssignment);
		this.active = new HashSet<InternalState>(cfg.active);
		this.history = new HashMap<Region, InternalState>(cfg.history);
		this.doActions = new HashMap<CompositeState, Action>(cfg.doActions);
		this.alpha = cfg.alpha;
		this.completionState = cfg.completionState;
		this.beta = new LinkedList<Tuple<Action, State>>(cfg.beta);
		this.currentCoTr = cfg.currentCoTr;
		this.remainingCoTr = new LinkedList<CompoundTransition>(
				cfg.remainingCoTr);
	}

	// XXX Vergleich von Actions?
	/*
	 * Wir testen nicht, ob zwei Actions semantisch äquivalent sind.
	 * Stattdessen verwenden wir hier die Objekt-Gleichheit (==). Das
	 * sollte nur in ganz seltenen Fällen zu einer Verdoppelung von
	 * States führen. Die Skip-Actions sind als Singleton implementiert,
	 * um die Verdoppelung von Endzuständen auszuschließen.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CSMConfiguration))
			return false;
		final CSMConfiguration other = (CSMConfiguration) obj;
		if (!this.varAssignment.equals(other.varAssignment))
			return false;
		if (!this.active.equals(other.active))
			return false;
		if (!this.history.equals(other.history))
			return false;
		if (!this.doActions.equals(other.doActions))
			return false;
		if (!this.alpha.equals(other.alpha))
			return false;
		if (this.completionState != null
			&& !this.completionState.equals(other.completionState))
			return false;
		if (!this.beta.equals(other.beta))
			return false;
		if (!this.currentCoTr.equals(other.currentCoTr))
			return false;
		if (!this.remainingCoTr.equals(other.remainingCoTr))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hc = 0;
		hc += this.active.hashCode();
		hc += this.history.hashCode();
		hc += this.varAssignment.hashCode();
		hc += this.doActions.hashCode();
		hc += this.alpha.hashCode();
		if (this.completionState != null)
			hc += this.completionState.hashCode();
		hc += this.beta.hashCode();
		hc += this.currentCoTr.hashCode();
		hc += this.remainingCoTr.hashCode();
		return hc;
	}

	/**
	 * die Debug-Meldung, mit in der grafischen Darstellung des
	 * Nu-Automaten die States dekoriert werden
	 */
	// XXX hier können die Debug-Meldungen der Nu-States geändert werden
	public String debugString() {
		String dbg = this.varAssignment.toString();
		dbg += "\\nalpha: " + this.alpha.prettyprint();
		dbg += "\\nbeta:";
		for (Tuple<Action, State> t : this.beta)
			dbg += ' ' + t.l.prettyprint();
		return dbg;

	}
}

package semantics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import csm.CoreStateMachine;
import csm.expression.Action;
import csm.statetree.CSMComponent;
import csm.statetree.CompositeState;
import csm.statetree.ExitState;
import csm.statetree.Region;
import csm.statetree.State;
import csm.statetree.Transition;
import csm.statetree.Visitor;

/**
 * Ein paar grundlegende Mengen und Funktionen, die bei der semantischen Analyse
 * verwendet werden. Die Funktionen sind als Maps implementiert.
 */
public class BasicMappings extends Visitor {

	/**
	 * eine Liste aller Events
	 */
	public final Set<String> events;

	/**
	 * eine Liste aller Transitionen
	 */
	public final List<Transition> transitions;

	/**
	 * eine Liste aller ExitStates
	 */
	public final List<ExitState> exitstates;

	/**
	 * liefert zu jedem CompositeState eine Liste der direkten Subregionen.
	 * Implementiert die Funktion dsr aus dem Paper.
	 */
	public final ListMap<CompositeState, Region> directSubregions;

	/**
	 * liefert zu jedem CompositeState eine Liste der direkten Subregionen.
	 * Implementiert die Funktion dsr aus dem Paper.
	 */
	public final ListMap<Region, CompositeState> compositesInRegion;

	/**
	 * liefert zu jedem ExitState eine Liste derjenigen Transitionen, die diesen
	 * ExitState als Target haben
	 */
	public final ListMap<ExitState, Transition> transitionsPerExitstate;

	/**
	 * liefert zu jedem State eine Liste derjenigen Transitionen, die diesen
	 * State als Source haben
	 */
	public final ListMap<State, Transition> transitionsPerSourcestate;

	/**
	 * Liefert zu jedem CompositeState die diesem State zugeordnete DoAction
	 */
	public final Map<CompositeState, Action> doActions;

	/**
	 * Der Konstruktor erzeugt alle Maps
	 */
	public BasicMappings(CoreStateMachine csm) {
		this.events = csm.events.getKeys();
		this.transitions = new LinkedList<Transition>();
		this.exitstates = new LinkedList<ExitState>();
		this.directSubregions = new ListMap<CompositeState, Region>();
		this.compositesInRegion = new ListMap<Region, CompositeState>();
		this.transitionsPerExitstate = new ListMap<ExitState, Transition>();
		this.transitionsPerSourcestate = new ListMap<State, Transition>();
		this.doActions = new HashMap<CompositeState, Action>();

		visitRegion(csm.region);
	}

	@Override
	protected void visitCSMComponent(CSMComponent c) {
		visitChildren(c);
	}

	@Override
	protected void visitRegion(Region r) {
		if (r.getParent() != null)
			this.directSubregions.addTo((CompositeState) r.getParent(), r);
		visitChildren(r);

	}

	@Override
	protected void visitTransition(Transition t) {
		this.transitions.add(t);

		if (t.getTarget() instanceof ExitState) {
			final ExitState target = (ExitState) t.getTarget();
			this.transitionsPerExitstate.addTo(target, t);
		}

		final State source = t.getSource();
		this.transitionsPerSourcestate.addTo(source, t);

		visitChildren(t);
	}

	@Override
	protected void visitCompositeState(CompositeState s) {
		this.doActions.put(s, s.getDoAction());
		this.compositesInRegion.addTo(s.regOf(), s);
		visitChildren(s);
	}

	@Override
	protected void visitExitState(ExitState s) {
		this.exitstates.add(s);
		visitChildren(s);
	}

	List<Region> allSubRegions(Region r) {
		List<CompositeState> cs = this.compositesInRegion.get(r);
		List<Region> result = new LinkedList<Region>();
		for (CompositeState c : cs) {
			for (Region r2 : this.directSubregions.get(c))
				result.addAll(allSubRegions(r2));
		}
		result.add(r);
		return result;
	}
}

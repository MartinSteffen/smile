/**
 * 
 */
package gui;

import java.util.LinkedList;

import gui.graphicalobjects.*;
import csm.CoreStateMachine;
import csm.statetree.ChoiceState;
import csm.statetree.CompositeState;
import csm.statetree.EntryState;
import csm.statetree.ExitState;
import csm.statetree.FinalState;
import csm.statetree.OutermostRegion;
import csm.statetree.Region;
import csm.statetree.SubRegion;
import csm.statetree.Transition;
import csm.statetree.Visitor;

/**
 * Nach dem erzeugen der CSM, zeichnet diese Klasse die grafischen Elemente
 * auf den Workpanel. Mit dem Visitor-Patern wird die CSM zu diesem Zweck
 * durchlaufen und die Objekte erzeugt.
 * 
 * @author Oliver
 *
 */
public class GuiLoader extends Visitor {
	
	CoreStateMachine csm;
	Controller pc;
	WorkPanel wp;
	OutermostRegion outermost;
	GraphicalObject parent;
	LinkedList<Transition> transitions;
	
	// Der Konstruktor setzt die Elemente im WorkPanel und startet das Visitor-Pattern
	public GuiLoader(CoreStateMachine csm, WorkPanel wp, Controller pc) {
		this.csm = csm;
		this.pc = pc;
		this.wp = wp;
		outermost = csm.region;
		parent = null;
		transitions = new LinkedList<Transition>();
		this.visitRegion(csm.region);
		this.addTransitions();
	}

	//	 Die Transitionen werden in einer Liste gspeichert und am Ende gezeichnet
	private void addTransitions() {
		for(Transition transition : transitions) {
			TransitionGO t;
			GraphicalObject source = wp.getGraphicalObjectByComponent(transition.getSource());
			GraphicalObject target = wp.getGraphicalObjectByComponent(transition.getTarget());
			if(transition.getName() != null)
				t = new TransitionGO(transition, transition.getName(), source, target, pc);
			else
				t = new TransitionGO(transition, source, target, pc);
			wp.addObject(t);
		}
	}
	
	// Die folgenden Methoden implementieren das Visitor-Pattern.

	@Override
	protected void visitChoiceState(ChoiceState state) {
		ChoiceStateGO cs;
		GraphicalObject temp;
		if (state.getName() != null)
			cs = new ChoiceStateGO(state, (CompositeStateGO)parent, state.getName(), pc);
		else 
			cs = new ChoiceStateGO(state, (CompositeStateGO)parent, pc);
		wp.addObject(cs);
		temp = parent;
		parent = cs;
		visitChildren(state);
		parent = temp;
	}

	@Override
	protected void visitCompositeState(CompositeState state) {
		CompositeStateGO cs;
		GraphicalObject temp;
		if(state.getName() != null)
			cs = new CompositeStateGO(state, (CompositeStateGO)parent, state.getName(), pc);
		else
			cs = new CompositeStateGO(state, (CompositeStateGO)parent, pc);
		wp.addObject(cs);
		
		temp = parent;
		parent = cs;
		visitChildren(state);
		parent = temp;
	}

	@Override
	protected void visitEntryState(EntryState state) {
		EntryStateGO es;
		GraphicalObject temp;
		if(state.getName() != null)
			es = new EntryStateGO(state, (CompositeStateGO)parent, state.getName(), pc);
		else
			es = new EntryStateGO(state, (CompositeStateGO)parent, pc);
		wp.addObject(es);
		temp = parent;
		parent = es;
		visitChildren(state);
		parent = temp;
	}

	@Override
	protected void visitExitState(ExitState state) {
		ExitStateGO es;
		GraphicalObject temp;
		if(state.getName() != null)
			es = new ExitStateGO(state, (CompositeStateGO)parent, state.getName(), pc);
		else
			es = new ExitStateGO(state, (CompositeStateGO)parent, pc);
		wp.addObject(es);
		temp = parent;
		parent = es;
		visitChildren(state);
		parent = temp;
	}

	@Override
	protected void visitFinalState(FinalState state) {
		FinalStateGO cs;
		GraphicalObject temp;
		if(state.getName() != null)
			cs = new FinalStateGO(state, null, state.getName(),  pc);
		else
			cs = new FinalStateGO(state, null, pc);
		wp.addObject(cs);
		
		temp = parent;
		parent = cs;
		visitChildren(state);
		parent = temp;
	}

	@Override
	protected void visitRegion(Region region) {
		if(parent != null)
			((CompositeStateGO)parent).addSubregion((SubRegion)region);
		visitChildren(region);
	}

	@Override
	protected void visitTransition(Transition transition) {
		transitions.add(transition);
	}	
}

package csm.statetree;

import java.awt.Point;

import csm.CoreStateMachine;
import csm.exceptions.ErrTreeNotChanged;


/**
 * eine spezielle Region, die keine Parent-Komponente hat. Sie hat
 * zusätzlich einen Start-State und einen (unveränderlichen) Verweis auf
 * die CoreStateMachine, deren Teil sie ist.  
 * <p>
 * im Paper als SubRegion \epsilon bekannt
 * 
 * @author hs
 */
public final class OutermostRegion extends Region {

	CompositeState startState;

	private final CoreStateMachine containingCSM;

	public OutermostRegion(CoreStateMachine containingCSM) {
		super(new Point(0, 0));
		assert containingCSM != null;
		this.containingCSM = containingCSM;
	}

	public CoreStateMachine getCSM() {
		return containingCSM;
	}

	public CompositeState getStartState() {
		return startState;
	}

	/**
	 * setzt den Start-State, wenn er 1. ein Composite-State und 2.
	 * direkt in dieser äußersten Region enthalten ist.
	 * 
	 * @throws ErrTreeNotChanged wenn der neue Start-State nicht direkt
	 *             in dieser äußersten Region liegt.
	 */
	public void setStartState(CompositeState state)
			throws ErrTreeNotChanged {
		assert state != null;
		if (state.getParent() != this)
			throw new ErrTreeNotChanged("Could not set new start state");
		startState = state;
	}

	/**
	 * alle Komponenten mit neuen, eindeutigen uniqueIds versehen
	 */
	public void enumerateStates() {
		/*
		 * Verwende einen Visitor, um über alle Komponenten zu
		 * iterieren.
		 */
		new Visitor() {

			int count;

			/*
			 * der State erhält eine neue Nummer, danach werden seine
			 * Child-Komponenten besucht
			 */
			@Override
			final public void visitState(State s) {
				s.setUniqueId(this.count);
				this.count++;
				visitChildren(s);
			}

			/*
			 * bei anderen Komponenten werden nur deren
			 * Child-Komponenten besucht
			 */
			@Override
			final public void visitCSMComponent(CSMComponent c) {
				visitChildren(c);
			}
		}.visitRegion(this);
	}
}

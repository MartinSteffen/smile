package csm.statetree;

import java.awt.Point;

import csm.CoreStateMachine;
import csm.exceptions.ErrTreeNotChanged;


/**
 * eine spezielle Region, die keine Parent-Komponente hat. Sie hat
 * zusaetzlich einen Start-State und einen (unveraenderlichen) Verweis
 * auf die CoreStateMachine, deren Teil sie ist.
 * <p>
 * im Paper als SubRegion \epsilon bekannt
 * 
 * @author hs
 */
public final class OutermostRegion extends Region {

	private CompositeState startState;

	private final CoreStateMachine containingCSM;

	/**
	 * @param containingCSM die CoreStateMachine, zu der diese Region
	 *            gehoert; muss ungleich null sein
	 */
	public OutermostRegion(CoreStateMachine containingCSM) {
		super(new Point(0, 0));
		assert containingCSM != null;
		this.containingCSM = containingCSM;
	}

	@Override
	public CoreStateMachine getCSM() {
		return this.containingCSM;
	}

	/**
	 * @return der StartState der CoreStateMachine oder null, wenn kein
	 *         StartState definiert ist
	 */
	public CompositeState getStartState() {
		return this.startState;
	}

	/**
	 * setzt den Start-State, wenn er 1. ein Composite-State und 2.
	 * direkt in dieser aeussersten Region enthalten ist.
	 * 
	 * @param state der Start-State oder null
	 * @throws ErrTreeNotChanged wenn der neue Start-State nicht direkt
	 *             in dieser aeussersten Region liegt.
	 */
	public void setStartState(CompositeState state)
			throws ErrTreeNotChanged {
		if (state != null && state.getParent() != this)
			throw new ErrTreeNotChanged("Could not set new start state");
		this.startState = state;
	}

	/**
	 * alle Komponenten mit neuen, eindeutigen uniqueIds versehen
	 */
	public void enumerateStates() {
		/*
		 * Verwende einen Visitor, um ueber alle Komponenten zu
		 * iterieren.
		 */
		new Visitor() {

			int count;

			/*
			 * der State erhaelt eine neue Nummer, danach werden seine
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

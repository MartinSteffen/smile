package csm.statetree;

import java.awt.Point;

import csm.exceptions.ErrTreeNotChanged;


/**
 * im Paper als S_final bekannt
 * 
 * @author hsi
 */
public final class FinalState extends InternalState {

	public FinalState(Point position) {
		super(position);
	}

	@Override
	final void accept(Visitor visitor) {
		visitor.visitFinalState(this);
	}

	final public void add(ExitState child) throws ErrTreeNotChanged {
		addAnyChild(child);
	}

	@Override
	public CSMComponent transitionLocation(State target) {
		assert target != null;
		return null;
	}

}

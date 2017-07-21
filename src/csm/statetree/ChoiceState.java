package csm.statetree;

import java.awt.Point;

import csm.exceptions.ErrTreeNotChanged;


/**
 * im Paper als S_choice bekannt
 * 
 * @author hsi
 */
public final class ChoiceState extends InternalState {

	public ChoiceState(Point position) {
		super(position);
	}

	@Override
	final void accept(Visitor visitor) {
		visitor.visitChoiceState(this);
	}

	@Override
	CSMComponent transitionLocation(State target) {
		assert target != null;
		if (target instanceof ExitState)
			return null;
		else if (target instanceof CompositeState)
			return null;
		else if (this == target)
			return this;
		else if (regOf() == target.regOf())
			return regOf();
		else
			return null;
	}

	@Override
	public void dropHere(CSMComponent child) throws ErrTreeNotChanged {
		dropToParent(child);
	}

}

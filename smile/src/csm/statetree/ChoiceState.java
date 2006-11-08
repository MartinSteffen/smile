/**
 * 
 */
package csm.statetree;

import java.awt.Point;


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
	void accept(Visitor visitor) {
		visitor.visitChoiceState(this);
	}

	@Override
	void visitChildren(Visitor visitor) {
		// ChoiceStates haben keine Child-States
	}

	@Override
	public CSMComponent transitionLocation(State target) {
		assert target != null;
		if (target instanceof CompositeState)
			return null;
		if (regOf() == target.regOf())
			return regOf();
		else
			return null;

	}
}

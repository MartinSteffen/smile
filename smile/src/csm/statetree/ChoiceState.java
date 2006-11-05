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
	public boolean mayConnectTo(State target) {
		assert target != null;
		if (regOf() != target.regOf())
			return false;
		if (target instanceof CompositeState)
			return false;
		return true;
	}
}

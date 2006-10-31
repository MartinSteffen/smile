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

	public ChoiceState(Point position, Region parentRegion) {
		super(position, parentRegion);
	}

	@Override
	void visitMe(CSMVisitor visitor) {
		visitor.visitChoiceState(this);
	}

	@Override
	void visitChildren(CSMVisitor visitor) {
		// ChoiceStates haben keine Child-States
	}

	//
	// Connections ********************************

	@Override
	public boolean mayConnectTo(State target) {
		assert target != null;
		if (regOf() != target.regOf())
			return false;
		return target.mayConnectFromChoiceState(this);
	}

	@Override
	boolean mayConnectFromChoiceState(ChoiceState source) {
		return true;
	}

	@Override
	boolean mayConnectFromEntryState(EntryState source) {
		return true;
	}

	@Override
	boolean mayConnectFromExitState(ExitState source) {
		boolean sourceInFinal = source.stateOf() instanceof FinalState;
		return source.regOf() == regOf() && !sourceInFinal;
	}

}

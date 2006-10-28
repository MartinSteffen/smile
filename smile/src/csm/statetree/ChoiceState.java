/**
 * 
 */
package csm.statetree;

import java.awt.Point;

/**
 * @author hsi
 * 
 */
public final class ChoiceState extends InternalState {

	public ChoiceState(Point position, AbstractRegion parentRegion) {
		super(position, parentRegion);
	}

	@Override
	public void traverseCSM(CSMTraversal visitor) {
		visitor.visitChoiceState(this);
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

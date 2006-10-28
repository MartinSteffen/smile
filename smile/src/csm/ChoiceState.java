/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hsi
 * 
 */
public final class ChoiceState extends InternalState {

	public ChoiceState(Point position, AbstractRegion parentRegion) {
		super(position, parentRegion);
	}

	//
	// Connections ********************************

	@Override
	public final boolean mayConnectTo(State target) {
		if (this.regOf() != target.regOf())
			return false;
		return target.mayConnectFromChoiceState(this);
	}

	@Override
	protected final boolean mayConnectFromChoiceState(ChoiceState source) {
		return true;
	}

	@Override
	protected final boolean mayConnectFromEntryState(EntryState source) {
		return true;
	}

	@Override
	protected final boolean mayConnectFromExitState(ExitState source) {
		boolean sourceInFinal = source.stateOf() instanceof FinalState;
		return source.regOf() == this.regOf() && !sourceInFinal;
	}
}

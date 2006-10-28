/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hsi
 * 
 */
public final class FinalState extends ExitableState {

	public FinalState(Point position, AbstractRegion parentRegion) {
		super(position, parentRegion);
	}

	//
	// Connections ********************************

	@Override
	public final boolean mayConnectTo(State target) {
		return false;
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

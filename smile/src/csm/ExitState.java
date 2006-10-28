/**
 * 
 */
package csm;

//
// Semantik ***********************************
//
// Konstruktion *******************************
//
// Connections ********************************

import java.awt.Point;

/**
 * @author hsi
 * 
 */
public final class ExitState extends ConnectionPoint {

	private ExitableState parentExitable;

	@Override
	public final CSMComponent parent() {
		return this.parentExitable;
	}

	public ExitState(Point position, ExitableState parentExitable) {
		super(position);
		assert parentExitable != null;
		this.parentExitable = parentExitable;
		this.parentExitable.addChildExitState(this);
	}

	//
	// Connections ********************************

	@Override
	public boolean mayConnectTo(State target) {
		return target.mayConnectFromExitState(this);
	}

	@Override
	protected boolean mayConnectFromChoiceState(ChoiceState source) {
		return true;
	}

	@Override
	protected boolean mayConnectFromEntryState(EntryState source) {
		return false;
	}

	@Override
	protected boolean mayConnectFromExitState(ExitState source) {
		return source.stateOf().stateOf() == this.stateOf();
	}

}

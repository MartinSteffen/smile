/**
 * 
 */
package csm.statetree;

//
// Semantik ***********************************
//
// Konstruktion *******************************
//
// Connections ********************************

import java.awt.Point;


/**
 * im Paper als S_exit bekannt
 * 
 * @author hsi
 */
public final class ExitState extends ConnectionPoint {

	private ExitableState parentExitable;

	@Override
	public CSMComponent parent() {
		return this.parentExitable;
	}

	public ExitState(Point position, ExitableState parentExitable) {
		super(position);
		assert parentExitable != null;
		this.parentExitable = parentExitable;
		this.parentExitable.addChildExitState(this);
	}

	@Override
	public void traverseCSM(CSMTraversal visitor) {
		visitor.visitExitState(this);
	}

	//
	// Connections ********************************

	@Override
	public boolean mayConnectTo(State target) {
		assert target != null;
		return target.mayConnectFromExitState(this);
	}

	@Override
	boolean mayConnectFromChoiceState(ChoiceState source) {
		return true;
	}

	@Override
	boolean mayConnectFromEntryState(EntryState source) {
		return false;
	}

	@Override
	boolean mayConnectFromExitState(ExitState source) {
		return source.stateOf().stateOf() == stateOf();
	}


}

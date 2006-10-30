/**
 * 
 */
package csm.statetree;

import java.awt.Point;


/**
 * im Paper als S_exit bekannt
 * 
 * @author hsi
 */
public final class ExitState extends ConnectionPoint {

	private ExitableState parentExitable;

	public static enum KindOfExitstate {
		PR, NPR, CP
	};

	public KindOfExitstate kindOf = KindOfExitstate.PR;

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
	public void visitMe(CSMVisitor visitor) {
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

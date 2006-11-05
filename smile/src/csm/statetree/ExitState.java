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

	public static enum KindOfExitstate {
		PR, NPR, CP
	};

	public KindOfExitstate kindOf = KindOfExitstate.PR;

	public ExitState(Point position) {
		super(position);
	}

	@Override
	void accept(Visitor visitor) {
		visitor.visitExitState(this);
	}

	//
	// Connections ********************************

	@Override
	public boolean mayConnectTo(State target) {
		assert target != null;
		if (target instanceof ExitState)
			return this.stateOf().stateOf() == target.stateOf();
		if (target instanceof CompositeState)
			return false;
		// entry, final, choice:
		boolean sourceInFinal = this.stateOf() instanceof FinalState;
		return this.regOf() == target.regOf() && !sourceInFinal;
	}
}

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
	public CSMComponent connectionLocation(State target) {
		assert target != null;
		if (target instanceof CompositeState)
			return null;
		if (target instanceof ExitState
		// TODO Harald: this.regOf().stateOf() ... ?
				&& this.stateOf().stateOf() == target.stateOf())
			return target.stateOf();
		else {
			// entry, final, choice:
			if (this.stateOf() instanceof FinalState)
				return null;
			if (this.regOf() == target.regOf())
				return this.regOf();
			else
				return null;
		}
	}
}

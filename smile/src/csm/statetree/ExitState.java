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

	/**
	 * Der Typ des ExitStates.
	 * <p>
	 * Da dieser Typ keinen Einschränkungen unterliegt, ist er als
	 * öffentlich beschreibbare Variable implementiert.
	 */
	public KindOfExitstate kindOf = KindOfExitstate.PR;

	public ExitState(Point position) {
		super(position);
	}

	@Override
	final void accept(Visitor visitor) {
		visitor.visitExitState(this);
	}

	@Override
	public CSMComponent transitionLocation(State target) {
		assert target != null;
		if (target instanceof CompositeState)
			return null;
		if (target instanceof ExitState
				&& this.regOf().getParent() == target.stateOf())
			return target.stateOf();
		// entry, final, choice:
		// TODO Bedingung checken: Exitstate ausschließen?
		if (this.stateOf() instanceof FinalState)
			return null;
		if (this.regOf() == target.regOf())
			return this.regOf();
		return null;
	}
}

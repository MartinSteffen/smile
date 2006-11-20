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

	/**
	 * Der Typ des ExitStates.
	 */
	public static enum KindOfExitstate {
		PR, NPR, CP
	};

	private KindOfExitstate kindOf = KindOfExitstate.PR;

	/**
	 * erzeugt einen neuen ExitState mit kindOfExitstate=PR.
	 */
	public ExitState(Point position) {
		super(position);
	}

	/**
	 * @return der Typ des Exitstates
	 */
	public KindOfExitstate getKindOfExitstate() {
		return this.kindOf;
	}

	/**
	 * setzt den Typ des Exitstates
	 * 
	 * @param kindOf muss ungleich null sein
	 */
	public void setKindOfExitstate(KindOfExitstate kindOf) {
		assert kindOf != null;
		this.kindOf = kindOf;
	}

	@Override
	final void accept(Visitor visitor) {
		visitor.visitExitState(this);
	}

	@Override
	CSMComponent transitionLocation(State target) {
		assert target != null;
		if (target instanceof CompositeState)
			return null;
		if (target instanceof ExitState
				&& regOf().getParent() == target.stateOf())
			return target.stateOf();
		// entry, final, choice:
		// TODO Bedingung checken: Exitstate ausschlieﬂen?
		if (stateOf() instanceof FinalState)
			return null;
		if (regOf() == target.regOf())
			return regOf();
		return null;
	}
}

/**
 * 
 */
package csm.statetree;

import java.awt.Point;


/**
 * im Paper als S_final bekannt
 * 
 * @author hsi
 */
public final class FinalState extends ExitableState {

	public FinalState(Point position, Region parentRegion) {
		super(position, parentRegion);
	}

	@Override
	void visitMe(Visitor visitor) {
		visitor.visitFinalState(this);
	}

	@Override
	void visitChildren(Visitor visitor) {
		visitMyExitStates(visitor);
	}

	//
	// Connections ********************************

	@Override
	public boolean mayConnectTo(State target) {
		assert target != null;
		return false;
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

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

	public FinalState(Point position, AbstractRegion parentRegion) {
		super(position, parentRegion);
	}

	@Override
	public void visitMe(CSMVisitor visitor) {
		visitor.visitFinalState(this);
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

	@Override
	public void visitChildren(CSMVisitor visitor) {
		visitMyExitStates(visitor);
	}

}

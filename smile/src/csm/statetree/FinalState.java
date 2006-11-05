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

	public FinalState(Point position) {
		super(position);
	}

	@Override
	void accept(Visitor visitor) {
		visitor.visitFinalState(this);
	}

	@Override
	void visitChildren(Visitor visitor) {
		visitMyExitStates(visitor);
	}

	@Override
	public CSMComponent connectionLocation(State target) {
		assert target != null;
		return null;
	}

}

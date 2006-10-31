/**
 * 
 */
package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;


/**
 * @author hsi
 * 
 */
public abstract class ExitableState extends InternalState {

	private final LinkedList<ExitState> childExitStates =
			new LinkedList<ExitState>();

	//
	// Konstruktion *******************************

	ExitableState(Point position, Region parentRegion) {
		super(position, parentRegion);
	}

	final void visitMyExitStates(CSMVisitor visitor) {
		for (final ExitState s : this.childExitStates)
			s.visitMe(visitor);

	}

	final void addChildExitState(ExitState child) {
		this.childExitStates.add(child);
	}

	final void removeChildExitState(ExitState child) {
		this.childExitStates.remove(child);
	}
}

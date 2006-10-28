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
	private final LinkedList<ExitState> childExitStates = new LinkedList<ExitState>();

	//
	// Konstruktion *******************************

	protected ExitableState(Point position, AbstractRegion parentRegion) {
		super(position, parentRegion);
	}

	final void addChildExitState(ExitState child) {
		this.childExitStates.add(child);
	}

	final void removeChildExitState(ExitState child) {
		this.childExitStates.remove(child);
	}

	void visitMyExitStates(CSMTraversal visitor) {
		for(ExitState s : childExitStates)
			s.traverseCSM(visitor);
		
	}
	
}

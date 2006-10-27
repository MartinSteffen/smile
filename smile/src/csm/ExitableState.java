/**
 * 
 */
package csm;

import java.awt.Point;
import java.util.LinkedList;

/**
 * @author hsi
 * 
 */
public abstract class ExitableState extends InternalState {
	private final LinkedList<ExitState> childExitStates = new LinkedList<ExitState>();

	public ExitableState(Point position, Region parentRegion) {
		super(position, parentRegion);
	}

	public final void addChildExitState(ExitState child) {
		childExitStates.add(child);
	}

	public final void removeChildExitState(ExitState child) {
		childExitStates.remove(child);
	}

}

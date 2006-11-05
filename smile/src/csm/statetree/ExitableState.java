/**
 * 
 */
package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;


/**
 * @author hsi
 */
public abstract class ExitableState extends InternalState {

	private final LinkedList<ExitState> childExitStates = new LinkedList<ExitState>();

	ExitableState(Point position) {
		super(position);
	}

	final void visitMyExitStates(Visitor visitor) {
		for (final ExitState s : this.childExitStates)
			s.accept(visitor);

	}

	final public void add(ExitState child) {
		assert child != null;
		child.setParent(this);
		this.childExitStates.add(child);
	}

	final public void remove(ExitState child) {
		assert child != null;
		child.unsetParent(this);
		this.childExitStates.remove(child);
	}
}

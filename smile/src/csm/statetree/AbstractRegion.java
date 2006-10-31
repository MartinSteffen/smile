package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;


/**
 * Abstrakte Oberklasse aller Regionen
 * 
 * @author hsi
 */
public abstract class AbstractRegion extends CSMComponent {

	private final LinkedList<InternalState> childInternalStates =
			new LinkedList<InternalState>();

	//
	// Konstruktion *******************************

	protected AbstractRegion(Point position) {
		super(position);
	}

	final void addChildInternalState(InternalState child) {
		assert child.parent() == this;
		this.childInternalStates.add(child);
	}

	final void removeChildInternalState(InternalState child) {
		this.childInternalStates.remove(child);
	}

	@Override
	public void visitMe(CSMVisitor visitor) {
		visitor.visitRegion(this);
	}

	@Override
	public void visitChildren(CSMVisitor visitor) {
		for (final InternalState s : this.childInternalStates)
			s.visitMe(visitor);
	}

}

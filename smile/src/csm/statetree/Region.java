package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;


/**
 * Abstrakte Oberklasse aller Regionen
 * 
 * @author hsi
 */
public abstract class Region extends CSMComponent {

	private final LinkedList<InternalState> childInternalStates =
			new LinkedList<InternalState>();

	Region(Point position) {
		super(position);
	}

	@Override
	void visitMe(CSMVisitor visitor) {
		visitor.visitRegion(this);
	}

	@Override
	final void visitChildren(CSMVisitor visitor) {
		for (final InternalState s : this.childInternalStates)
			s.visitMe(visitor);
	}

	final void addChildInternalState(InternalState child) {
		assert child.parent() == this;
		this.childInternalStates.add(child);
	}

	final void removeChildInternalState(InternalState child) {
		this.childInternalStates.remove(child);
	}

}

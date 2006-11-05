package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;

import csm.exceptions.ErrTreeNotChanged;


/**
 * Abstrakte Oberklasse aller Regionen
 * 
 * @author hsi
 */
public abstract class Region extends CSMComponent {

	private final LinkedList<InternalState> childInternalStates = new LinkedList<InternalState>();

	Region(Point position) {
		super(position);
	}

	@Override
	final void accept(Visitor visitor) {
		visitor.visitRegion(this);
	}

	@Override
	final void visitChildren(Visitor visitor) {
		for (final InternalState s : this.childInternalStates)
			s.accept(visitor);
	}

	final public void add(InternalState child) throws ErrTreeNotChanged {
		assert child != null;
		child.setParent(this);
		this.childInternalStates.add(child);
	}

	final public void remove(InternalState child) throws ErrTreeNotChanged {
		assert child != null;
		child.unsetParent(this);
		this.childInternalStates.remove(child);
	}

}

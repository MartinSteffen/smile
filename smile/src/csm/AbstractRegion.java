package csm;

import java.awt.Point;
import java.util.LinkedList;

public abstract class AbstractRegion extends CSMComponent {

	private final LinkedList<InternalState> childInternalStates = new LinkedList<InternalState>();

	//
	// Konstruktion *******************************
	
	public AbstractRegion(Point position) {
		super(position);
	}

	public final void addChildInternalState(InternalState child) {
		childInternalStates.add(child);
	}

	public final void removeChildInternalState(InternalState child) {
		childInternalStates.remove(child);
	}

}
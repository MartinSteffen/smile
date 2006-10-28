package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;


public abstract class AbstractRegion extends CSMComponent {

	private final LinkedList<InternalState> childInternalStates = new LinkedList<InternalState>();

	//
	// Konstruktion *******************************
	
	protected AbstractRegion(Point position) {
		super(position);
	}

	final void addChildInternalState(InternalState child) {
		this.childInternalStates.add(child);
	}

	final void removeChildInternalState(InternalState child) {
		this.childInternalStates.remove(child);
	}
	
	@Override
	public void traverseCSM(CSMTraversal visitor) {
		if(! visitor.enterRegion(this))
			return;
		for(InternalState s : childInternalStates)
			s.traverseCSM(visitor);
		visitor.exitRegion(this);
		
	}
}
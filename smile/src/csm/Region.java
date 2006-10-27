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
public class Region extends CSMComponent {
	private CompositeState parentComposite;

	private final LinkedList<InternalState> childInternalStates = new LinkedList<InternalState>();

	Region(Point position, CompositeState parentComposite) {
		super(position);
		// XXX für die äußerste Region soll parent dann doch null sein
		// assert parentComposite != null;
		this.parentComposite = parentComposite;
		this.parentComposite.addSubregion(this);
	}

	@Override
	public final CSMComponent parent() {
		return this.parentComposite;
	}

	public final void addChildInternalState(InternalState child) {
		childInternalStates.add(child);
	}

	public final void removeChildInternalState(InternalState child) {
		childInternalStates.remove(child);
	}

}

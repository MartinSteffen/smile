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
public final class CompositeState extends ExitableState {
	private final LinkedList<EntryState> childEntryStates = new LinkedList<EntryState>();

	private final LinkedList<Region> subregions = new LinkedList<Region>();

	public CompositeState(Point position, Region parentRegion) {
		super(position, parentRegion);
	}

	public final void addChildEntryState(EntryState child) {
		childEntryStates.add(child);
	}

	public final void removeChildEntryState(EntryState child) {
		childEntryStates.remove(child);
	}

	public final void addSubregion(Region child) {
		subregions.add(child);
	}

	public final void removeSubregion(Region child) {
		subregions.remove(child);
	}

}

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

	//
	// Konstruktion *******************************

	public CompositeState(Point position, AbstractRegion parentRegion) {
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

	//
	// Connections ********************************

	@Override
	public final boolean mayConnectTo(State target) {
		return this == target;
	}

	@Override
	protected final boolean mayConnectFromChoiceState(ChoiceState source) {
		return false;
	}

	@Override
	protected final boolean mayConnectFromEntryState(EntryState source) {
		return false;
	}

	@Override
	protected boolean mayConnectFromExitState(ExitState source) {
		return false;
	}
}

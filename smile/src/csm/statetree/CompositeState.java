/**
 * 
 */
package csm.statetree;

import java.awt.Point;
import java.util.LinkedList;

/**
 * im Paper als S_com bekannt
 * 
 * @author hsi
 */
public final class CompositeState extends ExitableState {

	private final LinkedList<EntryState> childEntryStates = new LinkedList<EntryState>();

	private final LinkedList<Region> subregions = new LinkedList<Region>();

	//
	// Konstruktion *******************************

	public CompositeState(Point position, AbstractRegion parentRegion) {
		super(position, parentRegion);
	}

	void addChildEntryState(EntryState child) {
		this.childEntryStates.add(child);
	}

	void removeChildEntryState(EntryState child) {
		this.childEntryStates.remove(child);
	}

	void addSubregion(Region child) {
		this.subregions.add(child);
	}

	void removeSubregion(Region child) {
		this.subregions.remove(child);
	}

	@Override
	public void traverseCSM(CSMTraversal visitor) {
		if (!visitor.enterCompositeState(this))
			return;
		visitMyExitStates(visitor);
		for(final EntryState s : this.childEntryStates)
			s.traverseCSM(visitor);
		for(final Region r : this.subregions)
			r.traverseCSM(visitor);
		visitor.exitCompositeState(this);
	}

	//
	// Connections ********************************

	@Override
	public boolean mayConnectTo(State target) {
		assert target != null;
		return this == target;
	}

	@Override
	boolean mayConnectFromChoiceState(ChoiceState source) {
		return false;
	}

	@Override
	boolean mayConnectFromEntryState(EntryState source) {
		return false;
	}

	@Override
	boolean mayConnectFromExitState(ExitState source) {
		return false;
	}
}

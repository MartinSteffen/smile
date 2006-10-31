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

	private final LinkedList<EntryState> childEntryStates =
			new LinkedList<EntryState>();

	private final LinkedList<SubRegion> subregions =
			new LinkedList<SubRegion>();

	//
	// Konstruktion *******************************

	public CompositeState(Point position, Region parentRegion) {
		super(position, parentRegion);
	}

	@Override
	void visitMe(CSMVisitor visitor) {
		visitor.visitCompositeState(this);
	}

	@Override
	void visitChildren(CSMVisitor visitor) {
		visitMyExitStates(visitor);
		for (final EntryState s : this.childEntryStates)
			s.visitMe(visitor);
		for (final SubRegion r : this.subregions)
			r.visitMe(visitor);
	}

	void addChildEntryState(EntryState child) {
		this.childEntryStates.add(child);
	}

	void removeChildEntryState(EntryState child) {
		this.childEntryStates.remove(child);
	}

	void addSubregion(SubRegion child) {
		this.subregions.add(child);
	}

	void removeSubregion(SubRegion child) {
		this.subregions.remove(child);
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

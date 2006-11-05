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

	// entspricht dsr aus Def. 4
	private final LinkedList<SubRegion> subregions = new LinkedList<SubRegion>();

	public CompositeState(Point position) {
		super(position);
	}

	@Override
	void accept(Visitor visitor) {
		visitor.visitCompositeState(this);
	}

	@Override
	void visitChildren(Visitor visitor) {
		visitMyExitStates(visitor);
		for (final EntryState s : this.childEntryStates)
			s.accept(visitor);
		for (final SubRegion r : this.subregions)
			r.accept(visitor);
	}

	public void add(EntryState child) {
		assert child != null;
		child.setParent(this);
		this.childEntryStates.add(child);
	}

	public void remove(EntryState child) {
		assert child != null;
		child.unsetParent(this);
		this.childEntryStates.remove(child);
	}

	public void add(SubRegion child) {
		assert child != null;
		child.setParent(this);
		this.subregions.add(child);
	}

	public void removeSubregion(SubRegion child) {
		assert child != null;
		child.unsetParent(this);
		this.subregions.remove(child);
	}

	@Override
	public CSMComponent connectionLocation(State target) {
		assert target != null;
		if (this == target)
			return this;
		else
			return null;
	}

}

/**
 * 
 */
package csm.statetree;

import java.awt.Point;


/**
 * im Paper als S_entry bekannt
 * 
 * @author hsi
 */
public final class EntryState extends ConnectionPoint {

	public EntryState(Point position) {
		super(position);
	}

	@Override
	final void accept(Visitor visitor) {
		visitor.visitEntryState(this);
	}

	//
	// Connections ********************************

	@Override
	final CSMComponent transitionLocation(State target) {
		assert target != null;
		if (stateOf() != target.regOf().getParent())
			return null;
		if (target instanceof EntryState
				|| target instanceof ChoiceState
				|| target instanceof FinalState)
			return stateOf();
		else
			return null;
	}
}

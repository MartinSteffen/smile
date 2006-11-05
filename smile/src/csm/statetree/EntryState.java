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
	void accept(Visitor visitor) {
		visitor.visitEntryState(this);
	}

	//
	// Connections ********************************

	@Override
	public boolean mayConnectTo(State target) {
		assert target != null;
		if (stateOf() != target.stateOf().stateOf())
			return false;
		if (target instanceof EntryState)
			return true;
		if (target instanceof ExitState)
			return false;
		if (target instanceof ChoiceState)
			return true;
		if (target instanceof CompositeState)
			return false;
		if (target instanceof FinalState)
			return true;
		assert false; // never reached
		return false;
	}

}

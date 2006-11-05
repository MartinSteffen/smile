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
	public CSMComponent connectionLocation(State target) {
		assert target != null;
		// TODO Harald: regof or stateof
		if (stateOf() != target.stateOf().stateOf())
			return null;
		if (target instanceof EntryState
				|| target instanceof ChoiceState
				|| target instanceof FinalState)
			return stateOf();
		else
			return null;
	}
}

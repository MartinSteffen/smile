/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hsi
 * 
 */
public final class EntryState extends ConnectionPoint {

	private CompositeState parentComposite;

	@Override
	public final CSMComponent parent() {
		return this.parentComposite;
	}

	public EntryState(Point position, CompositeState parentComposite) {
		super(position);
		assert parentComposite != null;
		this.parentComposite = parentComposite;
		this.parentComposite.addChildEntryState(this);
	}

	//
	// Connections ********************************

	@Override
	public boolean mayConnectTo(State target) {
		if (this.stateOf() != target.stateOf().stateOf())
			return false;
		return target.mayConnectFromEntryState(this);
	}

	@Override
	protected final boolean mayConnectFromChoiceState(ChoiceState source) {
		return true;
	}

	@Override
	protected final boolean mayConnectFromEntryState(EntryState source) {
		return true;
	}

	@Override
	protected final boolean mayConnectFromExitState(ExitState source) {
		boolean sourceInFinal = source.stateOf() instanceof FinalState;
		return source.regOf() == this.regOf() && !sourceInFinal;
	}
}

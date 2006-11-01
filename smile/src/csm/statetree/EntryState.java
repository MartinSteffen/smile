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

	private CompositeState parentComposite;

	@Override
	public CSMComponent parent() {
		return this.parentComposite;
	}

	public EntryState(Point position, CompositeState parentComposite) {
		super(position);
		assert parentComposite != null;
		this.parentComposite = parentComposite;
		this.parentComposite.addChildEntryState(this);
	}

	@Override
	void visitMe(Visitor visitor) {
		visitor.visitEntryState(this);
	}

	//
	// Connections ********************************

	@Override
	public boolean mayConnectTo(State target) {
		assert target != null;
		if (stateOf() != target.stateOf().stateOf())
			return false;
		return target.mayConnectFromEntryState(this);
	}

	@Override
	boolean mayConnectFromChoiceState(ChoiceState source) {
		return true;
	}

	@Override
	boolean mayConnectFromEntryState(EntryState source) {
		return true;
	}

	@Override
	boolean mayConnectFromExitState(ExitState source) {
		boolean sourceInFinal = source.stateOf() instanceof FinalState;
		return source.regOf() == regOf() && !sourceInFinal;
	}

}

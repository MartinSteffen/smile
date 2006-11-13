package csm.statetree;

import java.awt.Point;

import csm.exceptions.ErrTreeNotChanged;


/**
 * im Paper als S_final bekannt
 * 
 * @author hsi
 */
public final class FinalState extends InternalState {

	public FinalState(Point position) {
		super(position);
	}

	@Override
	final void accept(Visitor visitor) {
		visitor.visitFinalState(this);
	}

	/**
	 * F�gt den Child-States dieser Komponente einen ExitState hinzu
	 * 
	 * @param child der hinzuzuf�gende ExitState
	 * @throws ErrTreeNotChanged wenn der ExitState schon das Child
	 *             irgendeiner Komponente ist
	 */
	final public void add(ExitState child) throws ErrTreeNotChanged {
		addAnyChild(child);
	}

	@Override
	CSMComponent transitionLocation(State target) {
		assert target != null;
		return null;
	}

}

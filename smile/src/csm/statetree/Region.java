package csm.statetree;

import java.awt.Point;

import csm.exceptions.ErrTreeNotChanged;


/**
 * Abstrakte Oberklasse aller Regionen
 * 
 * @author hsi
 */
public abstract class Region extends CSMComponent {

	Region(Point position) {
		super(position);
	}

	@Override
	final void accept(Visitor visitor) {
		visitor.visitRegion(this);
	}

	/**
	 * F�gt den Child-States dieser Region einen InternalState hinzu
	 * 
	 * @param child der hinzuzuf�gende InternalState
	 * @throws ErrTreeNotChanged wenn der InternalState schon das Child
	 *             irgendeiner Komponente ist, oder wenn er durch das
	 *             Hinzuf�gen ein Substate seiner selbst w�rde
	 */
	final public void add(InternalState child) throws ErrTreeNotChanged {
		addAnyChild(child);
	}

}

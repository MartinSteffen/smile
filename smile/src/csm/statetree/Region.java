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
	 * Fügt den Child-States dieser Region einen InternalState hinzu
	 * 
	 * @param child der hinzuzufügende InternalState
	 * @throws ErrTreeNotChanged wenn der InternalState schon das Child
	 *             irgendeiner Komponente ist, oder wenn er durch das
	 *             Hinzufügen ein Substate seiner selbst würde
	 */
	final public void add(InternalState child) throws ErrTreeNotChanged {
		addAnyChild(child);
	}

}

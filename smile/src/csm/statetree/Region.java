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
	 * Fuegt den Child-States dieser Region einen InternalState hinzu
	 * 
	 * @param child der hinzuzufuegende InternalState
	 * @throws ErrTreeNotChanged wenn der InternalState schon das Child
	 *             irgendeiner Komponente ist, oder wenn er durch das
	 *             Hinzufuegen ein Substate seiner selbst wuerde
	 */
	final public void add(InternalState child) throws ErrTreeNotChanged {
		addAnyChild(child);
	}

}

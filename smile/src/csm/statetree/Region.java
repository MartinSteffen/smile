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

	final public void add(InternalState child) throws ErrTreeNotChanged {
		addAnyChild(child);
	}

}

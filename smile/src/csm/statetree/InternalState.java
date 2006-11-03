/**
 * 
 */
package csm.statetree;

import java.awt.Point;


/**
 * @author hsi
 */
public abstract class InternalState extends State {

	private Region parentRegion;

	//
	// Semantik ***********************************

	@Override
	public final CSMComponent parent() {
		return this.parentRegion;
	}

	@Override
	public final InternalState stateOf() {
		return this;
	}

	//
	// Konstruktion *******************************

	InternalState(Point position, Region parentRegion) {
		super(position);
		assert parentRegion != null;
		this.parentRegion = parentRegion;
		this.parentRegion.addChildInternalState(this);
	}

}

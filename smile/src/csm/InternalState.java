/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hsi
 * 
 */
public abstract class InternalState extends State {

	private AbstractRegion parentRegion;

	//
	// Semantik ***********************************

	@Override
	public final CSMComponent parent() {
		return this.parentRegion;
	}

	@Override
	public final State stateOf() {
		return this;
	}

	//
	// Konstruktion *******************************

	public InternalState(Point position, AbstractRegion parentRegion) {
		super(position);
		assert parentRegion != null;
		this.parentRegion = parentRegion;
		this.parentRegion.addChildInternalState(this);
	}

}

/**
 * 
 */
package csm.statetree;

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
	public final InternalState stateOf() {
		return this;
	}

	//
	// Konstruktion *******************************

	protected InternalState(Point position, AbstractRegion parentRegion) {
		super(position);
		assert parentRegion != null;
		this.parentRegion = parentRegion;
		this.parentRegion.addChildInternalState(this);
	}

}

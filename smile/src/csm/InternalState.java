/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hsi
 * 
 */
public abstract class InternalState extends AbstractState {
	private Region parentRegion;

	public InternalState(Point position, Region parentRegion) {
		super(position);
		assert parentRegion != null;
		this.parentRegion = parentRegion;
		this.parentRegion.addChildInternalState(this);
	}

	@Override
	public final CSMComponent parent() {
		return this.parentRegion;
	}

	@Override
	public final AbstractState stateOf() {
		return this;
	}

}

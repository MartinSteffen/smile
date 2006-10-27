/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hsi
 * 
 */
public final class ExitState extends ConnectionPoint {
	private ExitableState parentExitable;

	public ExitState(Point position, ExitableState parentExitable) {
		super(position);
		assert parentExitable != null;
		this.parentExitable = parentExitable;
		this.parentExitable.addChildExitState(this);
	}

	@Override
	public final CSMComponent parent() {
		return this.parentExitable;
	}


}

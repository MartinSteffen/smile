/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hsi
 *
 */
public abstract class ConnectionPoint extends AbstractState {

	public ConnectionPoint(Point position) {
		super(position);
	}

	@Override
	public final AbstractState stateOf() {
		return (AbstractState) parent();
	}

}

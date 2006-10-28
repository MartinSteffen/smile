/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hsi
 *
 */
public abstract class ConnectionPoint extends State {

	@Override
	public final State stateOf() {
		return (State) parent();
	}

	public ConnectionPoint(Point position) {
		super(position);
	}

}

/**
 * 
 */
package csm.statetree;

import java.awt.Point;


/**
 * Abstrakte Oberklasse der Entry- und ExitStates
 * 
 * @author hsi
 */
public abstract class ConnectionPoint extends State {

	@Override
	public final InternalState stateOf() {
		return (InternalState) getParent();
	}

	ConnectionPoint(Point position) {
		super(position);
	}
}

/**
 * 
 */
package csm.statetree;

import java.awt.Point;


/**
 * @author hsi
 * 
 */
public abstract class ConnectionPoint extends State {

	@Override
	public final InternalState stateOf() {
		return (InternalState) parent();
	}

	ConnectionPoint(Point position) {
		super(position);
	}

	@Override
	final void visitChildren(CSMVisitor visitor) {
		// ConnectionPoins haben keine Child-States
	}

}

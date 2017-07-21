/**
 * 
 */
package csm.statetree;

import java.awt.Point;


/**
 * @author hsi
 */
public abstract class InternalState extends State {

	InternalState(Point position) {
		super(position);
	}

	@Override
	public final InternalState stateOf() {
		return this;
	}
}

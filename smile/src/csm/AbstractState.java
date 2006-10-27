/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hs
 *
 */
public abstract class AbstractState extends CSMComponent {

	/**
	 * @param position
	 */
	public AbstractState(Point position) {
		super(position);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see csm.CSMComponent#parent()
	 */
	@Override
	public CSMComponent parent() {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract AbstractState stateOf();

	public final Region regionOf() {
		return (Region) stateOf().parent();
	}
}

/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hs
 *
 */
public final class OutermostRegion extends AbstractRegion {

	@Override
	public final CSMComponent parent() {
		return null;
	}

	public OutermostRegion() {
		super(new Point(0,0));
	}

}

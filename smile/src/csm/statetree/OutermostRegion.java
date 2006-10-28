/**
 * 
 */
package csm.statetree;

import java.awt.Point;

/**
 * im Paper als Region \epsilon bekannt (parent() ist null)
 * 
 * @author hs
 */
public final class OutermostRegion extends AbstractRegion {

	@Override
	public final CSMComponent parent() {
		return null;
	}

	public OutermostRegion() {
		super(new Point(0, 0));
	}

}

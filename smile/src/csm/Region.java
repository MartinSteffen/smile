/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hsi
 * 
 */
public class Region extends AbstractRegion {
	
	private CompositeState parentComposite;

	@Override
	public final CSMComponent parent() {
		return this.parentComposite;
	}

	public Region(Point position, CompositeState parentComposite) {
		super(position);
		assert parentComposite != null;
		this.parentComposite = parentComposite;
		this.parentComposite.addSubregion(this);
	}

}

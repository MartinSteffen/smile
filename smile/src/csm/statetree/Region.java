/**
 * 
 */
package csm.statetree;

import java.awt.Point;


/**
 * @author hsi
 * 
 */
public final class Region extends AbstractRegion {
	
	private CompositeState parentComposite;

	@Override
	public CSMComponent parent() {
		return this.parentComposite;
	}

	public Region(Point position, CompositeState parentComposite) {
		super(position);
		assert parentComposite != null;
		this.parentComposite = parentComposite;
		this.parentComposite.addSubregion(this);
	}





}
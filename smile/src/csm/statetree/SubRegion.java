/**
 * 
 */
package csm.statetree;

import java.awt.Point;


/**
 * eine Unterregion eines Composite State (parent() ungleich null)
 * 
 * @author hsi
 */
public final class SubRegion extends Region {

	private CompositeState parentComposite;

	@Override
	public CSMComponent parent() {
		return this.parentComposite;
	}

	public SubRegion(Point position, CompositeState parentComposite) {
		super(position);
		assert parentComposite != null;
		this.parentComposite = parentComposite;
		this.parentComposite.addSubregion(this);
	}

}

/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * @author hsi
 *
 */
public final class EntryState extends ConnectionPoint {
	private CompositeState parentComposite;

	public EntryState(Point position, CompositeState parentComposite) {
		super(position);
		assert parentComposite != null;
		this.parentComposite = parentComposite;
		this.parentComposite.addChildEntryState(this);
	}

	@Override
	public final CSMComponent parent() {
		return this.parentComposite;
	}
}

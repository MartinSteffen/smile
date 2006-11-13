/**
 * 
 */
package csm.statetree;

import java.awt.Point;


/**
 * eine Unterregion eines Composite State (parent() ungleich null)
 * <p>
 * Diese Klasse kann als Unterregion in CompositeStates eingetragen
 * werden. Ansonsten ist sie mit der Region identisch.
 * 
 * @author hsi
 */
public final class SubRegion extends Region {

	public SubRegion(Point position) {
		super(position);
	}

}

/**
 * 
 */
package csm;

import java.awt.Point;

/**
 * Abstrakte Oberklasse aller States
 * <p>
 * besitzt die im Paper unter Definition 1 angegebenen Funktionen stateOf und
 * regionOf
 * 
 * @author hsi
 */
public abstract class AbstractState extends CSMComponent {

	public AbstractState(Point position) {
		super(position);
	}

	public abstract AbstractState stateOf();

	public final Region regOf() {
		return (Region) stateOf().parent();
	}
}

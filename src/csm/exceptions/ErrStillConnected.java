/**
 * Erstellt am 21.11.200615:45:38 Erstellt von rachid Projekt smile
 */
package csm.exceptions;

import csm.statetree.Transition;


/**
 * @author rachid
 */
public class ErrStillConnected extends CSMEditException {

	private static final long serialVersionUID = 1L;

	public final Transition transition;

	/**
	 * @param message
	 */
	public ErrStillConnected(Transition t) {
		super("there is at least one transition");
		this.transition = t;
	}

}

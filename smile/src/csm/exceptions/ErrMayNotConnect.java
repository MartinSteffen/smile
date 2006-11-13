/**
 * 
 */
package csm.exceptions;


/**
 * zeigt an, dass der VErsuch, zwei States mit einer Transition zu
 * verbinden, gescheitert ist
 * 
 * @author hsi
 */
public class ErrMayNotConnect extends CSMEditException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public ErrMayNotConnect() {
		super("illegal transition");
	}

}

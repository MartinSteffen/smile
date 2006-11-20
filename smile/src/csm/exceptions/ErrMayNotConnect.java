/**
 * 
 */
package csm.exceptions;

/**
 * zeigt an, dass der Versuch, zwei States mit einer Transition zu
 * verbinden, gescheitert ist
 * 
 * @author hsi
 */
public class ErrMayNotConnect extends CSMEditException {

	private static final long serialVersionUID = 1L;

	/**
	 * Zeigt an, dass versucht wurde, eine unerlaubte Transition
	 * zwischen zwei States zu erzeugen. Der Text dieser Exception ist
	 * lediglich "illegal transition".
	 */
	public ErrMayNotConnect() {
		super("illegal transition");
	}

}

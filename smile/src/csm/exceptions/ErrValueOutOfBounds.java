/**
 * 
 */
package csm.exceptions;

/**
 * Zeigt an, dass versucht wurde, einen Zahlenwert zu setzen, der
 * auﬂerhalb der erlaubten Grenzen liegt. Wird zum Beispiel von der
 * Klasse Variable verwendet, damit Minimal-, Maximal- und Initialwert
 * konsistent sind.
 * 
 * @author hs
 */
public final class ErrValueOutOfBounds extends CSMEditException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message ein kurzer Text, aus dem hervorgeht, welche Grenze
	 *            verletzt wurde
	 */
	public ErrValueOutOfBounds(String message) {
		super(message);
	}

}

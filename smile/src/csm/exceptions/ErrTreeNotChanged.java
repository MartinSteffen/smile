/**
 * 
 */
package csm.exceptions;

/**
 * Zeigt an, dass eine �nderung des Komponentenbaums gescheitert ist.
 * 
 * @author hsi
 */
public class ErrTreeNotChanged extends CSMEditException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message ein kurzer Text, der anzeigt, warum die �nderung
	 *            gescheitert ist
	 */
	public ErrTreeNotChanged(String message) {
		super(message);
	}

}

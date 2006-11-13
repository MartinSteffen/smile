/**
 * 
 */
package csm.exceptions;

/**
 * Zeigt an, dass eine Änderung des Komponentenbaums gescheitert ist.
 * 
 * @author hsi
 */
public class ErrTreeNotChanged extends CSMEditException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message ein kurzer Text, der anzeigt, warum die Änderung
	 *            gescheitert ist
	 */
	public ErrTreeNotChanged(String message) {
		super(message);
	}

}

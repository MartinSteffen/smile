/**
 * 
 */
package csm.exceptions;

/**
 * zeigt an, dass eine Änderung des Komponentenbaums gescheitert ist
 * 
 * @author hsi
 */
public class ErrTreeNotChanged extends CSMEditException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public ErrTreeNotChanged(String message) {
		super(message);
	}

}

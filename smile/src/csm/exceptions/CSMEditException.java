/**
 * 
 */
package csm.exceptions;

/**
 * @author hs
 */
abstract class CSMEditException extends Exception {

	/**
	 * @param message
	 */
	public CSMEditException(String message) {
		super(message);
	}

	/**
	 * TODO eventuell löschen
	 * 
	 * @param message
	 * @param cause
	 */
	@Deprecated
	public CSMEditException(String message, Throwable cause) {
		super(message, cause);

	}

}

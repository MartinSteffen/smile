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
	 * TODO eventuell l�schen
	 * 
	 * @param message
	 * @param cause
	 */
	public CSMEditException(String message, Throwable cause) {
		super(message, cause);

	}

}

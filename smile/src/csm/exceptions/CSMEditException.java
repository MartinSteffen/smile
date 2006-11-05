/**
 * 
 */
package csm.exceptions;

/**
 * @author hsi
 */
abstract class CSMEditException extends Exception {

	public CSMEditException(String message) {
		super(message);
	}

	@Deprecated
	public CSMEditException(String message, Throwable cause) {
		super(message, cause);

	}

}

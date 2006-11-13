/**
 * 
 */
package csm.exceptions;

/**
 * @author hsi
 */
abstract public class CSMEditException extends Exception {

	public CSMEditException(String message) {
		super(message);
	}

	@Deprecated
	public CSMEditException(String message, Throwable cause) {
		super(message, cause);

	}

}

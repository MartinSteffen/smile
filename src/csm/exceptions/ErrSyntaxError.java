/**
 * 
 */
package csm.exceptions;

/**
 * Zeigt an, dass beim Parsen eines Ausdrucks ein Syntaxfehler
 * aufgetreten ist
 * 
 * @author hs
 */
public final class ErrSyntaxError extends CSMEditException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param msg die Fehlermeldung des Parsers
	 */
	public ErrSyntaxError(String msg) {
		super(msg);
	}

}

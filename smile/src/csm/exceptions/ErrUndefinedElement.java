/**
 * 
 */
package csm.exceptions;

/**
 * @author hs
 */
public final class ErrUndefinedElement extends CSMEditException {

	private static final long serialVersionUID = 1L;

	/**
	 * Zeigt an, dass versucht wurde, aus einem Dictionary einen Wert zu
	 * lesen, der darin nicht vorhanden ist.
	 * <p>
	 * erzeugt eine Exception mit dem Text "$name is not defined", wobei
	 * für $name der übergebene Name eingesetzt wird
	 */
	public ErrUndefinedElement(String name) {
		super(name + " is not defined");
	}

}

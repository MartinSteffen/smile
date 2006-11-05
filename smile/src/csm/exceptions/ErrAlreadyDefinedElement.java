/**
 * 
 */
package csm.exceptions;

/**
 * Zeigt an, dass versucht wurde, einem Dictionary einen Wert
 * hinzuzuf�gen, der dort bereits vorhanden ist.
 * 
 * @author hsi
 */
public final class ErrAlreadyDefinedElement extends CSMEditException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * erzeugt eine Exception mit dem Text "$name is already defined",
	 * wobei f�r $name der �bergebene Name eingesetzt wird
	 */
	public ErrAlreadyDefinedElement(String name) {
		super(name + " is already defined");
	}
}

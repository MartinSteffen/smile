/**
 * 
 */
package csm.exceptions;

/**
 * Zeigt an, dass versucht wurde, einem Dictionary einen Wert
 * hinzuzufuegen, der dort bereits vorhanden ist.
 * 
 * @author hsi
 */
public final class ErrAlreadyDefinedElement extends CSMEditException {

	private static final long serialVersionUID = 1L;

	/**
	 * der Name, der bereits im Dictionary enthalten ist
	 */
	final public String name;

	/**
	 * Erzeugt eine Exception mit dem Text "$name is already defined",
	 * wobei fuer $name der uebergebene Name eingesetzt wird
	 * 
	 * @param name der Name, der bereits im Dictionary enthalten ist
	 */
	public ErrAlreadyDefinedElement(String name) {
		super(name + " is already defined");
		this.name = name;
	}
}

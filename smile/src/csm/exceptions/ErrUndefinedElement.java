/**
 * 
 */
package csm.exceptions;

/**
 * Zeigt an, dass versucht wurde, aus einem Dictionary einen Wert zu
 * lesen, der darin nicht vorhanden ist.
 * 
 * @author hs
 */
public final class ErrUndefinedElement extends CSMEditException {

	private static final long serialVersionUID = 1L;

	/**
	 * der Name des Elementes, das im Dictionary nicht gefunden wurde
	 */
	final public String name;

	/**
	 * Erzeugt eine Exception mit dem Text "$name is not defined", wobei
	 * für $name der übergebene Name eingesetzt wird.
	 * 
	 * @param name der Name, der nicht gefunden wurde
	 */
	public ErrUndefinedElement(String name) {
		super(name + " is not defined");
		this.name = name;
	}

}

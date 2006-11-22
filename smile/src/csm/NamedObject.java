package csm;

import java.util.Observable;

import csm.exceptions.ErrAlreadyDefinedElement;


/**
 * ein benanntes Objekt, das in einem {@link Dictionary} verwaltet
 * werden kann
 * <p>
 * Der Name kann nicht von ausserhalb des Packages gesetzt werden, da
 * sonst nicht sicherzustellen ist, dass in einem {@link Dictionary}
 * jeder Name nur einmal vorkommt.
 */
public abstract class NamedObject extends Observable {

	final public Dictionary parent;

	private String name;

	/**
	 * @param name ungleich null
	 * @throws ErrAlreadyDefinedElement
	 */
	NamedObject(Dictionary parent, String name)
			throws ErrAlreadyDefinedElement {
		assert parent != null;
		this.parent = parent;
		parent.add(this);
		setName(name);
	}

	/**
	 * @param name ungleich null
	 */
	final void setName(String name) {
		assert name != null;
		this.name = name;
	}

	final public String getName() {
		return this.name;
	}
}

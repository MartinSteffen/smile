package csm;

/**
 * ein benanntes Objekt, das in einem {@link Dictionary} verwaltet
 * werden kann
 * <p>
 * Der Name kann nicht von au�erhalb des Packages gesetzt werden, da
 * sonst nicht sicherzustellen ist, dass in einem {@link Dictionary}
 * jeder Name nur einmal vorkommt.
 */
public abstract class NamedObject {

	private String name;

	/**
	 * @param name ungleich null
	 */
	NamedObject(String name) {
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

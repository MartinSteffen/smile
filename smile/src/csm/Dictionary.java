package csm;

import java.util.HashMap;
import java.util.Observable;

import csm.exceptions.ErrAlreadyDefinedElement;
import csm.exceptions.ErrUndefinedElement;


/**
 * excute the list of the events and the list of the variable which will
 * be used from the csm the hash map contents coulden't be managed from
 * the outside of the package
 * 
 * @param contents
 */

public final class Dictionary<Elem extends NamedObject> extends
		Observable {

	final private HashMap<String, Elem> contents = new HashMap<String, Elem>();

	/**
	 * Returns true if the map contents contains a mapping for the
	 * specified key
	 */
	public boolean contains(String key) {
		assert key != null;
		return this.contents.containsKey(key);
	}

	/**
	 * @param elem to look for
	 * @throws ErrUndefinedElement if the map contents dosen't contains
	 *             a mapping for the specified elem.getName
	 */
	public void mustContain(Elem elem) throws ErrUndefinedElement {
		mustContain(elem.getName());
	}

	/**
	 * throws an exception if the map contents dosen't contains a
	 * mapping for the specified key
	 * 
	 * @param key to look for
	 * @throws ErrUndefinedElement
	 */

	public void mustContain(String key) throws ErrUndefinedElement {
		assert key != null;
		if (!contains(key))
			throw new ErrUndefinedElement(key);
	}

	/**
	 * throws an exception if the map contents already contains a
	 * mapping for the specified elem.getName
	 * 
	 * @param elem to look for
	 * @throws ErrAlreadyDefinedElement
	 */

	public void mayNotContain(Elem elem)
			throws ErrAlreadyDefinedElement {
		mayNotContain(elem.getName());
	}

	/**
	 * throws an exception if the map contents already contains a
	 * mapping for the specified key
	 * 
	 * @param key to look for
	 * @throws ErrAlreadyDefinedElement
	 */
	public void mayNotContain(String key)
			throws ErrAlreadyDefinedElement {
		assert key != null;
		if (contains(key))
			throw new ErrAlreadyDefinedElement(key);
	}

	/**
	 * Returns the elem to which the specified key is mapped in this
	 * identity hash map or throws the exception ErrUndefinedElement if
	 * the map contents contains no mapping for this key.
	 * 
	 * @param key to look for
	 * @throws ErrUndefinedElement,
	 */
	public Elem get(String key) throws ErrUndefinedElement {
		assert key != null;
		this.mustContain(key);
		return this.contents.get(key);
	}

	/**
	 * Associates the specified elem with the specified key in this map.
	 * throws an exception If the map previously contained a mapping for
	 * this key
	 * 
	 * @param elem
	 * @throws ErrAlreadyDefinedElement
	 */
	// TODO Funktionalität in Konstruktor von Elem schieben
	@Deprecated
	void add(Elem elem) throws ErrAlreadyDefinedElement {
		assert elem != null;
		this.mayNotContain(elem.getName());
		contents.put(elem.getName(), elem);
	}

	/**
	 * * renams the elem to which the specified key is mapped in this
	 * hash map and throws an exception if the map contents haven't got
	 * the specified key
	 * <p>
	 * Diese Methode benennt ein Element nur im Dictionary um, aber
	 * nicht an den Stellen, an denen es verwendet wird. Deshalb sollte
	 * sie niemals aufgerufen werden!
	 * <p>
	 * 
	 * @param key
	 * @param newkey
	 * @throws ErrUndefinedElement
	 * @throws ErrAlreadyDefinedElement
	 */
	@Deprecated
	public void rename(String key, String newkey)
			throws ErrUndefinedElement, ErrAlreadyDefinedElement {
		assert key != null;
		this.mustContain(key);
		Elem e = this.remove(key);
		e.setName(newkey);
		this.add(e);

	}

	/**
	 * removes the mapping for this key from this map if
	 * present.otherwise throws an exception
	 * <p>
	 * Diese Methode entfernt ein Element auch dann, wenn es woanders
	 * noch gebraucht wird. Deshalb sollte sie niemals verwendet werden!
	 * <p>
	 * 
	 * @param key
	 * @throws ErrUndefinedElement wenn das Dictionary kein Element
	 *             dieses Namens enthält
	 */
	@Deprecated
	public Elem remove(String key) throws ErrUndefinedElement {
		assert key != null;
		this.mustContain(key);
		return this.contents.remove(key);
	}

	/**
	 * @return eine neues Array, das die Initialwerte aller definierten
	 *         Variablen enthält
	 */
	public HashMap<String, Integer> getInitials() {
		// TODO Auto-generated method stub
		return null;
	}
}

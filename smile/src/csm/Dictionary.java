package csm;

import java.util.HashMap;
import java.util.Observable;

import csm.exceptions.ErrAlreadyDefinedElement;
import csm.exceptions.ErrUndefinedElement;

/**
 * verwaltet die Liste aller Events und die Liste aller , die von
 * der CSM verwendet werden.
 *  die Hashtabelle kann nicht von au�erhalb des Packages 
 * beeinflu�t werden
 * @param contents 
 */

public final class Dictionary<Elem extends NamedObject> extends
		Observable {

	final private HashMap<String, Elem> contents = new HashMap<String, Elem>();

	// TODO: rename / remove
/**
 *   Returns true if the map contents contains a mapping for the specified key
 */
	public boolean contains(String key) {
		assert key != null;
		return this.contents.containsKey(key);
	}

	/**
	 * @param elem  to look for
     * @throws ErrUndefinedElement if the map contents dosen't contains a mapping for 
     * the specified elem.getName
	 */
	public void mustContain(Elem elem) throws ErrUndefinedElement {
		mustContain(elem.getName());
	}

/**
 * throws an exception if the map contents dosen't contains a mapping for the specified key
 * @param key to look for
 * @throws ErrUndefinedElement
 */

	public void mustContain(String key) throws ErrUndefinedElement {
		assert key != null;
		if (!contains(key))
			throw new ErrUndefinedElement(key);
	}
	/**
	 * throws an exception if the map contents already contains a mapping for 
	 * the specified elem.getName 
	 * @param elem to look for
	 * @throws ErrAlreadyDefinedElement 
	 */
	
	public void mayNotContain(Elem elem)
			throws ErrAlreadyDefinedElement {
		mayNotContain(elem.getName());
	}
	/**
	 * throws an exception if the map contents already contains a mapping for the specified key
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
 *   Returns the elem to which the specified key is mapped 
 *   in this identity hash map or throws the exception ErrUndefinedElement
 *   if the map contents contains no mapping for this key.
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
 *  throws an exception If the map previously contained a mapping for this key
 * @param elem
 * @throws ErrAlreadyDefinedElement
 */
	public void set(Elem elem) throws ErrAlreadyDefinedElement {
		assert elem != null;
		this.mayNotContain(elem.getName());
		contents.put(elem.getName(), elem);
	}
/**
 * * renams the elem to which the specified key is mapped 
 * in this hash map and throws an exception if the map contents haven't got
 * the specified key
 * @param key
 * @param newkey
 * @throws ErrUndefinedElement
 * @throws ErrAlreadyDefinedElement
 */
	public void rename(String key,String newkey) throws ErrUndefinedElement, ErrAlreadyDefinedElement
	{
		assert key != null;
		this.mustContain(key);
		Elem e = this.remove(key);
		e.setName(newkey);
		this.set(e);
		
	}
	/**
	 * removes the mapping for this key from this map if 
	 * present.otherwise throws an exception
	 * @param key
	 * @throws ErrUndefinedElement
	 */
	public Elem remove(String key) throws ErrUndefinedElement
	{
		assert key != null;
		this.mustContain(key);
		return this.contents.remove(key);
	}
}

package csm;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import csm.exceptions.ErrAlreadyDefinedElement;
import csm.exceptions.ErrTreeNotChanged;
import csm.exceptions.ErrUndefinedElement;


/**
 * excute the list of the events and the list of the variable which will
 * be used from the csm the hash map contents coulden't be managed from
 * the outside of the package
 * 
 * @param contents
 */

public final class Dictionary<Elem extends NamedObject> extends
		ModelNode<CoreStateMachine> {

	final private HashMap<String, Elem> contents = new HashMap<String, Elem>();

	Dictionary(CoreStateMachine csm) {
		assert csm != null;
		try {
			setParent(csm);
		} catch (final ErrTreeNotChanged e) {
			assert false : "should never happen";
		}
	}

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

	public void mayNotContain(Elem elem) throws ErrAlreadyDefinedElement {
		mayNotContain(elem.getName());
	}

	/**
	 * throws an exception if the map contents already contains a
	 * mapping for the specified key
	 * 
	 * @param key to look for
	 * @throws ErrAlreadyDefinedElement
	 */
	public void mayNotContain(String key) throws ErrAlreadyDefinedElement {
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
	/*
	 * Enthält der Name Gänsefüßchen, dann kann die CSM nicht mehr
	 * geladen und gespeichert werden
	 */

	void add(Elem elem) throws ErrAlreadyDefinedElement {
		assert elem != null;

		this.mayNotContain(elem.getName());
		try {
			elem.setParent(this);
		} catch (final ErrTreeNotChanged e) {
			// sollte niemals eintreten, da dieses Objekt neu erzeugt
			// ist und keine Child-Objekte hat
			assert false : "should never happen";
		}
		this.contents.put(elem.getName(), elem);
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
		assert false : "defunct";
		assert key != null;
		this.mustContain(key);
		// final Elem e = this.remove(key);
		// e.setName(newkey);
		// this.add(e);
		announceChanges();
		throw new ErrAlreadyDefinedElement(newkey);
	}

	/**
	 * Entfernt eine Definition aus dem Dictionary, sofern diese
	 * Definition nicht mehr benötigt wird.
	 * 
	 * @param key
	 * @throws ErrTreeNotChanged wenn das Element nocht gelöscht werden
	 *             konnte, weil es noch in Gebrauch ist
	 */
	public void remove(String key) throws ErrTreeNotChanged {
		try {
			Elem value = get(key);
			Set<String> defs = getKeys();
			defs.remove(key);
			// wie man sieht, wäre diese Methode in CoreStateMachine
			// besser aufgehoben
			if (value instanceof Event
				&& getParent().region.firstUndefinedEvent(defs) != null)
				throw new ErrTreeNotChanged("event " + key + " is in use");
			else if (value instanceof Variable
				&& getParent().region.firstUndefinedVar(defs) != null)
				throw new ErrTreeNotChanged("variable " + key
					+ " is in use");
			contents.remove(key);
		} catch (ErrUndefinedElement e) {
			// was nicht da ist, muss auch nicht entfernt werden
		}
	}

	/**
	 * @return eine neues Array, das die Initialwerte aller definierten
	 *         Variablen enthaelt
	 * @deprecated use getContents()
	 */
	@Deprecated
	public HashMap<String, Integer> getInitials() {
		final HashMap<String, Integer> r = new HashMap<String, Integer>();
		final Iterator<String> it = this.contents.keySet().iterator();
		while (it.hasNext()) {
			final String key = it.next();
			// heikle Sache, das:
			final Variable v = (Variable) this.contents.get(key);
			final int value = v.getInitialValue();
			r.put(key, value);
		}
		return r;
	}

	/**
	 * @return eine UnmodifyableMap, die den Namen der enthaltenen
	 *         Elemente diese Elemente zuordnet
	 */
	public Collection<Elem> getContents() {
		return Collections.unmodifiableCollection(this.contents.values());
	}

	/**
	 * @return a set of all Keys
	 */
	public Set<String> getKeys() {
		return new HashSet<String>(this.contents.keySet());
	}
}

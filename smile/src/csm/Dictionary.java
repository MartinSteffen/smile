package csm;

import java.util.HashMap;
import java.util.Observable;

import csm.exceptions.ErrAlreadyDefinedElement;
import csm.exceptions.ErrUndefinedElement;


public final class Dictionary<Elem extends NamedObject> extends Observable{

	final private HashMap<String, Elem> contents = new HashMap<String, Elem>();

	// TODO: rename / remove

	public boolean contains(String key) {
		assert key != null;
		return this.contents.containsKey(key);

	}

	// TODO void check() throws ...
	
	public Elem get(String key) throws ErrUndefinedElement {
		assert key != null;
		final Elem elem = this.contents.get(key);
		if (elem != null)
			return elem;
		else
			throw new ErrUndefinedElement(key);
	}

	public void set(Elem elem) throws ErrAlreadyDefinedElement {
		assert elem != null;
		if (contains(elem.getName()))
			throw new ErrAlreadyDefinedElement(elem.getName());
		contents.put(elem.getName(), elem);
	}
}

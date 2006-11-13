package csm;

import java.util.HashMap;
import java.util.Observable;

import csm.exceptions.ErrAlreadyDefinedElement;
import csm.exceptions.ErrUndefinedElement;

// TODO kommentieren
public final class Dictionary<Elem extends NamedObject> extends
		Observable {

	final private HashMap<String, Elem> contents = new HashMap<String, Elem>();

	// TODO: rename / remove

	public boolean contains(String key) {
		assert key != null;
		return this.contents.containsKey(key);
	}

	public void mustContain(Elem elem) throws ErrUndefinedElement {
		mustContain(elem.getName());
	}

	public void mustContain(String key) throws ErrUndefinedElement {
		assert key != null;
		if (!contains(key))
			throw new ErrUndefinedElement(key);
	}

	public void mayNotContain(Elem elem)
			throws ErrAlreadyDefinedElement {
		mayNotContain(elem.getName());
	}

	public void mayNotContain(String key)
			throws ErrAlreadyDefinedElement {
		assert key != null;
		if (contains(key))
			throw new ErrAlreadyDefinedElement(key);
	}

	public Elem get(String key) throws ErrUndefinedElement {
		assert key != null;
		this.mustContain(key);
		return this.contents.get(key);
	}

	public void set(Elem elem) throws ErrAlreadyDefinedElement {
		assert elem != null;
		this.mayNotContain(elem.getName());
		contents.put(elem.getName(), elem);
	}

}

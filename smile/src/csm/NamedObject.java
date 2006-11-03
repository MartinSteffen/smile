package csm;

public abstract class NamedObject {

	String name;

	NamedObject(String name) {
		setName(name);
	}

	final void setName(String name) {
		assert name != null;
		this.name = name;
	}

	final public String getName() {
		return this.name;
	}
}

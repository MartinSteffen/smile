/**
 * 
 */
package csm;

import csm.exceptions.ErrAlreadyDefinedElement;

/**
 * Events sind "irgendwelche" benannte Objekte, die sich von anderen
 * Objekten unterscheiden.
 * 
 * @author hsi
 */
public final class Event extends NamedObject {

	public Event(Dictionary<Event> parent, String name) throws ErrAlreadyDefinedElement {
		super(parent, name);
	}

	final void tumbleweed() {
	}
}

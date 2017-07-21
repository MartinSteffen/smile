/**
 * 
 */
package csm;

import csm.exceptions.ErrAlreadyDefinedElement;
import csm.exceptions.ErrSyntaxError;


/**
 * Events sind "irgendwelche" benannte Objekte, die sich von anderen
 * Objekten unterscheiden.
 * 
 * @author hsi
 */
public final class Event extends NamedObject {

	/**
	 * @param parent das Dictionary, in dem dieses Objekt verwaltet
	 *            wird. Muss ungleich null sein.
	 * @param name der Name des Elements; ein String ohne Leerzeichen
	 *            oder Anfuehrungszeichen. Muss ungleich null sein.
	 * @throws ErrAlreadyDefinedElement wenn sich in dem uebergebenen
	 *             Dictionary bereits ein Element dieses Namens
	 *             befindet.
	 * @throws ErrSyntaxError 
	 */
	public Event(Dictionary<Event> parent, String name)
			throws ErrAlreadyDefinedElement, ErrSyntaxError {
		super(name);
		assert parent != null;

		parent.add(this);
		announceChanges();
	}

	final void tumbleweed() {
		// nothing ever happens
	}
}

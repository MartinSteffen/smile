/**
 * 
 */
package csm;

import java.util.HashMap;

import csm.statetree.CSMComponent;


/**
 * Mithilfe dieses Objektes k�nnen Metadaten f�r eine graphische
 * Oberfl�che gespeichert werden.
 * 
 * @author hs
 */
public class GuiMetadata {

	public final String guiId;
	public final HashMap<CSMComponent, String> data;

	GuiMetadata(String guiId) {
		assert guiId != null;
		this.guiId = guiId;
		data = new HashMap<CSMComponent, String>();
	}
}

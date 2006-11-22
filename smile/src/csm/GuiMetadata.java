/**
 * 
 */
package csm;

import java.util.HashMap;

import csm.statetree.CSMComponent;


/**
 * Mithilfe dieses Objektes koennen Metadaten fuer eine graphische
 * Oberflaeche gespeichert werden.
 * 
 * @author hs
 */
public class GuiMetadata {

	public final String guiId;
	public final HashMap<CSMComponent, String> data;

	GuiMetadata(String guiId) {
		assert guiId != null;
		this.guiId = guiId;
		this.data = new HashMap<CSMComponent, String>();
	}
}

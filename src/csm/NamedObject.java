package csm;

import csm.exceptions.ErrSyntaxError;
import csm.expression.parser.ExpressionParser;

/**
 * ein benanntes Objekt, das in einem {@link Dictionary} verwaltet
 * werden kann
 * <p>
 * Der Name kann nicht von ausserhalb des Packages gesetzt werden, da
 * sonst nicht sicherzustellen ist, dass in einem {@link Dictionary}
 * jeder Name nur einmal vorkommt.
 */
public abstract class NamedObject extends ModelNode<Dictionary<? extends NamedObject>> {

	private String name;

	/**
	 * @param name der Name des Elements; ein String ohne Leerzeichen
	 *            oder Anfuehrungszeichen. Muss ungleich null sein.
	 * @throws ErrSyntaxError 
	 */
	NamedObject(String name) throws ErrSyntaxError {
		assert name != null;
		this.name =ExpressionParser.parseIdent(name);
	}

	final public String getName() {
		return this.name;
	}
}

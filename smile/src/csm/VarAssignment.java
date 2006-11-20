package csm;

import java.util.HashMap;


/**
 * eine Variablenbelegung
 * <p>
 * Variablenbelegungen werden nur in der semantischen Analyse verwendet.
 * Die initiale Variablenbelegung wird in den Variablen-Objekten
 * gesetzt.
 */
public final class VarAssignment {

	private final Dictionary<Variable> dictionary;

	private final HashMap<String, Integer> values;

	/**
	 * erzeugt ein initiales Variablen-Assignment aus der Variablenliste
	 * der CoreStateMachine. Dabei werden alle Variablen auf den in
	 * deren Variablenliste eingetragenen Initialwert gesetzt.
	 * 
	 * @param variableList die Liste der Variablen,
	 */
	public VarAssignment(Dictionary<Variable> dictionary) {
		assert dictionary != null;
		this.dictionary = dictionary;
		this.values = dictionary.getInitials();
	}

	/**
	 * ermittelt den Integer-Wert, der in diesem VarAssignment an einen
	 * Variablennamen gebunden ist. Ist dem übergebenen Variablennamen
	 * kein Wert zugeordnet, dann ist das Verhalten der Funktion
	 * undefiniert.
	 * 
	 * @param varname der Variablenname
	 */
	public int lookupVar(String varname) {
		assert varname != null;
		assert false; // oops
		return 0;
	}

	/**
	 * Setzt den einem Variablennamen zugeordneten Integer-Wert. Dabei
	 * werden keinerlei Überprüfunen des Gültigkeitsbereichs
	 * vorgenommen. Ist an den übergebenen Variablennamen bei der
	 * Erzeugung des VarAssignment-Objektes kein Wert gebunden worden,
	 * dann ist das Verhalten der Funktion undefiniert.
	 */
	public VarAssignment setVar(String n, int value) {
		assert false; // TODO oops
		return null;
	}
}

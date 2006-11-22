package csm;

import java.util.HashMap;


/**
 * eine Variablenbelegung
 * <p>
 * Variablenbelegungen werden nur in der semantischen Analyse verwendet.
 * Die initiale Variablenbelegung wird in den Variablen-Objekten
 * gesetzt.
 * <p>
 * neben den aktuellen Werten enthaelt ein VarAssignment den Namen und
 * den Wert des im letzten Schritt gesendeten Events: Wenn eine Aktion
 * keinen Event sendet, muss sie den Namen sendEventName auf null
 * setzen. wenn sie einen Event senden will, muss sie sendEventName und
 * sendEventValue setzen.
 */
public final class ExpressionEnvironment {

	private final Dictionary<Variable> dictionary;

	private final HashMap<String, Integer> values;

	/**
	 * der Name des in der letzten Aktion gesendeten Events ode null,
	 * wenn kein Event gesendet wurde
	 */
	public String sendEventName;

	public int sendEventValue;

	public Boolean nab;

	public Boolean wla;

	/**
	 * erzeugt ein initiales Variablen-Assignment aus der Variablenliste
	 * der CoreStateMachine. Dabei werden alle Variablen auf den in
	 * deren Variablenliste eingetragenen Initialwert gesetzt.
	 * 
	 * @param variableList die Liste der Variablen,
	 */
	public ExpressionEnvironment(Dictionary<Variable> dictionary) {
		assert dictionary != null;
		this.dictionary = dictionary;
		this.values = dictionary.getInitials();
	}

	/**
	 * ermittelt den Integer-Wert, der in diesem VarAssignment an einen
	 * Variablennamen gebunden ist. Ist dem uebergebenen Variablennamen
	 * kein Wert zugeordnet, dann ist das Verhalten der Funktion
	 * undefiniert.
	 * 
	 * @param varname der Variablenname
	 */
	public int lookupVar(String varname) {
		assert varname != null;
		return this.values.get(varname);
	}

	/**
	 * Setzt den einem Variablennamen zugeordneten Integer-Wert. Dabei
	 * werden keinerlei Ueberpruefungen des Gueltigkeitsbereichs
	 * vorgenommen. Ist an den uebergebenen Variablennamen bei der
	 * Erzeugung des VarAssignment-Objektes kein Wert gebunden worden,
	 * dann ist das Verhalten der Funktion undefiniert.
	 */
	public void setVar(String n, int value) {
		assert n != null;
		this.values.put(n, value);
	}

	/**
	 * erzeugt eine Kopie des Environments, in der sendEventName auf
	 * null gesetzt ist
	 */
	public ExpressionEnvironment(ExpressionEnvironment e) {
		this.dictionary = e.dictionary;
		this.values = new HashMap<String, Integer>(e.values);
		this.nab = e.nab;
		this.wla = e.wla;
		this.sendEventName = null;
	}
}

package csm;

import java.io.StringWriter;
import java.util.HashMap;

import csm.exceptions.ErrUndefinedElement;


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

	/*
	 * Falls diese Variablenbelegiung einen Wertebereich überschreitet,
	 * gibt varOutOfBounds an, welche Variable außerhalb ihres Bereichs
	 * liegt
	 */
	String varOutOfBounds;

	/**
	 * der Name des in der letzten Aktion gesendeten Events oder null,
	 * wenn kein Event gesendet wurde
	 */
	// XXX Ergebnis von Action#evaluate als Tupel(Environment, Event)?
	/*
	 * Ein unschöner Hack: Nachdem eine Action ausgewertet wurde, muss
	 * dieser Wert getestet werden, um festzustellen, ob die Action
	 * einen Event ausgegeben hat.
	 */
	public String sendEventName;

	public int sendEventValue;

	// XXX History als Argument?
	/*
	 * Ebenfalls ein unschöner Hack: Bevor in einem Environment ein
	 * Guard-Ausdruck ausgewertet werden kann. müssen diese beiden Werte
	 * gesetzt werden. Die Alternative wäre, an Expression#evaluate
	 * entweder den aktiven State und die History oder die aktuellen
	 * Werte als Parameter zu übergeben. In jedem Fall wäre es ein
	 * Fortschritt, wenn Transitionen über Methoden 'evalGuard(..)' und
	 * 'calcAction(..)' verfügen würden.
	 */
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
		this.values = new HashMap<String, Integer>();
		for (final Variable v : dictionary.getContents())
			this.values.put(v.getName(), v.getInitialValue());
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
	 * Setzt den einem Variablennamen zugeordneten Integer-Wert. Ist an
	 * den uebergebenen Variablennamen bei der Erzeugung des
	 * VarAssignment-Objektes kein Wert gebunden worden, dann ist das
	 * Verhalten der Funktion undefiniert.
	 * <p>
	 * Wird der Gültigkeitsbereich der Variablen über- oder
	 * unterschritten, dann wird im ExpressionEnvironment gespeichert,
	 * welche Variable den Gültigkeitsbereich verletzt. Der Verwender
	 * des ExpressionEnvironments kann dann auf diese Situation
	 * reagieren.
	 */
	public void setVar(String n, int value) {
		assert n != null;
		this.values.put(n, value);
		try {
			Variable v = this.dictionary.get(n);
			if (value > v.getMaxValue() || value < v.getMinValue())
				this.varOutOfBounds = n;
		} catch (ErrUndefinedElement e) {
			assert false : "Zugriff auf undefinierte Variable";
		}
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ExpressionEnvironment) {
			final ExpressionEnvironment other = (ExpressionEnvironment) obj;
			return this.values.equals(other.values);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.values.hashCode();
	}

	/**
	 * Gibt an, ob (mindestens) eine Variable ihren Gültigkeitsbereich
	 * über- oder unterschreitet
	 */
	public boolean isOutOfBounds() {
		return this.varOutOfBounds != null;
	}

	/**
	 * Gibt den zuletzt illegal an eine Variable zugewiesenen Wert als
	 * String der Form "var := 42" aus.
	 */
	public String getOutOfBoundsDescription() {
		return this.varOutOfBounds + " := "
			+ lookupVar(this.varOutOfBounds);
	}

	@Override
	public String toString() {
		StringWriter s = new StringWriter();
		s.append("{ ");
		for (String v : this.dictionary.getKeys()) {
			s.append(v + ":=" + this.values.get(v) + "; ");
		}
		if (varOutOfBounds != null)
			s.append(" -->" + varOutOfBounds + " !");
		s.append('}');
		return s.toString();
	}

}

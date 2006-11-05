package csm;

/**
 * eine Variablenbelegung Variablenbelegungen werden nur in der
 * semantischen Analyse verwendet. Die initiale Variablenbelegung wird
 * in den Variablen-Objekten gesetzt.
 */
public class VarAssignment {

	// XXX vorerst nicht implementieren

	Dictionary variableList;

	public VarAssignment(Dictionary variableList) {
		assert variableList != null;
		this.variableList = variableList;
	}

	public int lookup(String varname) {
		// TODO implementieren
		return 0;
	}
}

package csm;

/**
 * eine Variablenbelegung Variablenbelegungen werden nur in der
 * semantischen Analyse verwendet. Die initiale Variablenbelegung wird
 * in den Variablen-Objekten gesetzt. XXX vorerst nicht implementieren
 */
public class VarAssignment {

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

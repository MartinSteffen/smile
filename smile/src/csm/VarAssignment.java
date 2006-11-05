package csm;

/**
 * eine Variablenbelegung Variablenbelegungen werden nur in der
 * semantischen Analyse verwendet. Die initiale Variablenbelegung wird
 * in den Variablen-Objekten gesetzt.
 */
public final class VarAssignment {

	// XXX vorerst nicht implementieren

	Dictionary<Variable> variableList;

	public VarAssignment(Dictionary<Variable> variableList) {
		assert variableList != null;
		this.variableList = variableList;
	}

	public int lookup(String varname) {
		assert false; // oops
		return 0;
	}
	
	public VarAssignment setVar(String n, int value) {
		assert false; // oops
		return null;
	}
}

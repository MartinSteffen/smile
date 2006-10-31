package csm;

import java.util.HashMap;


public final class VariableList {

	HashMap<String, Variable> variables;

	/**
	 * Eine Variable in der Liste suchen. Wenn sie nicht enthalten ist, wird sie
	 * automatisch erzeugt und in die Liste eingefügt.
	 */
	public Variable getVariable(String name) {
		Variable var = this.variables.get(name);
		if (var != null)
			return var;
		var = new Variable(name);
		this.variables.put(name, var);
		return var;
	}
}

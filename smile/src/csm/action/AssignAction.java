package csm.action;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;
import csm.intExpression.Term;


public final class AssignAction extends Action {

	final public String varname;
	final public Term term;

	public AssignAction(String varname, Term term) {
		assert varname != null;
		assert term != null;

		this.varname = varname;
		this.term = term;
	}

	@Override
	/**
	 * @param Eine Variablenassignment ordnet der Variabel "varname" den
	 *            Wert des Termes "term" zu,
	 * @returns Eine neue Variablenassignment pre, in deren
	 *          Variablenliste varname den neuen Wert besitzt.
	 */
	public VarAssignment doAction(VarAssignment pre) {
		int i = 0;
		// TODO assignment-action

		i = this.term.evaluate(pre);
		pre.setVar(this.varname, i);
		return pre;
	}

	@Override
	public String prettyprint() {
		return this.varname + " := " + this.term.prettyprint();
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> dict)
			throws ErrUndefinedElement {
		dict.mustContain(this.varname);
		this.term.noUndefinedVars(dict);
	}

}

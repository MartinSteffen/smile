package csm.action;

import csm.VarAssignment;
import csm.term.Term;


public final class AssignAction extends Action {

	//final 
	public String varname;
	final public Term term;

	public AssignAction(String varname, Term term) {
		assert varname != null;
		assert term != null;

		this.varname = varname;
		this.term = term;
	}

	@Override
	/**
	 * @param Eine Variablenassignment
	 * ordnet der Variabel "varname" den Wert des Termes "term" zu,
	 * @returns Eine neue Variablenassignment pre, in deren Variablenliste
	 * varname den neuen Wert besitzt. 
	 */
	public VarAssignment doAction(VarAssignment pre) {
		int i = 0;
		// TODO assignment-action
		if(pre.variableList.contains(this.varname))
		{
		i = this.term.evaluate(pre);
		pre.setVar(this.varname, i);
		}
		else 
			System.out.println(" Die Variabel"+this.varname+ "existiert nicht in der Varialeliste "  );
		return pre;
	}

	@Override
	public String prettyprint() {
		return varname + " := " + term.prettyprint();  
		}

	
}

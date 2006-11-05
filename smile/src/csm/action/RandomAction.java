package csm.action;

import java.util.List;

import csm.VarAssignment;


public final class RandomAction extends Action {

	final public String varname;

	/**
	 * Die Werte werden nicht auf Gültigkeit geprüft. Eine solche
	 * Prüfung findet erst in der semantischen Analyse statt.
	 */
	final public List<Integer> possibleValues;

	public RandomAction(String varname, List<Integer> pv) {
		assert varname != null;
		assert pv != null;
		assert pv.size()>0; // wir nehmen das einfach mal an...

		this.varname = varname;
		this.possibleValues = pv;
	}

	@Override
	public final VarAssignment doAction(VarAssignment pre) {
		// TODO RandomAction
		return null;
	}

	@Override
	public String prettyprint() {	
		final StringBuilder b = new StringBuilder(this.varname+'(');
		b.append(this.possibleValues.get(0));
		for(int i=1;i<this.possibleValues.size();i++) {
			b.append(", ");
			b.append(this.possibleValues.get(i));
		};
		b.append(')');
		return b.toString();
	}

}

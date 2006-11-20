package csm.action;

import java.util.List;
import java.util.Random;

import csm.Dictionary;
import csm.VarAssignment;
import csm.Variable;
import csm.exceptions.ErrUndefinedElement;


public final class RandomAction extends Action {

	final public String varname;

	/**
	 * Die Werte werden nicht auf Gültigkeit geprüft. Eine solche
	 * Prüfung findet erst in der semantischen Analyse statt.
	 * 
	 * @param varname der Variablenname
	 * @param pv eine Liste der möglichen Werte. Die Liste muss
	 *            mindestens ein Element enthalten.
	 */
	final public List<Integer> possibleValues;

	/**
	 * Wir verwenden einen systemweiten Zufallsgenerator
	 */
	final static Random randomGen = new Random();

	public RandomAction(String varname, List<Integer> pv) {
		assert varname != null;
		assert pv != null;
		assert pv.size() > 0; // wir nehmen das einfach mal an...

		this.varname = varname;
		this.possibleValues = pv;
	}

	@Override
	/**
	 * @param pre das zu ändernde VarAssignment
	 * @returns eine neue Variableassignment
	 */
	public final VarAssignment doAction(VarAssignment pre) {
		final int randomIndex = RandomAction.randomGen
				.nextInt(this.possibleValues.size());
		final int randomValue = this.possibleValues.get(randomIndex);
		pre.setVar(this.varname, randomValue);
		return pre;
	}

	@Override
	public String prettyprint() {
		final StringBuilder b = new StringBuilder(this.varname + '(');
		b.append(this.possibleValues.get(0));
		for (int i = 1; i < this.possibleValues.size(); i++) {
			b.append(", ");
			b.append(this.possibleValues.get(i));
		}
		;
		b.append(')');
		return b.toString();
	}

	@Override
	public void noUndefinedVars(Dictionary<Variable> dict)
			throws ErrUndefinedElement {
		dict.mustContain(this.varname);
	}

}

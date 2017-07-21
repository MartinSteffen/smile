package csm.expression;

import java.util.List;
import java.util.Random;
import java.util.Set;

import csm.ExpressionEnvironment;


public final class DoRandom extends Action {

	final public String varname;

	/**
	 * Die Werte werden nicht auf Gueltigkeit geprueft. Eine solche
	 * Pruefung findet erst in der semantischen Analyse statt.
	 * 
	 * @param varname der Variablenname
	 * @param pv eine Liste der moeglichen Werte. Die Liste muss
	 *            mindestens ein Element enthalten.
	 */
	final public List<Expression<Integer>> possibleValues;

	/**
	 * Wir verwenden einen systemweiten Zufallsgenerator
	 */
	final static Random randomGen = new Random();

	public DoRandom(String varname, List<Expression<Integer>> pv) {
		assert varname != null;
		assert pv != null;
		assert pv.size() > 0; // wir nehmen das einfach mal an...

		this.varname = varname;
		this.possibleValues = pv;
	}

	@Override
	public String prettyprint() {
		final StringBuilder b = new StringBuilder(this.varname
			+ " := random (");
		// siehe oben: wir haben angenommen, dass size>0
		b.append(this.possibleValues.get(0).prettyprint());
		for (int i = 1; i < this.possibleValues.size(); i++) {
			b.append(", ");
			b.append(this.possibleValues.get(i).prettyprint());
		}
		b.append(')');
		return b.toString();
	}

	public String firstUndefinedVar(Set<String> dict) {
		if (!dict.contains(this.varname))
			return this.varname;
		for (Expression<Integer> i : this.possibleValues) {
			String u = i.firstUndefinedVar(dict);
			if (u != null)
				return u;
		}
		return null;
	}

	@Override
	public final ExpressionEnvironment[] evaluate(ExpressionEnvironment pre) {
		ExpressionEnvironment[] result = new ExpressionEnvironment[this.possibleValues
				.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = new ExpressionEnvironment(pre);
			result[i].setVar(this.varname, this.possibleValues.get(i)
					.evaluate(pre));
		}
		return result;
	}
}

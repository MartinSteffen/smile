/**
 * Erstellt am 31.10.200614:34:21 Erstellt von rachid Projekt smile
 */
package csm;

import csm.exceptions.ErrAlreadyDefinedElement;
import csm.exceptions.ErrSyntaxError;
import csm.exceptions.ErrValueOutOfBounds;

/**
 * @author rachid
 */
public final class Variable extends NamedObject {

	private int initialValue;

	private int minValue;

	private int maxValue;

	/**
	 * @param ini
	 *            Initialwert
	 * @param min
	 *            Minimalwert
	 * @param max
	 *            Maximalwert
	 * @throws ErrValueOutOfBounds
	 *             wenn Minimal-, Maximal- und Initialwerte nicht konsistent
	 *             sind
	 * @throws ErrAlreadyDefinedElement
	 * @throws ErrSyntaxError
	 */
	public Variable(Dictionary<Variable> parent, String name, int ini, int min,
			int max) throws ErrValueOutOfBounds, ErrAlreadyDefinedElement,
			ErrSyntaxError {
		this(parent, name);
		setValues(ini, min, max);
	}

	/**
	 * setzt die Minimal-, Maximal- und Initialwerte auf 0
	 * 
	 * @param name
	 *            ungleich <code>null</code>
	 * @throws ErrAlreadyDefinedElement
	 * @throws ErrSyntaxError
	 */
	public Variable(Dictionary<Variable> parent, String name)
			throws ErrAlreadyDefinedElement, ErrSyntaxError {
		super(name);

		assert parent != null;
		parent.add(this);

		/*
		 * hier werden die Werte direkt gesetzt, ohne setValues zu verwenden,
		 * weil der Konstruktor sonst mit der faktisch nie auftretenden
		 * Exception ErrValueOutOfBounds umgehen muesste
		 */
		this.initialValue = 0;
		this.minValue = 0;
		this.maxValue = 0;
		announceChanges();
	}

	/**
	 * setzt alle drei Werte auf einmal
	 * 
	 * @param ini
	 *            Initialwert
	 * @param min
	 *            Minimalwert
	 * @param max
	 *            Maximalwert
	 * @throws ErrValueOutOfBounds
	 */
	public void setValues(int ini, int min, int max) throws ErrValueOutOfBounds {
		if (ini < min)
			throw new ErrValueOutOfBounds("initial value " + ini
					+ " of variable " + getName()
					+ " is smaller than minimum value " + min);
		if (ini > max)
			throw new ErrValueOutOfBounds("initial value " + ini
					+ " of variable " + getName()
					+ " is greater than maximum value " + max);
		this.initialValue = ini;
		this.minValue = min;
		this.maxValue = max;
		announceChanges();
	}

	public int getInitialValue() {
		return this.initialValue;
	}

	/**
	 * @throws ErrValueOutOfBounds
	 *             wenn der Initialwert ausserhalb der Grenzen liegt
	 */
	public void setInitialValue(int initialValue) throws ErrValueOutOfBounds {
		if (initialValue < this.minValue)
			throw new ErrValueOutOfBounds("new initial value " + initialValue
					+ "of variable " + getName()
					+ " is smaller than minimum value " + this.minValue);
		if (initialValue > this.maxValue)
			throw new ErrValueOutOfBounds("new initial value " + initialValue
					+ "of variable " + getName()
					+ " is greater than maximum value " + this.maxValue);
		this.initialValue = initialValue;
		announceChanges();
	}

	public int getMinValue() {
		return this.minValue;
	}

	/**
	 * @throws ErrValueOutOfBounds
	 *             wenn der Minimalwert ausserhalb der Grenzen liegt
	 */
	public void setMinValue(int minValue) throws ErrValueOutOfBounds {
		if (minValue > this.initialValue)
			throw new ErrValueOutOfBounds("new minimum value " + minValue
					+ "of variable " + getName()
					+ " is greater than initial value " + this.initialValue);
		this.minValue = minValue;
		announceChanges();
	}

	public int getMaxValue() {
		return this.maxValue;
	}

	/**
	 * @throws ErrValueOutOfBounds
	 *             wenn der Maximalwert ausserhalb der Grenzen liegt
	 */
	public void setMaxValue(int maxValue) throws ErrValueOutOfBounds {
		if (maxValue < this.initialValue)
			throw new ErrValueOutOfBounds("new maximum value " + maxValue
					+ "of variable " + getName()
					+ " is smaller than initial value " + this.initialValue);
		this.maxValue = maxValue;
		announceChanges();
	}

}

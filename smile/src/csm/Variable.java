/**
 * Erstellt am 31.10.200614:34:21 Erstellt von rachid Projekt smile
 */
package csm;

import csm.exceptions.ErrValueOutOfBounds;


/**
 * @author rachid
 */
public final class Variable extends NamedObject {

	private int initialValue;
	private int minValue;
	private int maxValue;

	/**
	 * @param i Initialwert
	 * @param min Minimalwert
	 * @param max Maximalwert
	 * @throws ErrValueOutOfBounds wenn Minimal-, Maximal- und
	 *             Initialwerte nicht konsistent sind
	 */
	public Variable(String name, int i, int min, int max)
			throws ErrValueOutOfBounds {
		super(name);
		if (this.initialValue < this.minValue)
			throw new ErrValueOutOfBounds("initial value "
					+ this.initialValue
					+ " is smaller than minimum value " + this.minValue);
		if (this.initialValue > this.maxValue)
			throw new ErrValueOutOfBounds("initial value "
					+ this.initialValue
					+ " is greater than maximum value " + this.maxValue);
		this.initialValue = i;
		this.minValue = min;
		this.maxValue = max;
	}

	/**
	 * setzt die Minimal-, Maximal- und Initialwerte auf 0
	 * 
	 * @param name ungleich <code>null</code>
	 */
	public Variable(String name) {
		super(name);
		this.initialValue = 0;
		this.minValue = 0;
		this.maxValue = 0;
	}

	public int getInitialValue() {
		return this.initialValue;
	}

	/**
	 * @throws ErrValueOutOfBounds wenn der Initialwert auﬂerhalb der
	 *             Grenzen liegt
	 */
	public void setInitialValue(int initialValue)
			throws ErrValueOutOfBounds {
		if (initialValue < this.minValue)
			throw new ErrValueOutOfBounds("new initial value "
					+ initialValue + " is smaller than minimum value "
					+ this.minValue);
		if (initialValue > this.maxValue)
			throw new ErrValueOutOfBounds("new initial value "
					+ initialValue + " is greater than maximum value "
					+ this.maxValue);
		this.initialValue = initialValue;
	}

	public int getMinValue() {
		return this.minValue;
	}

	/**
	 * @throws ErrValueOutOfBounds wenn der Minimalwert auﬂerhalb der
	 *             Grenzen liegt
	 */
	public void setMinValue(int minValue) throws ErrValueOutOfBounds {
		if (minValue > this.initialValue)
			throw new ErrValueOutOfBounds("new minimum value "
					+ minValue + " is greater than initial value "
					+ this.initialValue);
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return this.maxValue;
	}

	/**
	 * @throws ErrValueOutOfBounds wenn der Maximalwert auﬂerhalb der
	 *             Grenzen liegt
	 */
	public void setMaxValue(int maxValue) throws ErrValueOutOfBounds {
		if (maxValue < this.initialValue)
			throw new ErrValueOutOfBounds("new maximum value "
					+ maxValue + " is smaller than initial value "
					+ this.initialValue);
		this.maxValue = maxValue;
	}

}

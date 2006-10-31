/**
 * Erstellt am 31.10.200614:34:21
 * Erstellt von rachid
 * Projekt smile
 */
package csm;

/**
 * @author rachid
 * 
 */
public final class Variable {

	public final String name;

	private int initialValue;

	private int minValue;

	private int maxValue;

	// TODO Werte prüfen

	public Variable(String name, int i, int min, int max) {
		this.name = name;
		setInitialValue(i);
		setMinValue(min);
		setMaxValue(max);
	}

	public Variable(String name) {
		this(name, 0, 0, 0);
	}

	/**
	 * @return the initialValue
	 */
	public int getInitialValue() {
		return this.initialValue;
	}

	/**
	 * @param initialValue
	 *            the initialValue to set
	 */
	public void setInitialValue(int initialValue) {
		this.initialValue = initialValue;
	}

	/**
	 * @return the maxValue
	 */
	public int getMaxValue() {
		return this.maxValue;
	}

	/**
	 * @param maxValue
	 *            the maxValue to set
	 */
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * @return the minValue
	 */
	public int getMinValue() {
		return this.minValue;
	}

	/**
	 * @param minValue
	 *            the minValue to set
	 */
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

}

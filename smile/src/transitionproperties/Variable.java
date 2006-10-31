/**
 * Erstellt am 31.10.200614:34:21
 * Erstellt von rachid
 * Projekt smile
 */
package transitionproperties;

/**
 * @author rachid
 * 
 */
public final class Variable {

	private String name;

	private int initialValue;

	private int minValue;

	private int maxValue;

	// TODO Werte prüfen
	Variable(String n, int i, int min, int max) {
		name = n;
		initialValue = i;
		minValue = min;
		maxValue = max;
	}

	/**
	 * @return the initialValue
	 */
	public int getInitialValue() {
		return initialValue;
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
		return maxValue;
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
		return minValue;
	}

	/**
	 * @param minValue
	 *            the minValue to set
	 */
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

}

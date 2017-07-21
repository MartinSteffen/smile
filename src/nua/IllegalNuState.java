/**
 * 
 */
package nua;

/**
 * @author hs
 */
public class IllegalNuState extends NuState {

	final String description;

	/**
	 * @param nua
	 */
	public IllegalNuState(NuAutomaton nua, String description) {
		super(nua);
		this.description = description;
		nua.illegalStates.add(this);
	}

	/**
	 * @param nua
	 * @param isRootState
	 */
	public IllegalNuState(NuAutomaton nua, String description,
			boolean isRootState) {
		super(nua, isRootState);
		this.description = description;
		nua.illegalStates.add(this);
	}

}

/**
 * 
 */
package csm.statetree;

/**
 * implementiert zusammen mit der Methode {@link CSMComponent#traverseCSM()} ein
 * modifiziertes Visitor-Pattern. Ob ein Unterbaum traversiert wird, hängt vom
 * Rückgabewert von enterXXX ab. 
 * 
 * @author hs
 */
public interface CSMTraversal {

	/**
	 * @return true, wenn Inhalte der Region traversiert werden sollen
	 */
	boolean enterRegion(AbstractRegion region);

	void exitRegion(AbstractRegion region);

	/**
	 * @return true, wenn Inhalte des CompositeState traversiert werden sollen
	 */
	boolean enterCompositeState(CompositeState scom);

	void exitCompositeState(CompositeState scom);

	/**
	 * @return true, wenn Inhalte des FinalState traversiert werden sollen
	 */
	boolean enterFinalState(FinalState sfin);

	void exitFinalState(FinalState sfin);

	void visitEntryState(EntryState sentry);

	void visitExitState(ExitState sexit);

	void visitChoiceState(ChoiceState schoice);
}

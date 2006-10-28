/**
 * 
 */
package csm.statetree;

/**
 * implementiert zusammen mit der Methode CSMComponent#traverseCSM() ein
 * modifiziertes Visitor-Pattern. Ob ein Unterbaum traversiert wird, hängt vom
 * Rückgabewert von enterXXX ab. 
 * 
 * @author hs
 */
public interface CSMTraversal {

	/**
	 * @return true, wenn Substates der Region traversiert werden sollen
	 */
	boolean enterRegion(AbstractRegion region);

	void exitRegion(AbstractRegion region);

	/**
	 * @return true, wenn Substates des CompositeState traversiert werden sollen
	 */
	boolean enterCompositeState(CompositeState scom);

	void exitCompositeState(CompositeState scom);

	/**
	 * @return true, wenn Substates des FinalState traversiert werden sollen
	 */
	boolean enterFinalState(FinalState sfin);

	void exitFinalState(FinalState sfin);

	void visitEntryState(EntryState sentry);

	void visitExitState(ExitState sexit);

	void visitChoiceState(ChoiceState schoice);
}

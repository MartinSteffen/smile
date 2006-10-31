/**
 * 
 */
package csm.statetree;

/**
 * implementiert ein Visitor-Pattern für CSM-Komponentenbäume.
 * <p>
 * Die Methode visitChildren(CSMComponent) besucht alle Children der Komponente.
 * <p>
 * Das sollte ausreichen, um die meisten Operationen auf Komponentenbäumen zu
 * implementieren.
 * 
 * 
 * @author hs
 */
public class CSMVisitor {

	/**
	 * alle Child-Komponenten besuchen
	 * <p>
	 * mit dieser Methode können Visitors gebaut werden, die den
	 * CSM-Komponentenbaum rekursiv besuchen
	 */
	protected final void visitChildren(CSMComponent component) {
		component.visitChildren(this);
	}

	protected void visitCSMComponent(CSMComponent component) {
		visitChildren(component);
	}

	// ** Regionen

	protected void visitRegion(Region region) {
		visitCSMComponent(region);
	}

	// ** States
	protected void visitState(State state) {
		visitCSMComponent(state);
	}

	// Connection Points

	protected void visitConnectionPoint(ConnectionPoint point) {
		visitState(point);
	}

	protected void visitEntryState(EntryState state) {
		visitConnectionPoint(state);
	}

	protected void visitExitState(ExitState state) {
		visitConnectionPoint(state);
	}

	// Internal States

	protected void visitInternalState(InternalState state) {
		visitState(state);
	}

	protected void visitCompositeState(CompositeState state) {
		visitInternalState(state);
	}

	protected void visitFinalState(FinalState state) {
		visitInternalState(state);
	}

	protected void visitChoiceState(ChoiceState state) {
		visitInternalState(state);
	}
}

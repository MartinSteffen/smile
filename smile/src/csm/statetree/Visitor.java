/**
 * 
 */
package csm.statetree;

/**
 * implementiert ein Visitor-Pattern f�r CSM-Komponentenb�ume.
 * <p>
 * Die Methode visitChildren(CSMComponent) besucht alle Children der
 * Komponente.
 * <p>
 * Das sollte ausreichen, um die meisten Operationen auf
 * Komponentenb�umen zu implementieren.
 * 
 * @author hsi
 */
public class Visitor {

	protected void visitCSMComponent(CSMComponent component) {
		// die visitXXX-Methoden werden bei Bedarf in den Child-Klassen
		// �berschrieben
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

	// ** Transitionen
	
	protected void visitTransition(Transition transition) {
		visitCSMComponent(transition);

	}

	/**
	 * alle Child-Komponenten besuchen
	 * <p>
	 * mit dieser Methode k�nnen Visitors gebaut werden, die den
	 * CSM-Komponentenbaum rekursiv besuchen
	 * <p>
	 * sie geh�rt strenggenommen nicht zum Visitor-Pattern 
	 */
	protected final void visitChildren(CSMComponent component) {
		component.visitChildren(this);
	}
}

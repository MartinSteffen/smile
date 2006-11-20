package csm.statetree;

/**
 * ein Visitor-Pattern für CSM-Komponentenbäume.
 * <p>
 * Die Methode visitChildren(CSMComponent) besucht alle Children der
 * Komponente. Das sollte ausreichen, um die meisten Operationen auf
 * Komponentenbäumen zu implementieren. Ein Beispiel findet sich unter
 * {@link OutermostRegion#enumerateStates()}, ein anderes unter
 * {@link csm.CSMSaver}
 * 
 * @author hsi
 */
public class Visitor {

	protected void visitCSMComponent(CSMComponent component) {
		// die visitXXX-Methoden werden bei Bedarf in den Child-Klassen
		// überschrieben
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
	 * Mit dieser Methode können Visitors gebaut werden, die den
	 * CSM-Komponentenbaum rekursiv besuchen. Sie gehört strenggenommen
	 * nicht zum Visitor-Pattern. Sie ist trotzdem an dieser Stelle
	 * implementiert, weil wir dadurch den Zugriff auf die
	 * Child-Komponenten ermöglichen, ohne die Kapselung der
	 * Children-Listen aufzugeben.
	 */
	protected final void visitChildren(CSMComponent component) {
		component.visitChildren(this);
	}
}

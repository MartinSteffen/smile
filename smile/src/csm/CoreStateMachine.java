/**
 * 
 */
package csm;

import java.util.List;

import csm.statetree.CSMComponent;
import csm.statetree.OutermostRegion;
import csm.statetree.State;


/**
 * @author hs
 */
public final class CoreStateMachine {

	public final OutermostRegion region = new OutermostRegion();

	public final Dictionary events = new Dictionary<Event>();

	public final Dictionary variables = new Dictionary<Variable>();

	public CoreStateMachine() {
	}

	public Transition connect(State source, State target) {
		assert source != null;
		assert target != null;
		// TODO connect implementieren
		return null;
	}

	public void disconnect(Transition t) {
		// TODO disconnect implementieren
	}

	/*
	 * sollen die parent-Felder gelöschter States auf null gesetzt
	 * werden? wohin mit den transitionen?
	 */
	public List<Transition> deleteComponent(CSMComponent component) {
		// alle Transitionen unterhalb der Komponente entfernen
		// und in die Ergebnisliste eintragen
		// dann die Komponente aus ihrem parent entfernen
		// TODO implementieren
		return null;
	}

}

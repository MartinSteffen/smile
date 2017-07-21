package nua;

import java.util.HashSet;
import java.util.Set;


/**
 * Der \nu-Automat gemaess Definition 1 der Aufgabenbeschreibung
 */
public class NuAutomaton {

	final public Set<NuState> states;

	final public Set<NuState> rootStates;

	final public Set<NuTransition> transitions;

	public boolean isConcrete() {
		return this.rootStates.size() == 1;
	}

	public NuAutomaton() {
		this.states = new HashSet<NuState>();
		this.rootStates = new HashSet<NuState>();
		this.transitions = new HashSet<NuTransition>();
	}
}

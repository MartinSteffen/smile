package nua;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Der \nu-Automat gemaess Definition 1 der Aufgabenbeschreibung
 * <p>
 * States werden mit <code>new NuState(myAutomaton)</code> erzeugt.
 * <p>
 * Transitionen werden mit
 * <code>new NuTransition(myAutomaton, source, targetSet)</code> erzeugt.
 * <p>
 * Beide Konstruktoren tragen die erzeugten Objekte in den entsprechenden Listen
 * des Automaten ein.
 * <p>
 * Das Löschen einmal erzeugter States und Transitionen ist nicht vorgesehen.
 * Die Implementierung erlaubt derzeit, dass man Transitionen und States löscht,
 * indem man direkt auf die Sets zugreift. Wir garantieren aber nicht, dass der
 * Automat danach in einem konsistenten Zustand ist.
 */
public class NuAutomaton {

	public int numStates;

	public int numTransitions;

	final public Set<NuState> states;

	final public Set<NuState> rootStates;

	final public Set<NuState> illegalStates;

	/**
	 * gibt an, ob dieser \nu-Automat konkret ist, d.h. ob er genau einen
	 * Root-State hat, ob jedes Transitionslabel maximal einmal pro Source-State
	 * vorkommt, und außerdem für jeden Sourcestate und jedes Label aus expected
	 * eine Transition existiert.
	 */
	public boolean isConcrete(Set<String> expected) {
		if (this.rootStates.size() != 1)
			return false;
		for (NuState s : this.states)
			if (!s.isConcreteState(expected))
				return false;
		return true;
	}

	/**
	 * gibt an, ob dieser \nu-Automat konkret ist, d.h. ob er genau einen
	 * Root-State hat, ob jedes Transitionslabel maximal einmal pro Source-State
	 * vorkommt.
	 */
	public boolean isConcrete() {
		return isConcrete(new HashSet<String>());
	}

	/**
	 * erzeugt einen neuen, leeren \nu-Automaten
	 */
	public NuAutomaton() {
		this.states = new HashSet<NuState>();
		this.rootStates = new HashSet<NuState>();
		this.illegalStates = new HashSet<NuState>();
		this.numStates = 0;
		this.numTransitions = 0;
	}

	/**
	 * trägt einen State in der Stateliste dieses Nu-Automaten ein
	 */
	final void registerState(NuState s) {
		this.states.add(s);
		this.numStates++;
	}

	/**
	 * trägt eine Transition in der Transitionsliste ihres Source-States ein
	 */
	final void registerTransition(NuTransition t) {
		t.source.registerOutgoingTransition(t);
		this.numTransitions++;
	}

	final public boolean hasIllegalStates() {
		return this.illegalStates.size() > 0;
	}

	/**
	 * macht den Nu-Automaten kreisfrei und entfernt dann alle Transitionen, die
	 * nicht zu illegalen States führen.
	 */
	final public void reduceToIllegalPaths() {
		for (NuState s1 : this.states)
			s1.tag = 0;
		for (NuState s : this.rootStates)
			keepPathsToIllegalState(s);
	}

	int keepPathsToIllegalState(NuState s) {
		s.tag = 1;
		for (NuTransition tr : new LinkedList<NuTransition>(s.transitions)) {
			for (NuState s2 : new LinkedList<NuState>(tr.targets)) {
				if (s instanceof IllegalNuState)
					tr.targets.remove(s2);
				else if (s2.tag != 1) {
					if (keepPathsToIllegalState(s2) == 0) {
						tr.targets.remove(s2);
						this.states.remove(s2);
					}
				} else if (s2.tag == 1) {
					// kreisfrei machen
					tr.targets.remove(s2);
				} else if (s2.tag == 2) {
					// alternativer Pfad bleibt erhalten
				}
			}
			if (tr.targets.size() == 0)
				s.transitions.remove(tr);
		}

		s.tag = 2; // bin zurück
		if (s instanceof IllegalNuState) {
			return 1;
		} else if (s.isRootState)
			return 1;
		else if (s.transitions.size() > 0)
			return 1;
		else {
			//
			return 0;
		}
	}
}

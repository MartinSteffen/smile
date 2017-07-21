package semantics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import csm.statetree.CompositeState;
import csm.statetree.ExitState;
import csm.statetree.FinalState;
import csm.statetree.Region;
import csm.statetree.Transition;


public class CoTransitionMaker {

	private final BasicMappings basicMappings;

	private final ListMap<ExitState, CompoundTransition> upsilon;

	public final List<CompoundTransition> coTr;

	public CoTransitionMaker(Region outermost, BasicMappings basicMappings) {
		this.basicMappings = basicMappings;

		// setze zunächst Upsilon auf {}
		this.upsilon = new ListMap<ExitState, CompoundTransition>();

		// erzeuge dann für jeden ExitState einen Eintrag in upsilon
		for (final ExitState s : basicMappings.exitstates)
			makeUpsilonEntry(s);

		this.coTr = new LinkedList<CompoundTransition>();
		makeCoTransitions();
	}

	private void makeUpsilonEntry(ExitState s) {
		if (s.getParent() instanceof FinalState) {

			// für alle s in S_exit mit parent(s) in S_final setze
			// upsilon(s):={{}}
			this.upsilon.addTo(s, new CompoundTransition());

		} else if (s.getParent() instanceof CompositeState) {

			final CompositeState scom = (CompositeState) s.getParent();
			// wende den hergeleiteten Algorithmus an
			final List<Region> dsr = this.basicMappings.directSubregions
					.get(scom);

			LinkedList<Map<Region, Transition>> phi = new LinkedList<Map<Region, Transition>>();
			phi.add(new HashMap<Region, Transition>());
			for (final Region r : dsr) {
				final LinkedList<Map<Region, Transition>> phialt = phi;
				phi = new LinkedList<Map<Region, Transition>>();
				for (final Map<Region, Transition> p : phialt)
					for (final Transition t : this.basicMappings.transitionsPerExitstate
							.get(s)) {
						if (t.getSource().regOf() == r) {
							final HashMap<Region, Transition> p2 = new HashMap<Region, Transition>(
									p);
							p2.put(r, t);
							phi.add(p2);
						}
					}
			}

			for (final Map<Region, Transition> f : phi) {
				LinkedList<Map<Region, CompoundTransition>> psi = new LinkedList<Map<Region, CompoundTransition>>();
				psi.add(new HashMap<Region, CompoundTransition>());
				for (final Region r : dsr) {
					final LinkedList<Map<Region, CompoundTransition>> psialt = psi;
					psi = new LinkedList<Map<Region, CompoundTransition>>();
					for (final Map<Region, CompoundTransition> p : psialt)
						// Zu jedem Schlüssel f.get(r).getSource wird
						// sein Wert List<CompoundTransition>
						// unter upsilon dem Schlüssel r(Region)
						// zugeordnet und zum Map p2 hinzugefügt.
						for (final CompoundTransition t : this.upsilon
								.get((ExitState) f.get(r).getSource())) {
							final Map<Region, CompoundTransition> p2 = new HashMap<Region, CompoundTransition>(
									p);
							p2.put(r, t);
							psi.add(p2);
						}
				}
				for (final Map<Region, CompoundTransition> F : psi) {
					final CompoundTransition ups = new CompoundTransition();
					for (final Region r : dsr) {
						ups.add(f.get(r));
						ups.addAll(F.get(r));
					}
					this.upsilon.get(s).add(ups);
				}
			}

		}
	}

	/**
	 * aus jeder Transition eine Menge von CompoundTransitions basteln:
	 * entweder eine einelementige Menge aus Elementen von T_int, oder
	 * die Menge der Kombinationen mit den Elementen von
	 * Upsilon(t.getSource())
	 * <p>
	 * Diejenigen CompoundTransitions, deren Transitionen
	 * unterschiedliche Events haben, werden dabei aussortiert, denn
	 * CompoundTransitions werden nur gebraucht, wenn allen ihren
	 * Bestandteilen der gleiche Event (oder null) zugeordnet ist.
	 * <p>
	 * folgt der Definition von CoTr aus dem Paper
	 * 
	 * @param upsilon die Funktion upsilon, dargestellt als Map
	 * @param outermost die Region, in der nach Transitionen gesucht
	 *            wird
	 */
	private void makeCoTransitions() {

		for (final Transition t : this.basicMappings.transitions) {
			if (t.getSource() instanceof CompositeState) {
				// Einelementige Menge aus T_int hinzufügen
				final CompoundTransition single = new CompoundTransition();
				single.add(t);
				this.coTr.add(single);
			} else if ((t.getSource() instanceof ExitState)
				&& !(t.getTarget() instanceof ExitState)) {
				final ExitState source = (ExitState) t.getSource();
				// {t} jeweils mit Element von Upsilon(src(t))
				// vereinigen
				final List<CompoundTransition> ups = this.upsilon
						.get(source);
				for (final CompoundTransition T : ups) {
					final CompoundTransition ups2 = new CompoundTransition(
							T);
					ups2.add(t);
					// CoTransitions mit unterschiedlichen Events
					// rausschmeissen
					if (!ups2.hasDifferentEvents())
						this.coTr.add(ups2);
				}
			}
		}
	}

}

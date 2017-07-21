package semantics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import nua.IllegalNuState;
import nua.NuAutomaton;
import nua.NuState;
import nua.NuTransition;
import csm.CoreStateMachine;
import csm.ExpressionEnvironment;
import csm.expression.Action;
import csm.expression.DoSkip;
import csm.statetree.ChoiceState;
import csm.statetree.CompositeState;
import csm.statetree.EntryState;
import csm.statetree.ExitState;
import csm.statetree.FinalState;
import csm.statetree.InternalState;
import csm.statetree.Region;
import csm.statetree.State;
import csm.statetree.Transition;


public class NuGenerator {

	boolean debug;

	BasicMappings basicMappings;

	private final List<CompoundTransition> coTr;

	public final NuAutomaton nuAutomaton;

	/**
	 * Alle States des Nu-Automaten mit den zugehörigen Konfigurationen
	 */
	HashMap<CSMConfiguration, NuState> states;

	/**
	 * die States, für die noch keine Nachfolger gesucht wurden, die
	 * aber schon in der HashMap states eingetragen sind
	 */
	Stack<CSMConfiguration> todo;

	/**
	 * erzeugt einen \nu-Automaten aus einer gegebenen CSM
	 */
	static public NuAutomaton generateNuAutomaton(CoreStateMachine csm) {
		return generateNuAutomaton(csm, false);
	}

	/**
	 * erzeugt einen \nu-Automaten aus einer gegebenen CSM
	 */
	static public NuAutomaton generateNuAutomaton(CoreStateMachine csm,
			boolean debug) {
		assert csm != null;
		return new NuGenerator(csm, debug).nuAutomaton;
	}

	NuGenerator(CoreStateMachine csm, boolean debug) {
		this.debug = debug;

		this.basicMappings = new BasicMappings(csm);

		this.coTr = new CoTransitionMaker(csm.region, this.basicMappings).coTr;

		this.nuAutomaton = new NuAutomaton();

		final CSMConfiguration startConfig = new CSMConfiguration(csm,
				this.basicMappings.doActions);

		this.states = new HashMap<CSMConfiguration, NuState>();

		this.todo = new Stack<CSMConfiguration>();

		// Startkonfiguration als Root-State des Nu-Automaten eintragen
		this.states.put(startConfig, new NuState(this.nuAutomaton, true));

		// bei Startkonfiguration anfangen, die Nachfolgestates zu
		// berechnen
		this.todo.push(startConfig);

		// Hauptschleife: solange es States gibt, für die noch keine
		// Nachfolger berechnet wurden...
		while (this.todo.size() > 0) {
			// ..nimm einen dieser States...
			final CSMConfiguration act = this.todo.pop();
			// ..und berechne seine Nachfolger
			applyRulesTo(act, this.states.get(act));
		}
	}

	/**
	 * Wenn eine Konfiguration berechnet wurde, prüft diese Methode, ob
	 * diese Konfiguration schon bekannt ist. Wenn ja, dann wird nur der
	 * zugehörige NuState ermittelt. Wenn nein, dann wird
	 * <p>
	 * 1. ein neuer NuState erzeugt, der diese Konfiguration
	 * repräsentiert
	 * <p>
	 * 2. die neue Konfiguration auf den todo-Stack gelegt, so dass in
	 * der Hauptschleife auch die Nachfolger dieser Konfiguration
	 * gesucht werden
	 */
	void addConfigToTargets(CSMConfiguration state, Set<NuState> nuTargets) {
		// zugeordneten NuState ermitteln, sofern er existiert
		String debugInfo = debug ? state.debugString() : null;
		NuState ns = this.states.get(state);
		if (ns == null) {
			// wenn er nicht existiert, neuen erzeugen
			// Sind alle Variablen im Gültigkeitsbereich?
			if (!state.varAssignment.isOutOfBounds()) {
				// dann einen normalen NuState erzeugem
				ns = new NuState(this.nuAutomaton, debugInfo);
				// und auf die Todo-Liste setzen, damit von diesem aus
				// weitergerechnet wird
				this.todo.push(state);
			} else {
				// andernfalls einen ausgezeichneten NuState erzeugen,
				// der die Bereichsverletzung angibt
				ns = new IllegalNuState(this.nuAutomaton,
						state.varAssignment.getOutOfBoundsDescription());
				// von diesem aus wird nicht weitergerechnet
			}
			// so oder so wird der neue NuState der Konfiguration
			// zugeordnet
			this.states.put(state, ns);
		}
		nuTargets.add(ns);
	}

	private void applyRulesTo(CSMConfiguration state, NuState nustate) {
		// alle Regeln anwenden,
		// dabei neue States in states, todo eintragen, NuTransition
		// erzeugen
		applyRuleDoAct(state, nustate);
		applyRuleCurAct(state, nustate);
		applyRuleNextTr2(state, nustate);
		applyRuleNextCom(state, nustate);
		applyRuleNextInt(state, nustate);
		applyRuleNextCompletion(state, nustate);
		applyRuleNextTrigger(state, nustate);
		applyRuleDefer(state, nustate);
		applyRuleAFin(state, nustate);
		applyRuleACh(state, nustate);
		applyRuleAEn(state, nustate);
		applyRuleAEx(state, nustate);
	}

	private void applyRuleDoAct(CSMConfiguration state, NuState nustate) {
		if (state.alpha.equals(DoSkip.skipAction)
			&& state.completionState != null)
			return;

		for (final InternalState s : state.active) {
			if (!(s instanceof CompositeState))
				continue;
			if (state.doActions.get(s).equals(DoSkip.skipAction))
				continue;

			Action action = state.doActions.get(s);
			final ExpressionEnvironment[] follow = NuGenerator.calcAction(
					state, action);

			String event = makeLabelForSendevent(follow);

			final Set<NuState> nuTargets = new HashSet<NuState>();
			for (final ExpressionEnvironment env : follow) {
				final CSMConfiguration newState = new CSMConfiguration(
						state);
				newState.varAssignment = env;
				newState.doActions.put((CompositeState) s,
						DoSkip.skipAction);

				env.sendEventName = null;
				addConfigToTargets(newState, nuTargets);
			}

			new NuTransition(this.nuAutomaton, nustate, event, nuTargets,
					"doAct(" + action.prettyprint() + ')');
		}
	}

	private void applyRuleCurAct(CSMConfiguration state, NuState nustate) {
		if (state.alpha.equals(DoSkip.skipAction))
			return;
		final ExpressionEnvironment[] follow = NuGenerator.calcAction(
				state, state.alpha);

		String event = makeLabelForSendevent(follow);

		final Set<NuState> nuTargets = new HashSet<NuState>();
		for (final ExpressionEnvironment env : follow) {
			final CSMConfiguration newState = new CSMConfiguration(state);
			newState.varAssignment = env;
			newState.alpha = DoSkip.skipAction;
			env.sendEventName = null;
			addConfigToTargets(newState, nuTargets);
		}
		new NuTransition(this.nuAutomaton, nustate, event, nuTargets,
				"curAct");
	}

	private void applyRuleNextTr2(CSMConfiguration state, NuState nustate) {
		if (!state.alpha.equals(DoSkip.skipAction))
			return;
		if (state.completionState != null)
			return;

		for (final Tuple<Action, State> bs : state.beta) {
			final CSMConfiguration newState = new CSMConfiguration(state);
			final State s = bs.r;
			newState.beta.remove(bs);
			newState.alpha = bs.l;
			newState.completionState = s;
			makeSingleTransition(nustate, newState, null, "NextTr2");
		}
	}

	private void applyRuleNextCom(CSMConfiguration state, NuState nustate) {
		if (state.alpha.equals(DoSkip.skipAction))
			return;
		if (state.completionState != null)
			return;
		if (state.beta.size() > 0)
			return;

		for (final CompoundTransition tstrich : state.remainingCoTr) {
			if (tstrich.isTInt())
				continue;

			final List<Tuple<Action, State>> beta = new LinkedList<Tuple<Action, State>>();

			addToBeta: for (final Transition t : tstrich) {
				for (final Transition ts : tstrich) {
					if (t.getSource().stateOf().isSubComponentOf(
							ts.getSource().stateOf()))
						continue addToBeta;
				}

				beta.add(new Tuple<Action, State>(DoSkip.skipAction, t
						.getSource()));
			}

			final CSMConfiguration newState = new CSMConfiguration(state);
			newState.beta = beta;
			newState.currentCoTr = tstrich;
			newState.remainingCoTr.remove(tstrich);
			makeSingleTransition(nustate, newState, null, "NextCom");
		}
	}

	private void applyRuleNextInt(CSMConfiguration state, NuState nustate) {
		if (!state.alpha.equals(DoSkip.skipAction))
			return;
		if (state.completionState != null)
			return;
		if (state.beta.size() != 0)
			return;

		for (final CompoundTransition t : state.remainingCoTr) {
			if (!t.isTInt())
				continue;
			final CSMConfiguration newState = new CSMConfiguration(state);
			newState.beta.add(new Tuple<Action, State>(
					t.get(0).getAction(), null));
			newState.currentCoTr = t;
			newState.remainingCoTr.remove(t);
			makeSingleTransition(nustate, newState, null, "NextInt");
		}
	}

	private void applyRuleNextCompletion(CSMConfiguration state,
			NuState nustate) {
		if (state.completionState != null)
			return;
		if (state.beta.size() != 0)
			return;
		if (state.remainingCoTr.size() != 0)
			return;

		for (final List<CompoundTransition> remainingCoTr : fireable(null,
				state))
			for (final CompoundTransition tstrich : remainingCoTr) {
				final CSMConfiguration newState = new CSMConfiguration(
						state);
				newState.remainingCoTr = new LinkedList<CompoundTransition>();
				newState.remainingCoTr.add(tstrich);
				makeSingleTransition(nustate, newState, null,
						"NextCompletion");
			}
	}

	private void applyRuleNextTrigger(CSMConfiguration state,
			NuState nustate) {
		if (!state.alpha.equals(DoSkip.skipAction))
			return;
		if (state.completionState != null)
			return;
		if (state.beta.size() != 0)
			return;
		if (state.remainingCoTr.size() != 0)
			return;

		if (fireable(null, state).size() != 0)
			return;

		for (final String event : this.basicMappings.events)
			for (final List<CompoundTransition> remainingCoTr : fireable(
					event, state)) {
				final CSMConfiguration newState = new CSMConfiguration(
						state);
				newState.remainingCoTr = remainingCoTr;
				makeSingleTransition(nustate, newState, event,
						"NextTrigger");
			}
	}

	private void applyRuleDefer(CSMConfiguration state, NuState nustate) {
		if (state.completionState != null)
			return;
		if (state.beta.size() != 0)
			return;
		if (state.remainingCoTr.size() != 0)
			return;

		final Set<String> defevents = new HashSet<String>();
		for (final State a : state.active)
			if (a instanceof CompositeState)
				defevents.addAll(((CompositeState) a)
						.getDeferredEventNames());

		for (final String events : defevents) {
			if (fireable(events, state).size() != 0)
				continue;
			final Set<NuState> nuTargets = new HashSet<NuState>();
			nuTargets.add(nustate);
			new NuTransition(this.nuAutomaton, nustate, "defer(" + events
				+ ')', nuTargets, "Defer");
		}
	}

	private void applyRuleAFin(CSMConfiguration state, NuState nustate) {
		if (!(state.completionState instanceof FinalState))
			return;
		if (!state.alpha.equals(DoSkip.skipAction))
			return;

		final CSMConfiguration newState = new CSMConfiguration(state);

		final List<Region> rs = this.basicMappings
				.allSubRegions(state.completionState.regOf());
		for (final Region r : rs)
			newState.history.put(r, null);
		newState.active.add((FinalState) state.completionState);
		newState.completionState = null;
		makeSingleTransition(nustate, newState, null, "aFin");
	}

	private void applyRuleACh(CSMConfiguration state, NuState nustate) {
		if (!state.alpha.equals(DoSkip.skipAction))
			return;
		if (!(state.completionState instanceof ChoiceState))
			return;

		for (final Transition t : this.basicMappings.transitionsPerSourcestate
				.get(state.completionState))
			if (NuGenerator.evalGuard(state, t)) {
				final CSMConfiguration newState = new CSMConfiguration(
						state);
				newState.completionState = null;
				newState.beta.add(new Tuple<Action, State>(t.getAction(), t
						.getTarget()));
				makeSingleTransition(nustate, newState, null, "ACh("
					+ t.getGuard().prettyprint() + ')');
			}
	}

	private void applyRuleAEn(CSMConfiguration state, NuState nustate) {
		if (!(state.completionState instanceof EntryState))
			return;
		if (!state.alpha.equals(DoSkip.skipAction))
			return;

		CompositeState stateOf = (CompositeState) state.completionState
				.stateOf();
		List<Region> dsr = basicMappings.directSubregions.get(stateOf);

		List<Transition> tps = new LinkedList<Transition>();
		for (Transition t : basicMappings.transitionsPerSourcestate
				.get(state.completionState)) {
			if (evalGuard(state, t))
				tps.add(t);
		}

		List<Map<Region, Transition>> fs = new LinkedList<Map<Region, Transition>>();
		fs.add(new HashMap<Region, Transition>());

		for (Region r : dsr) {
			List<Map<Region, Transition>> oldfs = fs;
			fs = new LinkedList<Map<Region, Transition>>();
			for (Map<Region, Transition> phi : oldfs)
				for (Transition t : tps) {
					if (t.getTarget().regOf() != r)
						continue;
					HashMap<Region, Transition> phi2 = new HashMap<Region, Transition>(
							phi);
					phi2.put(r, t);
					fs.add(phi2);
				}
		}

		for (Map<Region, Transition> f : fs) {
			final CSMConfiguration newstate = new CSMConfiguration(state);
			newstate.doActions.put(stateOf, stateOf.getDoAction());
			for (Region r : dsr)
				newstate.beta.add(new Tuple<Action, State>(f.get(r)
						.getAction(), f.get(r).getTarget()));
			newstate.completionState = null;
			newstate.active.add(stateOf);
			makeSingleTransition(nustate, newstate, null, "AEn");
		}
	}

	private void applyRuleAEx(CSMConfiguration state, NuState nustate) {
		if (!(state.completionState instanceof ExitState))
			return;
		if (state.alpha != DoSkip.skipAction)
			return;

		// hier wird in eine der beiden Regeln AEx1 und AEx2
		// verzweigt, damit die selbe Bedingung nicht zweimal getestet
		// werden muss
		for (final Tuple<Action, State> tuple : state.beta)
			if (tuple.r == state.completionState) {
				applyRuleAEx2(state, nustate);
				return;
			}
		for (final InternalState s : state.active)
			if (state.completionState.stateOf().isSubComponentOf(s)) {
				applyRuleAEx2(state, nustate);
				return;
			}

		applyRuleAEx1(state, nustate);
	}

	private void applyRuleAEx1(CSMConfiguration state, NuState nustate) {
		for (final Transition t : state.currentCoTr) {
			if (t.getSource() != state.completionState)
				continue;
			final CSMConfiguration newState = new CSMConfiguration(state);
			newState.active.remove(state.completionState.stateOf());
			state.history.put(state.completionState.regOf(),
					state.completionState.stateOf());
			newState.completionState = null;
			newState.beta.add(new Tuple<Action, State>(t.getAction(), t
					.getTarget()));
			makeSingleTransition(nustate, newState, null, "AEx1");
		}
	}

	private void applyRuleAEx2(CSMConfiguration state, NuState nustate) {
		final CSMConfiguration newState = new CSMConfiguration(state);
		newState.completionState = null;
		makeSingleTransition(nustate, newState, null, "AEx2");
	}

	private String makeLabelForSendevent(
			final ExpressionEnvironment[] follow) {
		String event;
		if (follow[0].sendEventName != null)
			// Wenn follow einen SendEvent enthält, enthält es nur ein
			// Resultat, da random und send nicht im selben
			// Action-Ausdruck vorkommen können. Sollte random
			// irgendwann einmal in den Rang einer Expression erhoben
			// werden, wird diese Codestelle zu ändern sein.
			event = "send(" + follow[0].sendEventName + ','
				+ follow[0].sendEventValue + ')';
		else
			event = null;
		return event;
	}

	/**
	 * erzeugt eine Transition, die von einem NuState zu einer
	 * einelementigen Target-Menge führt. Das einzige Element dieser
	 * Target-Menge ist charakterisiert durch die CSM-Konfiguration
	 * target.
	 * 
	 * @param source der NuState, von dem diese Transition ausgeht
	 * @param target die Konfiguration, die als einzige Element der
	 *            Target-Menge sein soll
	 * @param event der Event, der dieser Transition zugeordnet wird
	 */
	private void makeSingleTransition(NuState source,
			CSMConfiguration target, String event, String debugInfo) {
		final Set<NuState> nuTargets = new HashSet<NuState>();
		addConfigToTargets(target, nuTargets);
		new NuTransition(this.nuAutomaton, source, event, nuTargets,
				debugInfo);
	}

	// Menge Enable nach Def.
	LinkedList<CompoundTransition> getEnabled(String event,
			CSMConfiguration c) {
		final LinkedList<CompoundTransition> l = new LinkedList<CompoundTransition>();
		for (final CompoundTransition ct : this.coTr) {
			if (ct.isEnabled(event, c))
				l.add(ct);
		}
		return l;
	}

	List<List<CompoundTransition>> fireable(String event, CSMConfiguration c) {
		final LinkedList<CompoundTransition> enable = getEnabled(event, c);

		final List<List<CompoundTransition>> pEnabled = powerset(enable);

		final List<List<CompoundTransition>> pFireable = new LinkedList<List<CompoundTransition>>();

		outer: for (final List<CompoundTransition> Tss : pEnabled) {
			if (Tss.size() == 0)
				continue;
			for (final CompoundTransition Tstrich : enable) {
				if (Tss.contains(Tstrich))
					continue;
				for (final CompoundTransition T : Tss) {
					if (Tstrich.hasPriorityOver(T))
						continue outer;
				}

				boolean found = false;
				for (final CompoundTransition T : Tss) {
					if (T.conflictsWith(Tstrich)) {
						found = true;
						break;
					}
				}
				if (!found)
					continue outer;

				for (final CompoundTransition T1 : Tss) {
					for (final CompoundTransition T2 : Tss) {
						if (T1.conflictsWith(T2) && T1 != T2)
							continue outer;
					}
				}
			}

			pFireable.add(Tss);
		}
		return pFireable;
	}

	/**
	 * berechnet die Potenzmenge von m
	 */
	List<List<CompoundTransition>> powerset(List<CompoundTransition> m) {
		final int pssize = 1 << m.size();
		final Vector<List<CompoundTransition>> result = new Vector<List<CompoundTransition>>(
				pssize);
		result.add(0, new LinkedList<CompoundTransition>());
		for (int i = 0; i < m.size(); i++) {
			for (int j = 0; j < 1 << i; j++) {
				final LinkedList<CompoundTransition> r = new LinkedList<CompoundTransition>(
						result.get(j));
				r.add(m.get(i));
				result.add(j + (1 << i), r);
			}
		}
		return result;
	}

	/**
	 * Funktion calc wie im Paper: alpha' ist immer skip, l ist
	 * env.sendEventName, sigma ist env
	 * 
	 * @param env die um wla, nab und sendEventName erweiterte
	 *            Variablenbelegung
	 * @param action die auszuführende Aktion
	 * @return eine Liste der Folge-Zustände; meistens einelementig, bei
	 *         random-Actions auch mehrelementig (kann auch mehrere
	 *         gleiche Folgezustände enthalten)
	 */
	static private ExpressionEnvironment[] calcAction(CSMConfiguration env,
			Action action) {
		return action.evaluate(env.varAssignment);
	}

	/**
	 * Funktion eval wie im Papaer
	 * 
	 * @param state
	 * @param t
	 * @return
	 */
	static private boolean evalGuard(CSMConfiguration state, Transition t) {
		boolean guard;
		final State h = state.history.get(t.getTarget().regOf());
		state.varAssignment.wla = h == t.getTarget().stateOf();
		state.varAssignment.nab = h == null || h instanceof FinalState;
		guard = t.getGuard().evaluate(state.varAssignment);
		state.varAssignment.wla = false;
		state.varAssignment.nab = false;
		return guard;
	}

}

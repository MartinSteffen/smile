package nua;

import java.util.LinkedList;

public class NuAutomaton {
	final public LinkedList<NuState> states;

	final public LinkedList<NuState> rootStates;

	final public LinkedList<NuTransition> transitions;

	boolean isConcrete() {
		return rootStates.size() == 1;
	}
}

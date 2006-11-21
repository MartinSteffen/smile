package nua;

import java.util.LinkedList;

import csm.Event;

public class NuTransition {
	public final NuState source;

	public final Event action;

	public final LinkedList<NuState> targets;
}

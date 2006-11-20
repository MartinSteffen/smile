package csm.statetree;

import java.awt.Point;


/**
 * Abstrakte Oberklasse aller States
 * 
 * @author hsi
 */
public abstract class State extends CSMComponent {

	private int uniqueId;

	State(Point position) {
		super(position);
	}

	/**
	 * Connections
	 * <p>
	 * Definition 4 des Papers gibt an, welche States durch Transitionen
	 * verbunden werden dürfen. Daraus ergibt sich folgende Tabelle, die
	 * in der Methode mayConnectTo implementiert ist:
	 * <p>
	 * source, target, Bedingung
	 * <p>
	 * Sfin, *,false <br>
	 * Scom, *, source == target
	 * <p>
	 * Sexit, Sexit, stateOf(stateOf(source)) == stateOf(target)
	 * <p>
	 * Sexit, {Sentry, Sfinal, Schoice}, regOf(target)==regOf(source)
	 * <p>
	 * Sexit, Scom, false
	 * <p>
	 * Sentry, *, stateOf(source) == stateOf(stateOf(target))
	 * <p>
	 * Schoice, *,regOf(target)==regOf(source)
	 * <p>
	 * {Sentry, Schoice}, {Sexit, Scom},false
	 * <p>
	 * {Sentry, Schoice}, {Sentry, Sfinal, Schoice}, true
	 * <p>
	 * <i>transitionLocation()</i> gibt gemäß Def. 4 des Papers an, ob
	 * von diesem State eine Transition zum Target-State ausgehen darf.
	 * <p>
	 * Wenn eine solche Transition erlaubt ist, dann ermittelt die
	 * Methode die innerste Komponente, innerhalb derer die Transition
	 * verläuft. Also der Scom selbst bei inneren Transitionen, der
	 * stateOf eines EntryStates bei Transitionen hin zu inneren States,
	 * die umgebende Region bei Transitionen zwischen InternalStates.
	 * <p>
	 * <i> Aufgrund der Komplexität der Bedingungen ist diese Funktion
	 * in Methoden der potentiellen Source-States aufgeteilt worden.
	 * <p>
	 * Diese Funktion ist das Herzstück des
	 * Connect-/Disconnect-Mechanismus. Wenn sich irgendwann mal die
	 * Bedingungen ändern, unter denen eine Transition zwei States
	 * verbinden darf, muss nur diese Funktion angepasst werden. </i>
	 * 
	 * @return die Komponente, innerhalb derer verbunden wird, wenn von
	 *         diesem State eine Transition zu dem State target gehen
	 *         darf; null, wenn nicht.
	 */
	abstract CSMComponent transitionLocation(State target);

	/**
	 * Damit beim Laden der CSM die Zuordnung der Connections zu ihren
	 * Source- und Target-States erhalten bleibt, erhält jeder State
	 * eine eindeutige ID. Diese ID wird beim Modifizieren von States
	 * nicht geupdated. Deshalb wird der Statetree bei jedem Speichern
	 * neu durchnummeriert.
	 * <p>
	 * <i>Wer die uniqueId außerhalb des csm-Pakets verwendet, ist
	 * selbst schuld.</i>
	 * 
	 * @return die uniqueId dieses States
	 */

	public int getUniqueId() {
		return this.uniqueId;
	}

	/**
	 * gemäß Paper, Definition 1: stateOf(parent(this))
	 * 
	 * @return die umgebende SubRegion dieses States
	 */
	public final Region regOf() {
		return (Region) stateOf().getParent();
	}

	/**
	 * gemäß Paper, Definition 1
	 * 
	 * @return für einen ConnectionPoint der enthaltende InternalState,
	 *         für einen InternalState diesen selbst
	 */
	public abstract InternalState stateOf();

	protected void setUniqueId(int id) {
		this.uniqueId = id;
	}

	/**
	 * @return true, wenn eine Transition diesen State mit dem
	 *         angegebenen Target-State verbinden darf
	 */
	public boolean mayConnectTo(State target) {
		return transitionLocation(target) != null;
	}

}

package csm;

import java.util.Observable;

import csm.exceptions.ErrTreeNotChanged;
import csm.statetree.OutermostRegion;


/**
 * Oberklasse aller Bestandteile des Datenmodells.
 * <p>
 * Diese Klasse implementiert die Baumstruktur der CoreStateMachine.
 * Jeder Knoten dieses Baumes ist eine Erweiterung der Klasse
 * Observable. Das heißt, eine graphische Oberfläche kann sich bei jedem
 * Knoten als Observer anmelden. Die Bestandteile verfügen über
 * öffentliche Methoden, mittels derer man die Datenstruktur ändern
 * kann. Ändert sich durch den Aufruf einer solchen Methode ein
 * Bestandteil der Datenstruktur, dann wird intern die Methode
 * announceChanges() aufgerufen.
 * <p>
 * Diese ruft erst für diesen Bestandteil und alle parent-Bestandteile
 * die Methode setChanged() auf, um bekanntzumachen, dass dieser Teil
 * des Datenbaumes geändert wurde. Dann ruft sie von der geänderten
 * Komponente bis zum Wurzelknoten für alle Komponenten die Methode
 * notifyObservers(Object arg) auf, wobei arg das geänderte Objekt ist.
 * <p>
 * Es genügt also, wenn eine GUI sich für das Wurzelobjekt des von ihr
 * beobachteten Teilbaums als Observer registieren läßt, um über alle
 * Änderungen in diesem Teilbaum informiert zu werden.
 */
public class ModelNode<Parent extends ModelNode<?>> extends Observable {

	private Parent parent;

	/**
	 * die Funktion parent gemaess Definition 1 des Skripts
	 * <p>
	 * Der Definitionsbereich wurde erweitert auf alle Objekte, die Teil
	 * einer CoreStateMachine sind.
	 * 
	 * @return die CSM-Komponente, in der diese Komponente enthalten ist
	 */
	public final Parent getParent() {
		return this.parent;
	}

	final protected void setParent(Parent parent) throws ErrTreeNotChanged {
		assert parent != null : "please use unsetParent()";

		if (getParent() != null)
			throw new ErrTreeNotChanged("component is already in use");
		if (parent.isComponentOf(this))
			throw new ErrTreeNotChanged(
					"tried to create circular dependency");
		this.parent = parent;
	}

	final protected void unsetParent() throws ErrTreeNotChanged {
		if (getParent() == null)
			throw new ErrTreeNotChanged("no parent to delete from");
		this.parent = null;
	}

	/**
	 * ermittelt die CSM, in der diese Komponente verbaut ist.
	 * 
	 * @return entweder die CoreStateMachine oder null, wenn die
	 *         Komponente in keiner CSM verbaut ist
	 */
	// muss in CoreStateMachine und OutermostRegion ueberschrieben
	// werden
	public CoreStateMachine getCSM() {
		// Default-Verhalten für unverbaute Komponenten
		if (this.parent == null)
			return null;
		// Default-Verhalten für eingebaute Komponenten
		return this.parent.getCSM();
	}

	/**
	 * testet, ob dieser Node in einem RootNode wurzelt. Im Kontext der
	 * CoreStateMachine bedeutet das, dass dieser Node in einer
	 * CoreStateMachine verbaut ist.
	 * <p>
	 * Wir nutzen dabei aus, dass jede Komponente, die in einer
	 * OutermostRegion gründet, auch eine CoreStateMachine besitzt.
	 * Dafür ist diese Methode in OutermostRegion überschrieben.
	 */
	public final boolean isRooted() {
		return getCSM() != null;
	}

	/**
	 * die Funktion > gemaess Definition 1 des Skripts, also die
	 * transitive Huelle der parent-Funktion
	 * <p>
	 * Man beachte, dass die Zugehoerigkeit zu einer CoreStateMachine
	 * nicht mit dieser Methode geprueft werden kann, weil das
	 * Parent-Attibut der OutermostRegion null ist. Deshalb ist hierfür
	 * getCSM() zu verwenden.
	 * 
	 * @param possibleParent die Komponente, in der moeglicherweise this
	 *            enthalten ist
	 * @return true, wenn dieses Objekt ein Unterobjekt des Parameters
	 *         ist
	 */
	public final boolean isSubComponentOf(ModelNode<?> possibleParent) {
		// durchsuche von this ausgehend die Kette der Parents, bis
		// entweder das Argument oder das Ende der Kette erreicht ist
		if (this.getParent() == possibleParent)
			return true;
		if (this.getParent() == null)
			return false;
		return getParent().isSubComponentOf(possibleParent);
	}

	/**
	 * die Funktion >= gemaess Def. 1 des Skriptes, also die
	 * reflexiv-transitive Huelle der parent-Funktion
	 * 
	 * @param possibleParent die Komponente, in der moeglicherweise this
	 *            enthalten ist
	 * @return true, wenn dieses Objekt ein Unterobjekt von
	 *         possibleParent oder possibleParent selbst ist
	 */
	public final boolean isComponentOf(ModelNode<?> possibleParent) {
		return possibleParent == this || isSubComponentOf(possibleParent);
	}

	/**
	 * Benachrichtigt alle Observer ueber Aenderungen an diesem Objekt.
	 * <p>
	 * Setzt 'changed' fuer diese und alle Parent-Komponenten. Ruft dann
	 * fuer diese und alle Parent-Komponenten notifyObservers auf.
	 */
	final protected void announceChanges() {
		setChanged();
		notifyObservers(this);
	}

	/*
	 * @see java.util.Observable#notifyObservers(Object)
	 */
	@Override
	final public void notifyObservers(Object arg) {
		super.notifyObservers(arg);
		if (getParent() != null)
			getParent().notifyObservers(arg);
		else if (this instanceof OutermostRegion && getCSM() != null)
			// Ausnahme weil OutermostRegion-getParent()==null
			getCSM().notifyObservers(arg);
	}

	/*
	 * @see java.util.Observable#setChanged()
	 */
	@Override
	protected void setChanged() {
		super.setChanged();
		if (getParent() != null)
			getParent().setChanged();
		else if (this instanceof OutermostRegion && getCSM() != null)
			// Ausnahme weil OutermostRegion-getParent()==null
			getCSM().setChanged();
	}

}

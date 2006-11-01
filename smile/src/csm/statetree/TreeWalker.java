/**
 * 
 */
package csm.statetree;

/**
 * implementiert ein Visitor-Pattern f�r CSM-Komponentenb�ume.
 * <p>
 * Die Methode visitChildren(CSMComponent) besucht alle Children der Komponente.
 * <p>
 * Das sollte ausreichen, um die meisten Operationen auf Komponentenb�umen zu
 * implementieren.
 * 
 * 
 * @author hsi
 */
public class TreeWalker extends Visitor {

	/**
	 * alle Child-Komponenten besuchen
	 * <p>
	 * mit dieser Methode k�nnen Visitors gebaut werden, die den
	 * CSM-Komponentenbaum rekursiv besuchen
	 */
	protected final void visitChildren(CSMComponent component) {
		component.visitChildren(this);
	}

	protected void visitCSMComponent(CSMComponent component) {
		visitChildren(component);
	}

}

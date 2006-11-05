package csm.statetree;

import java.awt.Point;


/**
 * im Paper als SubRegion \epsilon bekannt (parent() ist null)
 * 
 * @author hs
 */
public final class OutermostRegion extends Region {

	public OutermostRegion() {
		super(new Point(0, 0));
	}

	/**
	 * alle Komponenten mit neuen, eindeutigen uniqueIds versehen
	 */
	public void enumerateStates() {
		/*
		 * Verwende einen CSMVisitor, um über alle Komponenten zu
		 * iterieren.
		 */
		new TreeWalker() {

			int count;

			/*
			 * States werden neu nummeriert, Regionen werden ohne
			 * Änderung traversiert
			 */
			@Override
			final public void visitState(State s) {
				s.setUniqueId(this.count);
				this.count++;
				visitChildren(s);
			}
		}.visitRegion(this);
	}
}

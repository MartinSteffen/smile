package csm.statetree;

import java.awt.Point;


/**
 * im Paper als SubRegion \epsilon bekannt (parent() ist null)
 * 
 * @author hs
 */
public final class OutermostRegion extends Region {

	@Override
	public final CSMComponent parent() {
		return null;
	}

	public OutermostRegion() {
		super(new Point(0, 0));
	}

	/**
	 * alle Komponenten mit neuen, eindeutigen uniqueIds versehen
	 */
	public void enumerateStates() {
		/*
		 * Verwende einen CSMVisitor, um �ber alle Komponenten zu iterieren.
		 */
		new CSMVisitor() {

			int count;

			/*
			 * States werden neu nummeriert, Regionen werden ohne �nderung
			 * traversiert
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

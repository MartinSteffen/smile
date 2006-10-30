package csm;

import java.awt.Point;

import csm.statetree.CompositeState;
import csm.statetree.ExitState;
import csm.statetree.FinalState;
import csm.statetree.OutermostRegion;
import csm.statetree.Region;


// Testklasse, wird nicht in der endgültigen Version enthalten sein 
public class Scratch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final OutermostRegion o = new OutermostRegion();
		final CompositeState s1 =
				new CompositeState(new Point(10, 12), o);
		final Region r1 = new Region(new Point(20, 24), s1);
		new Region(new Point(30, 36), s1);
		new FinalState(new Point(35, 42), r1);
		new ExitState(new Point(37, 44), s1);
		final StringBuilder xmlstring = new StringBuilder();
		StatetreeSaver.statetreeToXML(xmlstring, o);
		System.out.print(xmlstring);
	}
}

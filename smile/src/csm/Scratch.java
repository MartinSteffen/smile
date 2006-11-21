package csm;

import csm.expression.LogicalAnd;
import csm.expression.Expression;
import csm.expression.LogicalFalse;
import csm.expression.LogicalOr;
import csm.expression.LogicalTrue;


/**
 * Testklasse f�r unsere internen Zwecke, wird nicht in der endg�ltigen
 * Version enthalten sein
 * 
 * @author hsi
 */
public class Scratch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * final CompositeState s1 = new CompositeState(new Point(10,
		 * 12), o); final SubRegion r1 = new SubRegion(new Point(20,
		 * 24)); new SubRegion(new Point(30, 36), s1); new
		 * FinalState(new Point(35, 42), r1); new ExitState(new
		 * Point(37, 44), s1);
		 */
		final StringBuilder xmlstring = new StringBuilder();
		// StatetreeSaver.statetreeToXML(xmlstring, o);
		System.out.print(xmlstring);
		Expression<Boolean> e = new LogicalAnd(new LogicalFalse(),
				new LogicalOr(new LogicalFalse(), new LogicalTrue()));
		System.out.println(e.prettyprint());
	}
}

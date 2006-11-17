package csm.action;

import java.util.List;
import java.util.Random;

import csm.VarAssignment;


public final class RandomAction extends Action {

	//final 
	public String varname;

	/**
	 * Die Werte werden nicht auf Gültigkeit geprüft. Eine solche
	 * Prüfung findet erst in der semantischen Analyse statt.
	 */
	final public List<Integer> possibleValues;

	public RandomAction(String varname, List<Integer> pv) {
		assert varname != null;
		assert pv != null;
		assert pv.size()>0; // wir nehmen das einfach mal an...

		this.varname = varname;
		this.possibleValues = pv;
	}

	@Override
	/**
	 * Wir nehmen an possiblevalue sei geordnet
	 * @param pre variableassignment
	 * Es wird eine zu der List possiblevalue gehörende Zufallszahl generiert
	 * und varname zugeordnet
	 * @returns eine neue Variableassignment
	 */
	public final VarAssignment doAction(VarAssignment pre) {
		// TODO RandomAction
		if(pre.variableList.contains(this.varname))
		{
		Random r = new Random();
		int i,j;
		j=this.possibleValues.size()-1;
		do{
		i = r.nextInt()%this.possibleValues.get(j);
		}
		while(this.possibleValues.contains(i));
		pre.setVar(this.varname, i);
		}
		else 
			System.out.println(" Die Variabel"+this.varname+ "existiert nicht in der Varialeliste "  );
		return pre;
	}

	@Override
	public String prettyprint() {	
		final StringBuilder b = new StringBuilder(this.varname+'(');
		b.append(this.possibleValues.get(0));
		for(int i=1;i<this.possibleValues.size();i++) {
			b.append(", ");
			b.append(this.possibleValues.get(i));
		};
		b.append(')');
		return b.toString();
	}


}

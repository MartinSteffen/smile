package modelchecking;

import java.util.LinkedList;

/**
 * Parser <br>
 * Klasse zum Parsen der eingegebenen Formel
 * 
 * @author mtr
 * 
 */

public class Parser {

	char[] stack;

	Expression[] exprs;

	LinkedList<Expression> printQueue = new LinkedList<Expression>();

	MatchingTable table;

	LinkedList<LinkedList<String>> checkerQueue = new LinkedList<LinkedList<String>>();

	LinkedList<Expression> treeQueue = new LinkedList<Expression>();

	int top;

	ModelCheckingGui gui;

	LinkedList<String> bindedVariables;

	LinkedList<String> subst;

	public Parser(ModelCheckingGui gui) {
		this.gui = gui;
	}

	//Ueberpruefen, ob im String Zahlen vorkommen
	private int[] num(String s,int ind) {
		String n = "";
		int index = ind;
		char c = s.charAt(index);
		while (c < '0' || c > '9') {
			index++;
			if (index >= s.length())
				break;
			c = s.charAt(index);
		}

		int[] res = new int[2];
		res[0] = -1;
		res[1] = -1;

		if (index < s.length()) {
			while ((c >= '0' && c <= '9')) {
				n = n + c;
				index++;
				if (index >= s.length())
					break;
				c = s.charAt(index);
			}
			res[0] = Integer.parseInt(n);
			res[1] = index;
		}
		
		return res;
	}

	/**
	 * Vor dem Parsen werden Strings in [...] und <...> durchnummeriert und durch Zahlen ersetzt.
	 * @param s: Formel
	 * @return Formel mit vorgenommenen Ersetzungen
	 */

	public String preparsing(String s) {
		subst = new LinkedList<String>();
		String res = "";
		int index1 = -1;
		int index2 = -1;
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '"') {
				if (index1 == -1) {
					index1 = i;
				} else {
					index2 = i;
				}
			} else {
				if (index1 == -1) {
					if (s.charAt(i) <= '9' && s.charAt(i) >= '0') {
						return null;
					}
					res = res + s.charAt(i);
				}
			}
			if (index1 != -1 && index2 != -1) {
				res = res + String.valueOf(count);
				subst.add(s.substring(index1, index2 + 1));
				index1 = -1;
				index2 = -1;
				count++;
			}
		}

		return res;
	}
	
	/**
	 * Zurückgewinnung der Strings in [...] und <...> nach dem Parsen
	 * @param expr
	 */
	public void postparsing(Expression expr) {
		treeQueue = new LinkedList<Expression>();
		treeQueue.add(expr);
		while (treeQueue.size() != 0) {
			Expression tmp = treeQueue.removeFirst();
			if (subst.size() != 0) {
				String s = tmp.getExpr();
				int[] n = num(s,0);
				while (n[0] >= 0) {
					String sub = subst.get(n[0]);
					String to_rep =String.valueOf(n[0]);
					int index = n[1]-to_rep.length();
					s = s.substring(0,index)+sub+s.substring(n[1]);
					n = num(s,n[1]-to_rep.length()+sub.length());
				}
				tmp.setExpr(s);
			}
			if (tmp.getLeft() != null)
				treeQueue.add(tmp.getLeft());
			if (tmp.getRight() != null)
				treeQueue.add(tmp.getRight());
		}
	}

	/**
	 * 
	 * @param s
	 *            eingegebene Formel mit vorgenommenen Ersetzungen
	 * @return expr {@link Expression}
	 *
	 */

	// Durchlauf bis zur ersten schliessenden Klammer
	public Expression parse(String s) {

		String tmp_s = "";

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != '\n' && s.charAt(i) != ' '
					&& s.charAt(i) != '\t') {
				tmp_s += s.charAt(i);
			}
		}
		if (tmp_s.equals(""))
			return null;
		s = tmp_s;

		bindedVariables = new LinkedList<String>();
		stack = new char[s.length()];
		exprs = new Expression[s.length()];
		table = new MatchingTable();
		top = 0;
		Expression expr = null;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			push(c, null);

			if (c == ')') { //Beginne bei der ersten schliessenden Klammer
				String tmp = "";
				Expression expr1 = null, expr2 = null;
				while (c != '(' && top > 0) { //gehe zurück zu der öffnenden Klammer
					c = pop();				 //und sammle auf dem Weg Variablen und Ersetzungen
					tmp = c + tmp;
					if (table.bigLetter(c) || c == '§') {
						if (expr2 == null) {
							expr2 = exprs[top];
						} else {
							expr1 = exprs[top];
						}
					}

				}
				expr = table.match(tmp, expr1, expr2);
				if (expr == null) {
					push('1', null);
					break;
				}
				push('§', expr);

			}
		}

		if (pop() != '§' || top > 0) { //Fehlerausgabe
			gui.writeError(table.getError() + '\n');
			expr = null;
		} else { //syntaktische Korrektheit
			gui.write("Formel syntaktisch korrekt" + '\n');
		}
		return expr;
	}

	
	private void push(char c, Expression expr) {
		stack[top] = c;
		exprs[top] = expr;
		top++;
	}

	private char pop() {
		top--;
		char c = stack[top];
		return c;
	}

/**
 * Ueberpruefung auf das Vorkommen von freien Variablen oder mehrmals gebundenen Variablen
 * @param expr
 * @return
 */
	public String variablenChecker(Expression expr) {

		String erg = "";

		treeQueue.add(expr);
		checkerQueue.add(new LinkedList<String>());
		// -------------------------
		while (treeQueue.size() != 0) {
			Expression e = treeQueue.removeFirst();
			LinkedList<String> variables = checkerQueue.removeFirst();

			if (!e.isLeaf()) {
				LinkedList<String> tmp = new LinkedList<String>(variables);
				addBindedVariable(tmp, e.getExpr());
				if (e.getLeft() != null) {
					treeQueue.add(e.getLeft());
					checkerQueue.add(tmp);
				}
				if (e.getRight() != null) {
					treeQueue.add(e.getRight());
					checkerQueue.add(tmp);
				}
			} else {
				String s = e.getExpr();
				if (s.length()>1) s = e.getExpr().substring(1, 2);
				if (!findInList(variables, s)) {
					erg += e.getExpr() + " ";
				}
			}

		}
		if (!erg.equals("")) {
			erg = "found free variables :  " + erg;
		}

		String tmp_erg = "";
		while (bindedVariables.size() != 0) {
			String tmp = bindedVariables.removeFirst();
			if (findInList(bindedVariables, tmp)) {
				tmp_erg = tmp_erg + tmp;
			}
		}
		if (!tmp_erg.equals("")) {
			erg = erg + "  double binded variables: " + tmp_erg;
		}
		return erg;
	}
	

	//Suche Blaetter im Baum
	
	private LinkedList<Expression> getLeaves(Expression expr) {
		LinkedList<Expression> q1 = new LinkedList<Expression>();
		LinkedList<Expression> res = new LinkedList<Expression>();
		q1.add(expr);
		while (q1.size() != 0) {
			Expression tmp = q1.removeFirst();
			if (tmp.isLeaf())
				res.add(tmp);
			if (tmp.getLeft() != null) {
				tmp.getLeft().setFather(tmp);
				q1.add(tmp.getLeft());
			}
			if (tmp.getRight() != null) {
				tmp.getRight().setFather(tmp);
				q1.add(tmp.getRight());
			}
		}

		return res;
	}

	/**
	 * Zusammenführung vom Baum zum Automaten
	 * @param expr
	 * @return Expression
	 */
	public Expression completeTree(Expression expr) {
		LinkedList<Expression> leaves = getLeaves(expr);
		for (int i = 0; i < leaves.size(); i++) {
			Expression leaf = leaves.get(i);
			char contVar = leaf.getExpr().charAt(0);
			if (leaf.getExpr().length()>1) contVar = leaf.getExpr().charAt(1);
			Expression father = leaf.getFather();

			while (father != null) {
				char c = father.getExpr().charAt(1);
				if (c == 'µ' || c == 'v') { //Kante von der gebundenen Variable zum zugehörigen Knoten
					char var = father.getExpr().charAt(2);
					if (var == contVar) {
						leaf.setBind(father);
						break;
					}
				}
				father = father.getFather();
			}
		}
		return expr;
	}
	
	/**
	 * Ausgabe der Bindungen von Variablen und des Wertes der Tetta-Funktion
	 * @param expr Expression
	 */

	public void print(Expression expr) {
		printQueue.add(expr);
		while (printQueue.size() != 0) {
			Expression tmp = printQueue.removeFirst();
			Expression b = tmp.getBind();
			int tetta = tmp.getTetta();
			String zusatz = "";
			if (b != null) {
				zusatz = " [ " + b.getExpr().charAt(1) + " -binding to "
						+ b.getExpr() + " ]";
			}
			gui.write(tmp.getExpr() + zusatz + " tetta = " + tetta + '\n');

			if (tmp.getLeft() != null)
				printQueue.add(tmp.getLeft());
			if (tmp.getRight() != null)
				printQueue.add(tmp.getRight());
		}
	}

	//Suche in der Formel nach bestimmten Strings
	private boolean findInList(LinkedList list, String tmp) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(tmp)) {
				return true;
			}
		}
		return false;
	}

	//Hinzufügen der Bindung zu den Variablen
	private void addBindedVariable(LinkedList<String> list, String expr) {

		if (expr.charAt(1) == 'µ' || expr.charAt(1) == 'v') {
			list.add(expr.substring(2, 3));
			bindedVariables.add(expr.substring(2, 3));
		}

	}

	/**
	 * Ausrechnen des Wertes der Tetta-Funktion zu den einzelnen Knoten
	 * @param node Expression
	 */
	public void calculatingTetta(Expression node) {

		if (node.isLeaf()) { //Beginne bei den Blaettern
			return;
		}

		int left_tetta = -1;
		int right_tetta = -1;

		if (node.getLeft() != null) { //Ausrechnen im linken Teilbaum
			calculatingTetta(node.getLeft());
			left_tetta = node.getLeft().getTetta();
		}
		if (node.getRight() != null) {  //Ausrechnen im rechten Teilbaum
			calculatingTetta(node.getRight());
			right_tetta = node.getRight().getTetta();
		}

		char op = node.getExpr().charAt(1);
		char opl = 'l';
		char opr = 'r';

		if (node.getLeft() != null)
			if (!node.getLeft().isLeaf()) {
				opl = node.getLeft().getLast();
			}

		if (node.getRight() != null)
			if (!node.getRight().isLeaf()) {
				opr = node.getRight().getLast();
			}

		if (left_tetta >= right_tetta) {  //Wenn der Wert von Tetta im linken Teilbaum groesser ist,
			node.setTetta(left_tetta);   //als im rechten, uebernehme ihn, sonst andersrum
			if (op == 'µ' || op == 'v') {
				node.setLast(op);
				if (left_tetta == 0) {
					if (op == 'µ')
						node.setTetta(1);
				} else {
					if (op != opl) {
						if (opl == 'µ' || opl == 'v')
							node.setTetta(left_tetta + 1);
					}
				}

			} else {
				if (node.getLeft() != null)
					node.setLast(node.getLeft().getLast());
			}
		} else {
			node.setTetta(right_tetta);
			if (op == 'µ' || op == 'v') {
				if (op != opr) {
					if (opr == 'µ' || opr == 'v')
						node.setTetta(right_tetta + 1);
				}

			} else {
				if (node.getRight() != null)
					node.setLast(node.getRight().getLast());
			}
		}

	}
}

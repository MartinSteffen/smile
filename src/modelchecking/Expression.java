package modelchecking;

import java.util.LinkedList;

/**
 * Expression
 * 
 * Expressions sind Knoten im Baum.
 * 
 * @author mtr
 * 
 */

public class Expression {

	LinkedList<String> varcheck = new LinkedList<String>();

	String expr;

	int tetta = 0;
	
	char last = 'l';

	Expression left = null, right = null, father = null, bind = null;

	public Expression(String s) {
		expr = s;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}
/**
 * Vater im Baum
 */
	public Expression getFather() {
		return father;
	}

	public void setFather(Expression father) {
		this.father = father;
	}

	/**
	 * Linker Sohn
	 */
	public Expression getLeft() {
		return left;
	}

	public void setLeft(Expression left) {
		this.left = left;
	}
	
	/**
	 * Rechter Sohn
	 */

	public Expression getRight() {
		return right;
	}

	public void setRight(Expression right) {
		this.right = right;
	}
	
	/**
	 * Variablen Checker
	 */

	public void addToList(char c) {
		String tmp = "";
		varcheck.add(tmp + c);
	}
	
	/**
	 * Blaetter im Baum
	 */

	public boolean isLeaf() {
		return (left == null && right == null);
	}
/**
 * 
 * Bindung der Variablen zu den zugehoerigen µ- oder v- Knoten
 */
	public Expression getBind() {
		return bind;
	}

	public void setBind(Expression bind) {
		this.bind = bind;
	}
	
	/**
	 * Tetta-Funktion
	 */

	public int getTetta() {
		return tetta;
	}

	public void setTetta(int tetta) {
		this.tetta = tetta;
	}

	public char getLast() {
		return last;
	}

	public void setLast(char last) {
		this.last = last;
	}

}

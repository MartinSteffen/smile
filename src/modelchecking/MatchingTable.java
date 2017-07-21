package modelchecking;

/**
 * MatchingTable <br>
 * Klasse zum Ueberpruefen von Subformeln und Bilden von Teilbaeumen
 * 
 * @author mtr
 * 
 */

public class MatchingTable {

	/**
	 * Erlaubte Ausdruecke nach Grammatik des mu - Kalkuels zum Ueberpruefen und
	 * Bilden von Teilbaeumen
	 */
	String[] patterns = { "(¤&¤)", "(¤|¤)", "(µ¤.¤)", "(v¤.¤)", "(<$>¤)",
			"([$]¤)", "(¤)" };

	String error = "Formel falsch. Ueberpruefen Sie Ihre Klammern!";

	/**
	 * 
	 * @param s
	 *            Subformel der eigentlichen Formel
	 * @param expr1
	 *            schon ersetzte {@link Expression}
	 * @param expr2
	 *            schon ersetzte {@link Expression}
	 * @return {@link Expression} match als Teilbaum oder Null, falls kein
	 *         gueltiger Ausdrueck nach Grammatik
	 */
	public Expression match(String s, Expression expr1, Expression expr2) {
		Expression match = null;

		// Ersetze alle Variablen (Grossbuchstaben) durch das Symbol '¤'
		String erg = null;
		String tmp = s;
		for (int i = 0; i < s.length(); i++) {
			if (bigLetter(s.charAt(i))) {

				erg = s.replace(s.charAt(i), '¤');
				s = erg;
			}
		}

		// Ersetze alle Strings, die hier schon Zahlen sind, in <> und [] durch
		// das Symbol '$'
		int index1 = -1;
		int index2 = -1;
		for (int i = 0; i < s.length(); i++) {
			if ((s.charAt(i) == '[') || (s.charAt(i) == '<'))
				index1 = i;
			if ((s.charAt(i) == ']') || (s.charAt(i) == '>'))
				index2 = i;
		}
		if (index1 != -1 && index2 != -1) {
			String tmp_s = s.substring(index1 + 1, index2);
			boolean not_allowed = false; // doppelte Klammern sind nicht
											// erlaubt
			for (int i = 0; i < tmp_s.length(); i++) {
				if (tmp_s.charAt(i) == ')' || tmp_s.charAt(i) == '('
						|| tmp_s.charAt(i) == '[' || tmp_s.charAt(i) == ']'
						|| tmp_s.charAt(i) == '<' || tmp_s.charAt(i) == '>') {
					not_allowed = true;
				}
			}
			if (!not_allowed)
				s = s.substring(0, index1 + 1) + '$' + s.substring(index2);
		}

		// Teilbaum ausgeben
		for (int i = 0; i < patterns.length; i++) {
			if (patterns[i].equals(s)) {
				match = buildSubtree(tmp, expr1, expr2);
				break;
			}
		}
		if (match == null) {
			if (expr2 == null) {
				error = "Dieser Ausdruck ist systaktisch nicht erlaubt: " + tmp;
			} else {
				error = "Dieser Ausdruck ist systaktisch nicht erlaubt: "
						+ tmp.replaceAll("¤", expr2.getExpr());
			}
		}

		return match;
	}

	// Teilbaum bilden
	private Expression buildSubtree(String s, Expression expr1, Expression expr2) {

		Expression res = null;
		String tmp = s.substring(1, s.length() - 1);
		char c = tmp.charAt(0);
		switch (c) {
		case 'µ': // Fall: mu
			if (expr2 == null) {
				error = "Dieser Ausdruck ist systaktisch nicht erlaubt: " + s;
				return null;
			}
			res = new Expression("(" + tmp.substring(0, tmp.length() - 1)
					+ expr2.getExpr() + ")");
			res.setLeft(expr2);
			break;
		case 'v': // Fall: nu
			if (expr2 == null) {
				error = "Dieser Ausdruck ist systaktisch nicht erlaubt: " + s;
				return null;
			}
			res = new Expression("(" + tmp.substring(0, tmp.length() - 1)
					+ expr2.getExpr() + ")");
			res.setLeft(expr2);
			break;
		case '<': // Fall: < >
			if (expr2 == null) {
				error = "Dieser Ausdruck ist systaktisch nicht erlaubt: " + s;
				return null;
			}
			res = new Expression("(" + tmp.substring(0, tmp.length() - 1)
					+ expr2.getExpr() + ")");
			res.setLeft(expr2);
			break;
		case '[': // Fall: [Ê]
			if (expr2 == null) {
				error = "Dieser Ausdruck ist systaktisch nicht erlaubt: " + s;
				return null;
			}
			res = new Expression("(" + tmp.substring(0, tmp.length() - 1)
					+ expr2.getExpr() + ")");
			res.setLeft(expr2);
			break;

		default: // Fall: Variablen
			if (tmp.length() > 1) {
				// Ueberpruefen, ob Variablen oder Subformeln vorkommen
				String v1 = tmp.substring(0, 1);
				String v2 = tmp.substring(2, 3);
				String op = tmp.substring(1, 2);
				if (v1.equals("¤")) {
					String e = null;
					if (expr1 != null)
						e = expr1.getExpr();
					else
						e = expr2.getExpr();
					tmp = e + op + v2;
				} else {
					expr1 = new Expression(v1);
				}
				if (v2.equals("¤")) {
					tmp = tmp.replace("¤", expr2.getExpr());
				} else {
					if (v1.equals("¤")) {
						expr1 = expr2;
					}
					expr2 = new Expression(v2);
				}
				res = new Expression("(" + tmp + ")");
				res.setLeft(expr1);
				res.setRight(expr2);
			} else {
				if (expr2 != null) {

					res = expr2;
				} else {

					res = new Expression("(" + tmp + ")");
				}
			}
			break;

		}
		return res;
	}

	// Variablen sind Grossbuchstaben
	public boolean bigLetter(char c) {
		return ((byte) c) > 64 && ((byte) c) < 91;
	}

	// Fehlerausgabe
	public String getError() {
		return error;
	}
}
package csm;

import java.util.Set;


/**
 * Jede Klasse, die irgendwo Variablen oder Events verwendet und Teil
 * einer CSM ist, die diese Variablen und Events definieren muß, soll
 * dieses Interface implementieren
 */
public interface ContainsVarsAndEvents {

	/**
	 * Ermittelt die Menge der Variablen, die in dieser Komponente oder
	 * ihrer Unterkomponenten verwendet wird, ohne in definedVars
	 * definiert zu sein.
	 * 
	 * @param definedVars die Menge der definierten Variablen
	 * @return die Menge aller Variablen, die verwendet werden, aber
	 *         nicht in definedVars enthalten sind. Falls keine
	 *         undefinierten Variablen gefunden wurden, kann auch ein
	 *         Nullpointer zurückgegeben werden. Die Ergebnis-Sets
	 *         sollen nicht wiederverwendet werden, so dass z.B. eine
	 *         zweistelliger Operator einfach die undefinierten
	 *         Variablen des zweiten Arguments zu der Ergebnismenge des
	 *         ersten Arguments hinzufügen kann.
	 */
	String firstUndefinedVar(Set<String> definedVars);

	/**
	 * Ermittelt die Menge der Events, die in dieser Komponente oder
	 * ihrer Unterkomponenten verwendet wird, ohne in definedEvents
	 * definiert zu sein.
	 * 
	 * @param definedEvents die Menge der definierten Events
	 * @return die Menge aller Events, die verwendet werden, aber nicht
	 *         in definedEvents enthalten sind. Falls keine
	 *         undefinierten Events gefunden wurden, kann auch ein
	 *         Nullpointer zurückgegeben werden. Die Ergebnis-Sets
	 *         sollen nicht wiederverwendet werden, so dass z.B. eine
	 *         zweistelliger Operator einfach die undefinierten Events
	 *         des zweiten Arguments zu der Ergebnismenge des ersten
	 *         Arguments hinzufügen kann.
	 */

	String firstUndefinedEvent(Set<String> definedEvents);

}

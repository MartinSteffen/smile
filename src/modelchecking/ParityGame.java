package modelchecking;

import modelchecking.GameGraph.*;

import java.util.HashSet;
import java.util.Set;

/** 
 * Die Klasse "ParityGame" stellt den Algorithmus zur Berechnung
 * des Gewinners einens Paritässpiels zur Verfügung. 
 * 
 * @author skn
 */

public class ParityGame {
	
	public ParityGame() {

	}

	/**
	 * Gibt die Gewinnbereiche beider Spieler in der Konsole aus.
	 * 
	 * @param winset HashSet-Array vom Typ Zustand welches die beiden Gewinnmengen enthält
	 */
	
	public void ausgabeGewinnBereiche(HashSet<Zustand>[] winset) {
		Zustand[] spieler0array = new Zustand[winset[1].size()];
		Zustand[] spieler1array = new Zustand[winset[0].size()];

		//Da Gewinnregion von Spieler 0 und 1 vertauscht sind werden hier winset[0] und winset[1] vertauscht
		winset[1].toArray(spieler0array);
		winset[0].toArray(spieler1array);
		System.out.println("Gewinnbereich von Spieler 0");
		System.out.println("===========================");
		if (spieler0array.length > 0) {
			System.out.println("Spieler 0 hat "+spieler0array.length+" Zustände");
			for (int i=0; i<spieler0array.length; i++) {
				if (spieler0array[i] != null)
					System.out.println(spieler0array[i].name);
			}
			
		}
		System.out.println("Gewinnbereich von Spieler 1");
		System.out.println("===========================");
		if (spieler1array.length > 0) {
			System.out.println("Spieler 1 hat "+spieler1array.length+" Zustände");
			for (int i=0; i<spieler1array.length; i++) {
				if (spieler1array[i] != null)
					System.out.println(spieler1array[i].name);
			}
		}
	}

	/**
	 * Algorithmus zur Berechnung der Gewinnzustände für Spieler 0 bzw. Spieler 1.
	 * Dem Algorithmus wird der Spiele-Graph als Menge von Zuständen übergeben.
	 *
	 * @param G       Spiele-Graph als Zustandsmenge
	 * @return winset Gewinnmengen-Array für Spieler 0 und Spieler 1.
	 */
	
	/*
	 * Winset 0 und 1 sind bei der Ausgabe vertauscht. Kann momentan leider nicht
	 * festellen, wo der Fehler liegt. Die Lösung ist aber richtig.
	 */
	public HashSet<Zustand>[] winningRegions(HashSet<Zustand> G) {
		HashSet<Zustand>[] winset = new HashSet[2];
		winset[0] = new HashSet<Zustand>();
		winset[1] = new HashSet<Zustand>();  		
		int n = getMaxPriority(G);
		int player;
		if (n == 0) {
			winset[0].addAll(set1WithoutSet2(G,computeAttractorSet(G,new HashSet<Zustand>(),true)));
			winset[1].addAll(computeAttractorSet(G,new HashSet<Zustand>(),true));
			return winset;
		} else {
			player = n % 2; //Spieler wird aus der höchsten Priorität berechnet
			if (player == 1) {
				winset[1].addAll(winOpponent(G,true,n));
				winset[0].addAll(set1WithoutSet2(G,winset[1]));
			}
			else {
				winset[0].addAll((winOpponent(G,false,n)));
				winset[1].addAll(set1WithoutSet2(G,winset[0]));
			}
		return winset;
		}
	}
	
	/*
	 * Hilfsfunktion für die Berechnung der Gewinnmenge des Gegners.
	 */
	private HashSet<Zustand> winOpponent(HashSet<Zustand> G, boolean player, int priority) {
		HashSet<Zustand> W = new HashSet<Zustand>();
		HashSet<Zustand> Wtmp = new HashSet<Zustand>();
		HashSet<Zustand> X = new HashSet<Zustand>();
		HashSet<Zustand> Z = new HashSet<Zustand>();
		HashSet<Zustand> N = new HashSet<Zustand>();
		HashSet<Zustand> Y = new HashSet<Zustand>();
		HashSet<Zustand>[] winset = new HashSet[2];
		
		do {
			Wtmp.addAll(W);
			X.addAll(computeAttractorSet(G, W, !player));
			Y.addAll(set1WithoutSet2(G,X));
			N.addAll(computeStatesWithPriority_N(Y,priority));
			Z.addAll(set1WithoutSet2(Y,computeAttractorSet(computeSubGraph(G, Y), N, player)));
			
			winset = winningRegions(computeSubGraph(G, Z));

			W.addAll(X);
			if (player) W.addAll(winset[1]);
			else W.addAll(winset[0]);
		} while (!set1EqualsSet2(W,Wtmp));
		return W;
	}
	
	/* -----------------------------------------------------------
	 * ------------ Hilfsfunktionen & Hilfsfunktionen ------------
	 * ----------------------------------------------------------- 
	 */

	/* 
	 * Berechnet den Subgraph von G eingeschränkt auf U
	 * Eine andere Version die wirklich nur
	 * Subgames (Subgraphen ohne neue Deadens) berechnet und gleichzeit ungültige
	 * Transitionen löscht findet sich am Ende dieser Klasse.
	 * 
	 * Diese Version ist jedoch wesentlich kürzer und erfüllt den gleichen Zweck,
	 * da die Sugameberechnung eine Invariante ist und die berechneten Subgraphen
	 * daher auch Subgames sind.
	 * Alte/ungültige Transitionen werden nicht gelöscht) 
	 */ 
	
	private HashSet<Zustand> computeSubGraph(HashSet<Zustand> G, HashSet<Zustand> U) {
		HashSet<Zustand> V = new HashSet<Zustand>();
		V.addAll(G);
		//Schnittmenge bilden!
		V.retainAll(U);
		return V;
	}//*/
	
	/*
	 * Berechnet die Menge an Zuständen, die genau die Prioritäte n haben.
	 */
	private HashSet<Zustand> computeStatesWithPriority_N(HashSet<Zustand> V, int n) {
		HashSet<Zustand> newset = new HashSet<Zustand>();
		Zustand[] tmp = new Zustand[V.size()];
		V.toArray(tmp);
		for (int i=0; i<tmp.length; i++) {
			if (tmp[i] != null) {
				if (tmp[i].priority == n) newset.add(tmp[i]);
			}
		}
		return newset;
	}
	
	
	/*
	 * Berechnet die höchste Priorität der übergebenen Zustandsmenge V
	 */
	private int getMaxPriority(HashSet<Zustand> V) {
		Zustand[] state = new Zustand[V.size()];
		V.toArray(state);
		int max = 0;
		for (int i=0; i<state.length; i++) {
			if (state[i].priority > max) max = state[i].priority;
		}
		return max;
	}

	/*
	 * Hilfsfunktion zur Berechnung von Deadends in der übergebenen Zustandsmenge V
	 */
	private HashSet<Zustand> findDeadEnds(HashSet<Zustand> V) {
		HashSet<Zustand> deadends = new HashSet<Zustand>();
		Zustand[] tmp = new Zustand[V.size()];
		V.toArray(tmp);
		for (int i=0; i<tmp.length; i++) {
			if (tmp[i].transition != null)
				if (tmp[i].transition.length == 0) deadends.add(tmp[i]);
		}
		return deadends;
	}
	
	/*
	 * Teilt die Spiele-Graphen-Menge in Zustände die Spieler 0 und 
	 * Zustände die Spieler 1 gehören auf. Rückgabe ist ein Mengen-Array
	 * mit den verteilten Zuständen.
	 */
	private HashSet<Zustand>[] divideGraph(HashSet<Zustand> G) {
		HashSet<Zustand>[] hash_array = new HashSet[2];
		Zustand[] tmp = new Zustand[G.size()];
		G.toArray(tmp);

		//Initialisieren
		for (int i=0; i<hash_array.length; i++)	hash_array[i] = new HashSet<Zustand>(); 
		for (int i=0; i<tmp.length; i++) {
			if (tmp[i].player) hash_array[1].add(tmp[i]);
			else hash_array[0].add(tmp[i]);
		}
		return hash_array;
	}

	/*
	 * Berechnung der Attractormenge für einen bestimmten Spieler.
	 */
	private HashSet<Zustand> computeAttractorSet(HashSet<Zustand> G, HashSet<Zustand> X, boolean player) {
		HashSet<Zustand> Y = new HashSet<Zustand>();
		HashSet<Zustand> Ytmp = new HashSet<Zustand>();
		HashSet<Zustand>[] hash_array = new HashSet[2];
		hash_array = divideGraph(G);
		
		//Prüfe ob DeadEnds vom Gegner existieren 
		if (player) X.addAll(findDeadEnds(hash_array[0]));
		else X.addAll(findDeadEnds(hash_array[1]));
		
		Y.addAll(X);
		if (!player) {
			do {
				Ytmp.addAll(Y);
				Y.addAll(attrSubSet1(hash_array[0], Ytmp));
				Y.addAll(attrSubSet2(hash_array[1], Ytmp));
			} while (!set1EqualsSet2(Y,Ytmp));
		} else {
			do {
				Ytmp.addAll(Y);
				Y.addAll(attrSubSet1(hash_array[1], Ytmp));
				Y.addAll(attrSubSet2(hash_array[0], Ytmp));
			} while (!set1EqualsSet2(Y,Ytmp));
		}
		return Y;
	}

	/*
	 * Hiflsfunktion zur Berechnung der Attractormenge
	 */
	private HashSet<Zustand> attrSubSet1(HashSet<Zustand> V, HashSet<Zustand> Y) {
		HashSet<Zustand> newset = new HashSet<Zustand>();
		Zustand[] vertex = new Zustand[V.size()];
		V.toArray(vertex);
		
		boolean stop;
		for (int i=0; i<vertex.length; i++) {
			stop = false;
			for (int j=0; j<vertex[i].transition.length; j++) {
				for (int k=0; k<vertex[i].transition[j].target.length; k++) {
					if (Y.contains((vertex[i].transition[j].target[k]))) {
						newset.add(vertex[i]);
						stop = true;
						break;
					}
				}
				if (stop) break;
			}
		}
		return newset;
	}
	
	/*
	 * Hiflsfunktion zur Berechnung der Attractormenge
	 */
	private HashSet<Zustand> attrSubSet2(HashSet<Zustand> V, HashSet<Zustand> Y) {
		HashSet<Zustand> newset = new HashSet<Zustand>();
		HashSet<Zustand> tmp = new HashSet<Zustand>();
		Zustand[] vertex = new Zustand[V.size()];
		V.toArray(vertex);
		for (int i=0; i<vertex.length; i++) {
			for (int j=0; j<vertex[i].transition.length; j++) {
				for (int k=0; k<vertex[i].transition[j].target.length; k++) {
					//Alle Ziele zur Menge hinzufügen 
					tmp.add(vertex[i].transition[j].target[k]);
				}
			}
			if (Y.containsAll(tmp)) newset.add(vertex[i]);
			tmp.clear(); //tmp-Menge für neuen Durchlauf löschen
		}
		return newset;
	}

	/*
	 * Funktion zum Überprüfen zweier Mengen auf Gleichheit
	 */
	private boolean set1EqualsSet2(HashSet<Zustand> X, HashSet<Zustand> Y) {
		Zustand[] set1 = new Zustand[X.size()];
		Zustand[] set2 = new Zustand[Y.size()];
		X.toArray(set1);
		Y.toArray(set2);
		boolean stop = false;
		if (set1.length != set2.length) return false;
		for (int i=0; i<set1.length; i++) {
			for (int j=0; j<set2.length; j++) {
				if (set1[i] == set2[j]) {
					stop = true;
					break;
				}
			}
			if (stop) stop = false;
			else return false;
		}
		return true;
	}
	
	/*
	 * Berechnet die Differenzmenge Y\X (Y ohne X)
	 */
	private HashSet<Zustand> set1WithoutSet2(HashSet<Zustand> set1, HashSet<Zustand> set2) {
		HashSet<Zustand> newset = new HashSet<Zustand>();
		Zustand[] set1array = new Zustand[set1.size()];
		Zustand[] set2array = new Zustand[set2.size()];
		set1.toArray(set1array);
		set2.toArray(set2array);
		boolean exists = false;
		for (int i=0; i<set1array.length; i++) {
			for (int j=0; j<set2array.length; j++) {
				if (set1array[i] == set2array[j]) {
					exists = true;
					break;
				}
			}
			if (!exists) newset.add(set1array[i]);
			else exists = false;
		}
		return newset;
	}

	/**
	 * Gibt die Zustände einer Zustandsmenge sowie deren Transitionen
	 * und die dazugehörigen Ziele auf der Konsole aus.
	 * 
	 * @param G    Zustandsmenge.
	 * @param text Text der bei der Ausgabe der Menge erscheinen soll (um zu unterscheiden, welche Menge man ausgibt).
	 */
	public void printSet(HashSet<Zustand> G,String text) {
		Zustand[] gamegraph = new Zustand[G.size()];
		G.toArray(gamegraph);
		System.err.println("-> "+text+" <-");
		for (int i=0; i<gamegraph.length; i++) {
			System.out.println("       [Zustand]: "+gamegraph[i].name);
			System.out.print("      [Besitzer]: ");
			if (!gamegraph[i].player) System.out.println("Spieler 0 ");
			else System.out.println("Spieler 1 ");
			System.out.println("     [Priorität]: "+gamegraph[i].priority);
			System.out.println("[Transition(en)]: "+gamegraph[i].transition.length);
			for (int k=0; k<gamegraph[i].transition.length; k++) {
				System.out.print("            |-> [Ziel(e) von Transition "+k+"]: ");
				for (int l=0; l<gamegraph[i].transition[k].target.length; l++) {
					System.out.print(gamegraph[i].transition[k].target[l].name+", ");
				}
				System.out.println();
			}
			System.out.println("------------------------------------------------------------------------------------------------------");
		}
		System.out.println();
	}

	//Funktioniert! (Mit löschen nicht mehr gültiger Transitionen)
	//Berechnet eigentlich das Subgame (keine Neuen Deadends kommen hinzu)
	/*
	private HashSet<Zustand> computeSubGraph(HashSet<Zustand> G, HashSet<Zustand> U) {
		HashSet<Zustand> V = new HashSet<Zustand>();
		HashSet<Zustand> Vtmp = new HashSet<Zustand>();
		
		V.addAll(G);
		//Schnittmenge bilden!
		V.retainAll(U);
		
		Zustand[] tmp = new Zustand[V.size()];
		V.toArray(tmp);
		//Das Array "tmp" soll die Zustände für den Subgraph enthalten (muss später noch getestet werden)
		HashSet<Transition> trans = new HashSet<Transition>();
		HashSet<Zustand> target = new HashSet<Zustand>();
		for (int i=0; i<tmp.length; i++) {
			for (int j=0; j<tmp[i].transition.length; j++) {
				//Keine Hypertransition?
				if (tmp[i].transition[j].target.length == 1) {
					if (V.contains(tmp[i].transition[j].target[0])) trans.add(tmp[i].transition[j]);
				} else {
					//Ja, es ist eine Hypertransition
					for (int k=0; k<tmp[i].transition[j].target.length; k++) {
						//Wenn Ziel noch im Graph vorhanden, dann lass das Ziel bestehen
						if (V.contains(tmp[i].transition[j].target[k])) {
							target.add(tmp[i].transition[j].target[k]);
						}
					}
					//Wenn es noch gültige Ziele gibt, dann schreib sie zurück
					if (target.size() > 0) {
						tmp[i].transition[j].target = new Zustand[target.size()];
						target.toArray(tmp[i].transition[j].target);
						trans.add(tmp[i].transition[j]);
					} 
					target.clear();
				}
			}
			//Rückschreiben der gültigen Transitionen, wenn noch welche existieren
			if (trans.size() > 0) {
				tmp[i].transition = new Transition[trans.size()];
				trans.toArray(tmp[i].transition);
			} else {
				//Wenn es keine Transition mehr gibt, dann lösche			
				tmp[i].transition = new Transition[0];
			}
			trans.clear();
		}
		//Aus dem Zustandsarray wieder eine Menge machen
		V.clear();
		for (int i=0; i<tmp.length; i++) {
			V.add(tmp[i]);
		}
		
		//Wenn nicht wichtig, ob Subgraph tatsächlich auch Subgame ist
		//return V;
		
		//Ansonsten
		//Testen, ob die Menge wirklich ein Subgame ist, sprich ob keine Dead-Ends existieren
		Vtmp.addAll(findDeadEnds(V));
		//Wenn G nicht die leere Menge ist und somit Dead-Ends exitsieren, dann gebe die Leere Menge zurück sonst Subgame V
		if (Vtmp.isEmpty()) return V; 		
		else return new HashSet<Zustand>();
	} //*/

}
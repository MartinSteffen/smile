package modelchecking;

import nua.NuAutomaton;
import nua.NuState;
import java.util.LinkedList;
import nua.NuTransition;
import java.util.HashSet;

/** 
 * Diese Klasse stellt den Spiele-Graphen f�r den Parity-Game-Algorithmus zur Verf�gung.
 * Die Prozedur zur Berechnung des Spielegraphen aus Nu-Automat und Alternating-Tree-Automat
 * hei�t generateGameGraph()
 *  
 * @author skn
 */

public class GameGraph {
	
	private static int labelposition = 0;  //Gibt die aktuelle Position im LabelStructurearray an
	private static LabelStructure[] label; //Beeinhaltet s�mtliche Label aus der Formel  
	private static Zustand[] treestate;    //Zust�nde aus dem Alternating-Tree-Automaten
	private static LinkedList<TreeToArrayStructure> expressions = new LinkedList<TreeToArrayStructure>();
	
	public GameGraph() {
		
	}	
	
	/**
	 * Berechnet die duale Formel zu der �bergebenen Formel.
	 * (� wird zu v, [] zu <> und & zu | und nat�rlich umgekehrt)
	 * 
	 * @param formel Orignialformel.
	 * @return dual  Die dazugeh�rige duale Formel.
	 */ 
	
	//Berechnet zu einer gegebenen Formel die duale Formel
	//(wird nachher vom Satisfaction-Check ben�tigt)
	public String computeDualFormula(String formel) {
		String dual = new String();
		for (int i=0; i<formel.length(); i++) {
			switch (formel.charAt(i)) {
				case '�':dual += 'v';break;
				case 'v':dual += '�';break;
				case '&':dual += '|';break;
				case '|':dual += '&';break;
				case '[':dual += '<';break;
				case ']':dual += '>';break;
				case '<':dual += '[';break;
				case '>':dual += ']';break;
				default: dual += formel.charAt(i); 
			}
		}
		return dual;
	}

	/** 
	 * Macht aus dem erstellten Spielegraphen eine Menge von Zust�nden f�r 
	 * die sp�tere Berechnung beim im ParityGame-Algorithmus
	 * 
	 * @param gamegraph Ein Spiele-Graph vom Typ Zustand als Array der Dimension 2.
	 * @return game_set Der Spiele-Graph als Menge von Zust�nden .
	 */
	
	public HashSet<Zustand> gameGraphToSet(Zustand[][] gamegraph) {
		HashSet<Zustand> parity_game = new HashSet<Zustand>();
		for (int i=0; i<gamegraph.length; i++) {
			for (int j=0; j<gamegraph[i].length; j++) {
				parity_game.add(gamegraph[i][j]);
			}
		}
		return parity_game;
	}	
	
	/** 
	 * Berechnet den Spielegraphen aus dem gegebenen nu-Automaten und dem 
	 * Alternating Tree-Automaten und gibt ein Array von Zust�nden zur�ck.
	 * Dabei sind die Tupel (s,q) �ber das zur�ckgegebnen Zustandsarray
	 * gamegraph[s][q] ansprechbar.
	 * 
	 * @param nua        Ein Nu-Automat.
	 * @param tree       Ein Alternating-Tree-Automat (Dargestellt als Baum vom Typ Expression).  
	 * @return gamegraph Der generierte Gamegraph aus Nu-Automat und Alternating-Tree-Automat.  
	 */
	
	public Zustand[][] generateGameGraph(NuAutomaton nua,Expression tree) {
		int s = nua.numStates;
		int q = countTreeStates(tree);
		int newstatepos = nua.numStates;

		//Alle Nustates in einem Array
		NuState[] nustate = new NuState[s];
		nua.states.toArray(nustate);

		treestate = generateTreeGraph(tree);
		
		/* Neue Anzahl der NuStates berechnen unter Ber�cksichtigung 
		 * der Switchbedingung aus Table 1 (<>)
		 */
		for (int i=0; i<label.length; i++) {
			for (int j=0; j<nua.numStates; j++) {
				NuTransition[] nutransition = new NuTransition[nustate[j].outgoingTransitions().size()];
				nustate[j].outgoingTransitions().toArray(nutransition);
				for (int k=0; k<nutransition.length; k++) {
					if (nutransition[k].action.compareTo(label[i].name) == 0) {
						/* Neuer Zustand wird nur dann erzeugt, wenn die Labels �bereinstimmen.
						 * siehe (Switchbedingung Table 1)
						 * Anzahl Zust�nde "s" wird nur erh�ht, wenn es sich um einen <>-State handelt
						 */
						if (label[i].type) s++; //Gr��e der NuStates erh�hen (wegen Switch)
					}
				}
			}
		}
		
		//Spielegraph erzeugen und aus vorhandenen Informationen zusammensetzten 
		Zustand[][] gamegraph = new Zustand[s][q];

		//Schritt 1) Zust�nde aus TreeAutomat f�r jeden NuState generieren
		for (int i=0; i<gamegraph.length; i++) {
			gamegraph[i] = generateTreeGraph(tree);
		}
		
		//Die Zust�nde eindeutig benennen
		for (int i=0; i<gamegraph.length; i++) {
			for (int j=0; j<gamegraph[i].length; j++) {
				gamegraph[i][j].name += " [Zustand("+i+","+j+")]";
			}
		}
		
		//Schritt 2) Zust�nde in Abh�ngigkeit von den Labels untereinander verlinken
		//Die Nustates im Gamegraph untereinander in Verbindung setzten.
		//Dazu einfach die Transitionen der betroffene Zust�nde l�schen und neu verlinken 
		for (int j=0; j<nua.numStates; j++) {
			for (int i=0; i<label.length; i++) {
				NuTransition[] nutransition = new NuTransition[nustate[j].outgoingTransitions().size()];
				nustate[j].outgoingTransitions().toArray(nutransition);

				//Anzahl an Transitionen mit bestimmten Label berechnen (wichtig f�r sp�tere Zuweisung)
				int transcount = 0;
				for (int k=0; k<nutransition.length; k++) {
					if (nutransition[k].action.compareTo(label[i].name) == 0) transcount++;
				}
				//Anzahl "transcount" Transitionen erstellen f�r aktuellen Zustand
				int transno = 0;
				boolean flag = true;
				for (int k=0; k<nutransition.length; k++) {
					if (nutransition[k].action.compareTo(label[i].name) == 0) {
						//Dient zum �berpr�fen, ob Transitionen ver�ndert werden m�ssen
						if (flag) {
							if (label[i].type) {
								gamegraph[j][label[i].index].transition = new Transition[transcount];
								//In den Neuen Zust�nden gibt es jeweils nur eine Transition (normal oder Hypertransition)
								gamegraph[newstatepos][label[i].index].transition = new Transition[1];
							} else {
								gamegraph[j][label[i].index].transition = new Transition[transcount];
							}
							flag = false;
						}
						NuState[] target = new NuState[nutransition[k].targets.size()];
						nutransition[k].targets.toArray(target);
						
						int[] stateindex = getStateIndex(nustate,target);
						
						if (label[i].type) { //Diamond <>
							//Hier spezielle Aktionen, da neue Zust�nde erzeugt werden (switch-Bedingung)
							//Jede Transition f�hrt in einen neuen Zustand der Player 0 geh�rt. Daher
							//gibt es auch nur ein Target! Nun wird der alte Zustand auf den neuen verlinkt
							gamegraph[newstatepos][label[i].index].name = treestate[label[i].index].name + " [Zustand("+newstatepos+","+label[i].index+")]";
							gamegraph[newstatepos][label[i].index].player = false;
							gamegraph[newstatepos][label[i].index].transition[0] = new Transition(stateindex.length);
							for (int l=0; l<stateindex.length; l++) {
								 //Hier auf Nachfolger verweisen(q' statt q)
								gamegraph[newstatepos][label[i].index].transition[0].target[l] = gamegraph[stateindex[l]][label[i].index+1];
							}
							
							gamegraph[j][label[i].index].name = treestate[label[i].index].name + " [Zustand("+j+","+label[i].index+")]" ;
							gamegraph[j][label[i].index].transition[transno] = new Transition(1);
							 //Hier wird nicht auf den nachfolgen Index verlinkt, da dies nur ein Zwischenschritt ist (switch-Bedingung)
							gamegraph[j][label[i].index].transition[transno].target[0] = gamegraph[newstatepos][label[i].index];
							newstatepos++;
						} else { // Square []
							//Hier nur einfach verlinken
							gamegraph[j][label[i].index].name = treestate[label[i].index].name + " [Zustand("+j+","+label[i].index+")]" ;
							gamegraph[j][label[i].index].transition[transno] = new Transition(stateindex.length);
							for (int l=0; l<stateindex.length; l++) {
								//Hier auf Nachfolger verweisen(q' statt q)
								gamegraph[j][label[i].index].transition[transno].target[l] = gamegraph[stateindex[l]][label[i].index+1];
							}
						}
						transno++;
					}
				}
				//Wenn es kein Label gibt, was �bereinstimmt, so handelt es sich um ein Dead-End
				if (flag) gamegraph[j][label[i].index].transition = new Transition[0];
			}
		}
		//Jetzt noch alle Transitionen von den neu hinzugef�gten States l�schen, die nicht vom Typ [] sind! 
		for (int i=nua.numStates; i<newstatepos; i++) {
			for (int j=0; j<label.length; j++) {
				if (!label[j].type) gamegraph[i][label[j].index].transition = new Transition[0];
			}
		}
		return gamegraph;
	}
	
	
	/* 
	 * -----------------------------------------------------------
	 * ------------ Hilfsprozeduren & Hilfsfunktionen ------------
	 * ----------------------------------------------------------- 
	 */
	
	/*
	 * Hilfsfunktion zur Berechnung des NuState-Indexes f�r jedes Target
	 */
	private int[] getStateIndex(NuState[] state, NuState[] target) {
		int[] stateindex = new int [target.length];
		for (int i=0; i<target.length; i++) {
			for (int j=0; j<state.length; j++) {
				if (target[i] == state[j]) {
					stateindex[i] = j;
					break;
				}
			}
		}
		return stateindex;
	}
	
	/*
	 * Hilfsprozedur zur bestimmung der Labels aus der Formel (Alternating-Tree-Automat)
	 */
	private void getLabelsFromFormula(String formel) {
		//Label aus Formel auslesen (nur die Labels in <> spitzen Klammern sind entscheidend 
		//Ausgangsformel zum auslesen der Labels ben�tigt.
		LinkedList<String> labels = new LinkedList<String>();
		int start=0,end=0;
		int v1,v2;
		while (true) {
			v1 = formel.indexOf('<');
			v2 = formel.indexOf('[');
			if ((v1 != -1) & (v2 != -1)) {
				if (v1 < v2) {
					start = v1+1;
					end = formel.indexOf('>');
				}
				else {
					start = v2+1;
					end = formel.indexOf(']');
				}
			}
			else if (v1 != -1) {
				start = v1+1;
				end = formel.indexOf('>');
			}
			else if (v2 != -1) {
				start = v2+1;
				end = formel.indexOf(']');
			}
			else break;
			labels.add(formel.substring(start,end));
			formel = formel.substring(end+1);
		}
		
		label = new LabelStructure[labels.size()];
		int i=0;
		while (!labels.isEmpty()) {
			label[i] = new LabelStructure();
			label[i].name = labels.removeFirst();
			i++;
		}
	}
	
	/*
	 * Gibt die Arrayposition zur�ck in der sich die rechte Expression befindet.
	 * Es wird -1 zur�ckgegeben, wenn der Eintrag nicht existiert.
	 * Notwendig zum korrekten Verlinken der Zust�nde. 
	 */
	private int getArrayIndexForRightElement(String s,String[] s_array) {

		if (s.contains(")|(")) s = s.substring(s.indexOf(")|(")+2);
		else if (s.contains(")&(")) s = s.substring(s.indexOf(")&(")+2);
		else return -1;

		int pos,count = 0;

		String tmp = s;
		while ((pos = tmp.indexOf("(")) != -1) {
			count++;
			tmp = tmp.substring(pos+1);
		}
		
		tmp = s;
		while ((pos = tmp.indexOf(")")) != -1) {
			count--;
			tmp = tmp.substring(pos+1);
		}

		if (count > 0) {
			for (int i=0; i<count; i++) s = s+")";
		}
		if (count < 0) s = s.substring(0,s.length()+count); 

		for (int i=s_array.length-1; i>0; i--) {
			if (s.compareTo(s_array[i]) == 0) return i;
		}
		return -1;
	}
	
	/*
	 * Erstellt ein Array von Zust�nden aus dem Alternating-Tree-Automaten
	 */
	private Zustand[] generateTreeGraph(Expression tree) {
		Zustand[] tmp = new Zustand[countTreeStates(tree)];
		//TreeAutomat in ein Array umwandeln
		TreeToArrayStructure[] treestructure = treeToArray(tree);
		String[] treestate = new String[treestructure.length];
		for (int i=0; i<treestructure.length; i++) {
			treestate[i] = treestructure[i].name;
		}
		
		//Label auslesen und speichern
		getLabelsFromFormula(tree.getExpr());
		labelposition = 0;
		//Bestimme f�r jeden Zustand, welchem Spieler er geh�rt.
		for (int i=0; i<tmp.length; i++) {
			tmp[i] = new Zustand();
			tmp[i].name = treestructure[i].name;
			tmp[i].priority = treestructure[i].priority;
			

			//solange �ffnende Klammern auftreten, erh�he pos
			int pos = 0;
			while (treestate[i].charAt(pos) == '(') {
				pos++;
			}
			switch (treestate[i].charAt(pos)) {
			case '�': //Mu -> Player1
				tmp[i].player = true;
				break;
			case 'v': //Nu -> Player0
				tmp[i].player = false;
				break;
			case '<': //< -> Player1 //Erst die aus den Outgoing Transitions w�hlt Player0
				//�berpr�fen, ob zusammengesetzte Formel existiert (& oder |)
				pos = 0;
				while (treestate[i].charAt(pos) == '(') {
					pos++;
				}
				if (pos > 1) {
					if (treestate[i].contains(")|(")) {
						tmp[i].player = false;
					} else if (treestate[i].contains(")&(")) {
						tmp[i].player = true;
					}
				} else {
					//Hier entsteht der eigentliche �bergang zum n�chsten nu state
					tmp[i].player = true; //Geh�rt auch Spieler 1
					label[labelposition].index = i;
					label[labelposition++].type = true; //Diamond
				}
				break;
			case '[': //[ -> Player1
				//�berpr�fen, ob zusammengesetzte Formel existiert (& oder |)
				pos = 0;
				while (treestate[i].charAt(pos) == '(') {
					pos++;
				}
				if (pos > 1) {
					if (treestate[i].contains(")|(")) {
						tmp[i].player = false;
					} else if (treestate[i].contains(")&(")) {
						tmp[i].player = true;
					}
				} else {				
					tmp[i].player = true;			
					label[labelposition].index = i;
					label[labelposition++].type = false;
				}
				break;
			default:
				if (treestate[i].contains(")|(")) {
					tmp[i].player = false;
				} else if (treestate[i].contains(")&(")) {
					tmp[i].player = true;
				} else if (treestate[i].contains("|")) {
					tmp[i].player = false;
				} else if (treestate[i].contains("&")) {
					tmp[i].player = true;
				} else {
					//Klammern um einzelne Variablen l�schen
					if (treestate[i].charAt(0) == '(') treestate[i] = ""+treestate[i].charAt(1);
					for (int j=0; j<treestate.length; j++) {
						//solange �ffnende Klammern auftreten, erh�he pos
						pos = 0;
						while (treestate[j].charAt(pos) == '(') {
							pos++;
						}
						if (pos == treestate[j].indexOf("�"+treestate[i].charAt(0))) {
							tmp[i].player = true;
							break;
						} else if (pos == treestate[j].indexOf("v"+treestate[i].charAt(0))) {
							tmp[i].player = false;
							break;
						} 
						//Dieser Fall tritt nur dann ein, wenn ungebundene
						//Variablen in der Formel auftauchen.
						//Diese Zust�nde brauchen nicht beachtet zu werden
						//K�nnen zur sp�teren Optimierung auch gel�scht werden!
						else {
							tmp[i].player = false;
							tmp[i].transition = null;
						}
						
					}
				}
				
			}
		}
		Zustand[] zustand = new Zustand[tmp.length];
		for (int i=0; i<zustand.length; i++) {
			zustand[i] = new Zustand();

			//solange �ffnende Klammern auftreten, erh�he pos
			int pos = 0;
			while (treestate[i].charAt(pos) == '(') {
				pos++;
			}
			switch (treestate[i].charAt(pos)) {
			case '�': //Mu -> Player1
				zustand[i] = tmp[i];
				zustand[i].transition = new Transition[1];
				zustand[i].transition[0] = new Transition();
				zustand[i].transition[0].target = new Zustand[1];
				zustand[i].transition[0].target[0] = tmp[i+1];
				break;
			case 'v': //Nu -> Player0
				zustand[i] = tmp[i];
				zustand[i].transition = new Transition[1];
				zustand[i].transition[0] = new Transition();
				zustand[i].transition[0].target = new Zustand[1];
				zustand[i].transition[0].target[0] = tmp[i+1];
				break;
			case '<': //< -> Player1 //Erst die aus den Outgoing Transitions w�hlt Player0
				//�berpr�fen, ob zusammengesetzte Formel existiert (& oder |)
				pos = 0;
				while (treestate[i].charAt(pos) == '(') {
					pos++;
				}
				if (pos > 1) {
					if (treestate[i].contains(")|(")) {
						zustand[i] = tmp[i];
						zustand[i].transition = new Transition[2];
						zustand[i].transition[0] = new Transition();
						zustand[i].transition[1] = new Transition();					
						zustand[i].transition[0].target = new Zustand[1];
						zustand[i].transition[1].target = new Zustand[1];
						//Der linke part vor dem "Oder |" ist immer der der n�chste
						//linke Pfad im Baum.
						zustand[i].transition[0].target[0] = tmp[i+1];
						/* Hier m�ssen wir eine Berechnung des Array-Index anstellen
						 * da man nicht sicher sagen kann, ob der Teil nach dem
						 * "Oder |" der �bern�chste Eintrag im Array ist. */
						int index = getArrayIndexForRightElement(treestate[i],treestate);
						if  (index == -1) zustand[i].transition[1].target[0] = tmp[i+2];
						else zustand[i].transition[1].target[0] = tmp[index];
					} else if (treestate[i].contains(")&(")) {
						zustand[i] = tmp[i];
						zustand[i].transition = new Transition[2];
						zustand[i].transition[0] = new Transition();
						zustand[i].transition[1] = new Transition();					
						zustand[i].transition[0].target = new Zustand[1];
						zustand[i].transition[1].target = new Zustand[1];
						zustand[i].transition[0].target[0] = tmp[i+1];
						int index = getArrayIndexForRightElement(treestate[i],treestate);
						if  (index == -1) zustand[i].transition[1].target[0] = tmp[i+2];
						else zustand[i].transition[1].target[0] = tmp[index];
					}
				} else {
					//Hier entsteht der eigentliche �bergang zum n�chsten nu state
					//Spieler 0 kann erst im n�chsten Zug entscheiden, welche
					//Transition er nimmt
					zustand[i] = tmp[i];
					zustand[i].transition = new Transition[1];
					zustand[i].transition[0] = new Transition();
					zustand[i].transition[0].target = new Zustand[1];
					zustand[i].transition[0].target[0] = tmp[i+1];
				}
				break;
			case '[': //[ -> Player1
				//�berpr�fen, ob zusammengesetzte Formel existiert (& oder |)
				pos = 0;
				while (treestate[i].charAt(pos) == '(') {
					pos++;
				}
				if (pos > 1) {
					if (treestate[i].contains(")|(")) {
						zustand[i] = tmp[i];
						zustand[i].transition = new Transition[2];
						zustand[i].transition[0] = new Transition();
						zustand[i].transition[1] = new Transition();					
						zustand[i].transition[0].target = new Zustand[1];
						zustand[i].transition[1].target = new Zustand[1];
						//Der linke part vor dem "Oder |" ist immer der der n�chste
						//linke Pfad im Baum.
						zustand[i].transition[0].target[0] = tmp[i+1];
						/* Hier m�ssen wir eine Berechnung des Array-Index anstellen
						 * da man nicht sicher sagen kann, ob der Teil nach dem
						 * "Oder |" der �bern�chste Eintrag im Array ist. */
						int index = getArrayIndexForRightElement(treestate[i],treestate);
						if  (index == -1) zustand[i].transition[1].target[0] = tmp[i+2];
						else zustand[i].transition[1].target[0] = tmp[index];//tmp[getArrayIndexForRightElement(treestate[i],treestate)];
					} else if (treestate[i].contains(")&(")) {
						zustand[i] = tmp[i];
						zustand[i].transition = new Transition[2];
						zustand[i].transition[0] = new Transition();
						zustand[i].transition[1] = new Transition();					
						zustand[i].transition[0].target = new Zustand[1];
						zustand[i].transition[1].target = new Zustand[1];
						zustand[i].transition[0].target[0] = tmp[i+1];
						int index = getArrayIndexForRightElement(treestate[i],treestate);
						if  (index == -1) zustand[i].transition[1].target[0] = tmp[i+2];
						else zustand[i].transition[1].target[0] = tmp[index];//tmp[getArrayIndexForRightElement(treestate[i],treestate)];
					}
				} else {				
					zustand[i] = tmp[i];
					zustand[i].transition = new Transition[1];
					zustand[i].transition[0] = new Transition();
					zustand[i].transition[0].target = new Zustand[1];
					zustand[i].transition[0].target[0] = tmp[i+1];
				}
				break;
			default:
				if (treestate[i].contains(")|(")) {
					zustand[i] = tmp[i];
					zustand[i].transition = new Transition[2];
					zustand[i].transition[0] = new Transition();
					zustand[i].transition[1] = new Transition();					
					zustand[i].transition[0].target = new Zustand[1];
					zustand[i].transition[1].target = new Zustand[1];
					//Der linke part vor dem "Oder |" ist immer der der n�chste
					//linke Pfad im Baum.
					zustand[i].transition[0].target[0] = tmp[i+1];
					/* Hier m�ssen wir eine Berechnung des Array-Index anstellen
					 * da man nicht sicher sagen kann, ob der Teil nach dem
					 * "Oder |" der �bern�chste Eintrag im Array ist. */

					//Pr�fen, ob es noch rechte Teilformel gibt
					int index = getArrayIndexForRightElement(treestate[i],treestate);
					if  (index == -1) zustand[i].transition[1].target[0] = tmp[i+2];
					else zustand[i].transition[1].target[0] = tmp[index];//tmp[getArrayIndexForRightElement(treestate[i],treestate)];
				} else if (treestate[i].contains(")&(")) {
					zustand[i] = tmp[i];
					zustand[i].transition = new Transition[2];
					zustand[i].transition[0] = new Transition();
					zustand[i].transition[1] = new Transition();					
					zustand[i].transition[0].target = new Zustand[1];
					zustand[i].transition[1].target = new Zustand[1];
					zustand[i].transition[0].target[0] = tmp[i+1];

					//Pr�fen, ob es noch rechte Teilformel gibt
					int index = getArrayIndexForRightElement(treestate[i],treestate);
					if  (index == -1) zustand[i].transition[1].target[0] = tmp[i+2];
					else zustand[i].transition[1].target[0] = tmp[index];//tmp[getArrayIndexForRightElement(treestate[i],treestate)];
				} else if (treestate[i].contains("|")) {
					zustand[i] = tmp[i];
					zustand[i].transition = new Transition[2];
					zustand[i].transition[0] = new Transition();
					zustand[i].transition[1] = new Transition();					
					zustand[i].transition[0].target = new Zustand[1];
					zustand[i].transition[1].target = new Zustand[1];
					zustand[i].transition[0].target[0] = tmp[i+1];
					//Hier k�nnen wir den Array-Index sicher angeben
					zustand[i].transition[1].target[0] = tmp[i+2];
				} else if (treestate[i].contains("&")) {
					zustand[i] = tmp[i];
					zustand[i].transition = new Transition[2];
					zustand[i].transition[0] = new Transition();
					zustand[i].transition[1] = new Transition();					
					zustand[i].transition[0].target = new Zustand[1];
					zustand[i].transition[1].target = new Zustand[1];
					zustand[i].transition[0].target[0] = tmp[i+1];
					//Hier k�nnen wir den Array-Index sicher angeben
					zustand[i].transition[1].target[0] = tmp[i+2];
				} else {
					//Klammern um einzelne Variablen l�schen
					if (treestate[i].charAt(0) == '(') treestate[i] = ""+treestate[i].charAt(1);
					for (int j=0; j<treestate.length; j++) {
						//solange �ffnende Klammern auftreten, erh�he pos
						pos = 0;
						while (treestate[j].charAt(pos) == '(') {
							pos++;
						}
						if (pos == treestate[j].indexOf("�"+treestate[i].charAt(0))) {
							zustand[i] = tmp[i];
							zustand[i].transition = new Transition[1];
							zustand[i].transition[0] = new Transition();
							zustand[i].transition[0].target = new Zustand[1];
							zustand[i].transition[0].target[0] = tmp[j]; //Auf sein Ursprung verweisen
							break;
						} else if (pos == treestate[j].indexOf("v"+treestate[i].charAt(0))) {
							zustand[i] = tmp[i];
							zustand[i].transition = new Transition[1];
							zustand[i].transition[0] = new Transition();
							zustand[i].transition[0].target = new Zustand[1];
							zustand[i].transition[0].target[0] = tmp[j]; //Auf sein Ursprung verweisen
							break;
						} 
						//Dieser Fall tritt nur dann ein, wenn ungebundene
						//Variablen in der Formel auftauchen.
						//Diese Zust�nde brauchen nicht beachtet zu werden
						//K�nnen zur sp�teren Optimierung auch gel�scht werden!
						else {
							zustand[i] = tmp[i];
							zustand[i].transition = null;
						}
						
					}
				}
				
			}
		}
		//printLabelArray();
		return zustand;
	}
	
	/* 
	 * Berechnet die Anzahl an Zust�nden in einem Alternating-Tree-Automaten
	 */
	private int countTreeStates(Expression tree) {
		return treeToArray(tree).length;	
	}
	
	/*
	 * Hilfsfunktion zum Umwandeln eines Expression-Tree in ein String-Array
	 */
	private TreeToArrayStructure[] treeToArray(Expression tree) {
		expressions.clear(); //gespeicherte Expressions zu jedem Zustand
		treeToArrayHelp(tree);
		TreeToArrayStructure[] struct = new TreeToArrayStructure[expressions.size()];
		expressions.toArray(struct);
		return struct;
	}
	
	/*
	 * Hilfsprozedur f�r treeToArray. Geht den Expression-Baum rekursiv durch und
	 * f�gt neue Knoten der globalen Liste "expressions" hinzu.
	 */
	private void treeToArrayHelp(Expression tree) {
		if (tree == null) return;
		else {
			//S�mtliche Zust�nde aus dem TreeAutomat werden hinzugef�gt,
			//keine Unterscheidung auf Duplikate
			expressions.add(new TreeToArrayStructure(tree.getExpr(),tree.getTetta()));
			treeToArrayHelp(tree.left);
			treeToArrayHelp(tree.right);
			return;
		}
	}

	/*
	 * Hilfsprozedur zum Ausgeben der einzelnen Labels.
	 * Eigentlich nur noch f�r Testzwecke n�tig.
	 */
	private void printLabelArray() {
		for (int i=0; i<label.length; i++) {
			System.out.println("Labelname: "+label[i].name+", Zustand: "+label[i].index+", <>: "+label[i].type);
		}
		
	}
	
	/**
	 * Hilfsprozedur zur Ausgabe eines Zustandarrays (Beispielsweise nach Aufruf von "generateTreeGraph")
	 * 
	 * @param statearray Zustandsarray (Beispielsweise nach Generierung der Zust�nde aus der �-Kalk�l-Formel)
	 */
	public void printStateArray(Zustand[] state) {
		System.out.println("Es gibt "+state.length+" Zust�nde");
		for (int i=0; i<state.length; i++) {
			if (state[i] != null) {
				System.out.println(" Zustand ["+i+"] = "+state[i].name);
				System.out.println("==============");
				if (state[i].player) System.out.println("Zustand geh�rt: Spieler 1");
				else System.out.println("Zustand geh�rt: Spieler 0");
				System.out.println("Zustand hat die Priorit�t: "+state[i].priority);
				
				int hyper,normal;
				hyper = normal = 0;
				if (state[i].transition != null) {
					for (int j=0; j<state[i].transition.length; j++) {
						if (state[i].transition[j].target.length > 1) hyper++;
						else normal++;
					}
					System.out.println("Der Zustand hat "+state[i].transition.length+" Transition(en)");
					System.out.println(" -> Davon " + normal + " Normale (Ein Ziel) Transition(en)");
					System.out.println(" -> Davon " + hyper + " Hypertransition(en) (Mehrere Ziele)");
				} else System.out.println("Der Zustand hat 0 Transitionen (Da keine gebundenen Variablen existieren)!");
				System.out.println();
			}
		}
	}
	
	/**
	 * Gibt den Erzeugten Spiele-Graphen in der Konsole aus.
	 * N�tzlich zum �berpr�fen der einzelnen Transitionen und deren Ziele.
	 * 
	 * @param gamegraph Spiele-Graph der ausgegeben werden soll.
	 */
	public void printGameGraph(Zustand[][] gamegraph) {
		for (int i=0; i<gamegraph.length; i++) {
			System.out.println("State: "+i);
			System.out.println("============================================================================");
			for (int j=0; j<gamegraph[i].length; j++) {
				System.out.println("       [Zustand]: "+gamegraph[i][j].name);
				System.out.print("      [Besitzer]: ");
				if (!gamegraph[i][j].player) System.out.println("Spieler 0 ");
				else System.out.println("Spieler 1 ");
				System.out.println("     [Priorit�t]: "+gamegraph[i][j].priority);
				System.out.println("[Transition(en)]: "+gamegraph[i][j].transition.length);
				
				for (int k=0; k<gamegraph[i][j].transition.length; k++) {
					System.out.print("            |-> [Ziel(e) von Transition "+k+"]: ");
					for (int l=0; l<gamegraph[i][j].transition[k].target.length; l++) {
						System.out.print(gamegraph[i][j].transition[k].target[l].name+", ");
					}
					System.out.println();
				}
				System.out.println("----------------------------------------------------------------------------");
			}
			System.out.println("============================================================================");
			System.out.println();
		}
	}
	
	
	
	/* 
	 * Die Klasse Zustand stellt die Struktur f�r einen Zustand des 
	 * Spielegraphen dar. Die Klasse Transition beinhaltet die Struktur
	 * einer Transition. Ein Zustand kann mehere Transitionen haben,
	 * darunter auch Hypertransitionen. Daher ist target ein Array vom Typ Zustand.
	 * Aus dieser Definition von Zustand und Transition ergibt sich sofort, dass
	 * der Spiele-Graph G aus einem Array vom Typ Zustand der Dimension 2 besteht.
	 */
	public class Zustand {
		int priority;   //Farbe bzw. Priorit�t des aktuellen Zustands
		boolean player; //True = Player 1, False = Player 0
		Transition[] transition;  //Transitionen eines Zustands
		String name; //Name des Zustands (Bsp: Zustand(0,7))
	}		

	public class Transition {
		//Kantenbeschriftung im Spielegraphen nicht mehr n�tig
		//public String label; //Kantenbeschriftung einer Transition
		Zustand source; //Quell-Zustand
		Zustand[] target; //Ziel-Zustand/-Zust�nde(Hypertransitions)

		public Transition() {
						
		}
		
		public Transition(int anzahl) {
			source = new Zustand();
			target = new Zustand[anzahl];
			for (int j=0; j<anzahl; j++) {
				target[j] = new Zustand();
			}
		}
	}

	private class LabelStructure {
		String name;  //Labelname
		int index;    //Gibt den Index im Treestate-Array zu dem entsprechenden Label an
		boolean type; //Gibt den Typ des labels an (false = [] true = <>) 
	}
	
	/* 
	 * Diese Klasse wirde ben�tigt um bei der Generierung der Zust�nde aus der �-Kalk�l-Formel
	 * Sowohl den Namen als auch die Priorit�t des entsprechenden Knotes zu speichern.
	 */ 
	private class TreeToArrayStructure {
		String name;
		int priority;
		TreeToArrayStructure(String name, int priority) {
			this.name = name;
			this.priority = priority;
		}
	}
}

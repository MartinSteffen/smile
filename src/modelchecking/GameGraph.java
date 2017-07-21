package modelchecking;

import nua.NuAutomaton;
import nua.NuState;
import java.util.LinkedList;
import nua.NuTransition;
import java.util.HashSet;

/** 
 * Diese Klasse stellt den Spiele-Graphen für den Parity-Game-Algorithmus zur Verfügung.
 * Die Prozedur zur Berechnung des Spielegraphen aus Nu-Automat und Alternating-Tree-Automat
 * heißt generateGameGraph()
 *  
 * @author skn
 */

public class GameGraph {
	
	private static int labelposition = 0;  //Gibt die aktuelle Position im LabelStructurearray an
	private static LabelStructure[] label; //Beeinhaltet sämtliche Label aus der Formel  
	private static Zustand[] treestate;    //Zustände aus dem Alternating-Tree-Automaten
	private static LinkedList<TreeToArrayStructure> expressions = new LinkedList<TreeToArrayStructure>();
	
	public GameGraph() {
		
	}	
	
	/**
	 * Berechnet die duale Formel zu der übergebenen Formel.
	 * (µ wird zu v, [] zu <> und & zu | und natürlich umgekehrt)
	 * 
	 * @param formel Orignialformel.
	 * @return dual  Die dazugehörige duale Formel.
	 */ 
	
	//Berechnet zu einer gegebenen Formel die duale Formel
	//(wird nachher vom Satisfaction-Check benötigt)
	public String computeDualFormula(String formel) {
		String dual = new String();
		for (int i=0; i<formel.length(); i++) {
			switch (formel.charAt(i)) {
				case 'µ':dual += 'v';break;
				case 'v':dual += 'µ';break;
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
	 * Macht aus dem erstellten Spielegraphen eine Menge von Zuständen für 
	 * die spätere Berechnung beim im ParityGame-Algorithmus
	 * 
	 * @param gamegraph Ein Spiele-Graph vom Typ Zustand als Array der Dimension 2.
	 * @return game_set Der Spiele-Graph als Menge von Zuständen .
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
	 * Alternating Tree-Automaten und gibt ein Array von Zuständen zurück.
	 * Dabei sind die Tupel (s,q) über das zurückgegebnen Zustandsarray
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
		
		/* Neue Anzahl der NuStates berechnen unter Berücksichtigung 
		 * der Switchbedingung aus Table 1 (<>)
		 */
		for (int i=0; i<label.length; i++) {
			for (int j=0; j<nua.numStates; j++) {
				NuTransition[] nutransition = new NuTransition[nustate[j].outgoingTransitions().size()];
				nustate[j].outgoingTransitions().toArray(nutransition);
				for (int k=0; k<nutransition.length; k++) {
					if (nutransition[k].action.compareTo(label[i].name) == 0) {
						/* Neuer Zustand wird nur dann erzeugt, wenn die Labels übereinstimmen.
						 * siehe (Switchbedingung Table 1)
						 * Anzahl Zustände "s" wird nur erhöht, wenn es sich um einen <>-State handelt
						 */
						if (label[i].type) s++; //Größe der NuStates erhöhen (wegen Switch)
					}
				}
			}
		}
		
		//Spielegraph erzeugen und aus vorhandenen Informationen zusammensetzten 
		Zustand[][] gamegraph = new Zustand[s][q];

		//Schritt 1) Zustände aus TreeAutomat für jeden NuState generieren
		for (int i=0; i<gamegraph.length; i++) {
			gamegraph[i] = generateTreeGraph(tree);
		}
		
		//Die Zustände eindeutig benennen
		for (int i=0; i<gamegraph.length; i++) {
			for (int j=0; j<gamegraph[i].length; j++) {
				gamegraph[i][j].name += " [Zustand("+i+","+j+")]";
			}
		}
		
		//Schritt 2) Zustände in Abhängigkeit von den Labels untereinander verlinken
		//Die Nustates im Gamegraph untereinander in Verbindung setzten.
		//Dazu einfach die Transitionen der betroffene Zustände löschen und neu verlinken 
		for (int j=0; j<nua.numStates; j++) {
			for (int i=0; i<label.length; i++) {
				NuTransition[] nutransition = new NuTransition[nustate[j].outgoingTransitions().size()];
				nustate[j].outgoingTransitions().toArray(nutransition);

				//Anzahl an Transitionen mit bestimmten Label berechnen (wichtig für spätere Zuweisung)
				int transcount = 0;
				for (int k=0; k<nutransition.length; k++) {
					if (nutransition[k].action.compareTo(label[i].name) == 0) transcount++;
				}
				//Anzahl "transcount" Transitionen erstellen für aktuellen Zustand
				int transno = 0;
				boolean flag = true;
				for (int k=0; k<nutransition.length; k++) {
					if (nutransition[k].action.compareTo(label[i].name) == 0) {
						//Dient zum überprüfen, ob Transitionen verändert werden müssen
						if (flag) {
							if (label[i].type) {
								gamegraph[j][label[i].index].transition = new Transition[transcount];
								//In den Neuen Zuständen gibt es jeweils nur eine Transition (normal oder Hypertransition)
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
							//Hier spezielle Aktionen, da neue Zustände erzeugt werden (switch-Bedingung)
							//Jede Transition führt in einen neuen Zustand der Player 0 gehört. Daher
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
				//Wenn es kein Label gibt, was übereinstimmt, so handelt es sich um ein Dead-End
				if (flag) gamegraph[j][label[i].index].transition = new Transition[0];
			}
		}
		//Jetzt noch alle Transitionen von den neu hinzugefügten States löschen, die nicht vom Typ [] sind! 
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
	 * Hilfsfunktion zur Berechnung des NuState-Indexes für jedes Target
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
		//Ausgangsformel zum auslesen der Labels benötigt.
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
	 * Gibt die Arrayposition zurück in der sich die rechte Expression befindet.
	 * Es wird -1 zurückgegeben, wenn der Eintrag nicht existiert.
	 * Notwendig zum korrekten Verlinken der Zustände. 
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
	 * Erstellt ein Array von Zuständen aus dem Alternating-Tree-Automaten
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
		//Bestimme für jeden Zustand, welchem Spieler er gehört.
		for (int i=0; i<tmp.length; i++) {
			tmp[i] = new Zustand();
			tmp[i].name = treestructure[i].name;
			tmp[i].priority = treestructure[i].priority;
			

			//solange öffnende Klammern auftreten, erhöhe pos
			int pos = 0;
			while (treestate[i].charAt(pos) == '(') {
				pos++;
			}
			switch (treestate[i].charAt(pos)) {
			case 'µ': //Mu -> Player1
				tmp[i].player = true;
				break;
			case 'v': //Nu -> Player0
				tmp[i].player = false;
				break;
			case '<': //< -> Player1 //Erst die aus den Outgoing Transitions wählt Player0
				//Überprüfen, ob zusammengesetzte Formel existiert (& oder |)
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
					//Hier entsteht der eigentliche Übergang zum nächsten nu state
					tmp[i].player = true; //Gehört auch Spieler 1
					label[labelposition].index = i;
					label[labelposition++].type = true; //Diamond
				}
				break;
			case '[': //[ -> Player1
				//Überprüfen, ob zusammengesetzte Formel existiert (& oder |)
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
					//Klammern um einzelne Variablen löschen
					if (treestate[i].charAt(0) == '(') treestate[i] = ""+treestate[i].charAt(1);
					for (int j=0; j<treestate.length; j++) {
						//solange öffnende Klammern auftreten, erhöhe pos
						pos = 0;
						while (treestate[j].charAt(pos) == '(') {
							pos++;
						}
						if (pos == treestate[j].indexOf("µ"+treestate[i].charAt(0))) {
							tmp[i].player = true;
							break;
						} else if (pos == treestate[j].indexOf("v"+treestate[i].charAt(0))) {
							tmp[i].player = false;
							break;
						} 
						//Dieser Fall tritt nur dann ein, wenn ungebundene
						//Variablen in der Formel auftauchen.
						//Diese Zustände brauchen nicht beachtet zu werden
						//Können zur späteren Optimierung auch gelöscht werden!
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

			//solange öffnende Klammern auftreten, erhöhe pos
			int pos = 0;
			while (treestate[i].charAt(pos) == '(') {
				pos++;
			}
			switch (treestate[i].charAt(pos)) {
			case 'µ': //Mu -> Player1
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
			case '<': //< -> Player1 //Erst die aus den Outgoing Transitions wählt Player0
				//Überprüfen, ob zusammengesetzte Formel existiert (& oder |)
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
						//Der linke part vor dem "Oder |" ist immer der der nächste
						//linke Pfad im Baum.
						zustand[i].transition[0].target[0] = tmp[i+1];
						/* Hier müssen wir eine Berechnung des Array-Index anstellen
						 * da man nicht sicher sagen kann, ob der Teil nach dem
						 * "Oder |" der übernächste Eintrag im Array ist. */
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
					//Hier entsteht der eigentliche Übergang zum nächsten nu state
					//Spieler 0 kann erst im nächsten Zug entscheiden, welche
					//Transition er nimmt
					zustand[i] = tmp[i];
					zustand[i].transition = new Transition[1];
					zustand[i].transition[0] = new Transition();
					zustand[i].transition[0].target = new Zustand[1];
					zustand[i].transition[0].target[0] = tmp[i+1];
				}
				break;
			case '[': //[ -> Player1
				//Überprüfen, ob zusammengesetzte Formel existiert (& oder |)
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
						//Der linke part vor dem "Oder |" ist immer der der nächste
						//linke Pfad im Baum.
						zustand[i].transition[0].target[0] = tmp[i+1];
						/* Hier müssen wir eine Berechnung des Array-Index anstellen
						 * da man nicht sicher sagen kann, ob der Teil nach dem
						 * "Oder |" der übernächste Eintrag im Array ist. */
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
					//Der linke part vor dem "Oder |" ist immer der der nächste
					//linke Pfad im Baum.
					zustand[i].transition[0].target[0] = tmp[i+1];
					/* Hier müssen wir eine Berechnung des Array-Index anstellen
					 * da man nicht sicher sagen kann, ob der Teil nach dem
					 * "Oder |" der übernächste Eintrag im Array ist. */

					//Prüfen, ob es noch rechte Teilformel gibt
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

					//Prüfen, ob es noch rechte Teilformel gibt
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
					//Hier können wir den Array-Index sicher angeben
					zustand[i].transition[1].target[0] = tmp[i+2];
				} else if (treestate[i].contains("&")) {
					zustand[i] = tmp[i];
					zustand[i].transition = new Transition[2];
					zustand[i].transition[0] = new Transition();
					zustand[i].transition[1] = new Transition();					
					zustand[i].transition[0].target = new Zustand[1];
					zustand[i].transition[1].target = new Zustand[1];
					zustand[i].transition[0].target[0] = tmp[i+1];
					//Hier können wir den Array-Index sicher angeben
					zustand[i].transition[1].target[0] = tmp[i+2];
				} else {
					//Klammern um einzelne Variablen löschen
					if (treestate[i].charAt(0) == '(') treestate[i] = ""+treestate[i].charAt(1);
					for (int j=0; j<treestate.length; j++) {
						//solange öffnende Klammern auftreten, erhöhe pos
						pos = 0;
						while (treestate[j].charAt(pos) == '(') {
							pos++;
						}
						if (pos == treestate[j].indexOf("µ"+treestate[i].charAt(0))) {
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
						//Diese Zustände brauchen nicht beachtet zu werden
						//Können zur späteren Optimierung auch gelöscht werden!
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
	 * Berechnet die Anzahl an Zuständen in einem Alternating-Tree-Automaten
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
	 * Hilfsprozedur für treeToArray. Geht den Expression-Baum rekursiv durch und
	 * fügt neue Knoten der globalen Liste "expressions" hinzu.
	 */
	private void treeToArrayHelp(Expression tree) {
		if (tree == null) return;
		else {
			//Sämtliche Zustände aus dem TreeAutomat werden hinzugefügt,
			//keine Unterscheidung auf Duplikate
			expressions.add(new TreeToArrayStructure(tree.getExpr(),tree.getTetta()));
			treeToArrayHelp(tree.left);
			treeToArrayHelp(tree.right);
			return;
		}
	}

	/*
	 * Hilfsprozedur zum Ausgeben der einzelnen Labels.
	 * Eigentlich nur noch für Testzwecke nötig.
	 */
	private void printLabelArray() {
		for (int i=0; i<label.length; i++) {
			System.out.println("Labelname: "+label[i].name+", Zustand: "+label[i].index+", <>: "+label[i].type);
		}
		
	}
	
	/**
	 * Hilfsprozedur zur Ausgabe eines Zustandarrays (Beispielsweise nach Aufruf von "generateTreeGraph")
	 * 
	 * @param statearray Zustandsarray (Beispielsweise nach Generierung der Zustände aus der µ-Kalkül-Formel)
	 */
	public void printStateArray(Zustand[] state) {
		System.out.println("Es gibt "+state.length+" Zustände");
		for (int i=0; i<state.length; i++) {
			if (state[i] != null) {
				System.out.println(" Zustand ["+i+"] = "+state[i].name);
				System.out.println("==============");
				if (state[i].player) System.out.println("Zustand gehört: Spieler 1");
				else System.out.println("Zustand gehört: Spieler 0");
				System.out.println("Zustand hat die Priorität: "+state[i].priority);
				
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
	 * Nützlich zum Überprüfen der einzelnen Transitionen und deren Ziele.
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
				System.out.println("     [Priorität]: "+gamegraph[i][j].priority);
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
	 * Die Klasse Zustand stellt die Struktur für einen Zustand des 
	 * Spielegraphen dar. Die Klasse Transition beinhaltet die Struktur
	 * einer Transition. Ein Zustand kann mehere Transitionen haben,
	 * darunter auch Hypertransitionen. Daher ist target ein Array vom Typ Zustand.
	 * Aus dieser Definition von Zustand und Transition ergibt sich sofort, dass
	 * der Spiele-Graph G aus einem Array vom Typ Zustand der Dimension 2 besteht.
	 */
	public class Zustand {
		int priority;   //Farbe bzw. Priorität des aktuellen Zustands
		boolean player; //True = Player 1, False = Player 0
		Transition[] transition;  //Transitionen eines Zustands
		String name; //Name des Zustands (Bsp: Zustand(0,7))
	}		

	public class Transition {
		//Kantenbeschriftung im Spielegraphen nicht mehr nötig
		//public String label; //Kantenbeschriftung einer Transition
		Zustand source; //Quell-Zustand
		Zustand[] target; //Ziel-Zustand/-Zustände(Hypertransitions)

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
	 * Diese Klasse wirde benötigt um bei der Generierung der Zustände aus der µ-Kalkül-Formel
	 * Sowohl den Namen als auch die Priorität des entsprechenden Knotes zu speichern.
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

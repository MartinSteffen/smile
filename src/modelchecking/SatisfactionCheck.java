package modelchecking;

import modelchecking.GameGraph.*;
import nua.NuAutomaton;
import nua.NuState;

import java.util.HashSet;

/** 
 *  Die Klasse "SatisfactionCheck" dient zur Entscheidung, ob ein gegebener NuAutomat 
 *  eine gegebene µ-Kalkülformel erfüllt! Sie benutzt die beiden Klassen "GameGraph" und "ParityGame"
 *  Die Instanz von Satisfaction-Check ruft dann nur die Methode Check() auf um zu überprüfen ob der 
 *  übergebene Nu-Automat die übergebene Formel erfüllt.
 *
 * @author skn
 */

public class SatisfactionCheck {

	private HashSet<Zustand> G = new HashSet<Zustand>();
	private Zustand[][] gamegraph;
	private NuAutomaton nua;
	private Expression tree;
	
	/** 
	 * Der neuen Instanz von Satisfaction-Check werden
	 * die zu überprüfenden Parameter (Nu-Automat und Alternating-Tree-Automat) übergeben.
	 * @param nua  Ein Nu-Automat
	 * @param tree Ein Alternating-Tree-Automat
	 */
	
	public SatisfactionCheck(NuAutomaton nua, Expression tree) {
		GameGraph g = new GameGraph();
		//Erstellt den GameGraph und macht daraus eine Zustandsmenge vom Typ HashSet
		this.gamegraph = g.generateGameGraph(nua, tree);
		this.G = g.gameGraphToSet(this.gamegraph);
		this.nua = nua;
		this.tree = tree;
	}
	
	/** 
	 * Dient zur Entscheidung, ob der gegebenen Nu-Automat die gegebene µ-Kalkül-Formel erfüllt. 
	 * Check gibt folgende Werte als Ergebnis zurück:
	 * 
	 * @return 1  Falls der Nu-Automat die Formel erfüllt.
	 * @return 0  Falls der Nu-Automat die duale Formel erfüllt.
	 * @return -1 Sonst.
	 */
	
	public int Check() {
		HashSet<Zustand> win = computeWinningRegions();
		//Vergleichen, ob Alle Initialzustände in der Gewinnmenge enthalten sind
		if (CheckHelp(win)) return 1; //Nu-Automat erfüllt die Formel

		//Falls nicht berechne die duale Formel und das Ergebnis hierfür!!
		win = computeWinningRegionsForDualFormula();
		if (CheckHelp(win)) return 0; //Nu-Automat erfüllt die duale Formel
		return -1; //Nichts ist erfüllt
	}
	
	/*
	 * Berechnet die Gewinnmenge für Spieler 0 
	 */
	private HashSet<Zustand> computeWinningRegions() {
		ParityGame pg = new ParityGame();
		//Es interessiert nur der Gewinnbereich von Spieler0
		HashSet<Zustand> win = pg.winningRegions(this.G)[1]; //Hier 1, da Gewinnbereiche von Spieler 0 und 1 noch vertauscht
		return win;
	}
	
	/*
	 * Berechnet die Gewinnregion für die Duale Formel
	 */
	private HashSet<Zustand> computeWinningRegionsForDualFormula() {
		GameGraph g = new GameGraph();
		ParityGame pg = new ParityGame();
		Parser p = new Parser(new ModelCheckingGui(nua));
		String dualformula = p.preparsing(g.computeDualFormula(this.tree.getExpr()));

		Expression expr = p.parse(dualformula);
		expr = p.completeTree(expr);
		p.calculatingTetta(expr);
		p.postparsing(expr);
		
		this.gamegraph = g.generateGameGraph(nua, expr);
		this.G = g.gameGraphToSet(this.gamegraph);
		
		HashSet<Zustand> win = pg.winningRegions(this.G)[1];
		return win;
	}

	/* 
	 * CheckHelp gibt true zurück, wenn die Initialzustände des Gamegraphen
	 * in der Gewinnmenge enthalten sind. Falls nicht wird mit false vorzeitig
	 * abgebrochen, da true nur dann gilt, wenn alle Knoten in der Gewinnmenge
	 * enthalten sind. 
	 */
	private boolean CheckHelp(HashSet<Zustand> win) {
		NuState[] nustate = new NuState[nua.numStates];
		nua.states.toArray(nustate);
		for (int i=0; i<nustate.length; i++) {
			if (nustate[i].isRootState) 
				if (!win.contains(gamegraph[i][0])) return false;
		}
		return true;
	}
	
	/* Der Rootzustand im TreeAutomat ist der Initialzustand daher
	 * reicht es nur diesen zu vergleichen. Die auskommentierte Funktion
	 * geht lediglich einen Schritt weiter und setzt vorraus, das alle Zustände
	 * des TreeAutomaten zu einem bestimmten Nu-Automatenzustand Initialzustände
	 * sind und somit in der Gewinnmenge von Spieler 0 enthalten sein müssen
	 * damit die Formel erfüllt ist. 
	 */
	/*	
	private boolean CheckHelp(HashSet<Zustand> win) {
		NuState[] nustate = new NuState[nua.numStates];
		nua.states.toArray(nustate);
		for (int i=0; i<nustate.length; i++) {
			if (nustate[i].isRootState) {
				for (int j=0; j<gamegraph[i].length; j++) {
					if (!win.contains(gamegraph[i][j])) return false;
				}
			}
		}
		return true;
	}//*/
}

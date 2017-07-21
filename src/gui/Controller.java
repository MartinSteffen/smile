/**
 * 
 */
package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import semantics.NuGenerator;

import modelchecking.ModelCheckingGui;

import csm.CoreStateMachine;
import csm.exceptions.ErrLoadFailed;
import gui.options.Options;
import gui.popups.EventsPanel;
import gui.popups.StartStatePanel;
import gui.popups.VariablesPanel;

/**
 * Der Kern der GUI. Die meisten Klassen kommunizieren über den Controller miteinander. Da er die anderen
 * Klassen kennt und Methoden zur Kommunikation bereitstellt. Die Zentralen Elemente der GUI müssen sich hier
 * anmleden um anderen Zur Verfügung zu stehen. Im WorkPanel ist die Aktive Arbeitsoberfläche (Tab) verzeichnet
 * 
 * @author omi
 * 
 */
public class Controller {

	public Options ops;
	public boolean gridOn = true;
	private SMILETabbedPane tp;
	private WorkPanel wp;
	private MessagePanel mp;
	private ElementList el;
	private SMILEMenuBar mb;
	private SMILEToolBar tb;
	private int action;
	private boolean saveBreak = true;
	
	// Die Aktion die vom ToolBar gesetzt wurde
	public final static int ENTRY = 0;
	public final static int EXIT = 1;
	public final static int CHOICE = 2;
	public final static int FINAL = 3;
	public final static int COMPOSITE = 4;
	public final static int TRANSITION = 5;
	public final static int MARK = 6;
	public final static int MOVERESIZE = 7;

	// der Konstruktor startet die Options 
	public Controller() {
		ops = new Options();
		action = -1;
	}
	
	// Die Methoden zum Anmelden im Controller
	public void setMessagePanel(MessagePanel mp) {
		this.mp = mp;
	}

	public void setWorkPanel(WorkPanel wp) {
		this.wp = wp;
	}

	public void setMenuBar(SMILEMenuBar mb) {
		this.mb = mb;
	}

	public void setToolBar(SMILEToolBar tb) {
		this.tb = tb;
	}

	public void setTabbedPane(SMILETabbedPane tp) {
		this.tp = tp;
	}

	public void setElementList(ElementList el) {
		this.el = el;
	}
	
	// Getter für die Angemeldeten Klassen
	public ElementList getElementList() {
		return el;
	}
	
	public WorkPanel getWorkPanel() {
		return this.wp;
	}
	
	// erzeugt ein neues Tab und eine CSM
	public void newCSM() {
		tp.newTab();
		mb.enableClose(true);
	}

	// Öffnet einen FileChooser, veranlaßt die Erzeugung der CSM aus der Datei und
	// erzeugt einen neuen Workpanel, indem die CSM gezeichnet wird.
	public void loadCSM() {
		try {
			JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new XMLFilter());
			int val = fc.showOpenDialog(null);
			if (val == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				CoreStateMachine csm;
				csm = CoreStateMachine.loadCSM(new FileReader(file));
				newCSM();
				wp.setCSM(csm);
				el.refreshTree(wp.getCSM());
				sendMessage("CSM loaded.", false);
			}
		} catch (FileNotFoundException e) {
			sendMessage("File-Not-Found-Exception: " + e.getMessage(), true);
		} catch (ErrLoadFailed e) {
		    sendMessage("Err-Load-Failed-Exception: " + e.getMessage(), true);
		} catch (IOException e) {
			sendMessage("Input/Output-Exception: " + e.getMessage(), true);
		}
	}

	// Speichert eine CSM unter einem bereits bekannten Dateinamen, ist noch
	// kein Name bekannt, wird saveAsCSM ausgeführt.
	public void saveCSM() {
		if (wp.getFile() != null) {
			try {
				File file = wp.getFile();
				wp.getCSM().saveCSM(new FileWriter(file));
				wp.isChanged = false;
				sendMessage("CSM saved.", false);
			} catch (IOException e) {
				sendMessage("Input/Output-Exception: " + e.getMessage(), true);
			}
		} else {
			saveAsCSM();
		}
	}
	
	// Wählt für eine CSM einen Dateinamen und führt SaveCSM aus.
	public void saveAsCSM() {
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new XMLFilter());
		int val = fc.showSaveDialog(null);
		if (val == JFileChooser.APPROVE_OPTION) {
			wp.setFile(fc.getSelectedFile());
			saveCSM();
		}
		else saveBreak = true;
	}

	// Speichert die Daten aller Tabs.
	public void saveAllCSM() {
		int selection = tp.getSelectedIndex();
		int count = tp.getTabCount();
		for(int i = 0; i < count; i++) {
			tp.changePanel(i);
			saveCSM();
		}
		tp.changePanel(selection);
	}

	// Beendet die GUI. Erst werden alle Tabs geschlossen und nicht gespeicherte Änderungen ggf. gespeichert.
	public void exit() {
		closeAll();
		if(tp.getTabCount() == 0)
			System.exit(0);
	}

	// schließt einen Tab, gibt es nicht gespeicherte Änderungen, wird darauf hingewiesen.
	public void closeTab() {
		
		if (wp.isChanged) {
			int val = JOptionPane.showConfirmDialog(null, "Unsaved Changes! Save now?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (val == JOptionPane.YES_OPTION) {
				saveBreak = false;
				saveCSM();
				if(!saveBreak)
					tp.remove(tp.getSelectedIndex());
			}
			if (val == JOptionPane.NO_OPTION) {
				tp.remove(tp.getSelectedIndex());
			}
		} else {
			tp.remove(tp.getSelectedIndex());
		}
		if(tp.getComponentCount() == 0) {
			mb.enableClose(false);
		}
	}

	// Schließt alle Tabs
	public void closeAll() {
		int count = tp.getTabCount();
		for(int i = 0; i < count; i++) {
			tp.changePanel(i);
			closeTab();
		}
	}
	
	// Setzt die gewählte Aktion der ToolBar
	public void setAction(int action) {
		this.action = action;
	}

	// Gibt die voon der Toolbar eingestellte Aktion zurück
	public int getAction() {
		return action;
	}

	public void undo() {
		// TODO
	}

	public void redo() {
		// TODO
	}

	// ausführen des Löschens auf dem WorkPanel
	public void delete() {
		wp.delete(wp.getSelectedObjects());
	}

	// Öffnet den Dialog der Options
	public void showOptionDialog() {
		ops.showOptionsDialog();
	}

	// Startet das ModelChecking
	public void modelchecking() {
		ModelCheckingGui m = new ModelCheckingGui(NuGenerator.generateNuAutomaton(wp.getCSM()));
		m.setVisible(true);
	}
		
	// Öffnet das Panel für die Events
	public void showEvents() {
		new EventsPanel(wp);
	}
	
	// Öffnet das Panel für die Variablen
	public void showVariables() {
		new VariablesPanel(wp);
	}
	
	// Öffnet das Panel zum setzen des StartState
	public void showSetStartState() {
		new StartStatePanel(wp);
	}
	
	// sendet eine Nachricht an den MessagePanel
	// message - die Nachricht
	// error - true zeigt eine Fehlermeldung an
	public void sendMessage(String message, boolean error) {
		mp.addMessage(message, error);
	}
	
	// ein/aus - Schalten des Grid
	public void changeGrid() {
		if(gridOn) {
			gridOn = false;
			mb.setGridJMI(false);
		} else {
			gridOn = true;
			mb.setGridJMI(true);
		}
		wp.repaint();
	}
	
	// Rückgabe der GridSize
	public int getWPGridSize() {
		return wp.getGridSize();
	}
	
	public void setCSMName(String name) {
		tp.setTitleAt(tp.getSelectedIndex(), name);
	}
	
	
	/*
	 * public void addElement(CSMComponent parent, CSMComponent child){ assert
	 * el != null; el.addNode(parent, child); }
	 */
	
	
}

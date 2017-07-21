package gui;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author omi
 * @version 2.0
 */
public class SMILEMenuBar extends JMenuBar implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1856234209156538299L;

	private Controller pc;

	private JMenu fileJM, editJM, toolsJM, helpJM;

	private JMenuItem newJMI, loadJMI, closeJMI, closeAllJMI, saveJMI, modelJMI, optionsJMI,
			saveAsJMI, saveAllJMI, exitJMI, propertyJMI, helpJMI, infoJMI, undoJMI, redoJMI, deleteJMI,
			eventsJMI, variablesJMI, gridJMI, startStateJMI;

	private final Color bgColor = new Color(241, 240, 239);

	public void setMenuItemEnabled(JMenu menu, boolean state) {
		menu.setEnabled(state);
	}

	public void updateMenuBar() {
	}

	public SMILEMenuBar(Controller pc) {
		this.pc = pc;

		// Create MenuBar.
		setBackground(bgColor);

		// Build JMenus.
		fileJM = new JMenu("File");
		fileJM.setBackground(bgColor);
		fileJM.setMnemonic(KeyEvent.VK_F);
		add(fileJM);
		
		editJM = new JMenu("Edit");
		editJM.setBackground(bgColor);
		editJM.setMnemonic(KeyEvent.VK_E);
		add(editJM);
		
		toolsJM = new JMenu("Tools");
		toolsJM.setBackground(bgColor);
		toolsJM.setMnemonic(KeyEvent.VK_T);
		add(toolsJM);
		
		// Create JMenuItems for File
		newJMI = makeMenuItem("New", "img/new.gif", KeyEvent.VK_N, KeyStroke
				.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK), "new",
				fileJM);
		loadJMI = makeMenuItem("Open...", "img/open.gif", KeyEvent.VK_O,
				KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK),
				"open", fileJM);
		fileJM.addSeparator();
		closeJMI = makeMenuItem("Close", "img/close.gif", KeyEvent.VK_C,
				KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK),
				"close", fileJM);
		closeAllJMI = makeMenuItem("Close All", "img/closeAll.gif",
				KeyEvent.VK_L, "closeAll", fileJM);
		fileJM.addSeparator();
		saveJMI = makeMenuItem("Save", "img/save.gif", KeyEvent.VK_S, KeyStroke
				.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), "save",
				fileJM);
		saveAsJMI = makeMenuItem("Save As...", "img/saveas.gif", KeyEvent.VK_A,
				"saveAs", fileJM);
		saveAllJMI = makeMenuItem("Save All", "img/saveall.gif", KeyEvent.VK_A,
				KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK),
				"saveAll", fileJM);
		fileJM.addSeparator();
		exitJMI = makeMenuItem("Exit", "img/exit.gif", KeyEvent.VK_X, "exit",
				fileJM);

		// Create JMenuItems for Edit
	//	undoJMI = makeMenuItem("Undo", "img/undo.gif", KeyEvent.VK_U, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK), "undo", editJM);
	//	redoJMI = makeMenuItem("Redo", "img/redo.gif", KeyEvent.VK_R, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK), "redo", editJM);
		editJM.addSeparator();
		deleteJMI = makeMenuItem("Delete", "img/delete.gif", KeyEvent.VK_D, KeyStroke.getKeyStroke("DELETE"), "delete", editJM);

		// Create JMenuItems for Tools
		startStateJMI = makeMenuItem("Set Start State...", "img/sss.gif", "sss", toolsJM);
		eventsJMI = makeMenuItem("Events...", "img/events.gif", "events", toolsJM);
		variablesJMI = makeMenuItem("Variables...", "img/variables.gif", "variables", toolsJM);
		toolsJM.addSeparator();
		gridJMI = makeMenuItem("Turn off Grid", "img/grid.gif", "grid", toolsJM);
		toolsJM.addSeparator();
		modelJMI = makeMenuItem("Modelchecking...", "img/mc.gif", "modelchecking", toolsJM);
		toolsJM.addSeparator();
		optionsJMI = makeMenuItem("Options...", "img/ops.gif", "options", toolsJM);
		
		// "Eigenschaft"
		propertyJMI = new JMenuItem("Property", new ImageIcon("img/eigen.gif"));
		propertyJMI
				.setToolTipText("to make some property configuration please click here");
		propertyJMI.setMnemonic(KeyEvent.VK_P);
		propertyJMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				InputEvent.CTRL_MASK));
		propertyJMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

			}
		});
	//	editJM.add(propertyJMI);

		// Bilde drittes Menu "Hilfe" in der Menubar.
		helpJM = new JMenu("Help");
		helpJM.setBackground(bgColor);
		// helpJM.setMnemonic(KeyEvent.VK_H);
		add(helpJM);

		// a group of JMenuItems
		// "Hilfe"
		helpJMI = new JMenuItem("Help", new ImageIcon("img/helpb.gif"));
		helpJMI.setToolTipText("Tips and Tricks for using this program.");
		helpJMI.setMnemonic(KeyEvent.VK_H);
		helpJMI.setAccelerator(KeyStroke.getKeyStroke("F1"));
		helpJMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Help h = new Help();
				h.show();
			}
		});
		helpJM.add(helpJMI);

		// "Info"
		infoJMI = new JMenuItem("Info", new ImageIcon("img/info.gif"));
		//infoJMI.setForeground(Color.YELLOW);
		infoJMI.setToolTipText("Information about us");
		infoJMI.setMnemonic(KeyEvent.VK_I);
		infoJMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				InputEvent.CTRL_MASK));
		infoJMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null, "SMILE Project.\n\n By Students for Harald Fecher.\n Programming in the Many - Praktikum WS 06/07", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpJM.add(infoJMI);
	}

	private JMenuItem makeMenuItem(String name, String image, int mnemonic,
			KeyStroke keyStroke, String actionCommand, JMenu menu) {
		JMenuItem item = new JMenuItem(name, new ImageIcon(image));
		item.setMnemonic(mnemonic);
		item.setAccelerator(keyStroke);
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return item;
	}

	private JMenuItem makeMenuItem(String name, String image, int mnemonic,
			String actionCommand, JMenu menu) {
		JMenuItem item = new JMenuItem(name, new ImageIcon(image));
		item.setMnemonic(mnemonic);
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return item;
	}

	private JMenuItem makeMenuItem(String name, String image,
			String actionCommand, JMenu menu) {
		JMenuItem item = new JMenuItem(name, new ImageIcon(image));
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return item;
	}
	
	public void enableClose(boolean b) {
			closeJMI.setEnabled(b);
			closeAllJMI.setEnabled(b);
	}
	
	public void setGridJMI(boolean stat) {
		if (stat)
			gridJMI.setText("Turn off Grid");
		else
			gridJMI.setText("Turn on Grid");
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("new"))
			pc.newCSM();
		else if (cmd.equals("open"))
			pc.loadCSM();
		else if (cmd.equals("close"))
			pc.closeTab();
		else if (cmd.equals("closeAll"))
			pc.closeAll();
		else if (cmd.equals("save"))
			pc.saveCSM();
		else if (cmd.equals("saveAs"))
			pc.saveAsCSM();
		else if (cmd.equals("saveAll"))
			pc.saveAllCSM();
		else if (cmd.equals("exit"))
			pc.exit();
		else if (cmd.equals("undo"))
			pc.undo();
		else if (cmd.equals("redo"))
			pc.redo();
		else if (cmd.equals("delete"))
			pc.delete();
		else if (cmd.equals("options"))
			pc.showOptionDialog();
		else if (cmd.equals("modelchecking"))
			pc.modelchecking();
		else if (cmd.equals("variables"))
			pc.showVariables();
		else if (cmd.equals("events"))
			pc.showEvents();
		else if (cmd.equals("grid"))
			pc.changeGrid();
		else if (cmd.equals("sss"))
			pc.showSetStartState();
	}
}

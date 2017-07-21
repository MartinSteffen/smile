/**
 * 
 */
package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

import java.io.IOException;
import java.io.InputStream;

import java.io.FileInputStream;

/**
 * Klasse fuer die Erstellung und Anzeige der Hilfe Funktion
 * 
 * @author Sebastian
 *
 */
public class Help extends JPanel implements TreeSelectionListener {

	private static final long serialVersionUID = 1L;
	private JTree tree;
	private JEditorPane htmlPane;
	private String startHTML;
	
	
	// Konstruktor fuer die Hilfe
	public Help() {
		
		super(new GridLayout(1,2));

	    DefaultMutableTreeNode top =
	        new DefaultMutableTreeNode("<html><b>SMILE Help Topics</b></html>");
	    createNodes(top);
	    tree = new JTree(top);

        // Baum erzeugen, der eine Auswahl zur Zeit ermoeglicht
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);

        // Listener fuer Aenderung der Auswahl 
        tree.addTreeSelectionListener(this);

        // ScrollPane erzeugen und Baum hinzufuegen 
        JScrollPane treeView = new JScrollPane(tree);

        // EditorPane fuer HTML-Anzeige erzeugen
        htmlPane = new JEditorPane();
        htmlPane.setContentType("text/html");
        htmlPane.setEditable(false);
        initHelp();
        JScrollPane htmlView = new JScrollPane(htmlPane);

        // ScrollPanes zu SplitPane hinzufuegen 
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(treeView);
        splitPane.setRightComponent(htmlView);
        splitPane.setContinuousLayout(true);

        Dimension minimumSize = new Dimension(400, 200);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(minimumSize.width * 35 / 100);
        
        treeView.setPreferredSize(new Dimension(200, 200)); 

        splitPane.setPreferredSize(new Dimension(600, 400));

        // SplitPane zum Panel hinzufuegen
        add(splitPane);

	}
	
	/**
	 * Hilfethemen erzeugen und zum Baum hinzufuegen
	 * 
	 * @param top - Wurzel des Hilfebaumes 
	 * 
	 */ 	
	private void createNodes(DefaultMutableTreeNode top) {
	    DefaultMutableTreeNode category = null;
	    DefaultMutableTreeNode command = null;
	    
	    
	    // Hilfekategorie "File"
	    category = new DefaultMutableTreeNode("<html><i>File</i></html>");
	    top.add(category);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("New", "./GuiHelpFiles/File/new.html"));
	    category.add(command);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Open...", "./GuiHelpFiles/File/open.html"));
	    category.add(command);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Close", "./GuiHelpFiles/File/close.html"));
	    category.add(command);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Close all", "./GuiHelpFiles/File/close_all.html"));
	    category.add(command);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Save", "./GuiHelpFiles/File/save.html"));
	    category.add(command);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Save As", "./GuiHelpFiles/File/save_as.html"));
	    category.add(command);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Save All", "./GuiHelpFiles/File/save_all.html"));
	    category.add(command);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Exit", "./GuiHelpFiles/File/exit.html"));
	    category.add(command);
	    

	    // Hilfekategorie "Edit"
	    category = new DefaultMutableTreeNode("<html><i>Edit</i></html>");
	    top.add(category);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Undo", "./GuiHelpFiles/Edit/undo.html"));
	    category.add(command);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Redo", "./GuiHelpFiles/Edit/redo.html"));
	    category.add(command);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Delete", "./GuiHelpFiles/Edit/delete.html"));
	    category.add(command);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Property", "./GuiHelpFiles/Edit/property.html"));
	    category.add(command);
	    
	    
	    // Hilfekategorie "Info"
	    category = new DefaultMutableTreeNode("<html><i>Info</i></html>");
	    top.add(category);
	    
//	    command = new DefaultMutableTreeNode(new HelpEntry("Help", "help.html"));
//	    category.add(command);
	    
	    command = new DefaultMutableTreeNode(new HelpEntry("Info", "./GuiHelpFiles/Info/info.html"));
	    category.add(command);
	}
	
	
	// Initialisierung der Hilfe
    private void initHelp() {
    	startHTML = "./GuiHelpFiles/start.html";
        if (startHTML == null)
            System.err.println("Couldn't open help file: " + startHTML);
        
        displayText(startHTML);
    }
	
    
    /**
     * Klasse fuer Eintraege fuer Knoten des Hilfebaumes
     */ 
	private class HelpEntry {
        private String topicName;
        private String filename;

        /**
         * Konstruktor
         * 
         * @param topic - Name des Hilfethemas
         * @param file - Name der HTML-Datei fuer angegebenes Thema
         */
        public HelpEntry(String topic, String file) {
            topicName = topic;
            filename = file;
        }

        public String toString() {
            return topicName;
        }
        
        public String getFilename() {
        	return filename;
        }
	}
	
	
    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           tree.getLastSelectedPathComponent();
        
                
        if (node == null) return;

        Object nodeInfo = node.getUserObject();
        if (node.isLeaf()) {
            HelpEntry topic = (HelpEntry)nodeInfo;
            displayText(topic.getFilename());

        }
        else if(node.isRoot()) 
        	displayText(startHTML);
    }
    
    
    // Anzeigen des Hilfetextes
    private void displayText(String file) {
        try {
        	InputStream input = new FileInputStream(file); 
            if (input != null)
            	htmlPane.read(input, null);
            else {
            	htmlPane.setText("File Not Found");
            	System.out.println("Attempted to display a null URL.");
            }
        } catch (IOException e) {
            System.err.println("Attempted to read a bad URL: " + file);
        }
    }
    
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void show() {
/*        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }*/
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Fenster erstellen und einstellen
        JFrame frame = new JFrame("SMILE Help");

        this.setOpaque(true); //content panes must be opaque
        frame.setContentPane(this);

        // Fenster anzeigen        
		frame.setSize(screenSize.width * 3 / 4, screenSize.height * 3 / 4);
		frame.pack();
		frame.setLocationRelativeTo(null);
		
        frame.setVisible(true);
    }
    


}
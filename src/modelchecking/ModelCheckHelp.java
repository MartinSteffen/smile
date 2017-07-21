package modelchecking;



import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class ModelCheckHelp extends JPanel implements TreeSelectionListener {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTree tree;
	private JEditorPane htmlPane;
	private String startHTLM;
	
	public ModelCheckHelp() {
		
			super(new GridLayout(1,2));

		    DefaultMutableTreeNode top =
		        new DefaultMutableTreeNode("<html><b>Hilfe & Optionen</b></html>");
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
		    category = new DefaultMutableTreeNode("<html><i>Optionen</i></html>");
		    top.add(category);
		    
		    command = new DefaultMutableTreeNode(new HelpEntry("Check", "./mchilfetext/check.html"));
		    category.add(command);
		    
		    command = new DefaultMutableTreeNode(new HelpEntry("Open", "./mchilfetext/open.html"));
		    category.add(command);
		    
		    command = new DefaultMutableTreeNode(new HelpEntry("Save", "./mchilfetext/save.html"));
		    category.add(command);
		    
		    command = new DefaultMutableTreeNode(new HelpEntry("Exit", "./mchilfetext/exit.html"));
		    category.add(command);
		    

		    
		    // Hilfekategorie "Help"
		    category = new DefaultMutableTreeNode("<html><i>Help</i></html>");
		    top.add(category);
		    
		    command = new DefaultMutableTreeNode(new HelpEntry("Help", "./mchilfetext/help.html"));
		    category.add(command);
		    
		    command = new DefaultMutableTreeNode(new HelpEntry("Info", "./mchilfetext/info.html"));
		    category.add(command);
		}
		
		
		// Initialisierung der Hilfe
	    private void initHelp() {
	    	startHTLM = "./mchilfetext/start.html";
	        if (startHTLM == null)
	            System.err.println("Couldn't open help file: " + startHTLM);
	        
	        displayText(startHTLM);
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
	        	displayText(startHTLM);
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

	    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	        // Fenster erstellen und einstellen
	        JFrame frame = new JFrame("Model Checking Help");

	       // this.setOpaque(true); //content panes must be opaque
	        frame.setContentPane(this);

	        // Fenster anzeigen        
			frame.setSize(screenSize.width * 3 / 4, screenSize.height * 3 / 4);
			frame.pack();
			frame.setLocationRelativeTo(null);
			
	        frame.setVisible(true);
	    }
	    



	
}

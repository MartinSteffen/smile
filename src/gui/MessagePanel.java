package gui;

import java.awt.GridLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * <p>Title: MessagePanel</p>
 *
 * <p>Description: Der MessagePanel erzeugt eine Umgebung die HTML interpretiert. Diese Nachrichten stellen 
 * 				   StatusMeldungen für den Benutzer dar. Es sind zwei Typen von Nachrichten vorgesehen:
 * 				   1. Systemmeldungen - werden in schwarz angezeigt.
 * 				   2. Fehlermeldungen - werden in rot angezeigt und ERROR: wird vor die Meldung geschrieben</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Oliver
 * @version 1.0
 */
public class MessagePanel extends JPanel {
   
	private static final long serialVersionUID = -8989881198491656623L;
	
	private JEditorPane area;
	private JScrollPane scroller;
	String message;

	// Der Konstruktor erzeugt den EditorPane zum anzeigen der Nachrichten
	public MessagePanel(Controller pc) {
		pc.setMessagePanel(this);
		this.setLayout(new GridLayout(1,1));
		message = "<p>";
		area = new JEditorPane("text/html", message);
		area.setEditable(false);
		scroller = new JScrollPane(area);
		this.add(scroller);
		}
    
	/**
	 * Bekommt einen String und Erweitert ihn zu HTML-Code
	 * 
	 * @param message die Nachricht die im Panel angezeigt wird
	 * 
	 * @param error true - eine Error Nachricht wird erzeugt
	 * 				false - eine Systemmeldung wird erzeugt
	 */
	public void addMessage(String message, boolean error) {
		if(error) 
			this.message = this.message + "<font color=\"#FF0000\">ERROR: " + message + "</font><br>";
		else
			this.message = this.message + message + "<br>";
		area.setText(this.message);
	}
}

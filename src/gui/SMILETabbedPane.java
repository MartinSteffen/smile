/**
 * 
 */
package gui;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

/**
 * TabbedPane für den WorkPanel.
 * 
 * @author Oliver
 * 
 */
public class SMILETabbedPane extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5733299771249631430L;

	private Controller pc;

	/**
	 * Konstruktor
	 * 
	 * @param Der Kontroller
	 */
	public SMILETabbedPane(Controller pc) {
		super();
		this.pc = pc;
		newTab();
		
		this.addChangeListener(new JTabbedPane.ModelListener() {
			private static final long serialVersionUID = 4681041020934713548L;
			public void stateChanged(ChangeEvent e) {
				changePanel();
			};
		});
	}

	// Erzeugt ein neues Tab mit Workpanel und ScrollPane und setzt einen Standardnamen
	public void newTab() {
		String name = "CSM " + this.getTabCount();
		WorkPanel wp = new WorkPanel(pc, name);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(5000, 5000));
		scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.setViewportView(wp);
		addTab(name, scrollPane);
		setSelectedComponent(getComponentAt(getTabCount() - 1));
		pc.setWorkPanel(wp);
	}

	// wechselt den Panel und setzt den neuen Workpanel im Controller
	public void changePanel() {
		if (this.getComponentCount() > 1) {
			pc.setWorkPanel(((WorkPanel) ((JScrollPane) this
					.getSelectedComponent()).getViewport().getView()));
			pc.getElementList().refreshTree(pc.getWorkPanel().getCSM());
		}
	}
	
	public void changePanel(int count) {
		if(getTabCount()>0) {
			setSelectedIndex(count);
			pc.setWorkPanel(((WorkPanel) ((JScrollPane) this
					.getSelectedComponent()).getViewport().getView()));
		}
			
	}

}

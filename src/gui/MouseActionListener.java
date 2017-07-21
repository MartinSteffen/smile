/**
 * 
 */
package gui;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

/**
 * @author Oliver
 *
 */
public class MouseActionListener extends MouseInputAdapter {

	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			((WorkPanel)e.getSource()).leftClick(e.getPoint());
		else
			((WorkPanel)e.getSource()).rightClick(e.getPoint());
	}
	
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			((WorkPanel)e.getSource()).mousePressed(e.getPoint());
	}
	
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			((WorkPanel)e.getSource()).mouseReleased(e.getPoint());
	}
	
}

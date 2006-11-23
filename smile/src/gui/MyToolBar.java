package gui;

import javax.swing.JToolBar;
import javax.swing.JButton;
import java.net.URL;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MyToolBar extends JToolBar implements ActionListener
{
    private ProgramController pc;

    public MyToolBar(ProgramController pc)
    {
        super("Tools");

        this.pc = pc;
        pc.setToolBar(this);

        // create some tools button
        JButton button = null;

        //first button
        button = makeNavigationButton("Back24", "PREVIOUS",
                                      "Back to previous something-or-other",
                                      "Previous");
        add(button);

    }

    protected JButton makeNavigationButton(String imageName,
                                           String actionCommand,
                                           String toolTipText,
                                           String altText)
    {
        //Look for the image.
        String imgLocation = "images/" + imageName;

        URL imageURL = MyToolBar.class.getResource(imgLocation);

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        //image found
        if (imageURL != null)
        {
            button.setIcon(new ImageIcon(imageURL, altText));
        }
        else //no image found
        {
            button.setText(altText);
            System.err.println("Resource not found: "
                               + imgLocation);
        }
        return button;
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        // Handle each button.

        //first button clicked
        if ("PREVIOUS".equals(cmd))
        {

        }
        else if ("UP".equals(cmd)) // second button clicked
        {

        }
        else if ("NEXT".equals(cmd)) // third button clicked
        {

        }
    }
}

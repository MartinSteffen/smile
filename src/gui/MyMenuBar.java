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
public class MyMenuBar extends JMenuBar
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1856234209156538299L;
	private ProgramController pc;
    private JMenu fileJM, editJM, helpJM;
    private JMenuItem newJMI, exitJMI, propertyJMI, helpJMI, infoJMI;
    private final Color bgColor = new Color(241, 240, 239);
    public void setMenuItemEnabled(JMenu menu, boolean state)
    {
        menu.setEnabled(state);
    }
    public void updateMenuBar()
    {
    }
    public MyMenuBar(ProgramController pc)
    {
        this.pc = pc;
        pc.setMenuBar(this);
    }
    public void initMyMenuBar()
    {
        this.pc = pc;
        pc.setMenuBar(this);

        //Create the menu bar.
        setBackground(bgColor);
        //Build the first menu.
        fileJM = new JMenu("File");
        fileJM.setBackground(bgColor);
        //fileJM.setMnemonic(KeyEvent.VK_F);
        add(fileJM);

        //a group of JMenuItems
        // "Neu"  mit index 0
        newJMI = new JMenuItem("Neu...", new ImageIcon("img/new.gif"));
        newJMI.setToolTipText("to start please click here");
        newJMI.setMnemonic(KeyEvent.VK_N);
        newJMI.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        newJMI.addActionListener(
                new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                pc.jProc(Messages.NEW);
            }
        });
        fileJM.add(newJMI);

        fileJM.addSeparator();

        // "Beenden" mit index 1
        exitJMI = new JMenuItem("Exit" , new ImageIcon("img/exitb.gif"));
        exitJMI.setToolTipText("to end the program please click here");
        exitJMI.addActionListener(
                new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0);
            }
        });
        fileJM.add(exitJMI);

        //Bilde zweites Menu "Bearbeiten" in der Menubar.
        editJM = new JMenu("Edit");
        editJM.setBackground(bgColor);
       // editJM.setMnemonic(KeyEvent.VK_E);
        add(editJM);

        // "Eigenschaft"
        propertyJMI = new JMenuItem("Property", new ImageIcon("img/eigen.gif"));
        propertyJMI.setToolTipText("to make some property configuration please click here");
        propertyJMI.setMnemonic(KeyEvent.VK_P);
        propertyJMI.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        propertyJMI.addActionListener(
                new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                pc.jProc(Messages.PROPERTY);
            }
        });
        editJM.add(propertyJMI);

        //Bilde drittes Menu "Hilfe" in der Menubar.
        helpJM = new JMenu("Help");
        helpJM.setBackground(bgColor);
        //helpJM.setMnemonic(KeyEvent.VK_H);
        add(helpJM);

        //a group of JMenuItems
        // "Hilfe"
        helpJMI = new JMenuItem("Help", new ImageIcon("img/helpb.gif"));
        helpJMI.setToolTipText("Tips and Tricks for using this program.");
        helpJMI.setMnemonic(KeyEvent.VK_H);
        helpJMI.setAccelerator(
                KeyStroke.getKeyStroke("F1"));
        helpJMI.addActionListener(
                new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {

            }
        });
        helpJM.add(helpJMI);

        // "Info"
        infoJMI = new JMenuItem("Info", new ImageIcon("img/info.gif"));
        infoJMI.setForeground(Color.YELLOW);
        infoJMI.setToolTipText("Information about us");
        infoJMI.setMnemonic(KeyEvent.VK_I);
        infoJMI.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
        infoJMI.addActionListener(
                new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                AboutBox aboutBox = new AboutBox(null);
                aboutBox.pack();
                aboutBox.setLocationRelativeTo(null);
                aboutBox.setVisible(true);
            }
        });
        helpJM.add(infoJMI);
    }
}

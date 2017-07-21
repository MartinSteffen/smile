package gui;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JSplitPane;
import javax.swing.JPanel;

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
public class Gui extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1279008285203177345L;
	private ProgramController pc = new ProgramController();
    public Gui()
    {
        // Do frame stuff.
        super("Project Name");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // init all dialog
        // initAllDialog(this);

        //create menu
        MyMenuBar menubar = new MyMenuBar(pc);
        menubar.initMyMenuBar();
        setJMenuBar(menubar);
        menubar.setVisible(true);

        //create toolbar
        MyToolBar myToolBar = new MyToolBar(pc);

        //create ElementList
        ElementList el = new ElementList();
        pc.setElementList(el);

        //create workpanel
        WorkPanel wp = new WorkPanel();
        pc.setWorkPanel(wp);

        //layout ElementList and workpanel together in a splitpane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           el, wp);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(100, 50);
        el.setMinimumSize(minimumSize);
        wp.setMinimumSize(minimumSize);

        //create messagepanel
        MessagesPanel mp = new MessagesPanel();
        pc.setMessagePanel(mp);

        //layout them together in a splitpane
        JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                           splitPane, mp);
        jsp.setOneTouchExpandable(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jsp.setDividerLocation(screenSize.height*7/10);

        //set up main panel
        JPanel mainJP = new JPanel(new BorderLayout());

        //Lay out the main panel.
        mainJP.add(myToolBar, BorderLayout.PAGE_START);
        mainJP.add(jsp, BorderLayout.CENTER);

        setContentPane(mainJP);

        super.setSize(screenSize.width*3/4,screenSize.height*3/4);
        super.setLocationRelativeTo(null);

    }
    public static void main(String [] args)
    {
        Gui gui = new Gui();
        gui.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gui.setVisible(true);
    }
}

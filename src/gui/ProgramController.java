package gui;

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
public class ProgramController
{
    private MyMenuBar myMenuBar;
    private MyToolBar myToolBar;
    private ElementList el;
    private WorkPanel wp;
    private MessagesPanel mp;
    public ProgramController()
    {
    }

    /**
     * setMenuBar
     *
     * @param myMenuBar MyMenuBar
     */
    public void setMenuBar(MyMenuBar myMenuBar)
    {
        this.myMenuBar = myMenuBar;
    }

    /**
     * jProc
     *
     * @param message int
     */
    public void jProc(int message)
    {
        switch(message)
        {
        case Messages.NEW:
            break;
        }
    }

    /**
     * setWorkPanel
     *
     * @param wp WorkPanel
     */
    public void setWorkPanel(WorkPanel wp)
    {
        this.wp = wp;
    }

    /**
     * setMessagePanel
     *
     * @param mp MessagesPanel
     */
    public void setMessagePanel(MessagesPanel mp)
    {
        this.mp = mp;
    }

    /**
     * setElementList
     *
     * @param el ElementList
     */
    public void setElementList(ElementList el)
    {
        this.el = el;
    }

    /**
     * setToolBar
     *
     * @param myToolBar MyToolBar
     */
    public void setToolBar(MyToolBar myToolBar)
    {
        this.myToolBar = myToolBar;
    }
}

package ugent.bagger.views;

import javax.swing.JComponent;
import ugent.bagger.panels.RenamePanel;

/**
 *
 * @author nicolas
 */
public class RenameView extends DefaultView{
    private RenamePanel renamePanel;

    public RenamePanel getRenamePanel() {
        if(renamePanel == null){
            renamePanel = new RenamePanel();
        }
        return renamePanel;
    }
    public void setRenamePanel(RenamePanel renamePanel) {
        this.renamePanel = renamePanel;
    }   
    @Override
    protected JComponent createControl() {                
        return getRenamePanel();       
    }   
}
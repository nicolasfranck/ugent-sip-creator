package ugent.bagger.views;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import ugent.bagger.panels.RenamePanel;

/**
 *
 * @author nicolas
 */
public class RenameView extends DefaultView{
    RenamePanel renamePanel;

    public RenamePanel getRenamePanel() {
        if(renamePanel == null){
            renamePanel = new RenamePanel();
            renamePanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10)); 
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
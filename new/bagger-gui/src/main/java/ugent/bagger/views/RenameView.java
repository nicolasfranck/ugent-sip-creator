/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.views;

import javax.swing.JComponent;
import ugent.bagger.panels.AdvancedRenamePanel;


/**
 *
 * @author nicolas
 */
public class RenameView extends DefaultView{
    private AdvancedRenamePanel renamePanel;

    public AdvancedRenamePanel getRenamePanel() {
        if(renamePanel == null){
            renamePanel = new AdvancedRenamePanel();
        }
        return renamePanel;
    }
    public void setRenamePanel(AdvancedRenamePanel renamePanel) {
        this.renamePanel = renamePanel;
    }   
    @Override
    protected JComponent createControl() {                
        return getRenamePanel();       
    }
}
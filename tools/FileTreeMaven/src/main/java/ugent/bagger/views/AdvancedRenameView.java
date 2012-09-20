/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.views;

import ugent.bagger.panels.AdvancedRenamePanel;
import javax.swing.*;
import org.springframework.richclient.application.PageComponentContext;
import treetable.executors.ExpandCollapseTreeExecutor;
import treetable.executors.ReloadTreeExecutor;

/**
 *
 * @author nicolas
 */
public class AdvancedRenameView extends DefaultView{
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
    @Override
    protected void registerLocalCommandExecutors(PageComponentContext context){       
        ExpandCollapseTreeExecutor expand = new ExpandCollapseTreeExecutor(this);
        ReloadTreeExecutor reload = new ReloadTreeExecutor(this);
        context.register("expandCollapseTreeCommand",expand);
        context.register("reloadTreeCommand",reload);
        expand.setEnabled(true);
        reload.setEnabled(true);
    }
}
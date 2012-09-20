/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package treetable.executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.views.AdvancedRenameView;

/**
 *
 * @author nicolas
 */
public class ReloadTreeExecutor extends AbstractActionCommandExecutor{
    private AdvancedRenameView renameView;
    private static final Log log = LogFactory.getLog(ReloadTreeExecutor.class);
    public ReloadTreeExecutor(AdvancedRenameView renameView){
        setRenameView(renameView);         
    }
    @Override
    public void execute(){
        System.out.println("reloading..");
        getRenameView().getRenamePanel().reloadTreeTable(getRenameView().getRenamePanel().getLastFile());        
    }         
    private AdvancedRenameView getRenameView() {
        return renameView;
    }
    private void setRenameView(AdvancedRenameView renameView) {
        this.renameView = renameView;
    }    
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package treetable.executors;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import simple.views.RenameView;
import treetable.FileNode;

/**
 *
 * @author nicolas
 */
public class ReloadTreeExecutor extends AbstractActionCommandExecutor{
    private RenameView view;
    private static final Log log = LogFactory.getLog(ReloadTreeExecutor.class);
    public ReloadTreeExecutor(RenameView view){
        this.view = view;        
    }
    @Override
    public void execute(){
        view.reloadTreeTable(view.getLastFile());
    }          
}

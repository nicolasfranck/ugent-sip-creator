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
import simple.views.FileTreeView1;
import treetable.FileNode;

/**
 *
 * @author nicolas
 */
public class ReloadTreeExecutor extends AbstractActionCommandExecutor{
    private FileTreeView1 view;   
    private static final Log log = LogFactory.getLog(ReloadTreeExecutor.class);
    public ReloadTreeExecutor(FileTreeView1 view){
        this.view = view;        
    }
    @Override
    public void execute(){
        view.reloadTreeTable(view.getLastFile());
    }          
}

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
public class ExpandCollapseTreeExecutor extends AbstractActionCommandExecutor{
    private FileTreeView1 view;   
    private static final Log log = LogFactory.getLog(ExpandCollapseTreeExecutor.class);

    public ExpandCollapseTreeExecutor(FileTreeView1 view){
        this.view = view;        
    }
    @Override
    public void execute(){        
        JTree tree = view.getCurrentTreeTable().getTree();
        FileNode root = (FileNode)tree.getModel().getRoot();
        TreePath path = new TreePath(root);
        expandAll(tree.isCollapsed(path));
    }
    public void expandAll(boolean isCollapsed) {
        FileNode root = (FileNode)view.getCurrentTreeTable().getTree().getModel().getRoot();
        // Traverse tree from root
        expandAll(new TreePath(root),isCollapsed);
    }
    private void expandAll(TreePath parent,boolean isCollapsed) {
        // Traverse children
        FileNode node = (FileNode) parent.getLastPathComponent();
        FileNode [] children = (FileNode []) node.getChildren();

        for(FileNode child:children){
            TreePath path = parent.pathByAddingChild(child);
            expandAll(path,isCollapsed);
        }
        // Expansion or collapse must be done bottom-up
        JTree tree = view.getCurrentTreeTable().getTree();
        if(isCollapsed) {
            tree.expandPath(parent);
        }else{
            tree.collapsePath(parent);
        }        
    }    
}

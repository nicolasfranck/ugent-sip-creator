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
import ugent.bagger.views.RenameView;
import treetable.FileNode;

/**
 *
 * @author nicolas
 */
/*
* source: http://www.exampledepot.com/egs/javax.swing.tree/ExpandAll.html
*/
// If expand is true, expands all nodes in the tree.
// Otherwise, collapses all nodes in the tree.
    
public class ExpandCollapseTreeExecutor extends AbstractActionCommandExecutor{
    private RenameView renameView;
    private static final Log log = LogFactory.getLog(ExpandCollapseTreeExecutor.class);

    public ExpandCollapseTreeExecutor(RenameView renameView){
        setRenameView(renameView);        
    }
    private RenameView getRenameView() {
        return renameView;
    }
    private void setRenameView(RenameView renameView) {
        this.renameView = renameView;
    }    
    @Override
    public void execute(){        
        System.out.println("expanding..");
        JTree tree = getRenameView().getRenamePanel().getCurrentTreeTable().getTree();                
        FileNode root = (FileNode)tree.getModel().getRoot();
        TreePath path = new TreePath(root);
        expandAll(tree.isCollapsed(path));
    }
    public void expandAll(boolean isCollapsed) {
        FileNode root = (FileNode)getRenameView().getRenamePanel().getCurrentTreeTable().getTree().getModel().getRoot();
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
        JTree tree = getRenameView().getRenamePanel().getCurrentTreeTable().getTree();
        if(isCollapsed) {
            tree.expandPath(parent);
        }else{
            tree.collapsePath(parent);
        }        
    }    
}

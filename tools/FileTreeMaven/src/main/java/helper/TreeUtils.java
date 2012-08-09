/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author nicolas
 */
public class TreeUtils {
    public static DefaultMutableTreeNode findSimularNode(JTree tree,DefaultMutableTreeNode node){
        DefaultMutableTreeNode foundNode = null;
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
        for(int j = 0;j < rootNode.getChildCount();j++){
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) rootNode.getChildAt(j);           
            if(
                child.getUserObject().toString().compareTo(node.getUserObject().toString()) == 0
            ){
                foundNode = child;
                break;
            }
        }
        return foundNode;
    }
}

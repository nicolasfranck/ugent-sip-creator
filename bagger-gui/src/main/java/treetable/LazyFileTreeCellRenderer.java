package treetable;

import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;


/**
 *
 * @author nicolas
 */
public class LazyFileTreeCellRenderer extends DefaultTreeCellRenderer {
    private Icon directoryIcon = (Icon) UIManager.getIcon("FileView.directoryIcon");
    private Icon fileIcon = (Icon) UIManager.getIcon("FileView.fileIcon");


    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus
    ){
       
        JLabel renderer = (JLabel)super.getTreeCellRendererComponent(
                tree, value, sel, expanded, leaf, row, hasFocus);
        
        
        System.out.println("tree: "+tree);
        System.out.println("value: "+value);
        System.out.println("sel: "+sel);
        System.out.println("expanded: "+expanded);
        System.out.println("leaf: "+leaf);
        System.out.println("row: "+row);
        System.out.println("hasFocus: "+hasFocus);
        
        TreePath tpath = tree.getPathForRow(row);  
        
        if(tpath == null){
            return null;
        }
        
        System.out.println("path component: "+tpath.getLastPathComponent());
        LazyTreeNode node = (LazyTreeNode) tpath.getLastPathComponent();
        
        System.out.println("node: "+node);
        System.out.println("node user object: "+node.getUserObject());
        FileNode fnode = (FileNode)node.getUserObject();
        System.out.println("fnode: "+fnode);
        File currentFile = fnode.getFile();
        System.out.println("currentFile: "+currentFile);

        if (currentFile.isDirectory()){
            renderer.setIcon(directoryIcon);
        }
        else{          
            renderer.setIcon(fileIcon);
        }
        return renderer;
    }
}

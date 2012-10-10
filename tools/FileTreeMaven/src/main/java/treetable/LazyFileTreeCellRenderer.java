package treetable;

import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;


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
        
        System.out.println("value: "+value);
        LazyTreeNode node = (LazyTreeNode)value;
        System.out.println("node: "+node);
        System.out.println("node user object: "+node.getUserObject());
        FileNode fnode = (FileNode)node.getUserObject();
        File currentFile = fnode.getFile();

        if (currentFile.isDirectory()){
            renderer.setIcon(directoryIcon);
        }
        else{          
            renderer.setIcon(fileIcon);
        }
        return renderer;
    }
}

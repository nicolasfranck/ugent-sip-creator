package ugent.bagger.helper;

import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author nicolas
 */
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {
    Icon directoryIcon = (Icon) UIManager.getIcon("FileView.directoryIcon");
    Icon fileIcon = (Icon) UIManager.getIcon("FileView.fileIcon");

    @Override
    public Component getTreeCellRendererComponent(
        JTree tree,Object value, boolean sel,boolean expanded, boolean leaf,int row, boolean hasFocus
    ){
       
        JLabel renderer = (JLabel)super.getTreeCellRendererComponent(
            tree, value, sel, expanded, leaf, row, hasFocus
        );
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        File currentFile = (File)node.getUserObject();

        renderer.setIcon(currentFile.isDirectory() ? directoryIcon:fileIcon);
        
        return renderer;
    }
}

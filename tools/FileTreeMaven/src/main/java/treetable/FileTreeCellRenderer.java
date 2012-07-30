/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {
    private Icon directoryIcon = (Icon) UIManager.getIcon("FileView.directoryIcon");
    private Icon fileIcon = (Icon) UIManager.getIcon("FileView.fileIcon");
    private Icon errorIcon = (Icon) UIManager.getIcon("fileView.errorIcon");


    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus
    ){

        JLabel renderer = (JLabel)super.getTreeCellRendererComponent(
                tree, value, sel, expanded, leaf, row, hasFocus);

        FileNode node = (FileNode)value;
        File currentFile = (File)node.getFile();
      
        if (currentFile.isDirectory()){
            renderer.setIcon(directoryIcon);
        }else{      
            renderer.setIcon(fileIcon);
        }
        return renderer;
    }
}
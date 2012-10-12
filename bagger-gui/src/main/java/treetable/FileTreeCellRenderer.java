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
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.apache.log4j.Logger;

/**
 *
 * @author nicolas
 */
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {
    
    private static Icon directoryIcon = UIManager.getIcon("FileView.directoryIcon");
    private static Icon fileIcon = UIManager.getIcon("FileView.fileIcon");
    private static Icon errorIcon = UIManager.getIcon("FileView.errorIcon");
    private static Icon computerIcon = UIManager.getIcon( "FileView.computerIcon" );    
    private static Icon diskIcon = UIManager.getIcon( "FileView.hardDriveIcon" );

    private static Logger logger = Logger.getLogger(FileTreeCellRenderer.class);
    private static FileSystemView fsv = FileSystemView.getFileSystemView();

    @Override
    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value, 
        boolean sel, 
        boolean expanded, 
        boolean leaf,
        int row, 
        boolean hasFocus
    ){

        JLabel renderer = (JLabel)super.getTreeCellRendererComponent(
            tree, value, sel, expanded, leaf, row, hasFocus
        );

        FileNode node = (FileNode)value;
        File currentFile = (File)node.getFile();

        logger.debug("getTreeCellRendererComponent: currentFile: "+currentFile);

        if(fsv.isFileSystemRoot(currentFile)){
            logger.debug("getTreeCellRendererComponent: isFileSystemRoot");
            renderer.setIcon(diskIcon);
        }else if (currentFile.isDirectory()){
            logger.debug("getTreeCellRendererComponent: isDirectory");
            renderer.setIcon(directoryIcon);
        }else{
            logger.debug("getTreeCellRendererComponent: isNotADirectory");
            renderer.setIcon(fileIcon);
        }
        return renderer;
    }
}
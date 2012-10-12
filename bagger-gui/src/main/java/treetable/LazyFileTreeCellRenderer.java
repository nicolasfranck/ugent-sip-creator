package treetable;

import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 *
 * @author nicolas
 */
public class LazyFileTreeCellRenderer extends DefaultTreeCellRenderer {
    private static Icon directoryIcon = (Icon) UIManager.getIcon("FileView.directoryIcon");
    private static Icon fileIcon = (Icon) UIManager.getIcon("FileView.fileIcon");
    private static Icon errorIcon = UIManager.getIcon("FileView.errorIcon");
    private static Icon computerIcon = UIManager.getIcon( "FileView.computerIcon" );    
    private static Icon diskIcon = UIManager.getIcon( "FileView.hardDriveIcon" );
    private static FileSystemView fsv = FileSystemView.getFileSystemView();

    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus
    ){
       
        JLabel renderer = (JLabel)super.getTreeCellRendererComponent(
            tree, value, sel, expanded, leaf, row, hasFocus
        );        
        
        TreePath tpath = tree.getPathForRow(row);  
        
        if(tpath == null){     
            //enkel directories leveren een loading icon op => zet op voorhand op directoryIcon
            //errorIcon levert ".." op totdat je op de directory klikt..
            renderer.setIcon(directoryIcon);
            return renderer;
        }
        
        
        LazyTreeNode node = (LazyTreeNode) tpath.getLastPathComponent();                
        System.err.println("node.userObject: "+node.getUserObject());
        
        if(!(node.getUserObject() instanceof FileNode)){
            System.err.println("node.userObject is not an instance of FileNode");
            renderer.setIcon(directoryIcon);
            return renderer;
        }
        
        FileNode fnode = (FileNode)node.getUserObject();        
        File currentFile = fnode.getFile();

        if(fsv.isFileSystemRoot(currentFile)){
            System.err.println(currentFile+" is filesystem root");
            renderer.setIcon(diskIcon);
        }else if (currentFile.isDirectory()){
            System.err.println(currentFile+" is directory");
            renderer.setIcon(directoryIcon);
        }else{          
            System.err.println(currentFile+" is file");
            renderer.setIcon(fileIcon);
        }
        return renderer;
    }
}

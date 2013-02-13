package treetable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

/**
 *
 * @author nicolas
 */
public class FileLazyTreeModel extends LazyTreeModel{
    Mode mode = Mode.FILES_AND_DIRECTORIES;
    static Comparator defaultFileSorter =  new Comparator<File>(){
        @Override
        public int compare(File f1, File f2){
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    };
    public FileLazyTreeModel(TreeNode root, JTree tree){
        this(root,tree,Mode.FILES_AND_DIRECTORIES);
    }
    public FileLazyTreeModel(TreeNode root, JTree tree,Mode mode){
        super(root,tree);
        this.mode = mode;
    }
    @Override
    public LazyTreeNode[] loadChildren(LazyTreeNode parentNode) {
        FileNode fileNode = (FileNode)parentNode.getUserObject();
        File selectedFile = fileNode.getFile();        
        
        ArrayList<File>files = new ArrayList<File>();
        ArrayList<File>directories = new ArrayList<File>();

        File [] list = selectedFile.listFiles();
        if(list != null){
            for(File sb:list){
                if(sb.isDirectory()) {
                    if(mode == Mode.DIRECTORIES_ONLY || mode == Mode.FILES_AND_DIRECTORIES){
                        directories.add(sb);
                    }                
                }else{
                    if(mode == Mode.FILES_ONLY || mode == Mode.FILES_AND_DIRECTORIES){
                        files.add(sb);
                    }                
                }         
            }
        }
        Collections.sort(directories,defaultFileSorter);        
        Collections.sort(files,defaultFileSorter);              
        ArrayList<LazyTreeNode> children = new ArrayList<LazyTreeNode>();           
        
        for(File dir:directories){            
            children.add(new LazyTreeNode(dir.getAbsolutePath(),new FileNode(dir),true));            
        }               
        for(File file:files){
            children.add(new LazyTreeNode(file.getAbsolutePath(),new FileNode(file),false));            
        }
        return children.toArray(new LazyTreeNode [] {});
    }
    public enum Mode {
        FILES_ONLY,DIRECTORIES_ONLY,FILES_AND_DIRECTORIES
    }
}        

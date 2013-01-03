package treetable;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

/**
 *
 * @author nicolas
 */
public class LazyTree {
    public LazyTree() {
	JFrame frame = new JFrame("TreeTable");
        
        //root is geen echte file, eerder een plaatsvervanger die niet mag getoond worden
        //hieronder komen de verschillende roots van het bestandssysteem
        File rootFile = new File("");                
        
        final JTree tree = new JTree();                       
        
        LazyTreeNode rootNode = new LazyTreeNode("",new FileNode(rootFile),true);
        
        //sommige systemen hebben meerdere roots (C:/, D:/)
        for(File file:File.listRoots()){            
            FileNode fnode = new FileNode(file);
            LazyTreeNode node = new LazyTreeNode(
                file.getAbsolutePath(),
                fnode,
                file.isDirectory()
            );            
            rootNode.add(node);
        }        
        
        tree.setModel(new FileLazyTreeModel(rootNode,tree));        
        tree.setRootVisible(false);
        tree.setShowsRootHandles(false);
        tree.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(tree.isCollapsed(tree.getRowForLocation(e.getX(),e.getY()))) {
                    tree.expandRow(tree.getRowForLocation(e.getX(),e.getY()));
                }
                else{
                    tree.collapseRow(tree.getRowForLocation(e.getX(),e.getY()));
                }} 
        });
        tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent tee) throws ExpandVetoException {
                TreePath tpath = tee.getPath();
                LazyTreeNode node = (LazyTreeNode) tpath.getLastPathComponent();
                FileNode fileNode = (FileNode) node.getUserObject();
                File file = fileNode.getFile();                
                if(file.isDirectory()){                    
                    if(!file.canRead()){
                        JOptionPane.showMessageDialog(tree,""+file+" is not readable!",null,JOptionPane.ERROR_MESSAGE);
                        throw new ExpandVetoException(tee);
                    }else if(!file.canWrite()){
                        JOptionPane.showMessageDialog(tree,""+file+" is not writable! Rename form is disabled",null,JOptionPane.ERROR_MESSAGE);
                    }
                }                
            }
            @Override
            public void treeWillCollapse(TreeExpansionEvent tee) throws ExpandVetoException {             
            }
        });
        
       
        
	frame.addWindowListener(new WindowAdapter() {
            @Override
	    public void windowClosing(WindowEvent we) {
		System.exit(0);
	    }
	});
        
        tree.setPreferredSize(new Dimension(1024,1024));
	frame.getContentPane().add(new JScrollPane(tree));        
	frame.pack();
	frame.setVisible(true);
    }
    public static void main(String [] args){
       new LazyTree();
    }    
}

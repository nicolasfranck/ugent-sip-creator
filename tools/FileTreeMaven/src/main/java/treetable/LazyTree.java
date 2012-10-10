/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package treetable;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 *
 * @author nicolas
 */
public class LazyTree {
    public LazyTree() {
	JFrame frame = new JFrame("TreeTable");
        
        File rootFile = new File("/home/nicolas");
        JTree tree = new JTree();        
        
        LazyTreeNode rootNode = new LazyTreeNode(rootFile.getAbsolutePath(),new FileNode(rootFile),rootFile.isDirectory());
        
        
        tree.setModel(new FileLazyTreeModel(rootNode,tree));
        tree.setCellRenderer(new LazyFileTreeCellRenderer());
        /*
        tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
                TreePath treePath = e.getPath();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                Object [] listObjects = node.getUserObjectPath();
                for(Object o:listObjects){
                    System.out.println("object: "+o);
                }
             
            }
            @Override
            public void treeWillCollapse(TreeExpansionEvent e) throws ExpandVetoException {
             
            }
        });*/
        
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.views;

import helper.FileConstraint;
import helper.FileConstraintForm;
import helper.FileSource;
import helper.FileTreeCellRenderer;
import helper.FileUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.springframework.richclient.application.support.AbstractView;

;
/**
 *
 * @author nicolas
 */
public class FileTreeView extends AbstractView{
    private JTree fileTree;
    private TreeModel fileTreeModel;
    private JPanel panel;
    private JFileChooser fileChooser;
    @Override
    protected JComponent createControl() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        fileTree = getNewFileTree(new File("."));
        fileTree.setCellRenderer(new FileTreeCellRenderer());
        panel.add(fileTree,BorderLayout.CENTER);
        JButton chooseButton = new JButton("choose file..");
        chooseButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                File file = chooseFile();
                if(file == null)return;
                DefaultMutableTreeNode node = FileUtils.getTreeNode(new FileSource(file),0);
                fileTree.setModel(getNewFileTreeModel(node));
            }
        });
        panel.add(chooseButton,BorderLayout.SOUTH);

        //panel.add(new FileConstraintForm(new FileConstraint()).getControl(),BorderLayout.SOUTH);

        return panel;
    }
    public JTree getNewFileTree(File file) {
        DefaultMutableTreeNode root = FileUtils.getTreeNode(new FileSource(file));
        final JTree tree = new JTree(getNewFileTreeModel(root));
        return tree;
    }   
    public TreeModel getNewFileTreeModel(DefaultMutableTreeNode node){
        TreeModel model = new DefaultTreeModel(node,true);
        return model;
    }

    public JFileChooser getFileChooser(){
        if(fileChooser == null){
            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("choose a directory");
            fileChooser.setFileFilter(new FileFilter(){
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
                @Override
                public String getDescription() {
                    return "directories only";
                }
            });
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
        }
        return fileChooser;
    }
    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }
    public File chooseFile(){
        JFileChooser fchooser = getFileChooser();
        int freturn = fchooser.showOpenDialog(null);
        File file;
        if(freturn == JFileChooser.APPROVE_OPTION)file = fchooser.getSelectedFile();
        else file = new File(".");
        return file;
    }
}

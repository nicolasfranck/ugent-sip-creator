/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.views;

import RenameWandLib.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.springframework.richclient.application.support.AbstractView;
import treetable.FileNode;
import treetable.FileSystemModel;
import treetable.JTreeTable;
import treetable.TreeTableModel;


/**
 *
 * @author nicolas
 */
public class FileTreeView1 extends AbstractView{ 
    private JPanel panel1;
    private JPanel panel2;
    private JFileChooser fileChooser;
    private JComponent component;
    private FileNode fileNodeChoosen;
    private JTextField sourcePatternField;
    private JTextField destinationPatternField;
    private File lastFile;
    //private JTextArea console;
    private JTable resultTable;
    private DefaultTableModel resultTableModel;
    
    @Override
    protected JComponent createControl() {
        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);                
        
        //panel north: file tree
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        component = getNewTreeComponent(getLastFile());       
        panel1.add(component,BorderLayout.CENTER);
        
        //add panel north to split pane
        splitter.add(panel1);
        
        //panel south: renamer
        panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();        
        c.gridx = c.gridy = 0;                
        c.gridwidth = GridBagConstraints.REMAINDER;        
        c.fill = GridBagConstraints.NONE;
        c.weightx = c.weighty = 0.5;
        
        JButton chooseButton = new JButton("choose file..");                
        chooseButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                final File file = chooseFile();              
                if(file == null)return;              
                getStatusBar().getProgressMonitor().taskStarted("test",0);                               
                panel1.remove(component);
                component = getNewTreeComponent(file);                 
                panel1.add(component,BorderLayout.CENTER);                          
                panel1.revalidate();
                panel1.repaint();
                getStatusBar().getProgressMonitor().done();                                 
            }
        });
        
        c.gridy = 0;
        panel2.add(chooseButton,c);        
        
        c.gridy = 1;
        panel2.add(getNewRenameForm(),c);
        
        c.gridy = 2;
        /*console = new JTextArea();
        console.setColumns(50);
        console.setRows(5);*
        
        panel2.add(new JScrollPane(console),c);*/
        
        /*result tabel
        String [] [] rows = {};
        String [] cols = {"from","to"};
        resultTableModel = new DefaultTableModel(rows,cols);
        resultTable = new JTable(resultTableModel); 
        resultTable.getColumnModel().getColumn(0).setWidth(20);
        resultTable.getColumnModel().getColumn(1).setWidth(20);
      
        panel2.add(new JScrollPane(resultTable),c);*/
        
        //add panel south to split pane
        splitter.add(panel2);
        
        splitter.setDividerLocation(0.5);
        splitter.setResizeWeight(0.5);
        
        
        return splitter;
    }
    public JComponent getNewTreeComponent(File file){
        JTreeTable fileTree = getNewFileTree(file);           
        JScrollPane fileTreeScroller = new JScrollPane(fileTree);
        fileTreeScroller.setViewportView(fileTree);
        fileTreeScroller.setBorder(BorderFactory.createEmptyBorder());        
        return fileTreeScroller;
    }
    public JTreeTable getNewFileTree(File file) {       
        JTreeTable t = new JTreeTable(getNewFileTreeModel(file));                                
        final JTree tree = t.getTree();
        tree.addTreeSelectionListener(new TreeSelectionListener(){
            @Override
            public void valueChanged(TreeSelectionEvent tse) {               
                FileNode fn = (FileNode) tse.getPath().getLastPathComponent();                                 
                if(!fn.getFile().isDirectory()){
                    tree.clearSelection();
                    return;
                }
                fileNodeChoosen = fn;
            }        
        });
        return t;
    }   
    public TreeTableModel getNewFileTreeModel(File file){
        FileSystemModel model =  new FileSystemModel(file);                        
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
    public JComponent getNewRenameForm(){
        JPanel p = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        p.setLayout(layout);
        
        JLabel labelHead = new JLabel("Rename:");        
        JLabel labelSourcePattern = new JLabel("source pattern:",SwingConstants.RIGHT);
        JLabel labelDestinationPattern = new JLabel("destination pattern:",SwingConstants.RIGHT);        
        sourcePatternField = new JTextField(25);
        destinationPatternField = new JTextField(25);
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.weightx = c.weighty = 0.5;
        c.insets = new Insets(5,5,5,5);
        c.gridx = c.gridy = 0;        
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        p.add(labelHead,c);
        
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;        
        c.gridy = 1;
        p.add(labelSourcePattern,c);        
        
        c.gridx = 1;
        p.add(sourcePatternField);
        
        c.gridx = 0;
        c.gridy = 2;
        p.add(labelDestinationPattern,c);
        
        c.gridx = 1;
        p.add(destinationPatternField,c);  
        
        //p.setPreferredSize(new Dimension(500,100));
       
        JButton submit = new JButton("rename");
        c.gridy = 3;
        p.add(submit,c);
        
        submit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(fileNodeChoosen == null)return;
                rename();
            }        
        });        
        
        return p;        
    }
    public void rename(){
        if(
            !sourcePatternField.getText().isEmpty() &&
            !destinationPatternField.getText().isEmpty()            
        ){
            try{
                RenameWand renamer = new RenameWand();
                renamer.setCurrentDirectory(fileNodeChoosen.getFile());
                renamer.setRecurseIntoSubdirectories(true);
                renamer.setSourcePatternString(sourcePatternField.getText());
                renamer.setTargetPatternString(destinationPatternField.getText());
                renamer.setSimulateOnly(true);
                renamer.setRenameListener(new RenameListenerAdapter(){
                    @Override
                    public OnErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr) {
                        if(errorStr != null)System.err.println(errorStr);                      
                        /*
                        console.append("mv "+pair.getSource().getAbsolutePath()+" to "+pair.getTarget().getAbsolutePath()+" failed\n");
                        console.append("undoing all operations!\n");
                        console.append(errorType+":'"+errorStr+"'\n");
                        * 
                        */
                                             
                        return OnErrorAction.undoAll;
                    }
                    @Override
                    public void onRenameStart(RenameFilePair pair) {
                        /*
                        try {
                            console.append("renaming " + pair.getSource().getCanonicalPath() + " to " + pair.getTarget().getCanonicalPath()+"\n");
                        } catch (IOException ex) {
                            Logger.getLogger(RenameWand.class.getName()).log(Level.SEVERE, null, ex);
                        }*/
                    }
                    @Override
                    public void onRenameEnd(RenameFilePair pair) {
                        /*
                        try {
                            console.append("renaming " + pair.getSource().getCanonicalPath() + " to " + pair.getTarget().getCanonicalPath()+" DONE!\n");
                        } catch (IOException ex) {
                            Logger.getLogger(RenameWand.class.getName()).log(Level.SEVERE, null, ex);
                        }*/
                    }
                });
                renamer.rename();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public File getLastFile(){
        if(lastFile == null){           
            lastFile = new File(System.getProperty("user.home"));
            if(lastFile == null || !lastFile.isDirectory()){
                lastFile = File.listRoots()[0];
            }
        }
        return lastFile;
    }

    public void setLastFile(File lastFile) {
        this.lastFile = lastFile;
    }
    
}

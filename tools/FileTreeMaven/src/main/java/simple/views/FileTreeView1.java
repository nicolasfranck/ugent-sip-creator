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
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.support.AbstractView;
import treetable.FileNode;
import treetable.FileSystemModel;
import treetable.JTreeTable;
import treetable.TreeTableModel;
import treetable.executors.ExpandCollapseTreeExecutor;



/**
 *
 * @author nicolas
 */
public class FileTreeView1 extends AbstractView{ 
    private JPanel panel1;
    private JPanel panel2;
    private JFileChooser fileChooser;
    private JComponent scrollerTreeTable;
    private FileNode fileNodeChoosen;
    private JTextField sourcePatternField;
    private JTextField destinationPatternField;
    private File lastFile;    
    private JTable resultTable;
    private DefaultTableModel resultTableModel;
    private int numToRename = 0;
    private int numRenamedSuccess = 0;
    private int numRenamedError = 0;
    private JLabel statusLabel;
    private JTreeTable treeTable;
    private TreeTableModel treeTableModel;
    private boolean treeTableExpanded = false;
    private ExpandCollapseTreeExecutor expandExecutor  = new ExpandCollapseTreeExecutor(this);

    public JTreeTable getCurrentTreeTable(){
        if(treeTable == null){
            treeTable = getNewTreeTable(getLastFile());
        }
        return treeTable;
    }
    public TreeTableModel getCurrentTreeTableModel() {
        return treeTableModel;
    }

    @Override
    protected JComponent createControl() {
        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);                
        
        //panel north: file tree
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        scrollerTreeTable = new JScrollPane(getCurrentTreeTable());

        panel1.add(scrollerTreeTable,BorderLayout.CENTER);
        
        //add panel north to split pane
        splitter.add(panel1);
        
        //panel south: renamer
        panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();        
        c.gridx = c.gridy = 0;                
        c.gridwidth = GridBagConstraints.REMAINDER;        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = c.weighty = 0.5;
        
        JButton chooseButton = new JButton("choose file..");                
        chooseButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                final File file = chooseFile();              
                if(file == null)return;              
                getStatusBar().getProgressMonitor().taskStarted("test",0);
                setLastFile(file);                
                reloadTreeTable(file);                
                getStatusBar().getProgressMonitor().done();                                 
            }
        });
        
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        panel2.add(chooseButton,c);

        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        statusLabel = new JLabel();
        panel2.add(statusLabel,c);
        
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(getNewRenameForm(),c);
        
        c.gridy = 3;
        ///result tabel
        String [] [] rows = {};
        String [] cols = {"nr","van","naar","status"};
        resultTableModel = new DefaultTableModel(rows,cols);
        resultTable = new JTable(resultTableModel);
        resultTable.setFillsViewportHeight(true);
        resultTable.setRowSelectionAllowed(false);        

        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollerResultTable = new JScrollPane(resultTable);
        scrollerResultTable.setPreferredSize(new Dimension(500,200));
        panel2.add(scrollerResultTable,c);
        
        //add panel south to split pane
        splitter.add(panel2);
        
        splitter.setDividerLocation(0.5);
        splitter.setResizeWeight(0.5);
        
        
        return splitter;
    }
    protected JTreeTable getNewTreeTable(File file) {
        treeTable = new JTreeTable(getNewTreeTableModel(file));
        final JTree tree = treeTable.getTree();
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
        
        return treeTable;
    }    
    protected TreeTableModel getNewTreeTableModel(File file){
        treeTableModel =  new FileSystemModel(file);
        return treeTableModel;
    }
    protected JFileChooser getFileChooser(){
        if(fileChooser == null){
            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("choose a directory");
            fileChooser.setFileFilter(new FileFilter(){
                @Override
                public boolean accept(File file) {
                    return file.isDirectory() && file.canRead();
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
    protected void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }
    protected File chooseFile(){
        JFileChooser fchooser = getFileChooser();
        int freturn = fchooser.showOpenDialog(null);
        File file;
        if(freturn == JFileChooser.APPROVE_OPTION)file = fchooser.getSelectedFile();
        else file = getLastFile();
        return file;
    }
    protected JComponent getNewRenameForm(){
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

       
        final JButton testRenameButton = new JButton("Simulatie..");
        c.gridy = 3;
        c.gridx = 0;
        p.add(testRenameButton,c);

        final JButton renameButton = new JButton("Rename!");
        c.gridy = 3;
        c.gridx = 1;
        p.add(renameButton,c);
        
        testRenameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                renameButton.setEnabled(false);
                testRenameButton.setEnabled(false);
                rename(true);
                testRenameButton.setEnabled(true);
                renameButton.setEnabled(true);                
            }        
        });

        renameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                testRenameButton.setEnabled(false);
                renameButton.setEnabled(false);
                rename(false);
                reloadTreeTable(getLastFile());
                testRenameButton.setEnabled(true);
                renameButton.setEnabled(true);
            }
        });       
        
        return p;        
    }
    public void reloadTreeTable(File file){
        panel1.remove(scrollerTreeTable);
        scrollerTreeTable = new JScrollPane(getNewTreeTable(file));
        panel1.add(scrollerTreeTable,BorderLayout.CENTER);
        panel1.revalidate();
        panel1.repaint();
    }
    protected void rename(final boolean simulateOnly){

        resultTableModel.getDataVector().clear();
        resultTable.setModel(resultTableModel);
        statusLabel.setText(null);
        numRenamedSuccess = 0;
        numRenamedError = 0;

        if(fileNodeChoosen == null){
         
            statusLabel.setText("no file is selected");
            
        }else if(sourcePatternField.getText().isEmpty()){

            statusLabel.setText("source pattern not given");

        }else if(destinationPatternField.getText().isEmpty()){

            statusLabel.setText("destination pattern not given");

        }else if(destinationPatternField.getText().compareTo(sourcePatternField.getText()) == 0){

            statusLabel.setText("source pattern and destination pattern are the same");

        }
        else{
            try{

                RenameWand renamer = new RenameWand();                
                renamer.setCurrentDirectory(fileNodeChoosen.getFile());
                renamer.setRecurseIntoSubdirectories(true);
                renamer.setSourcePatternString(sourcePatternField.getText());
                renamer.setTargetPatternString(destinationPatternField.getText());
                renamer.setSimulateOnly(simulateOnly);                

                renamer.setRenameListener(new RenameListenerAdapter(){
                     @Override
                    public void onInit(final java.util.List<FileUnit> matchCandidates,final java.util.List<FileUnit> matches){
                         if(matchCandidates.size() == 0){
                             statusLabel.setText("selected directory does not contain any files");
                         }else if(matches.size() == 0){
                             statusLabel.setText("no matches found (candidates: "+matchCandidates.size()+")");
                         }
                    }
                    @Override
                    public boolean approveList(final java.util.List<FileUnit> list){
                        numToRename = list.size();
                        FileTreeView1.this.getStatusBar().getProgressMonitor().taskStarted("renaming",numToRename);
                        return true;
                    }
                    @Override
                    public OnErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr) {
                        numRenamedError++;
                        return OnErrorAction.skip;                        
                    }
                    @Override
                    public void onRenameSuccess(RenameFilePair pair) {                        
                        numRenamedSuccess++;
                    }
                    @Override
                    public void onRenameEnd(RenameFilePair pair) {
                        String status = simulateOnly ? "simulatie":(pair.isSuccess() ? "successvol":"error");
                        resultTableModel.insertRow(resultTableModel.getRowCount(),new String []{
                            ""+(resultTableModel.getRowCount()+1),pair.getSource().getName(),pair.getTarget().getName(),status
                        });
                        FileTreeView1.this.getStatusBar().getProgressMonitor().worked(numRenamedError + numRenamedSuccess);                        
                    }
                    @Override
                    public void onEnd(){
                        FileTreeView1.this.getStatusBar().getProgressMonitor().done();
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
    protected void setLastFile(File lastFile) {
        this.lastFile = lastFile;
    }

    /*
     * source: http://www.exampledepot.com/egs/javax.swing.tree/ExpandAll.html
     */
    // If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.    
    @Override
    protected void registerLocalCommandExecutors(PageComponentContext context){       
        context.register("expandCollapseTreeCommand",expandExecutor);
        expandExecutor.setEnabled(true);
    }
}

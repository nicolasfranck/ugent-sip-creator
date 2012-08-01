/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.views;

import RenameWandLib.*;
import forms.CleanParamsForm;
import forms.RenameParamsForm;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreePath;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.support.AbstractView;
import renaming.CleanParams;
import renaming.RenameParams;
import treetable.FileNode;
import treetable.FileSystemModel;
import treetable.JTreeTable;
import treetable.TreeTableModel;
import treetable.executors.ExpandCollapseTreeExecutor;
import treetable.executors.ReloadTreeExecutor;



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
    private ReloadTreeExecutor reloadTreeExecutor  = new ReloadTreeExecutor(this);

    private RenameParams renameParams;
    private RenameParamsForm renameParamsForm;
    

    public RenameParams getRenamePair() {
        if(renameParams == null)renameParams = new RenameParams();
        return renameParams;
    }
    public void setRenamePair(RenameParams renameParams) {
        this.renameParams = renameParams;
    }

    public RenameParamsForm getRenamePairForm() {
        if(renameParamsForm == null){
            renameParamsForm = new RenameParamsForm(getRenamePair());
        }
        return renameParamsForm;
    }

    public void setRenamePairForm(RenameParamsForm renamePairForm) {
        this.renameParamsForm = renamePairForm;
    }

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
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = c.weighty = 0.5;
        
        JButton chooseButton = new JButton("kies map..");
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
        resultTableModel = new DefaultTableModel(rows,cols){
            @Override
            public boolean isCellEditable(int row,int col){
                return false;
            }
            @Override
            public Class getColumnClass(int column) {                
                return column == 0 ? Integer.class:String.class;
          }
        };
        resultTable = new JTable(resultTableModel);
        resultTable.setFillsViewportHeight(true);
        resultTable.setRowSelectionAllowed(false);

        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(resultTableModel);

        resultTable.setRowSorter(sorter);

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
        //per default wordt de root geselecteerd
        TreePath tp = new TreePath(tree.getModel().getRoot());
        tree.setSelectionPath(tp);        

        return treeTable;
    }    
    protected TreeTableModel getNewTreeTableModel(File file){
        treeTableModel =  new FileSystemModel(file);
        treeTableModel.addTreeModelListener(new TreeModelListener(){
            @Override
            public void treeNodesChanged(TreeModelEvent tme) {
                reloadTreeTable(getLastFile());
            }
            @Override
            public void treeNodesInserted(TreeModelEvent tme) {
                reloadTreeTable(getLastFile());
            }
            @Override
            public void treeNodesRemoved(TreeModelEvent tme) {
                reloadTreeTable(getLastFile());
            }
            @Override
            public void treeStructureChanged(TreeModelEvent tme) {
                reloadTreeTable(getLastFile());
            }
        });
        return treeTableModel;
    }
    protected JFileChooser getFileChooser(){
        if(fileChooser == null){
            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("kies een map");
            fileChooser.setFileFilter(new FileFilter(){
                @Override
                public boolean accept(File file) {
                    return file.isDirectory() && file.canRead();
                }
                @Override
                public String getDescription() {
                    return "enkel mappen";
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
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 0.5;
        c.insets = new Insets(5,5,5,5);
        c.gridx = c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;

        RenameParamsForm renameForm = getRenamePairForm();
        final ValidatingFormModel renameFormModel = renameForm.getFormModel();

        JComponent componentForm = renameForm.getControl();
        panel.add(componentForm,c);        
        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        final JButton submitButton = new JButton("ok");
        buttonPanel.add(submitButton);
        final JButton simulateButton = new JButton("simuleer");
        buttonPanel.add(simulateButton);

        submitButton.setEnabled(false);
        submitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                renameFormModel.commit();
                submitButton.setEnabled(false);
                simulateButton.setEnabled(false);
                rename(false);
                reloadTreeTable(getLastFile());
                submitButton.setEnabled(true);
                simulateButton.setEnabled(true);
            }
        });

        renameForm.addValidationListener(new ValidationListener(){
            @Override
            public void validationResultsChanged(ValidationResults results) {
                submitButton.setEnabled(!results.getHasErrors());
                simulateButton.setEnabled(!results.getHasErrors());
            }
        });

        simulateButton.setEnabled(false);
        simulateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                renameFormModel.commit();
                submitButton.setEnabled(false);
                simulateButton.setEnabled(false);
                rename(true);
                submitButton.setEnabled(true);
                simulateButton.setEnabled(true);
            }
        });

        c.gridy += 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTHWEST;
        panel.add(buttonPanel,c);

        CleanParamsForm cleanForm = new CleanParamsForm(new CleanParams());
        final ValidatingFormModel cleanFormModel = cleanForm.getFormModel();

        componentForm = cleanForm.getControl();
        c.gridy += 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        panel.add(componentForm,c);

        c.gridy += 1;
        c.fill = GridBagConstraints.NONE;
        JPanel cleanButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JButton cleanSubmitButton = new JButton("ok");
        cleanButtonPanel.add(cleanSubmitButton);
        final JButton cleanSimulateButton = new JButton("simuleer");
        cleanButtonPanel.add(cleanSimulateButton);
        
        panel.add(cleanButtonPanel,c);




        return panel;        
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
        //belangrijk: ander weet de row-sorter van niets, wat resulteert in een nullpointer-exception
        resultTableModel.fireTableDataChanged();

        resultTable.setModel(resultTableModel);
        statusLabel.setText(null);
        numRenamedSuccess = 0;
        numRenamedError = 0;


        if(fileNodeChoosen == null){
         
            statusLabel.setText("geen map geselecteerd");
            
        }
        else{
            try{
              
                RenameWand renamer = new RenameWand();                
                renamer.setCurrentDirectory(fileNodeChoosen.getFile());

                renamer.setRecurseIntoSubdirectories(renameParams.isRecurseIntoSubdirectories());
                renamer.setSourcePatternString(renameParams.getSourcePattern());
                renamer.setTargetPatternString(renameParams.getDestinationPattern());
                renamer.setCopy(renameParams.isCopy());
                renamer.setOnRenameOperationError(renameParams.getOnErrorAction());
                renamer.setRenameDirectories(renameParams.isRenameDirectories());
                renamer.setSimulateOnly(simulateOnly);               
                renamer.setMatchLowerCase(renameParams.isMatchLowerCase());

                renamer.setRenameListener(new RenameListenerAdapter(){
                     @Override
                    public void onInit(final java.util.List<FileUnit> matchCandidates,final java.util.List<FileUnit> matches){

                         if(matchCandidates.size() == 0){
                             statusLabel.setText("geselecteerde map is leeg");
                         }else if(matches.size() == 0){
                             statusLabel.setText("geen bestanden gevonden overeenkomstig het bronpatroon");
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
                        resultTableModel.insertRow(resultTableModel.getRowCount(),new Object []{
                            new Integer(resultTableModel.getRowCount()+1),
                            pair.getSource().getName(),
                            pair.getTarget().getName(),
                            status
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
        context.register("reloadTreeCommand",reloadTreeExecutor);
        expandExecutor.setEnabled(true);
        reloadTreeExecutor.setEnabled(true);
    }
}

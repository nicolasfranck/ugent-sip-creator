package ugent.bagger.panels;

import RenameWandLib.ErrorAction;
import RenameWandLib.FileUnit;
import RenameWandLib.RenameError;
import RenameWandLib.RenameFilePair;
import RenameWandLib.RenameListenerAdapter;
import RenameWandLib.RenameWand;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.RowSorter;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import treetable.FileNode;
import treetable.FileSystemModel;
import treetable.JTreeTable;
import treetable.TreeTableModel;
import ugent.bagger.filters.DirectoryFilter;
import ugent.bagger.forms.AdvancedRenameParamsForm;
import ugent.bagger.helper.Context;
import ugent.bagger.params.AdvancedRenameParams;
import ugent.bagger.workers.DefaultWorker;

/**
 *
 * @author nicolas
 */
public class AdvancedRenamePanel extends JPanel{
    
    private static Log logger = LogFactory.getLog(AdvancedRenamePanel.class);
    
    private JPanel panelTreeTable;
    private JPanel panelModifier;
    private JPanel panelAdvancedRenamer;    
    private JPanel advancedRenameButtonPanel;       
    private JComponent scrollerTreeTable;
    private ArrayList<FileNode>fileNodesSelected = new ArrayList<FileNode>();
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
    private AdvancedRenameParams advancedRenameParams;
    private AdvancedRenameParamsForm advancedRenameParamsForm;
   
    private TreeSelectionListener treeTableSelectionListener;
    
    public AdvancedRenamePanel(){
        init();
    }    
    public TreeSelectionListener getNewTreeTableSelectionListener(final JTree tree) {
        treeTableSelectionListener = new TreeSelectionListener(){
            @Override
            public void valueChanged(TreeSelectionEvent tse) {            
                fileNodesSelected.clear();
                // tree.getSelectionPaths() != tse.getPaths()
                TreePath [] selectedPaths = tree.getSelectionPaths();
                logger.debug("RenameView: getNewTreeTableSelectionListener: selectedPaths: "+selectedPaths);
                if(selectedPaths == null) {
                    return;
                }
                for(int i = 0;i<selectedPaths.length;i++){
                    FileNode fn = (FileNode) selectedPaths[i].getLastPathComponent();
                    fileNodesSelected.add(fn);
                }               
            }
        };
        return treeTableSelectionListener;
    }

    public JTable getResultTable(){
        if(resultTable == null){
            resultTable = new JTable(getResultTableModel());
            resultTable.setFillsViewportHeight(true);
            resultTable.setRowSelectionAllowed(false);
            RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(resultTableModel);
            resultTable.setRowSorter(sorter);
            
            resultTable.setDefaultRenderer(Object.class,new TableCellRenderer(){
                @Override
                public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
                    JLabel label = new JLabel();

                    String labelSimulation = Context.getMessage("simulation");
                    String labelSuccess = Context.getMessage("success");
                    String labelFailed = Context.getMessage("failed");

                    if(row >= 0){
                        String value = null;
                        RenameFilePair pair = (RenameFilePair)o;                        
                        switch(col){                            
                            case 0:                                
                                value = pair.getSource().getName();
                                break;
                            case 1:
                                value = pair.getTarget().getName();
                                break;
                            case 2:
                                value = pair.isSimulateOnly() ? labelSimulation:(pair.isSuccess() ? labelSuccess:labelFailed);
                                break;                                
                        }                        
                        label.setText(value);                        
                        if(pair.isSimulateOnly()){                            
                            label.setForeground(Color.DARK_GRAY);
                        }else if(pair.isSuccess()){                            
                            label.setForeground(Color.green);
                        }else{                             
                            label.setForeground(Color.red);
                        }
                    }

                    return label;
                }
            });
            
        }
        return resultTable;
    }
    public void setResultTable(JTable resultTable) {
        this.resultTable = resultTable;
    }
    public DefaultTableModel getResultTableModel(){
        if(resultTableModel == null){
            String [] [] rows = {};
            String [] cols = {
                Context.getMessage("from"),
                Context.getMessage("to"),
                Context.getMessage("status")
            };
            resultTableModel = new DefaultTableModel(rows,cols){
                @Override
                public boolean isCellEditable(int row,int col){
                    return false;
                }
                @Override
                public Class getColumnClass(int column) {
                    return Object.class;
              }
            };            
        }
        return resultTableModel;
    }
    public void setResultTableModel(DefaultTableModel resultTableModel) {
        this.resultTableModel = resultTableModel;
    }
    public JLabel getStatusLabel() {
        if(statusLabel == null) {
            statusLabel = new JLabel();
        }
        return statusLabel;
    }
    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }
    public JPanel getNewButtonPanel(){
        return new JPanel(new FlowLayout(FlowLayout.LEFT));
    }
    
    public JPanel getAdvancedRenameButtonPanel() {
        if(advancedRenameButtonPanel == null) {
            advancedRenameButtonPanel = getNewButtonPanel();
        }
        return advancedRenameButtonPanel;
    }
    
    public void setAdvancedRenameButtonPanel(JPanel advancedRenameButtonPanel) {
        this.advancedRenameButtonPanel = advancedRenameButtonPanel;
    }
    
    public AdvancedRenameParams getAdvancedRenameParams() {
        if(advancedRenameParams == null) {
            advancedRenameParams = new AdvancedRenameParams();
        }
        return advancedRenameParams;
    }
    public void setAdvancedRenameParams(AdvancedRenameParams renameParams) {
        this.advancedRenameParams = renameParams;
    }

    public AdvancedRenameParamsForm getAdvancedRenameParamsForm() {
        if(advancedRenameParamsForm == null){
            advancedRenameParamsForm = new AdvancedRenameParamsForm(getAdvancedRenameParams());
        }
        return advancedRenameParamsForm;
    }
    public void setAdvancedRenameParamsForm(AdvancedRenameParamsForm renamePairForm) {
        this.advancedRenameParamsForm = renamePairForm;
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

    
    private void init() {
        JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        //panel west: file tree
        panelTreeTable = new JPanel();
        panelTreeTable.setLayout(new BorderLayout());
        scrollerTreeTable = new JScrollPane(getCurrentTreeTable());

        panelTreeTable.add(scrollerTreeTable,BorderLayout.CENTER);
        
        //add panel west to split pane
        splitter.add(panelTreeTable);        
        
        //panel east: modifier
        panelModifier = new JPanel();
        panelModifier.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();        
        c.gridx = c.gridy = 0;                
        c.gridwidth = GridBagConstraints.REMAINDER;        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = c.weighty = 0.5;

        //panel east: modifier: choose directory
        JButton chooseButton = new JButton(Context.getMessage("renameView.chooseFileButton.label"));
        chooseButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                final File [] files = ugent.bagger.helper.SwingUtils.chooseFiles(
                    Context.getMessage("RenameView.FileChooser.dialogtitle"),
                    new DirectoryFilter(Context.getMessage("RenameView.FileChooser.description")),
                    JFileChooser.DIRECTORIES_ONLY,
                    false
                );
                
                if(files.length == 0){
                    return;
                }      
                //TODO
                //getStatusBar().getProgressMonitor().taskStarted("test",0);
                setLastFile(files[0]);                
                reloadTreeTable(files[0]);                
                //getStatusBar().getProgressMonitor().done();                                 
            }
        });

        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        panelModifier.add(chooseButton,c);

        //panel east: modifier: status label
        c.gridy += 1;
        c.fill = GridBagConstraints.HORIZONTAL;        
        panelModifier.add(getStatusLabel(),c);

        //panel east:tabs for renamer en cleaner        

        //panel east:
        panelAdvancedRenamer = getNewAdvancedRenamePanel();       
        

        //panel east: add tabs
        c.gridy += 1;
        panelModifier.add(panelAdvancedRenamer,c);        
        
        //result tabel      

        c.gridy += 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 2;
        
        JScrollPane scrollerResultTable = new JScrollPane(getResultTable());
        scrollerResultTable.setPreferredSize(new Dimension(500,300));        
        panelModifier.add(scrollerResultTable,c);
        
        c.gridheight = 2;
        
        //add panel east to split pane
        splitter.add(panelModifier);
        
        splitter.setDividerLocation(0.7);
        splitter.setResizeWeight(0.7);        
        
        
        add(splitter);        
    }
    protected JTreeTable getNewTreeTable(File file) {
        treeTable = new JTreeTable(getNewTreeTableModel(file));
        final JTree tree = treeTable.getTree();
        tree.setSelectionPath(new TreePath(tree.getModel().getRoot()));
        tree.addTreeSelectionListener(getNewTreeTableSelectionListener(tree));
        fileNodesSelected.clear();
        fileNodesSelected.add(new FileNode(file));
        return treeTable;
    }
    protected void setFormsEnabled(boolean enabled){
        getAdvancedRenameParamsForm().setEnabled(enabled);
        //getCleanParamsForm().setEnabled(enabled);
    }    
    protected TreeTableModel getNewTreeTableModel(File file){
        treeTableModel =  new FileSystemModel(file);        
        return treeTableModel;
    }    
    
    protected JPanel getNewAdvancedRenamePanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 0.5;
        c.insets = new Insets(5,5,5,5);
        c.gridx = c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextArea descriptionArea = new JTextArea(Context.getMessage("RenameView.advancedRenamePanel.description.syntax"));
        descriptionArea.setEditable(false);        
        panel.add(descriptionArea,c);
        c.gridy += 1;

        //form
        AdvancedRenameParamsForm renameForm = getAdvancedRenameParamsForm();
        final ValidatingFormModel renameFormModel = renameForm.getFormModel();
        JComponent componentForm = renameForm.getControl();

        panel.add(componentForm,c);        
        
        //buttons
      
        final JButton submitButton = new JButton(Context.getMessage("ok"));
        getAdvancedRenameButtonPanel().add(submitButton);
        final JButton simulateButton = new JButton(Context.getMessage("simulate"));
        getAdvancedRenameButtonPanel().add(simulateButton);

        submitButton.setEnabled(false);
        submitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                FileNode fileNodeChoosen = fileNodesSelected.get(0);

                if(
                    fileNodesSelected.size() > 1 ||
                    fileNodeChoosen == null ||
                    !fileNodeChoosen.getFile().isDirectory()
                ){
                    getStatusLabel().setText(Context.getMessage("RenameView.fileNodesSelected.noDirectorySelected"));
                    return;
                }                
                renameFormModel.commit();
                submitButton.setEnabled(false);
                simulateButton.setEnabled(false);
                advancedRename(fileNodeChoosen.getFile(),false);
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
                FileNode fileNodeChoosen = fileNodesSelected.get(0);
                if(
                    fileNodeChoosen == null ||
                    fileNodesSelected.size() > 1 ||
                    !fileNodeChoosen.getFile().isDirectory()
                ){
                    getStatusLabel().setText(Context.getMessage("RenameView.fileNodesSelected.noDirectorySelected"));
                    return;
                }
                renameFormModel.commit();
                submitButton.setEnabled(false);
                simulateButton.setEnabled(false);
                advancedRename(fileNodeChoosen.getFile(),true);
                submitButton.setEnabled(true);
                simulateButton.setEnabled(true);
            }
        });
        c.gridy += 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTHWEST;

        panel.add(getAdvancedRenameButtonPanel(),c);
        
        return panel;        
    }
    public void reloadTreeTable(File file){        
        panelTreeTable.remove(scrollerTreeTable);
        scrollerTreeTable = new JScrollPane(getNewTreeTable(file));
        panelTreeTable.add(scrollerTreeTable,BorderLayout.CENTER);
        panelTreeTable.revalidate();
        panelTreeTable.repaint();
    }    
    protected void clearResultTable(){
        resultTableModel.getDataVector().clear();
        //belangrijk: ander weet de row-sorter van niets, wat resulteert in een nullpointer-exception
        resultTableModel.fireTableDataChanged();
        resultTable.setModel(resultTableModel);
    }
    protected void advancedRename(File directory,final boolean simulateOnly){
        monitor(new TaskAdvancedRename(advancedRenameParams, directory, simulateOnly),"renaming");        
    }    
    public File getLastFile(){
        if(lastFile == null){
            lastFile = File.listRoots()[0];
        }
        return lastFile;
    }
    protected void setLastFile(File lastFile) {
        this.lastFile = lastFile;
    }   
    private void monitor(SwingWorker worker,String title){
        ugent.bagger.helper.SwingUtils.monitor(worker,title,"renaming");        
    }    
    private class TaskAdvancedRename extends DefaultWorker {
        private AdvancedRenameParams advancedRenameParams;
        private File directory;
        private boolean simulateOnly ;
        public TaskAdvancedRename(AdvancedRenameParams advancedRenameParams,File directory,final boolean simulateOnly){
            this.advancedRenameParams = advancedRenameParams;
            this.directory = directory;
            this.simulateOnly = simulateOnly;
        }
        @Override
        protected Void doInBackground() throws Exception {
            clearResultTable();
            getStatusLabel().setText(null);        
            numRenamedSuccess = 0;
            numRenamedError = 0;

            try{

                RenameWand renamer = new RenameWand();
                renamer.setCurrentDirectory(directory);
                renamer.setRecurseIntoSubdirectories(advancedRenameParams.isRecurseIntoSubdirectories());
                renamer.setSourcePatternString(advancedRenameParams.getSourcePattern());
                renamer.setTargetPatternString(advancedRenameParams.getDestinationPattern());
                renamer.setCopy(advancedRenameParams.isCopy());
                renamer.setOnRenameOperationError(advancedRenameParams.getOnErrorAction());
                renamer.setSimulateOnly(simulateOnly);
                renamer.setMatchLowerCase(advancedRenameParams.isMatchLowerCase());
                renamer.setOverWrite(advancedRenameParams.isOverWrite());            
                renamer.setRenameDirectories(advancedRenameParams.isRenameDirectories());

                
                renamer.setRenameListener(new RenameListenerAdapter(){
                    @Override
                    public void onInit(final java.util.ArrayList<FileUnit> matchCandidates,final java.util.ArrayList<FileUnit> matches){                         
                         if(matchCandidates.isEmpty()){
                             getStatusLabel().setText("geselecteerde map is leeg");
                         }else if(matches.isEmpty()){
                             getStatusLabel().setText("geen bestanden gevonden overeenkomstig het bronpatroon");
                         }
                    }
                    @Override
                    public boolean approveList(final ArrayList<FileUnit> list){
                        numToRename = list.size();                       
                        return true;
                    }
                    @Override
                    public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr,int index) {
                        numRenamedError++;
                        return ErrorAction.skip;
                    }
                    @Override
                    public void onRenameSuccess(RenameFilePair pair,int index) {
                        numRenamedSuccess++;
                    }
                    @Override
                    public void onRenameEnd(RenameFilePair pair,int index) {
                        String status = simulateOnly ? "simulatie":(pair.isSuccess() ? "successvol":"error");
                        resultTableModel.insertRow(resultTableModel.getRowCount(),new Object []{                        
                            pair,
                            pair,
                            pair
                        });
                        int percent = (int)Math.floor( ((index+1) / ((float)numToRename))*100);                                                                        
                        setProgress(percent);                                   
                    }
                    @Override
                    public void onEnd(ArrayList<RenameFilePair>renamePairs,int numSuccess){
                        getStatusLabel().setText("totaal matches:"+renamePairs.size()+"\naantal geslaagd: "+numSuccess);                       
                    }
                });
                renamer.rename();
            }catch(Exception e){
                logger.debug(e.getMessage());
            }
            return null;
        }
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Panels;

import Filters.DirectoryFilter;
import Forms.AdvancedRenameParamsForm;
import Forms.CleanParamsForm;
import Forms.SimpleRenameParamsForm;
import Params.AdvancedRenameParams;
import Params.CleanParams;
import Params.SimpleRenameParams;
import RenameWandLib.CleanListenerAdapter;
import RenameWandLib.ErrorAction;
import RenameWandLib.FileUnit;
import RenameWandLib.RenameError;
import RenameWandLib.RenameFilePair;
import RenameWandLib.RenameListenerAdapter;
import RenameWandLib.RenameWand;
import SimpleRenamerLib.SimpleRenamer;
import Workers.DefaultWorker;
import helper.Context;
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
import javax.swing.JTabbedPane;
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

/**
 *
 * @author nicolas
 */
public class RenamePanel extends JPanel{
    
    private static Log logger = LogFactory.getLog(RenamePanel.class);
    
    private JPanel panelTreeTable;
    private JPanel panelModifier;
    private JPanel panelAdvancedRenamer;
    private JPanel panelSimpleRenamer;
    private JPanel panelCleaner;
    private JPanel advancedRenameButtonPanel;
    private JPanel simpleRenameButtonPanel;
    private JPanel cleanButtonPanel;    
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
    private SimpleRenameParams simpleRenameParams;
    private SimpleRenameParamsForm simpleRenameParamsForm;
    private AdvancedRenameParams advancedRenameParams;
    private AdvancedRenameParamsForm advancedRenameParamsForm;
    private CleanParams cleanParams;
    private CleanParamsForm cleanParamsForm;
    private TreeSelectionListener treeTableSelectionListener;
    
    public RenamePanel(){
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
    public JPanel getCleanButtonPanel() {
        if(cleanButtonPanel == null) {
            cleanButtonPanel = getNewButtonPanel();
        }
        return cleanButtonPanel;
    }
    public void setCleanButtonPanel(JPanel cleanButtonPanel) {
        this.cleanButtonPanel = cleanButtonPanel;
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

    public JPanel getSimpleRenameButtonPanel() {
        if(simpleRenameButtonPanel == null) {
            simpleRenameButtonPanel = getNewButtonPanel();
        }
        return simpleRenameButtonPanel;
    }

    public void setSimpleRenameButtonPanel(JPanel simpleRenameButtonPanel) {
        this.simpleRenameButtonPanel = simpleRenameButtonPanel;
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

    public SimpleRenameParams getSimpleRenameParams() {
        if(simpleRenameParams == null){
            simpleRenameParams = new SimpleRenameParams();           
        }
        return simpleRenameParams;
    }

    public void setSimpleRenameParams(SimpleRenameParams simpleRenameParams) {
        this.simpleRenameParams = simpleRenameParams;
    }

    public SimpleRenameParamsForm getSimpleRenameParamsForm() {
        if(simpleRenameParamsForm == null) {
            simpleRenameParamsForm = new SimpleRenameParamsForm(getSimpleRenameParams());
        }
        return simpleRenameParamsForm;
    }

    public void setSimpleRenameParamsForm(SimpleRenameParamsForm simpleRenameParamsForm) {
        this.simpleRenameParamsForm = simpleRenameParamsForm;
    }


    public CleanParams getCleanParams() {
        if(cleanParams == null) {
            cleanParams = new CleanParams();
        }
        return cleanParams;
    }

    public void setCleanParams(CleanParams cleanParams) {
        this.cleanParams = cleanParams;
    }
    public CleanParamsForm getCleanParamsForm() {
        if(cleanParamsForm == null) {
            cleanParamsForm = new CleanParamsForm(getCleanParams());
        }
        return cleanParamsForm;
    }

    public void setCleanParamsForm(CleanParamsForm cleanParamsForm) {
        this.cleanParamsForm = cleanParamsForm;
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
                final File [] files = helper.SwingUtils.chooseFiles(
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
        JTabbedPane tabs = new JTabbedPane();

        //panel east: tabs: panelRenamer en panelCleaner
        panelSimpleRenamer = getNewSimpleRenamePanel();
        panelAdvancedRenamer = getNewAdvancedRenamePanel();
        panelCleaner = getNewCleanerPanel();

        //panel east:tabs
        tabs.add(Context.getMessage("panelSimpleRenamer.label"),panelSimpleRenamer);
        tabs.add(Context.getMessage("panelAdvancedRenamer.label"),panelAdvancedRenamer);
        tabs.add(Context.getMessage("panelCleaner.label"),panelCleaner);

        //panel east: add tabs
        c.gridy += 1;
        panelModifier.add(tabs,c);        
        
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
        getCleanParamsForm().setEnabled(enabled);
    }    
    protected TreeTableModel getNewTreeTableModel(File file){
        treeTableModel =  new FileSystemModel(file);        
        return treeTableModel;
    }    
    protected JPanel getNewCleanerPanel(){
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        //form
        c.weightx = c.weighty = 0.5;
        c.insets = new Insets(5,5,5,5);
        c.gridx = c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        CleanParamsForm cleanForm = getCleanParamsForm();
        final ValidatingFormModel cleanFormModel = cleanForm.getFormModel();
        panel.add(cleanForm.getControl(),c);

        //buttons
        c.gridy += 1;
        c.fill = GridBagConstraints.NONE;
     
        final JButton cleanSubmitButton = new JButton("ok");
        getCleanButtonPanel().add(cleanSubmitButton);

        final JButton cleanSimulateButton = new JButton("simuleer");
        getCleanButtonPanel().add(cleanSimulateButton);

        cleanSimulateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(fileNodesSelected.isEmpty()){
                    getStatusLabel().setText(Context.getMessage("RenameView.fileNodesSelected.noFilesSelected"));
                    return;
                }
                ArrayList<File>cleanCandidates = new ArrayList<File>();
                for(FileNode fn:fileNodesSelected){
                    File file = fn.getFile();
                    if(file.isFile()){
                        cleanCandidates.add(file);
                    }else if(file.isDirectory()){
                        cleanCandidates.addAll(helper.FileUtils.listFiles(file));
                    }
                }

                cleanFormModel.commit();
                cleanSubmitButton.setEnabled(false);
                cleanSimulateButton.setEnabled(false);

                clean(cleanCandidates,true);
                
                cleanSubmitButton.setEnabled(true);
                cleanSimulateButton.setEnabled(true);
            }
        });

        cleanSubmitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(fileNodesSelected.isEmpty()){
                    getStatusLabel().setText(Context.getMessage("RenameView.fileNodesSelected.noFilesSelected"));
                    return;
                }
                ArrayList<File>cleanCandidates = new ArrayList<File>();
                for(FileNode fn:fileNodesSelected){
                    File file = fn.getFile();
                    if(file.isFile()){
                        cleanCandidates.add(file);
                    }else if(file.isDirectory()){
                        cleanCandidates.addAll(helper.FileUtils.listFiles(file));
                    }
                }

                cleanFormModel.commit();
                cleanSubmitButton.setEnabled(false);
                cleanSimulateButton.setEnabled(false);

                clean(cleanCandidates,false);
                reloadTreeTable(getLastFile());

                cleanSubmitButton.setEnabled(true);
                cleanSimulateButton.setEnabled(true);
            }
        });

        c.gridy += 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTHWEST;

        panel.add(getCleanButtonPanel(),c);

        return panel;
    }
    protected JPanel getNewSimpleRenamePanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 0.5;
        c.insets = new Insets(5,5,5,5);
        c.gridx = c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;

        
        JTextArea descriptionArea = new JTextArea(Context.getMessage("RenameView.simpleRenamePanel.description.syntax"));
        descriptionArea.setEditable(false);        
        panel.add(descriptionArea,c);
        c.gridy += 1;
        

        //form
        SimpleRenameParamsForm renameForm = getSimpleRenameParamsForm();
        final ValidatingFormModel renameFormModel = renameForm.getFormModel();
        JComponent componentForm = renameForm.getControl();       

        panel.add(componentForm,c);

        final JButton submitButton = new JButton(Context.getMessage("ok"));
        final JButton simulateButton = new JButton(Context.getMessage("simulate"));
        
        getSimpleRenameButtonPanel().add(submitButton);
        getSimpleRenameButtonPanel().add(simulateButton);
        c.gridy += 1;
        panel.add(getSimpleRenameButtonPanel(),c);

        submitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                ArrayList<File>candidates = new ArrayList<File>();
                for(FileNode fn:fileNodesSelected){
                    if(fn.getFile().isFile()) {
                        candidates.add(fn.getFile());
                    }
                }
                if(
                    candidates.size() <= 0
                ){
                    getStatusLabel().setText(Context.getMessage("RenameView.fileNodesSelected.noFilesSelected"));
                    return;
                }
                renameFormModel.commit();
                submitButton.setEnabled(false);
                simulateButton.setEnabled(false);
                simpleRename(candidates,false);
                reloadTreeTable(getLastFile());
                submitButton.setEnabled(true);
                simulateButton.setEnabled(true);
            }
        });
        simulateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {

                ArrayList<File>candidates = new ArrayList<File>();
                for(FileNode fn:fileNodesSelected){
                    if(fn.getFile().isFile()) {
                        candidates.add(fn.getFile());
                    }
                }
                
                if( candidates.size() <= 0 ){
                    getStatusLabel().setText(Context.getMessage("RenameView.fileNodesSelected.noFilesSelected"));
                    return;
                }      
                try{
                    renameFormModel.commit();
                }catch(Exception e){
                }
                submitButton.setEnabled(false);
                simulateButton.setEnabled(false);      
                simpleRename(candidates,true);
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
        return panel;
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
    protected void clean(ArrayList<File>files,final boolean simulateOnly){        

        clearResultTable();
        getStatusLabel().setText(null);
        
        try{
            RenameWand renamer = new RenameWand();            
            renamer.setCopy(cleanParams.isCopy());
            renamer.setSimulateOnly(simulateOnly);
            renamer.setCleanCandidates(files);
            renamer.setCleanDirectories(cleanParams.isCleanDirectories());
            renamer.setOverWrite(cleanParams.isOverWrite());

            renamer.setCleanListener(new CleanListenerAdapter(){
                @Override
                public void onInit(ArrayList<RenameFilePair>pairs){
                    getStatusLabel().setText(
                        Context.getMessage("RenameView.panelRenamer.cleanListener.onInit.status",new Object [] {
                            new Integer(pairs.size())
                        })
                    );
                }
                @Override
                public ErrorAction onError(final RenameFilePair pair, RenameError errorType, String errorStr){
                    String status = simulateOnly ? "simulatie":"failed";
                    resultTableModel.insertRow(resultTableModel.getRowCount(),new Object []{                        
                        pair,
                        pair,
                        pair
                    });
                    return cleanParams.getOnErrorAction();
                }
                @Override
                public void onCleanStart(final RenameFilePair pair){
                                       
                }
                @Override
                public void onCleanSuccess(final RenameFilePair pair){
                    String status = simulateOnly ? "simulatie":"successvol";
                    resultTableModel.insertRow(resultTableModel.getRowCount(),new Object []{                        
                        pair,
                        pair,
                        pair
                    });
                }
                @Override
                public void onEnd(ArrayList<RenameFilePair>renamePairs,int numSuccess){
                    getStatusLabel().setText(
                        Context.getMessage("RenameView.panelRenamer.cleanListener.onEnd.status",new Object []{
                            new Integer(renamePairs.size()),
                            new Integer(numSuccess)
                        })
                    );
                    //TODO
                    //RenameView.this.getStatusBar().getProgressMonitor().done();
                }
            });
            renamer.clean();
        }catch(Exception e){            
            logger.debug(e.getMessage());
        }
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
    public void simpleRename(ArrayList<File>files,final boolean simulateOnly){        
        monitor(new TaskSimpleRename(simpleRenameParams, files, simulateOnly),"renaming..");            
    }
    public File getLastFile(){
        if(lastFile == null){  
            /*logger.debug("trying to set empty lastFile user <user.home>");
            lastFile = new File(System.getProperty("user.home"));
            if(lastFile == null || !lastFile.isDirectory()){
                logger.debug("trying to set empty lastFile to first root folder");
                lastFile = File.listRoots()[0];
            }
            logger.debug("lastFile now: "+lastFile);*/
            lastFile = File.listRoots()[0];
        }
        return lastFile;
    }
    protected void setLastFile(File lastFile) {
        this.lastFile = lastFile;
    }   
    private void monitor(SwingWorker worker,String title){
        helper.SwingUtils.monitor(worker,title,"renaming");        
    }
    private class TaskSimpleRename extends DefaultWorker {
        private SimpleRenameParams simpleRenameParams;
        private ArrayList<File>files;
        private boolean simulateOnly = true;
        
        public TaskSimpleRename(SimpleRenameParams simpleRenameParams,ArrayList<File>files,final boolean simulateOnly){
            this.simpleRenameParams = simpleRenameParams;
            this.files = files;
            this.simulateOnly = simulateOnly;
        }
        @Override
        protected Void doInBackground() throws Exception {
            clearResultTable();
            getStatusLabel().setText(null);
            numRenamedSuccess = 0;
            numRenamedError = 0;

            try{
                SimpleRenamer renamer = new SimpleRenamer();
                for(File file:files){                
                    renamer.addRenameCandidate(file);
                }
                renamer.setSimulateOnly(simulateOnly);
                renamer.setPrefix(simpleRenameParams.getPrefix());
                renamer.setName(simpleRenameParams.getName());
                renamer.setSuffix(simpleRenameParams.getSuffix());
                renamer.setPreserveExtension(true);
                renamer.setStartNumber(simpleRenameParams.getStartNumber());
                renamer.setJumpNumber(simpleRenameParams.getJumpNumber());
                renamer.setNumPadding(simpleRenameParams.getNumPadding());
                renamer.setNumber(simpleRenameParams.isNumber());
                renamer.setPreserveExtension(simpleRenameParams.isPreserveExtension());
                
                renamer.setRenameListener(new SimpleRenamerLib.RenameListenerAdapter(){               
                    @Override
                    public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr) {
                        numRenamedError++;
                        return simpleRenameParams.getOnErrorAction();
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
                        
                        int percent = (int)Math.floor( ((index+1) / ((float)files.size()))*100);                                                                        
                        setProgress(percent);
                    
                        
                        //TODO
                        //RenameView.this.getStatusBar().getProgressMonitor().worked(numRenamedError + numRenamedSuccess);
                    }
                    @Override
                    public void onEnd(ArrayList<RenameFilePair> pairs, int numSuccess) {
                        getStatusLabel().setText("totaal bestanden:"+pairs.size()+", aantal geslaagd: "+numSuccess);
                        //TODO
                        //RenameView.this.getStatusBar().getProgressMonitor().done();
                    }
                });
                renamer.rename();
            }catch(Exception e){            
                logger.debug(e.getMessage());
            }    
            
            return null;
        }        
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

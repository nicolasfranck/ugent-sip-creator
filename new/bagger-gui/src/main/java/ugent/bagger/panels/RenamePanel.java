package ugent.bagger.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
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
import ugent.bagger.forms.RenameParamsForm;
import ugent.bagger.forms.RenumberParamsForm;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.RenameParams;
import ugent.bagger.params.RenumberParams;
import ugent.bagger.workers.DefaultWorker;
import ugent.rename.*;

/**
 *
 * @author nicolas
 */
public class RenamePanel extends JPanel{
    private static Log logger = LogFactory.getLog(RenamePanel.class);
    
    private JPanel panelTreeTable;
    private JPanel panelModifier;
    private JPanel panelRenamer;
    private JPanel panelRenumber;
    private JPanel renameButtonPanel;  
    private JPanel renumberButtonPanel;
    private JButton simulateRenameButton;
    private JButton submitRenameButton;
    private JButton simulateRenumberButton;
    private JButton submitRenumberButton;
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
    private RenameParams renameParams;
    private RenameParamsForm renameParamsForm;    
    private RenumberParams renumberParams;
    private RenumberParamsForm renumberParamsForm;
      
    private TreeSelectionListener treeTableSelectionListener;
    
    public RenamePanel(){
        init();
    }

    public RenumberParams getRenumberParams() {
        if(renumberParams == null){
            renumberParams = new RenumberParams();
        }
        return renumberParams;
    }
    public void setRenumberParams(RenumberParams renumberParams) {
        this.renumberParams = renumberParams;
    }
    public RenumberParamsForm getRenumberParamsForm() {
        if(renumberParamsForm == null){
            renumberParamsForm = new RenumberParamsForm(getRenumberParams());
        }
        return renumberParamsForm;
    }
    public void setRenumberParamsForm(RenumberParamsForm renumberParamsForm) {
        this.renumberParamsForm = renumberParamsForm;
    }    
    public TreeSelectionListener getNewTreeTableSelectionListener(final JTree tree) {
        treeTableSelectionListener = new TreeSelectionListener(){
            @Override
            public void valueChanged(TreeSelectionEvent tse) {            
                fileNodesSelected.clear();            
                TreePath [] selectedPaths = tree.getSelectionPaths();                
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
                            label.setForeground(new Color(89,150,59));
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
    public JPanel getRenameButtonPanel() {
        if(renameButtonPanel == null) {
            renameButtonPanel = getNewButtonPanel();
        }
        return renameButtonPanel;
    }
    public JPanel getRenumberButtonPanel() {
        if(renumberButtonPanel == null) {
            renumberButtonPanel = getNewButtonPanel();
        }
        return renumberButtonPanel;
    }

    public void setRenumberButtonPanel(JPanel renumberButtonPanel) {
        this.renumberButtonPanel = renumberButtonPanel;
    }
    
    
    public void setRenameButtonPanel(JPanel renameButtonPanel) {
        this.renameButtonPanel = renameButtonPanel;
    }
    public RenameParams getRenameParams() {
        if(renameParams == null) {
            renameParams = new RenameParams();
        }
        return renameParams;
    }
    public void setRenameParams(RenameParams renameParams) {
        this.renameParams = renameParams;
    }

    public RenameParamsForm getRenameParamsForm() {
        if(renameParamsForm == null){
            renameParamsForm = new RenameParamsForm(getRenameParams());
        }
        return renameParamsForm;
    }
    public void setRenameParamsForm(RenameParamsForm renamePairForm) {
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
                final File [] files = SwingUtils.chooseFiles(
                    Context.getMessage("RenameView.FileChooser.dialogtitle"),
                    new FileFilter [] {new DirectoryFilter(Context.getMessage("RenameView.FileChooser.description")) },
                    JFileChooser.DIRECTORIES_ONLY,
                    false,
                    RenamePanel.this
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
        c.gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;        
        panelModifier.add(getStatusLabel(),c);

        //panel renamer
        panelRenamer = getNewRenamePanel();         

        //panel east: add tabs
        c.gridy++;
        panelModifier.add(panelRenamer,c);  
        
        //panel renumber
        c.gridy++;
        panelRenumber = getNewRenumberPanel();
        panelModifier.add(panelRenumber,c);        
        
        //result tabel      
        c.gridy += 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 2;      
        
        
        JScrollPane scrollerResultTable = new JScrollPane(getResultTable());
        //scrollerResultTable.setPreferredSize(new Dimension(500,300));        
        panelModifier.add(scrollerResultTable,c);
        
        c.gridheight = 2;
        
        //add panel east to split pane
        JScrollPane scrollerPanelModifier = new JScrollPane(panelModifier);
        splitter.add(scrollerPanelModifier);
        
        splitter.setDividerLocation(0.7);
        splitter.setResizeWeight(0.7);        
        
        setLayout(new BorderLayout());
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
        getRenameParamsForm().setEnabled(enabled);
    }    
    protected TreeTableModel getNewTreeTableModel(File file){
        treeTableModel =  new FileSystemModel(file);        
        return treeTableModel;
    }
    protected JPanel getNewRenumberPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 0.5;
        c.insets = new Insets(5,5,5,5);
        c.gridx = c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;

        //form
        
        final ValidatingFormModel renumberFormModel = getRenumberParamsForm().getFormModel();
        JComponent componentForm = getRenumberParamsForm().getControl();

        panel.add(componentForm,c);        
        
        //buttons
      
        submitRenumberButton = new JButton(Context.getMessage("ok"));
        getRenumberButtonPanel().add(submitRenumberButton);
        simulateRenumberButton = new JButton(Context.getMessage("simulate"));
        getRenumberButtonPanel().add(simulateRenumberButton);

        //submitButton.setEnabled(false);
        submitRenumberButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                if(fileNodesSelected.size() <= 0){
                    getStatusLabel().setText("selecteer bestanden");
                    return;
                }
                renumberParams.setSimulateOnly(false);
                renumberFormModel.commit();
                submitRenumberButton.setEnabled(false);
                simulateRenumberButton.setEnabled(false);  
                //wordt in achtergrond uitgevoerd, dus liefst geen statements hier achter!
                renumber();               
            }
        });
        getRenumberParamsForm().addValidationListener(new ValidationListener(){
            @Override
            public void validationResultsChanged(ValidationResults results) {                
                submitRenumberButton.setEnabled(!results.getHasErrors());                
                simulateRenumberButton.setEnabled(!results.getHasErrors());
            }
        });
        
        //simulateButton.setEnabled(false);
        simulateRenumberButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){               
                if(fileNodesSelected.size() <= 0){
                    getStatusLabel().setText("selecteer bestanden");
                    return;
                }               
                renumberParams.setSimulateOnly(true);
                renumberFormModel.commit();
                submitRenumberButton.setEnabled(false);
                simulateRenumberButton.setEnabled(false);       
                //wordt in achtergrond uitgevoerd, dus liefst geen statements hier achter!
                renumber();               
            }
        });
        c.gridy += 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTHWEST;

        panel.add(getRenumberButtonPanel(),c);
        
        return panel; 
    }
    protected JPanel getNewRenamePanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 0.5;
        c.insets = new Insets(5,5,5,5);
        c.gridx = c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);        
        panel.add(descriptionArea,c);
        c.gridy += 1;

        //form
        RenameParamsForm renameForm = getRenameParamsForm();
        final ValidatingFormModel renameFormModel = renameForm.getFormModel();
        JComponent componentForm = renameForm.getControl();

        panel.add(componentForm,c);        
        
        //buttons
      
        submitRenameButton = new JButton(Context.getMessage("ok"));
        getRenameButtonPanel().add(submitRenameButton);
        simulateRenameButton = new JButton(Context.getMessage("simulate"));
        getRenameButtonPanel().add(simulateRenameButton);

        //submitButton.setEnabled(false);
        submitRenameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                if(fileNodesSelected.size() <= 0){
                    getStatusLabel().setText("selecteer bestanden");
                    return;
                }           
                renameParams.setSimulateOnly(false);
                renameFormModel.commit();
                submitRenameButton.setEnabled(false);
                simulateRenameButton.setEnabled(false);
                //wordt in achtergrond uitgevoerd, dus liefst geen statements hier achter!
                rename();                
            }
        });
        renameForm.addValidationListener(new ValidationListener(){
            @Override
            public void validationResultsChanged(ValidationResults results) {                
                submitRenameButton.setEnabled(!results.getHasErrors());
                simulateRenameButton.setEnabled(!results.getHasErrors());
            }
        });
        
        //simulateButton.setEnabled(false);
        simulateRenameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){               
                if(fileNodesSelected.size() <= 0){
                    getStatusLabel().setText("selecteer bestanden");
                    return;
                }
                renameParams.setSimulateOnly(true);
                renameFormModel.commit();
                submitRenameButton.setEnabled(false);
                simulateRenameButton.setEnabled(false);
                //wordt in achtergrond uitgevoerd, dus liefst geen statements hier achter!
                rename();                
            }
        });
        c.gridy += 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTHWEST;

        panel.add(getRenameButtonPanel(),c);
        
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
    protected void rename(){
        monitor(new TaskRename(getRenameParams()),"renaming");        
    }  
    protected void renumber(){
        monitor(new TaskRenumber(getRenumberParams()),"renumbering");
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
    private class TaskRenumber extends DefaultWorker {
        private RenumberParams renumberParams;
        public TaskRenumber(RenumberParams renumberParams){
            this.renumberParams = renumberParams;
        }
        @Override
        protected Void doInBackground(){
            clearResultTable();
            getStatusLabel().setText(null);        
            numRenamedSuccess = 0;
            numRenamedError = 0;
            
            try{                
                
                final Renumber renumber = new Renumber();    
                ArrayList<File>inputFiles = new ArrayList<File>();
                for(FileNode fileNode:fileNodesSelected){                    
                    inputFiles.add(fileNode.getFile());
                }
                renumber.setInputFiles(inputFiles);                            
                renumber.setCopy(false);               
                renumber.setSimulateOnly(renumberParams.isSimulateOnly());                                
                renumber.setOverwrite(renumberParams.isOverWrite());
                renumber.setStart(renumberParams.getStart());              
                int newEnd = renumberParams.getStart() + inputFiles.size()*renumberParams.getStep() - 1;
                renumber.setEnd(newEnd);
                renumber.setStep(renumberParams.getStep());
                renumber.setStartPos(renumberParams.getStartPos());
                renumber.setPadding(renumberParams.getPadding());                
                renumber.setPaddingChar(renumberParams.getPaddingChar());
                renumber.setStartPosType(renumberParams.getStartPosType());
                renumber.setStartPosRelative(renumberParams.getStartPosRelative());
                renumber.setSeparatorBefore(renumberParams.getSeparatorBefore());
                renumber.setSeparatorAfter(renumberParams.getSeparatorAfter());                
                renumber.setPreSort(renumberParams.getPreSort());
                if(renumberParams.getRadix() == Radix.ALPHABETHICAL){
                    renumber.setSequence(new AlphaSequence());
                }else if(renumberParams.getRadix() == Radix.HEXADECIMAL){
                    renumber.setSequence(new HexadecimalSequence());
                }else{
                    renumber.setSequence(new DecimalSequence());
                }                
                
                renumber.setRenameListener(new RenameListenerAdapter(){                    
                    @Override
                    public boolean approveList(final ArrayList<RenameFilePair> list){
                        numToRename = list.size();  
                        return true;
                    }
                    @Override
                    public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr,int index) {
                        numRenamedError++;
                        return renumberParams.getOnErrorAction();
                    }
                    @Override
                    public void onRenameSuccess(RenameFilePair pair,int index) {
                        numRenamedSuccess++;
                    }
                    @Override
                    public void onRenameEnd(RenameFilePair pair,int index) {
                        String status = renumber.isSimulateOnly() ? "simulatie":(pair.isSuccess() ? "successvol":"error");
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
                        simulateRenumberButton.setEnabled(true);
                        submitRenumberButton.setEnabled(true);
                        if(!renumberParams.isSimulateOnly()){
                            reloadTreeTable(getLastFile());
                        }                        
                    }
                });                
                renumber.rename();                
            }catch(Exception e){
                e.printStackTrace();
                logger.debug(e.getMessage());
            }
            
            return null;
        }
    }
    private class TaskRename extends DefaultWorker {
        private RenameParams renameParams;      
        public TaskRename(RenameParams renameParams){
            this.renameParams = renameParams;           
        }
        @Override
        protected Void doInBackground(){
            clearResultTable();
            getStatusLabel().setText(null);        
            numRenamedSuccess = 0;
            numRenamedError = 0;
            try{

                final Renamer renamer = new Renamer();    
                ArrayList<File>inputFiles = new ArrayList<File>();
                for(FileNode fileNode:fileNodesSelected){
                    inputFiles.add(fileNode.getFile());
                }
                renamer.setInputFiles(inputFiles);
                renamer.setSource(renameParams.getSource());
                renamer.setDestination(renameParams.getDestination());                
                renamer.setCopy(renameParams.isCopy());               
                renamer.setSimulateOnly(renameParams.isSimulateOnly());                                
                renamer.setOverwrite(renameParams.isOverWrite());            
                if(renameParams.isIgnoreCase()){
                    renamer.setPatternFlag(Pattern.CASE_INSENSITIVE);
                }else{
                    renamer.removePatternFlag((Pattern.CASE_INSENSITIVE));
                }
                if(renameParams.isRegex()){
                    renamer.removePatternFlag(Pattern.LITERAL);
                }else{
                    renamer.setPatternFlag(Pattern.LITERAL);
                }               
                
                renamer.setRenameListener(new RenameListenerAdapter(){                    
                    @Override
                    public boolean approveList(final ArrayList<RenameFilePair> list){
                        numToRename = list.size();  
                        if(renameParams.isOverWrite()){
                            return true;
                        }
                        //controleer of er geen risico is op overschrijving                        
                        ArrayList<String>seen = new ArrayList<String>();
                        int numFound = 0;
                        for(RenameFilePair pair:list){
                            if(!seen.contains(pair.getSource().getAbsolutePath())){
                                seen.add(pair.getSource().getAbsolutePath());
                            }else{
                                numFound++;
                            }
                            if(!seen.contains(pair.getTarget().getAbsolutePath())){
                                seen.add(pair.getTarget().getAbsolutePath());
                            }else{
                                numFound++;
                            }
                        }                       
                        
                        boolean approved = true;
                        if(numFound > 0){
                            int answer = JOptionPane.showConfirmDialog(SwingUtils.getFrame(),"Waarschuwing: één of meerdere bestanden zullen overschreven worden. Bent u zeker?");
                            approved = answer == JOptionPane.OK_OPTION;
                            renamer.setOverwrite(approved);
                            getRenameParamsForm().getValueModel("overWrite").setValue(new Boolean(true));
                            getRenameParamsForm().commit();
                            System.out.println("is overwrite now: "+(renamer.isOverwrite() ? "yes":"no"));
                        }
                        return approved;
                    }
                    @Override
                    public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr,int index) {
                        numRenamedError++;
                        return renameParams.getOnErrorAction();
                    }
                    @Override
                    public void onRenameSuccess(RenameFilePair pair,int index) {
                        numRenamedSuccess++;
                    }
                    @Override
                    public void onRenameEnd(RenameFilePair pair,int index) {
                        String status = renamer.isSimulateOnly() ? "simulatie":(pair.isSuccess() ? "successvol":"error");
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
                        simulateRenameButton.setEnabled(true);
                        submitRenameButton.setEnabled(true);
                        if(!renameParams.isSimulateOnly()){
                            reloadTreeTable(getLastFile());
                        }
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
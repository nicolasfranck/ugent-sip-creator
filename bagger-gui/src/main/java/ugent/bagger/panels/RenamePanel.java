package ugent.bagger.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import treetable.FileLazyTreeModel;
import treetable.FileLazyTreeModel.Mode;
import treetable.FileNode;
import treetable.FileSystemModel;
import treetable.JTreeTable;
import treetable.LazyFileTreeCellRenderer;
import treetable.LazyTreeModel;
import treetable.LazyTreeNode;
import treetable.TreeTableModel;
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
    private static final int BORDERWIDTH = 10;    
    private JPanel panelFileTree;
    private JPanel panelSouth;
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
    private JTextArea statusTextArea;
    private JTreeTable treeTable;
    private TreeTableModel treeTableModel;
    private boolean treeTableExpanded = false;   
    private RenameParams renameParams;
    private RenameParamsForm renameParamsForm;    
    private RenumberParams renumberParams;
    private RenumberParamsForm renumberParamsForm;      
    private TreeSelectionListener treeTableSelectionListener;
    
    private LazyTreeModel fileSystemModel;
    private JTree fileSystemTree;
    private LazyTreeNode fileSystemTreeNode;
    private static FileSystemView fsv = FileSystemView.getFileSystemView();
    
    public RenamePanel(){
        init();
    }
    public void ShowMessage(String title,String message){
        ShowMessage(title,message,JOptionPane.INFORMATION_MESSAGE);
    }
    public void ShowError(String title,String message){
        ShowMessage(title,message,JOptionPane.ERROR_MESSAGE);
    }
    public void ShowMessage(String title,String message,int type){
        JOptionPane.showMessageDialog(this,message,title,type);
    }
    public LazyTreeNode getFileSystemTreeNode() {
        if(fileSystemTreeNode == null){
            //root is geen echte file, eerder een plaatsvervanger die niet mag getoond worden
            //hieronder komen de verschillende roots van het bestandssysteem
            File rootFile = new File("");     
            fileSystemTreeNode = new LazyTreeNode("",new FileNode(rootFile),true);
        
            //sommige systemen hebben meerdere roots (C:/, D:/)
            for(File file:File.listRoots()){
                
                System.out.println("root: "+file);
                
                if(!fsv.isFileSystemRoot(file)){  
                    System.out.println("root: "+file+" is NOT a file system root");
                    continue;
                }
                
                System.out.println("root: "+file+" is a file system root");
                
                FileNode fn = new FileNode(file);
                final LazyTreeNode node = new LazyTreeNode(
                    file.getAbsolutePath(),
                    fn,
                    file.isDirectory()
                );    
                
                File [] children = file.listFiles();
                if(children == null){
                    continue;
                }
                for(File child:children){                
                    System.out.println("\tchild: "+child);
                    LazyTreeNode childNode = new LazyTreeNode(
                        child.getAbsolutePath(), 
                        new FileNode(child), 
                        child.isDirectory()
                    );                    
                    node.add(childNode);                                        
                }                
                
                fileSystemTreeNode.add(node);
            }
            
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    SwingUtils.expandTreeNode(fileSystemTree,fileSystemTreeNode);
                    fileSystemTree.setSelectionPath(new TreePath(fileSystemTreeNode.getPath()));
                }                    
            });
        }
        return fileSystemTreeNode;
    }

    public void setFileSystemTreeNode(LazyTreeNode fileSystemTreeNode) {
        this.fileSystemTreeNode = fileSystemTreeNode;
    }       

    public JTree getFileSystemTree() {
        if(fileSystemTree == null){
            fileSystemTree = new JTree(); 
            fileSystemModel = new FileLazyTreeModel(getFileSystemTreeNode(),fileSystemTree,Mode.DIRECTORIES_ONLY);
            fileSystemTree.setModel(fileSystemModel);            
            fileSystemTree.setCellRenderer(new LazyFileTreeCellRenderer());
            fileSystemTree.setRootVisible(false);
            fileSystemTree.setShowsRootHandles(false);
            fileSystemTree.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    if(fileSystemTree.isCollapsed(fileSystemTree.getRowForLocation(e.getX(),e.getY()))) {
                        fileSystemTree.expandRow(fileSystemTree.getRowForLocation(e.getX(),e.getY()));
                    }
                    else{
                        fileSystemTree.collapseRow(fileSystemTree.getRowForLocation(e.getX(),e.getY()));
                    }
                } 
            });
            fileSystemTree.addTreeWillExpandListener(new TreeWillExpandListener() {
                @Override
                public void treeWillExpand(TreeExpansionEvent tee) throws ExpandVetoException {
                    
                    getStatusTextArea().setText(null);
                    
                    TreePath tpath = tee.getPath();
                    LazyTreeNode node = (LazyTreeNode) tpath.getLastPathComponent();
                    FileNode fileNode = (FileNode) node.getUserObject();
                    File file = fileNode.getFile();                
                    if(file.isDirectory()){                    
                        if(!file.canRead()){
                            ShowError(null,""+file+" is not readable!");                            
                            throw new ExpandVetoException(tee);
                        }else if(!file.canWrite()){
                            getStatusTextArea().setText("kan niet schrijven in "+file.getAbsolutePath());                            
                        }else{
                            getStatusTextArea().setText(file.getAbsolutePath());                            
                        }
                    }                    
                    setFormsEnabled(file.isDirectory() && file.canWrite());                                        
                }
                @Override
                public void treeWillCollapse(TreeExpansionEvent tee) throws ExpandVetoException {             
                }                
            });
            fileSystemTree.addTreeSelectionListener(new TreeSelectionListener(){
                @Override
                public void valueChanged(TreeSelectionEvent tse) {            
                    TreePath tpath = tse.getPath();
                    LazyTreeNode node = (LazyTreeNode) tpath.getLastPathComponent();
                    FileNode fnode = (FileNode) node.getUserObject();
                    File file = fnode.getFile();
                    if(file.isDirectory() && file.canRead()){
                        setLastFile(file);
                        reloadTreeTable(file);        
                        setFormsEnabled(file.isDirectory() && file.canWrite());                        
                    }
                }
            });           
                   
        }
        return fileSystemTree;
    }
    public void setFormsEnabled(boolean enabled){
        getRenameParamsForm().setEnabled(enabled);
        SwingUtils.setJComponentEnabled(getRenameButtonPanel(),enabled && !getRenameParamsForm().hasErrors());
                        
        getRenumberParamsForm().setEnabled(enabled);                    
        SwingUtils.setJComponentEnabled(getRenumberButtonPanel(),enabled && !getRenameParamsForm().hasErrors());
    }
    public void setFileSystemTree(JTree fileSystemTree) {
        this.fileSystemTree = fileSystemTree;
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
            renumberParamsForm.setEnabled(false);
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
                getStatusTextArea().setText(null);
                
                TreePath [] selectedPaths = tree.getSelectionPaths();                
                if(selectedPaths != null) {                
                    for(int i = 0;i<selectedPaths.length;i++){
                        FileNode fn = (FileNode) selectedPaths[i].getLastPathComponent();
                        fileNodesSelected.add(fn);
                    }
                }                         
                
                File currentDir = getLastFile();
                
                if(currentDir.isDirectory() && !currentDir.canWrite()){
                    getStatusTextArea().setText("kan niet schrijven in "+currentDir.getAbsolutePath());
                }
                
                boolean formsEnabled = currentDir.isDirectory() && currentDir.canWrite() && selectedPaths != null && selectedPaths.length > 0;                
                setFormsEnabled(formsEnabled);             
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

    public JTextArea getStatusTextArea() {
        if(statusTextArea == null){
            statusTextArea = new JTextArea();
            statusTextArea.setEditable(false);            
        }
        return statusTextArea;
    }

    public void setStatusTextArea(JTextArea statusTextArea) {
        this.statusTextArea = statusTextArea;
    }
    
    public JPanel getNewButtonPanel(){
        return new JPanel(new FlowLayout(FlowLayout.LEFT));
    } 
    public JPanel getRenameButtonPanel() {
        if(renameButtonPanel == null) {
            renameButtonPanel = getNewButtonPanel();
            SwingUtils.setJComponentEnabled(renameButtonPanel,false);
        }
        return renameButtonPanel;
    }
    public JPanel getRenumberButtonPanel() {
        if(renumberButtonPanel == null) {
            renumberButtonPanel = getNewButtonPanel();
            SwingUtils.setJComponentEnabled(renumberButtonPanel,false);
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
            renameParamsForm.setEnabled(false);            
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
        //split pane
        JSplitPane splitterVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT);        
        
        //panel north: fileSystemTree and file tree
        JPanel panelNorth = new JPanel(new GridLayout(1,2));
       
        //panel west in panelNorth: fileSystemTree
        JPanel panelFileSystemTree = new JPanel(new BorderLayout());
        panelFileSystemTree.add(new JScrollPane(getFileSystemTree()));
        
        
        //panel east in panelNorth: file tree
        panelFileTree = new JPanel();
        panelFileTree.setLayout(new BorderLayout());
        scrollerTreeTable = new JScrollPane(getCurrentTreeTable());
        panelFileTree.add(scrollerTreeTable,BorderLayout.SOUTH);        
        panelFileTree.add(getStatusTextArea(),BorderLayout.NORTH);
        
        panelNorth.add(panelFileSystemTree);
        panelNorth.add(panelFileTree);
        
        //add panel west to split pane
        splitterVertical.add(panelNorth);        
        
        //panel south: modifier
        panelSouth = new JPanel(new GridLayout(1,2));        
        
        //panel tabs
        JTabbedPane tabs = new JTabbedPane();                          

        //panel renamer
        panelRenamer = getNewRenamePanel();                 
        //panel renumber        
        panelRenumber = getNewRenumberPanel();        
        
        tabs.addTab("rename",panelRenamer);
        tabs.addTab("renumber",panelRenumber);                        
        
        panelSouth.add(tabs);       
        
        
        //result tabel
        JScrollPane scrollerResultTable = new JScrollPane(getResultTable());
        scrollerResultTable.setPreferredSize(new Dimension(500,200));    
        
        panelSouth.add(scrollerResultTable);               
        
        //add panel east to split pane
        JScrollPane scrollerPanelModifier = new JScrollPane(panelSouth);
        splitterVertical.add(scrollerPanelModifier);
        
        splitterVertical.setDividerLocation(0.7);
        splitterVertical.setResizeWeight(0.7);                
        
        
        add(splitterVertical);        
    }
    protected JTreeTable getNewTreeTable(File file) {
        treeTable = new JTreeTable(getNewTreeTableModel(file));
        final JTree tree = treeTable.getTree();       
        tree.setShowsRootHandles(false);
        tree.setRootVisible(false);
        tree.addTreeSelectionListener(getNewTreeTableSelectionListener(tree));
        
        //Nicolas Franck: mouseListener bij tree zelf doet niets..
        treeTable.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){                                
                int row = tree.getRowForLocation(e.getX(),e.getY());                
                if(tree.isCollapsed(row)) {                    
                    tree.expandRow(row);
                }else{                    
                    tree.collapseRow(row);
                }
            } 
        });       
        
        fileNodesSelected.clear();        
        return treeTable;
    }     
    protected TreeTableModel getNewTreeTableModel(File file){
        treeTableModel =  new FileSystemModel(file);        
        return treeTableModel;
    }
    protected JPanel getNewRenumberPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));

        //form        
        final ValidatingFormModel renumberFormModel = getRenumberParamsForm().getFormModel();
        JComponent componentForm = getRenumberParamsForm().getControl();

        panel.add(new JScrollPane(componentForm));        
        
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
                    ShowError(null,"selecteer bestanden");                    
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
                    ShowError(null,"selecteer bestanden");                      
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

        panel.add(getRenumberButtonPanel());
        
        panel.setBorder(BorderFactory.createEmptyBorder(BORDERWIDTH,BORDERWIDTH,BORDERWIDTH,BORDERWIDTH));
        
        return panel; 
    }
    protected JPanel getNewRenamePanel(){
        JPanel panel = new JPanel();        
        panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);  
        
        JPanel panelDescription = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(panelDescription);        

        //form
        RenameParamsForm renameForm = getRenameParamsForm();
        final ValidatingFormModel renameFormModel = renameForm.getFormModel();
        JComponent componentForm = renameForm.getControl();

        panel.add(new JScrollPane(componentForm));        
        
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
                    ShowError(null,"selecteer bestanden");                      
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
                    ShowError(null,"selecteer bestanden");                      
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

        panel.add(getRenameButtonPanel());
        
        panel.setBorder(BorderFactory.createEmptyBorder(BORDERWIDTH,BORDERWIDTH,BORDERWIDTH,BORDERWIDTH));
        
        return panel;        
    }
    public void reloadTreeTable(File file){        
        panelFileTree.remove(scrollerTreeTable);
        scrollerTreeTable = new JScrollPane(getNewTreeTable(file));
        panelFileTree.add(scrollerTreeTable,BorderLayout.CENTER);
        panelFileTree.revalidate();
        panelFileTree.repaint();
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
            getStatusTextArea().setText(null);        
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
                        ShowMessage(null,"totaal matches:"+renamePairs.size()+"\naantal geslaagd: "+numSuccess);                         
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
            getStatusTextArea().setText(null);        
            numRenamedSuccess = 0;
            numRenamedError = 0;
            try{

                final Renamer renamer = new Renamer();    
                ArrayList<File>inputFiles = new ArrayList<File>();
                for(FileNode fileNode:fileNodesSelected){
                    inputFiles.add(fileNode.getFile());
                }
                renamer.setInputFiles(inputFiles);
                renamer.setSource(renameParams.getSource() != null ? renameParams.getSource() : "");
                renamer.setDestination(renameParams.getDestination() != null ? renameParams.getDestination():"");                
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
                            getRenameParamsForm().getValueModel("overWrite").setValue(new Boolean(approved));
                            getRenameParamsForm().commit();                            
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
                        ShowMessage(null,"totaal matches:"+renamePairs.size()+"\naantal geslaagd: "+numSuccess);
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

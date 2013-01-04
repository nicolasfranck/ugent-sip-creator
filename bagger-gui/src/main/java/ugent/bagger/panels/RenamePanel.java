package ugent.bagger.panels;


import gov.loc.repository.bagger.ui.BagView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.progress.BusyIndicator;
import treetable.FileLazyTreeModel;
import treetable.FileLazyTreeModel.Mode;
import treetable.FileNode;
import treetable.LazyFileTreeCellRenderer;
import treetable.LazyTreeModel;
import treetable.LazyTreeNode;
import ugent.bagger.forms.RenameParamsForm;
import ugent.bagger.forms.RenumberParamsForm;
import ugent.bagger.helper.Beans;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.AbstractFile;
import ugent.bagger.params.ContextObject;
import ugent.bagger.params.FileAbstractFile;
import ugent.bagger.params.RenameParams;
import ugent.bagger.params.RenumberParams;
import ugent.bagger.tables.ClassTable;
import ugent.bagger.workers.DefaultWorker;
import ugent.rename.*;

/**
 *
 * @author nicolas
 */
public class RenamePanel extends JPanel{
    static Log logger = LogFactory.getLog(RenamePanel.class);
    static final int BORDERWIDTH = 10;        
    JPanel panelSouth;
    JPanel panelRenamer;
    JPanel panelRenumber;
    JPanel renameButtonPanel;  
    JPanel renumberButtonPanel;
    JPanel fileTableButtonPanel;
    JButton simulateRenameButton;
    JButton submitRenameButton;
    JButton simulateRenumberButton;
    JButton submitRenumberButton;          
    JButton reloadFileTableButton;
    JButton parentFileButton;
    File lastFile;    
    JTable resultTable;
    DefaultTableModel resultTableModel;
    int numToRename = 0;
    int numRenamedSuccess = 0;
    int numRenamedError = 0;    
    RenameParams renameParams;
    RenameParamsForm renameParamsForm;    
    RenumberParams renumberParams;
    RenumberParamsForm renumberParamsForm;          
    LazyTreeModel fileSystemModel;
    JTree fileSystemTree;
    LazyTreeNode fileSystemTreeNode;
    static FileSystemView fsv = FileSystemView.getFileSystemView();    
    ClassTable<AbstractFile> fileTable;
    JScrollPane scrollerFileTable;
    JPanel panelFileTable;
    ArrayList<File>selectedFiles = new ArrayList<File>();
    HashMap<String,RenameParams>renameParamsTemplates;
    ArrayList<File>forbiddenFiles;
    
    CircularFifoBuffer selectedFileBuffer = new CircularFifoBuffer(10);

    public JButton getReloadFileTableButton() {
        if(reloadFileTableButton == null){
            reloadFileTableButton = new JButton(Context.getMessage("RenamePanel.reloadFileTableButton.label"));            
            reloadFileTableButton.setIcon(BagView.getInstance().getPropertyImage("refresh.icon"));
            reloadFileTableButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            BusyIndicator.showAt(RenamePanel.this);
                            reloadFileTable();
                            BusyIndicator.clearAt(RenamePanel.this);
                        }                            
                    });
                    
                }                
            });           
        }
        return reloadFileTableButton;
    }

    public JButton getParentFileButton() {
        if(parentFileButton == null){            
            parentFileButton = new JButton(Context.getMessage("RenamePanel.parentFileButton.label"));                        
            parentFileButton.setIcon(UIManager.getIcon("FileChooser.upFolderIcon"));
            parentFileButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            BusyIndicator.showAt(RenamePanel.this);
                            if(getLastFile().getParentFile() != null){
                                setLastFile(getLastFile().getParentFile());
                                reloadFileTable();
                            }                    
                            BusyIndicator.clearAt(RenamePanel.this);
                        }
                    });
                }                
            });
        }
        return parentFileButton;
    }
    
    public JPanel getFileTableButtonPanel() {
        if(fileTableButtonPanel == null){
            fileTableButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));                        
            fileTableButtonPanel.add(getParentFileButton()); 
            fileTableButtonPanel.add(getReloadFileTableButton());
        }
        return fileTableButtonPanel;
    }

    public ArrayList<File> getForbiddenFiles() {
        if(forbiddenFiles == null){
            forbiddenFiles = new ArrayList<File>();
        }
        return forbiddenFiles;
    }
    public void setForbiddenFiles(ArrayList<File> forbiddenFiles) {
        this.forbiddenFiles = forbiddenFiles;
    }
    public void setStatusMessage(String message){
        firePropertyChange("statusMessage",null,message);
    }
    public void setStatusError(String error){
        firePropertyChange("statusError",null,error);
    }
    public HashMap<String, RenameParams> getRenameParamsTemplates() {
        if(renameParamsTemplates == null){
            renameParamsTemplates = (HashMap<String,RenameParams>) Beans.getBean("renameParamsTemplates");
        }
        return renameParamsTemplates;
    }    
    public static ArrayList<AbstractFile>getList(File file){
        
        ArrayList<File>files = new ArrayList<File>();                        
        ArrayList<File>directories = new ArrayList<File>();
        
        FUtils.arrangeDirectoryEntries(file,files,directories);
        
        final ArrayList<AbstractFile>afiles = new ArrayList<AbstractFile>();

        for(File f:directories){                
            afiles.add(new FileAbstractFile(f));
        }
        for(File f:files){                
            afiles.add(new FileAbstractFile(f));
        }
        return afiles;        
        
    }
    public ClassTable<AbstractFile> getFileTable() {
        if(fileTable == null){                        
            final ArrayList<AbstractFile>afiles = getList(getLastFile());            
            fileTable = new ClassTable<AbstractFile>(
                afiles,
                new String [] {"name","mimeType"},
                "fileTable"
            );       
            
            fileTable.setDoubleClickHandler(new ActionCommandExecutor(){
                @Override
                public void execute() {
                    AbstractFile afile = fileTable.getSelected();
                    
                    //is geen map
                    if(afile == null || afile.isFile()){
                        return;
                    }
                    
                    final File file = (File)afile.getFile();
                    
                    //map is onleesbaar
                    if(!file.canRead()){
                        String error = Context.getMessage("RenamePanel.error.dirnotreadable",new String [] {
                            file.getAbsolutePath()
                        });
                        SwingUtils.ShowError(null,error);
                        setStatusError(error);
                        return;
                    }                    
                    
                    //map behoort tot lijst van verboden mappen
                    boolean isForbidden = false;
                    for(File forbiddenFile:getForbiddenFiles()){
                        if(forbiddenFile == null){
                            continue;
                        }                        
                        if(forbiddenFile.equals(file) || FUtils.isDescendant(forbiddenFile,file)){
                            isForbidden = true;
                            break;
                        }
                    }
                    if(isForbidden){
                        String error =  Context.getMessage("RenamePanel.error.forbidden",new String [] {
                            file.getAbsolutePath()
                        });
                        setStatusError(error);
                        SwingUtils.ShowError(null,error);
                        return;
                    }
                    
                    //herlaad tabel
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() { 
                            BusyIndicator.showAt(RenamePanel.this);
                            
                            reloadFileTable(file);                    
                            LazyTreeNode node = new LazyTreeNode(file.getAbsolutePath(),new FileNode(file),true);                    
                            TreePath tpath = getFileSystemTree().getSelectionPath();                    
                            TreePath tpath2 = tpath.pathByAddingChild(node);                       
                            getFileSystemTree().scrollPathToVisible(tpath2);                            
                            getFileSystemTree().setSelectionPath(tpath2);
                            
                            BusyIndicator.clearAt(RenamePanel.this);
                        }
                    });                  
                    
                }
                
            });
            final JTable table = (JTable) fileTable.getControl();
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                @Override
                public void valueChanged(ListSelectionEvent lse) {                                        
                    List<AbstractFile>selected = fileTable.getSelections();
                    selectedFiles.clear();
                    if(selected != null){
                        for(AbstractFile file:selected){
                            selectedFiles.add((File)file.getFile());
                        }
                    }
                }                
            });
            table.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer(){
                private Icon directoryIcon = UIManager.getIcon("FileView.directoryIcon");
                private Icon fileIcon = UIManager.getIcon("FileView.fileIcon");               
        
                @Override
                public Component getTableCellRendererComponent(JTable jtable,Object o,boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {                                        
                    JLabel label = new JLabel(o.toString());
                    label.setOpaque(true);
                    label.setBackground(Color.WHITE);
                    if(vColIndex == 0){
                        int modelRowIndex = table.convertRowIndexToModel(rowIndex);
                        AbstractFile file = (AbstractFile) fileTable.getTableModel().getElementAt(modelRowIndex);                                        
                        if(file.isDirectory()){                       
                            label.setIcon(directoryIcon);
                        }else{                       
                            label.setIcon(fileIcon);
                        }
                    }           
                    if(isSelected){                        
                        label.setForeground(table.getSelectionForeground());                                                
                        label.setBackground(table.getSelectionBackground());                        
                    }
                    
                    return label;
                }  
            });
            table.setShowGrid(false);            
        }
        return fileTable;
    }
    public void reloadFileTable(){
        reloadFileTable(getLastFile()); 
    }
    public void reloadFileTable(File file){         
        getFileTable().clearSort();        
        getFileTable().reset(getList(file));                
    }
    public void setFileTable(ClassTable<AbstractFile> fileTable) {
        this.fileTable = fileTable;
    }
    public RenamePanel(){
        setLayout(new BorderLayout());
        SwingUtils.ShowBusy();
        init();
        SwingUtils.ShowDone();             
    } 
    public LazyTreeNode getFileSystemTreeNode() {
        if(fileSystemTreeNode == null){
            //root is geen echte file, eerder een plaatsvervanger die niet mag getoond worden
            //hieronder komen de verschillende roots van het bestandssysteem
            File rootFile = new File("");     
            fileSystemTreeNode = new LazyTreeNode("",new FileNode(rootFile),true);
        
            //sommige systemen hebben meerdere roots (C:/, D:/)
            for(File file:File.listRoots()){
                
                if(!fsv.isFileSystemRoot(file)){                      
                    continue;
                }
                
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
                    BusyIndicator.showAt(RenamePanel.this);
                    
                    SwingUtils.expandTreeNode(fileSystemTree,fileSystemTreeNode);
                    fileSystemTree.setSelectionPath(new TreePath(fileSystemTreeNode.getPath()));
                    
                    BusyIndicator.clearAt(RenamePanel.this);
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
                    
                    setStatusError(null);                    
                    
                    TreePath tpath = tee.getPath();
                    LazyTreeNode node = (LazyTreeNode) tpath.getLastPathComponent();
                    FileNode fileNode = (FileNode) node.getUserObject();
                    File file = fileNode.getFile();                
                    
                    boolean doEnable = file.isDirectory() && file.canWrite() && !getForbiddenFiles().contains(file);
                    
                    if(file.isDirectory()){                    
                        if(!file.canRead()){
                            String error = Context.getMessage("RenamePanel.error.dirnotreadable",new String [] {
                                file.getAbsolutePath()
                            });
                            SwingUtils.ShowError(null,error);
                            setStatusError(error);
                            
                            setLastFile(file);
                            reloadFileTable();
                                    
                            throw new ExpandVetoException(tee);
                        }else if(!file.canWrite()){
                            setStatusError(
                                Context.getMessage("RenamePanel.error.dirnotwritable",new String [] {
                                    file.getAbsolutePath()
                                })    
                            );
                        }else if(getForbiddenFiles().contains(file)){
                            String error =  Context.getMessage("RenamePanel.error.forbidden",new String [] {
                                file.getAbsolutePath()
                            });
                            setStatusError(error);
                            SwingUtils.ShowError(null,error);
                            throw new ExpandVetoException(tee);
                        }else{                            
                            setStatusMessage(file.getAbsolutePath());
                        }
                    }                             
                    setFormsEnabled(doEnable);                                        
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
                    if(file.isDirectory()){
                        setLastFile(file);
                        reloadFileTable();      
                        boolean isForbidden = false;
                        for(File forbiddenFile:getForbiddenFiles()){
                            if(forbiddenFile == null){
                                continue;
                            }                            
                            if(forbiddenFile.equals(file) || FUtils.isDescendant(forbiddenFile,file)){
                                isForbidden = true;
                                break;
                            }
                        }
                        setFormsEnabled(file.isDirectory() && file.canWrite() && !isForbidden);                        
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
        SwingUtils.setJComponentEnabled(getRenumberButtonPanel(),enabled && !getRenumberParamsForm().hasErrors());
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
    private void init() {
        //split pane
        JSplitPane splitterVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT);        
        
        //panel north: fileSystemTree and file tree
        JPanel panelNorth = new JPanel(new GridLayout(1,2));
        panelNorth.setPreferredSize(new Dimension(1024,300));
        
       
        //panel west in panelNorth: fileSystemTree
        JPanel panelFileSystemTree = new JPanel(new BorderLayout());
        panelFileSystemTree.add(new JScrollPane(getFileSystemTree()));
        panelFileSystemTree.setPreferredSize(new Dimension(512,300));
        
        //panel east in panelNorth: file list
        panelFileTable = new JPanel(new BorderLayout());        
        panelFileTable.setPreferredSize(new Dimension(512,300));
        scrollerFileTable = new JScrollPane(getFileTable().getControl());  
        scrollerFileTable.setBorder(null);
        panelFileTable.add(scrollerFileTable); 
        panelFileTable.add(getFileTableButtonPanel(),BorderLayout.NORTH);
        
        panelNorth.add(panelFileSystemTree);
        panelNorth.add(panelFileTable);
        
        //add panel west to split pane
        splitterVertical.add(panelNorth);        
        
        //panel south: modifier
        panelSouth = new JPanel(new GridLayout(1,2));        
        panelSouth.setPreferredSize(new Dimension(1024,350));
        
        //panel tabs
        JTabbedPane tabs = new JTabbedPane();                          
        tabs.setPreferredSize(new Dimension(512,350));

        //panel renamer
        panelRenamer = getNewRenamePanel();  
        JScrollPane scrollPanelRenamer = new JScrollPane(panelRenamer);
        scrollPanelRenamer.setBorder(null);
        //panel renumber        
        panelRenumber = getNewRenumberPanel();        
        JScrollPane scrollerPanelRenumber = new JScrollPane(panelRenumber);
        scrollerPanelRenumber.setBorder(null);
        
        tabs.addTab(
            Context.getMessage("RenamePanel.tabRename.title"),
            scrollPanelRenamer
        );
        tabs.addTab(
            Context.getMessage("RenamePanel.tabRenumber.title"),
            scrollerPanelRenumber
        );                        
        
        panelSouth.add(tabs);       
        
        
        //result tabel
        JScrollPane scrollerResultTable = new JScrollPane(getResultTable());
        scrollerResultTable.setBorder(null);
        getResultTable().setPreferredSize(new Dimension(512,350));
        scrollerResultTable.setPreferredSize(new Dimension(512,350));    
        
        panelSouth.add(scrollerResultTable);               
        
        //add panel east to split pane
        JScrollPane scrollerPanelModifier = new JScrollPane(panelSouth);
        scrollerPanelModifier.setBorder(null);
        splitterVertical.add(scrollerPanelModifier);
        
        splitterVertical.setDividerLocation(0.4);
        splitterVertical.setResizeWeight(0.5);
        
        add(splitterVertical);  
        
        //register escape - listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("key pressed: "+e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.out.println("escape pressed!");
                }
            }
        });
        
    }    
    protected JPanel getNewRenumberPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        //form        
        final ValidatingFormModel renumberFormModel = getRenumberParamsForm().getFormModel();
        JComponent componentForm = getRenumberParamsForm().getControl();        

        JScrollPane scrollerComponentForm = new JScrollPane(componentForm);
        scrollerComponentForm.setBorder(null);
        scrollerComponentForm.setAlignmentX(LEFT_ALIGNMENT);  
        panel.add(scrollerComponentForm);        
        
        //buttons      
        submitRenumberButton = new JButton(Context.getMessage("ok"));
        getRenumberButtonPanel().add(submitRenumberButton);
        simulateRenumberButton = new JButton(Context.getMessage("simulate"));
        getRenumberButtonPanel().add(simulateRenumberButton);        
        
        submitRenumberButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                if(selectedFiles.size() <= 0){     
                    SwingUtils.ShowError(null,Context.getMessage("RenamePanel.error.selectFiles"));                    
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
        
        simulateRenumberButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){               
                if(selectedFiles.size() <= 0){
                    SwingUtils.ShowError(null,Context.getMessage("RenamePanel.error.selectFiles"));                          
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

        
        Dimension bpdim = getRenumberButtonPanel().getPreferredSize();
        getRenumberButtonPanel().setMaximumSize(new Dimension(Integer.MAX_VALUE,(int)bpdim.getHeight()));
        getRenumberButtonPanel().setAlignmentX(LEFT_ALIGNMENT);  
        
        panel.add(getRenumberButtonPanel());
        
        panel.setBorder(BorderFactory.createEmptyBorder(BORDERWIDTH,BORDERWIDTH,BORDERWIDTH,BORDERWIDTH));
        
        return panel; 
    }
    protected JPanel getNewRenamePanel(){
        JPanel panel = new JPanel();        
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));        
        
        //templates
        JPanel renameParamsTemplatesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        ContextObject [] list = new ContextObject[getRenameParamsTemplates().keySet().size()];
        Object [] keys = getRenameParamsTemplates().keySet().toArray();
        for(int i = 0;i < keys.length;i++){
            list[i] = new ContextObject(
                getRenameParamsTemplates().get(keys[i]),
                "renameParamsTemplates."+keys[i]+".label"
            );            
        }
        final JComboBox renameParamsTemplatesComboBox = new JComboBox(list);
        renameParamsTemplatesComboBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                ContextObject contextObject = (ContextObject) renameParamsTemplatesComboBox.getSelectedItem();                
                RenameParams params = (RenameParams) contextObject.getObject();
                if(params != null){
                    setRenameParams(params);
                    getRenameParamsForm().setFormObject(params);
                }
            }                       
        });
        renameParamsTemplatesComboBox.setAlignmentX(LEFT_ALIGNMENT);       
        JLabel renameParamsTemplatesLabel = new JLabel(Context.getMessage("renamePanel.renameParamsTemplatesLabel.label"));
        renameParamsTemplatesLabel.setAlignmentX(LEFT_ALIGNMENT);        
        
        renameParamsTemplatesPanel.add(renameParamsTemplatesLabel);
        renameParamsTemplatesPanel.add(renameParamsTemplatesComboBox);
        
        Dimension tpdim = renameParamsTemplatesPanel.getPreferredSize();
        renameParamsTemplatesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,(int)tpdim.getHeight()));
        panel.add(renameParamsTemplatesPanel);        
        
        renameParamsTemplatesPanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));        
        renameParamsTemplatesPanel.setAlignmentX(LEFT_ALIGNMENT);

        //form
        RenameParamsForm renameForm = getRenameParamsForm();
        final ValidatingFormModel renameFormModel = renameForm.getFormModel();
        JComponent componentForm = renameForm.getControl();

        JScrollPane scrollerComponentForm = new JScrollPane(componentForm);
        scrollerComponentForm.setBorder(null);
        scrollerComponentForm.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(scrollerComponentForm);        
        
        //buttons
      
        submitRenameButton = new JButton(Context.getMessage("ok"));
        getRenameButtonPanel().add(submitRenameButton);
        simulateRenameButton = new JButton(Context.getMessage("simulate"));
        getRenameButtonPanel().add(simulateRenameButton);
        

        //submitButton.setEnabled(false);
        submitRenameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                if(selectedFiles.size() <= 0){
                    SwingUtils.ShowError(null,Context.getMessage("RenamePanel.error.selectFiles"));                            
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
        
        simulateRenameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){               
                if(selectedFiles.size() <= 0){
                    SwingUtils.ShowError(null,Context.getMessage("RenamePanel.error.selectFiles"));                            
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
        
        getRenameButtonPanel().setAlignmentX(LEFT_ALIGNMENT);        
        
        Dimension rpdim = getRenameButtonPanel().getPreferredSize();
        getRenameButtonPanel().setMaximumSize(
            new Dimension((int)rpdim.getWidth(),(int)rpdim.getHeight())
        );
        panel.add(getRenameButtonPanel());
        
        panel.setBorder(BorderFactory.createEmptyBorder(BORDERWIDTH,BORDERWIDTH,BORDERWIDTH,BORDERWIDTH));
        
        return panel;        
    }       
    protected void clearResultTable(){
        resultTableModel.getDataVector().clear();
        //belangrijk: ander weet de row-sorter van niets, wat resulteert in een nullpointer-exception
        resultTableModel.fireTableDataChanged();
        resultTable.setModel(resultTableModel);
    }
    protected void rename(){
        SwingUtils.monitor(
            new TaskRename(getRenameParams()),
            Context.getMessage("RenamePanel.renaming.title"), 
            Context.getMessage("RenamePanel.renaming.description")
        );        
    }  
    protected void renumber(){
        SwingUtils.monitor(
            new TaskRenumber(getRenumberParams()),
            Context.getMessage("RenamePanel.renumbering.title"), 
            Context.getMessage("RenamePanel.renumbering.description")
        );        
    }
    public File getLastFile(){
        if(lastFile == null){
            lastFile = File.listRoots()[0];
        }
        return lastFile;
    }
    protected void setLastFile(File lastFile) { 
        selectedFileBuffer.add(lastFile);
        getParentFileButton().setEnabled(lastFile.getParentFile() != null);        
        this.lastFile = lastFile;
    }       
    private class TaskRenumber extends DefaultWorker {
        private RenumberParams renumberParams;
        public TaskRenumber(RenumberParams renumberParams){
            super();
            this.renumberParams = renumberParams;
        }
        @Override
        protected Void doInBackground(){
            clearResultTable();         
            setStatusError(null);
            
            
            numRenamedSuccess = 0;
            numRenamedError = 0;
            
            try{                
                
                final Renumber renumber = new Renumber();                    
                
                renumber.setInputFiles(selectedFiles);                            
                renumber.setCopy(false);               
                renumber.setSimulateOnly(renumberParams.isSimulateOnly());                                
                renumber.setOverwrite(renumberParams.isOverWrite());
                renumber.setStart(renumberParams.getStart());              
                int newEnd = renumberParams.getStart() + selectedFiles.size()*renumberParams.getStep() - 1;
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
                        
                        String message = Context.getMessage(
                            "RenamePanel.error."+errorType.toString()+".message",
                            new Object [] {
                                pair.getSource(),pair.getTarget(),errorStr
                            }
                        );
                        log(message);
                        
                        return renumberParams.getOnErrorAction();
                    }
                    @Override
                    public void onRenameSuccess(RenameFilePair pair,int index) {
                        numRenamedSuccess++;
                    }
                    @Override
                    public void onRenameEnd(RenameFilePair pair,int index) {                        
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
                        //report
                        String report = Context.getMessage("report.message",new Integer []{
                            numRenamedSuccess,numRenamedError
                        });
                        String reportLog = Context.getMessage("report.log");            
                        SwingUtils.ShowMessage(Context.getMessage("report.title"),report+"\n"+reportLog);
                        
                        simulateRenumberButton.setEnabled(true);
                        submitRenumberButton.setEnabled(true);
                        if(!renumberParams.isSimulateOnly()){
                            reloadFileTable();                            
                        }                        
                    }
                });                
                renumber.rename();                
            }catch(Exception e){
                String message = Context.getMessage("RenamePanel.unknownException.message",new Object [] {e.getMessage()});
                log(message);
                logger.error(e.getMessage());
            }
            
            return null;
        }
    }
    private class TaskRename extends DefaultWorker {
        private RenameParams renameParams;      
        public TaskRename(RenameParams renameParams){
            super();
            this.renameParams = renameParams;           
        }
        @Override
        protected Void doInBackground(){
            clearResultTable();
           
            setStatusError(null);
            
            numRenamedSuccess = 0;
            numRenamedError = 0;
                        
            try{

                final Renamer renamer = new Renamer();    
                
                renamer.setInputFiles(selectedFiles);
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
                            int answer = JOptionPane.showConfirmDialog(
                                SwingUtils.getFrame(),
                                Context.getMessage("RenamePanel.renameParamsForm.warning.overwrite")                                
                            );
                            approved = answer == JOptionPane.OK_OPTION;
                            renamer.setOverwrite(approved);
                            getRenameParamsForm().getValueModel("overWrite").setValue(approved);
                            getRenameParamsForm().commit();                            
                        }
                        return approved;
                    }
                    @Override
                    public ErrorAction onError(RenameFilePair pair, RenameError errorType,String errorStr,int index) {
                        numRenamedError++;
                        String message = Context.getMessage(
                            "RenamePanel.error."+errorType.toString()+".message",
                            new Object [] {
                                pair.getSource(),pair.getTarget(),errorStr
                            }
                        );
                        log(message);                        
                        return renameParams.getOnErrorAction();
                    }
                    @Override
                    public void onRenameSuccess(RenameFilePair pair,int index) {
                        numRenamedSuccess++;
                    }
                    @Override
                    public void onRenameEnd(RenameFilePair pair,int index) {                        
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
                        
                        //report
                        String report = Context.getMessage("report.message",new Integer []{
                            numRenamedSuccess,numRenamedError
                        });
                        String reportLog = Context.getMessage("report.log");            
                        SwingUtils.ShowMessage(Context.getMessage("report.title"),report+"\n"+reportLog);
                        
                        simulateRenameButton.setEnabled(true);
                        submitRenameButton.setEnabled(true);
                        if(!renameParams.isSimulateOnly()){
                            reloadFileTable();
                        }
                    }
                });
                renamer.rename();
            }catch(Exception e){
                String message = Context.getMessage("RenamePanel.unknownException.message",new Object [] {e.getMessage()});
                log(message);
                logger.error(e.getMessage());
            }
            
            return null;
        }       
    }
}

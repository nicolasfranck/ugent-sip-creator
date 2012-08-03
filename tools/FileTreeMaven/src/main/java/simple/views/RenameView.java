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
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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
public class RenameView extends AbstractView{
    private JPanel panelTreeTable;
    private JPanel panelModifier;
    private JPanel panelRenamer;
    private JPanel panelCleaner;
    private JPanel renameButtonPanel;
    private JPanel cleanButtonPanel;

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
    private CleanParams cleanParams;
    private CleanParamsForm cleanParamsForm;

    public JTable getResultTable(){
        if(resultTable == null){
            resultTable = new JTable(getResultTableModel());
            resultTable.setFillsViewportHeight(true);
            resultTable.setRowSelectionAllowed(false);
            RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(resultTableModel);
            resultTable.setRowSorter(sorter);
            /*
            resultTable.setDefaultRenderer(Object.class,new TableCellRenderer(){
                @Override
                public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSelected, boolean hasFocus, int row, int col) {
                    JLabel label = new JLabel();

                    if(row >= 0){
                        label.setText(o.toString());
                        System.out.println(o);
                        if(col == 3 && o.toString().compareTo("failed") == 0){
                            System.out.println("setting colors");
                            label.setForeground(Color.WHITE);
                            label.setBackground(Color.RED);
                        }
                    }

                    return label;
                }
            });
             * 
             */
            
        }
        return resultTable;
    }
    public void setResultTable(JTable resultTable) {
        this.resultTable = resultTable;
    }
    public DefaultTableModel getResultTableModel(){
        if(resultTableModel == null){
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
        }
        return resultTableModel;
    }
    public void setResultTableModel(DefaultTableModel resultTableModel) {
        this.resultTableModel = resultTableModel;
    }
    public JLabel getStatusLabel() {
        if(statusLabel == null)
            statusLabel = new JLabel();
        return statusLabel;
    }
    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }
    public JPanel getCleanButtonPanel() {
        if(cleanButtonPanel == null)
            cleanButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        return cleanButtonPanel;
    }
    public void setCleanButtonPanel(JPanel cleanButtonPanel) {
        this.cleanButtonPanel = cleanButtonPanel;
    }
    public JPanel getRenameButtonPanel() {
        if(renameButtonPanel == null)
            renameButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        return renameButtonPanel;
    }
    public void setRenameButtonPanel(JPanel renameButtonPanel) {
        this.renameButtonPanel = renameButtonPanel;
    }

    public RenameParams getRenameParams() {
        if(renameParams == null)renameParams = new RenameParams();
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

    public CleanParams getCleanParams() {
        if(cleanParams == null)
            cleanParams = new CleanParams();
        return cleanParams;
    }

    public void setCleanParams(CleanParams cleanParams) {
        this.cleanParams = cleanParams;
    }
    public CleanParamsForm getCleanParamsForm() {
        if(cleanParamsForm == null)
            cleanParamsForm = new CleanParamsForm(getCleanParams());
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

    @Override
    protected JComponent createControl() {
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
        panelModifier.add(chooseButton,c);

        //panel east: modifier: status label
        c.gridy += 1;
        c.fill = GridBagConstraints.HORIZONTAL;        
        panelModifier.add(getStatusLabel(),c);

        //panel east:tabs for renamer en cleaner
        JTabbedPane tabs = new JTabbedPane();

        //panel east: tabs: panelRenamer en panelCleaner
        panelRenamer = getNewRenamePanel();
        panelCleaner = getNewCleanerPanel();

        //panel east:tabs
        tabs.add("rename",panelRenamer);
        tabs.add("clean",panelCleaner);

        //panel east: add tabs
        c.gridy += 1;
        panelModifier.add(tabs,c);        
        
        //result tabel      

        c.gridy += 1;
        c.fill = GridBagConstraints.BOTH;
        JScrollPane scrollerResultTable = new JScrollPane(getResultTable());
        scrollerResultTable.setPreferredSize(new Dimension(500,200));
        panelModifier.add(scrollerResultTable,c);
        
        //add panel east to split pane
        splitter.add(panelModifier);
        
        splitter.setDividerLocation(0.7);
        splitter.setResizeWeight(0.7);        
        
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
                    getStatusLabel().setText("gelieve een map te selecteren");
                    tree.clearSelection();                    
                    setJComponentEnabled(getRenameButtonPanel(),false);
                    setJComponentEnabled(getCleanButtonPanel(),false);
                    setFormsEnabled(false);                    
                    return;
                }                
                setJComponentEnabled(getRenameButtonPanel(),!getRenameParamsForm().getFormModel().getHasErrors());
                setJComponentEnabled(getCleanButtonPanel(),!getCleanParamsForm().getFormModel().getHasErrors());
                setFormsEnabled(true);
                fileNodeChoosen = fn;
            }        
        });
        //per default wordt de root geselecteerd
        TreePath tp = new TreePath(tree.getModel().getRoot());
        tree.setSelectionPath(tp);        

        return treeTable;
    }
    protected void setFormsEnabled(boolean enabled){
        getRenameParamsForm().setEnabled(enabled);
        getCleanParamsForm().setEnabled(enabled);
    }
    protected void setJComponentEnabled(JComponent component,boolean enabled){
        component.setEnabled(enabled);        
        for(Component c:component.getComponents())
            c.setEnabled(enabled);
    }
    protected TreeTableModel getNewTreeTableModel(File file){
        treeTableModel =  new FileSystemModel(file);
        treeTableModel.addTreeModelListener(new TreeModelListener(){
            @Override
            public void treeNodesChanged(TreeModelEvent tme) {
                /*
                treeTable.getTree().collapsePath(tme.getTreePath());
                treeTable.getTree().expandPath(tme.getTreePath());
                 *
                 */
                //reloadTreeTable(getLastFile());
            }
            @Override
            public void treeNodesInserted(TreeModelEvent tme) {
                /*
                treeTable.getTree().collapsePath(tme.getTreePath());
                treeTable.getTree().expandPath(tme.getTreePath());*/
                //reloadTreeTable(getLastFile());
            }
            @Override
            public void treeNodesRemoved(TreeModelEvent tme) {
                /*
                treeTable.getTree().collapsePath(tme.getTreePath());
                treeTable.getTree().expandPath(tme.getTreePath());*/
                //reloadTreeTable(getLastFile());
            }
            @Override
            public void treeStructureChanged(TreeModelEvent tme) {
                //reloadTreeTable(getLastFile());
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
                cleanFormModel.commit();
                cleanSubmitButton.setEnabled(false);
                cleanSimulateButton.setEnabled(false);

                clean(true);
                
                cleanSubmitButton.setEnabled(true);
                cleanSimulateButton.setEnabled(true);
            }
        });

        cleanSubmitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                cleanFormModel.commit();
                cleanSubmitButton.setEnabled(false);
                cleanSimulateButton.setEnabled(false);

                clean(false);
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
    protected JPanel getNewRenamePanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 0.5;
        c.insets = new Insets(5,5,5,5);
        c.gridx = c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;

        //form
        RenameParamsForm renameForm = getRenameParamsForm();
        final ValidatingFormModel renameFormModel = renameForm.getFormModel();
        JComponent componentForm = renameForm.getControl();

        panel.add(componentForm,c);        
        
        //buttons
      
        final JButton submitButton = new JButton("ok");
        getRenameButtonPanel().add(submitButton);
        final JButton simulateButton = new JButton("simuleer");
        getRenameButtonPanel().add(simulateButton);

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
    protected void clean(final boolean simulateOnly){
        clearResultTable();
        getStatusLabel().setText(null);
        
        try{
            RenameWand renamer = new RenameWand();            
            renamer.setCopy(cleanParams.isCopy());
            renamer.setSimulateOnly(simulateOnly);
            renamer.setCleanCandidates(helper.FileUtils.listFiles(getLastFile()));
            renamer.setCleanDirectories(cleanParams.isCleanDirectories());
            renamer.setOverWrite(cleanParams.isOverWrite());


            renamer.setCleanListener(new CleanListenerAdapter(){
                @Override
                public void onInit(ArrayList<RenameFilePair>pairs){
                    getStatusLabel().setText(pairs.size()+" moeten opgekuist worden");
                }
                @Override
                public OnErrorAction onError(final RenameFilePair pair, RenameError errorType, String errorStr){
                    String status = simulateOnly ? "simulatie":"failed";
                    resultTableModel.insertRow(resultTableModel.getRowCount(),new Object []{
                        new Integer(resultTableModel.getRowCount()+1),
                        pair.getSource().getName(),
                        pair.getTarget().getName(),
                        status
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
                        new Integer(resultTableModel.getRowCount()+1),
                        pair.getSource().getName(),
                        pair.getTarget().getName(),
                        status
                    });
                }
                @Override
                public void onEnd(ArrayList<RenameFilePair>renamePairs,int numSuccess){
                    System.out.println("onEnd called:"+renamePairs.size()+", success:"+numSuccess);
                    System.out.println("is EDT:"+SwingUtilities.isEventDispatchThread());
                    System.out.println("renamePairs:"+renamePairs);
                    getStatusLabel().setText("totaal matches:"+renamePairs.size()+", aantal geslaagd: "+numSuccess);
                    RenameView.this.getStatusBar().getProgressMonitor().done();
                }
            });
            renamer.clean();
        }catch(Exception e){
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
    }
    protected void clearResultTable(){
        resultTableModel.getDataVector().clear();
        //belangrijk: ander weet de row-sorter van niets, wat resulteert in een nullpointer-exception
        resultTableModel.fireTableDataChanged();
        resultTable.setModel(resultTableModel);
    }
    protected void rename(final boolean simulateOnly){
        clearResultTable();
        getStatusLabel().setText(null);        
        numRenamedSuccess = 0;
        numRenamedError = 0;
       
        try{

            RenameWand renamer = new RenameWand();
            renamer.setCurrentDirectory(getLastFile());
            renamer.setRecurseIntoSubdirectories(renameParams.isRecurseIntoSubdirectories());
            renamer.setSourcePatternString(renameParams.getSourcePattern());
            renamer.setTargetPatternString(renameParams.getDestinationPattern());
            renamer.setCopy(renameParams.isCopy());
            renamer.setOnRenameOperationError(renameParams.getOnErrorAction());
            renamer.setSimulateOnly(simulateOnly);
            renamer.setMatchLowerCase(renameParams.isMatchLowerCase());
            renamer.setOverWrite(renameParams.isOverWrite());

            renamer.setRenameListener(new RenameListenerAdapter(){
                @Override
                public void onInit(final java.util.ArrayList<FileUnit> matchCandidates,final java.util.ArrayList<FileUnit> matches){

                     if(matchCandidates.size() == 0){
                         getStatusLabel().setText("geselecteerde map is leeg");
                     }else if(matches.size() == 0){
                         getStatusLabel().setText("geen bestanden gevonden overeenkomstig het bronpatroon");
                     }
                }
                @Override
                public boolean approveList(final java.util.ArrayList<FileUnit> list){
                    numToRename = list.size();
                    RenameView.this.getStatusBar().getProgressMonitor().taskStarted("renaming",numToRename);
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

                    RenameView.this.getStatusBar().getProgressMonitor().worked(numRenamedError + numRenamedSuccess);
                }
                @Override
                public void onEnd(ArrayList<RenameFilePair>renamePairs,int numSuccess){
                    getStatusLabel().setText("totaal matches:"+renamePairs.size()+"\naantal geslaagd: "+numSuccess);
                    RenameView.this.getStatusBar().getProgressMonitor().done();
                }
            });
            renamer.rename();
        }catch(Exception e){
            logger.debug(e.getMessage());
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
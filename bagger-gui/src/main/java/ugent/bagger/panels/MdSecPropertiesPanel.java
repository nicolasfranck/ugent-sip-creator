package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.ui.BagView;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.util.Assert;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.forms.MdSecForm;
import ugent.bagger.forms.MdWrapForm;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.tables.EditMdSecPropertiesTable;
import ugent.bagger.wizards.BagInfoImportWizard;
import ugent.bagger.wizards.BagInfoImportWizardDialog;
import ugent.bagger.wizards.CSVWizard;
import ugent.bagger.wizards.CSVWizardDialog;
import ugent.bagger.wizards.CrosswalkParamsDialog;
import ugent.bagger.wizards.CrosswalkParamsWizard;
import ugent.bagger.workers.TaskAddMdSecFromFile;

/**
 *
 * @author nicolas
 */
public class MdSecPropertiesPanel extends JPanel{
    static final Log log = LogFactory.getLog(MdSecPropertiesPanel.class);
    JComponent buttonPanel;
    EditMdSecPropertiesTable editDmdSecPropertiesTable;         
    protected ArrayList<MdSec>data; 
    MdSecForm mdSecForm;
    MdWrapForm mdWrapForm;
    boolean modusAdding = true;
    JButton updateButton;
    JButton removeButton;
    JButton addButton;        
    String id;
    ArrayList<MdSec>exceptions;
   
    public MdSecPropertiesPanel(final ArrayList<MdSec>data,String id){        
        this(data,id,new ArrayList<MdSec>());
    }
    public MdSecPropertiesPanel(final ArrayList<MdSec>data,String id,ArrayList<MdSec>exceptions){        
        Assert.notNull(data);
        this.data = data;         
        this.id = id;
        setLayout(new BorderLayout());
        setExceptions(exceptions);
        add(createContentPane());        
    }

    public JButton getRemoveButton() {
        if(removeButton == null){
            removeButton = new JButton(Context.getMessage("MdSecPanel.removeButton.label"));
        }
        return removeButton;
    }
    public JButton getAddButton() {
        if(addButton == null){
            addButton = new JButton(Context.getMessage("MdSecPanel.addButton.label"));   
        }
        return addButton;
    }
    public ArrayList<MdSec> getExceptions() {
        if(exceptions == null){
            exceptions = new ArrayList<MdSec>();
        }
        return exceptions;
    }    
    public void setExceptions(ArrayList<MdSec> exceptions) {
        this.exceptions = exceptions;        
    }   
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public MdSec newMdSec(){
        return new MdSec("");        
    }
    public MdSecForm getMdSecForm() {
        if(mdSecForm == null){
            mdSecForm = new MdSecForm(newMdSec());
            mdSecForm.addValidationListener(new ValidationListener() {
                @Override
                public void validationResultsChanged(ValidationResults results) {
                    if(modusAdding){
                        addButton.setEnabled(!results.getHasErrors());
                    }else{
                        updateButton.setEnabled(!results.getHasErrors());
                    }
                }
            });
        }
        return mdSecForm;
    }
    public MdWrapForm getMdWrapForm() {
        return mdWrapForm;
    }
    public void setMdWrapForm(MdWrapForm mdWrapForm) {
        this.mdWrapForm = mdWrapForm;
    }    
    public void setMdSecForm(MdSecForm mdSecForm) {
        this.mdSecForm = mdSecForm;
    }    
    public void reset(final ArrayList<MdSec>data){                       
        getEditDmdSecPropertiesTable().reset(data);
        this.data = data;       
        enableButtons(getMax() > 0 && data.size() >= getMax() ? false:true);
    }
    public JComponent getButtonPanel() {
        if(buttonPanel == null){
            buttonPanel = createButtonPanel();
        }
        return buttonPanel;
    }
    public void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }        
    public EditMdSecPropertiesTable getEditDmdSecPropertiesTable() {
        if(editDmdSecPropertiesTable == null){
            editDmdSecPropertiesTable = createMdSecPropertiesTable();
            editDmdSecPropertiesTable.addPropertyChangeListener("remove",new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent pce) {                    
                    MdSecPropertiesPanel.this.firePropertyChange("remove",pce.getOldValue(),pce.getNewValue());                    
                    if(getMax() > 0 && data.size() < getMax()){
                       enableButtons(true);
                    }
                }
            });
        }
        return editDmdSecPropertiesTable;
    }
    public EditMdSecPropertiesTable createMdSecPropertiesTable(){                        
        return new EditMdSecPropertiesTable(data,new String [] {"namespace","MDTYPE","rootName"},"mdSecTable",getExceptions());
    }
    public void setMdSecPropertiesTable(EditMdSecPropertiesTable editDmdSecPropertiesTable) {
        this.editDmdSecPropertiesTable = editDmdSecPropertiesTable;
    }  
    protected JComponent createContentPane() {
        JPanel panel = new JPanel(new BorderLayout());        
        panel.add(new JScrollPane(getEditDmdSecPropertiesTable().getControl()));        
        panel.add(getButtonPanel(),BorderLayout.NORTH);        
        return panel;
    }
    public JComponent createButtonPanel(){
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        boolean buttonsEnabled = getMax() > 0 && data.size() >= getMax() ? false:true;        
        
        //creatie buttons
        //addButton = new JButton(Context.getMessage("MdSecPanel.addButton.label"));                                
        //removeButton = new JButton(Context.getMessage("MdSecPanel.removeButton.label"));                  
        
        enableButtons(buttonsEnabled);
        
        JMenuItem addXMLMenuItem = new JMenuItem(Context.getMessage("addXMLMenuItem.label"));
        JMenuItem crosswalkMenuItem = new JMenuItem(Context.getMessage("crosswalkMenuItem.label"));
        
        final JPopupMenu addPopupMenu = new JPopupMenu();
        addPopupMenu.add(addXMLMenuItem);
        addPopupMenu.add(crosswalkMenuItem);        
        
        //action listeners
        getAddButton().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                addPopupMenu.show(getAddButton(),0,getAddButton().getHeight());
            }
        });
        crosswalkMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(getMax() > 0 && data.size() >= getMax()){
                    return;
                }
                
                CrosswalkParamsWizard wizard = new CrosswalkParamsWizard("CrosswalkParamsWizard");
                wizard.addPropertyChangeListener("mdSec",new PropertyChangeListener(){
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {                        
                        MdSec mdSec = (MdSec) pce.getNewValue();
                        getEditDmdSecPropertiesTable().add(mdSec);
                        getEditDmdSecPropertiesTable().refresh();
                        if(getMax() > 0 && data.size() >= getMax()){
                            enableButtons(false);
                        }
                        MdSecPropertiesPanel.this.firePropertyChange("crosswalkMdSec",null,mdSec);
                    }                    
                });
                CrosswalkParamsDialog wdialog = new CrosswalkParamsDialog(wizard);
                wdialog.setResizable(false);
                wdialog.showDialog();
                
                /*JDialog dialog = new XMLCrosswalkDialog(SwingUtils.getFrame(),true);  
                dialog.addPropertyChangeListener("mdSec",new PropertyChangeListener(){
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {                        
                        MdSec mdSec = (MdSec) pce.getNewValue();
                        getEditDmdSecPropertiesTable().add(mdSec);
                        getEditDmdSecPropertiesTable().refresh();
                        if(getMax() > 0 && data.size() >= getMax()){
                            enableButtons(false);
                        }
                        MdSecPropertiesPanel.this.firePropertyChange("crosswalkMdSec",null,mdSec);
                    }                    
                });
                
                dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);                
                SwingUtils.centerOnParent(dialog,true);
                dialog.setVisible(true);*/
            }
            
        });
        addXMLMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){    
                if(getMax() > 0 && data.size() >= getMax()){
                    return;
                }
                
                File [] files = SwingUtils.chooseFiles(
                    Context.getMessage("addXMLMenuItem.fileChooser.title"),
                    new FileExtensionFilter(
                        new String [] {"xml"},
                        Context.getMessage("addXMLMenuItem.fileFilter.label"),
                        true
                    ),
                    JFileChooser.FILES_ONLY,
                    true
                );
                if(files.length > 0){
                    monitor(new TaskAddMdSecFromFile(files));                
                }                
            }
        });
        getRemoveButton().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                getEditDmdSecPropertiesTable().deleteSelected();
                getEditDmdSecPropertiesTable().refresh();
                
                if(getMax() > 0 && data.size() < getMax()){
                    enableButtons(true);
                }                
            }        
        });        
        
        JMenuItem importCSVItem = new JMenuItem(Context.getMessage("importCSVItem.label"));
        JMenuItem importBagInfoItem = new JMenuItem(Context.getMessage("importBagInfoItem.label"));
        
        importCSVItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(getMax() > 0 && data.size() >= getMax()){
                    return;
                }
                
                CSVWizard wizard = new CSVWizard("CSVWizard");
                CSVWizardDialog dialog = new CSVWizardDialog(wizard);
                wizard.addPropertyChangeListener("addMdSec",new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {
                        MdSec mdSec = (MdSec) pce.getNewValue();
                        getEditDmdSecPropertiesTable().add(mdSec);
                    }
                });
                wizard.addPropertyChangeListener("doneMdSec",new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {                        
                        getEditDmdSecPropertiesTable().refresh();
                        
                        if(getMax() > 0 && data.size() >= getMax()){
                            enableButtons(false);
                        }
                    }
                });
                dialog.showDialog();
            }            
        });
        
        importBagInfoItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(getMax() > 0 && data.size() >= getMax()){
                    return;
                }
                
                BagInfoImportWizard wizard = new BagInfoImportWizard("BagInfoImportWizard");
                BagInfoImportWizardDialog dialog = new BagInfoImportWizardDialog(wizard);
                wizard.addPropertyChangeListener("addMdSec",new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {
                        MdSec mdSec = (MdSec) pce.getNewValue();
                        getEditDmdSecPropertiesTable().add(mdSec);
                    }
                });
                wizard.addPropertyChangeListener("doneMdSec",new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {                        
                        getEditDmdSecPropertiesTable().refresh();
                        if(getMax() > 0 && data.size() >= getMax()){
                            enableButtons(false);
                        }
                    }
                });
                dialog.showDialog();
            }            
        });        
        
        addPopupMenu.add(importCSVItem);
        addPopupMenu.add(importBagInfoItem);
        
        //voeg toe
        panel.add(getAddButton());                            
        panel.add(getRemoveButton()); 
        
        return panel;
    }
    protected void monitor(SwingWorker worker){
        SwingUtils.monitor(
            worker,
            Context.getMessage("MdSecPropertiesPanel.monitoring.title"),
            Context.getMessage("MdSecPropertiesPanel.monitoring.description"),
            getPropertyListeners()
        );        
    }   
    protected List<PropertyChangeListener> getPropertyListeners(){        
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {                
                final BagView bagView = BagView.getInstance();
                if(pce.getPropertyName().equals("state") && pce.getNewValue() == SwingWorker.StateValue.STARTED){                    
                    log.error(Context.getMessage("mdSecTable.addMdSec.start"));
                    //ApplicationContextUtil.addConsoleMessage(Context.getMessage("mdSecTable.addMdSec.start"));
                }else if(pce.getPropertyName().equals("log")){
                    log.error(pce.getNewValue().toString());
                    //ApplicationContextUtil.addConsoleMessage(pce.getNewValue().toString());                    
                }else if(pce.getPropertyName().equals("send")){
                    getEditDmdSecPropertiesTable().add((MdSec)pce.getNewValue());                    
                    if(getMax() > 0 && data.size() >= getMax()){
                        enableButtons(false);
                    }                    
                    MdSecPropertiesPanel.this.firePropertyChange("mdSec",null,pce.getNewValue());
                    
                }else if(
                    pce.getPropertyName().equals("report") && 
                    pce.getNewValue().toString().compareTo("success") == 0
                ){                    
                    getEditDmdSecPropertiesTable().refresh();
                }
            }
        };
        return Arrays.asList(new PropertyChangeListener [] {listener});        
    }
    protected int getMax(){
        return -1;
    }
    public void enableButtons(boolean enabled){
        getAddButton().setEnabled(enabled);              
        getRemoveButton().setEnabled(enabled);
    }
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        enableButtons(enabled);
    }
}
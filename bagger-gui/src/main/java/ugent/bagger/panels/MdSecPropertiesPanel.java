package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.BorderLayout;
import java.awt.Dialog;
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
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import ugent.bagger.dialogs.XMLCrosswalkDialog;
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
import ugent.bagger.workers.TaskAddMdSecFromFile;

/**
 *
 * @author nicolas
 */
public class MdSecPropertiesPanel extends JPanel{
    private JComponent buttonPanel;
    private EditMdSecPropertiesTable editDmdSecPropertiesTable;         
    private ArrayList<MdSec>data; 
    private MdSecForm mdSecForm;
    private MdWrapForm mdWrapForm;
    private boolean modusAdding = true;
    private JButton updateButton;
    private JButton removeButton;
    private JButton addButton;
    private JButton importButton;
    private JButton crosswalkButton;
    
    public MdSecPropertiesPanel(final ArrayList<MdSec>data){        
        assert(data != null);
        this.data = data;         
        setLayout(new BorderLayout());
        add(createContentPane());        
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
        }
        return editDmdSecPropertiesTable;
    }
    public EditMdSecPropertiesTable createMdSecPropertiesTable(){                        
        return new EditMdSecPropertiesTable(data,new String [] {"namespace","label","MDTYPE","OTHERMDTYPE"},"mdSecTable");
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
        
        //creatie buttons
        addButton = new JButton(Context.getMessage("MdSecPanel.addButton.label"));                
        importButton = new JButton(Context.getMessage("MdSecPanel.importButton.label"));       
        importButton.setToolTipText(Context.getMessage("MdSecPanel.importButton.toolTip"));
        removeButton = new JButton(Context.getMessage("MdSecPanel.removeButton.label"));          
        
        JMenuItem addXMLMenuItem = new JMenuItem(Context.getMessage("addXMLMenuItem.label"));
        JMenuItem crosswalkMenuItem = new JMenuItem(Context.getMessage("crosswalkMenuItem.label"));
        
        final JPopupMenu xmlPopupMenu = new JPopupMenu();
        xmlPopupMenu.add(addXMLMenuItem);
        xmlPopupMenu.add(crosswalkMenuItem);
        
        
        //action listeners
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                xmlPopupMenu.show(addButton,0,addButton.getHeight());
            }
        });
        crosswalkMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                JDialog dialog = new XMLCrosswalkDialog(SwingUtils.getFrame(),true);                
                
                dialog.addPropertyChangeListener("mdSec",new PropertyChangeListener(){
                    @Override
                    public void propertyChange(PropertyChangeEvent pce) {                        
                        MdSec mdSec = (MdSec) pce.getNewValue();
                        getEditDmdSecPropertiesTable().add(mdSec);
                        getEditDmdSecPropertiesTable().refresh();
                    }                    
                });
                
                dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);                
                dialog.setLocationRelativeTo(panel);
                dialog.pack();
                dialog.setVisible(true);
            }
            
        });
        addXMLMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){                                
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
                    monitor(new TaskAddMdSecFromFile(files),"inserting..");                
                }                
            }
        });
        removeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                getEditDmdSecPropertiesTable().deleteSelected();
                getEditDmdSecPropertiesTable().refresh();
            }        
        });        
        
        JMenuItem importCSVItem = new JMenuItem(Context.getMessage("importCSVItem.label"));
        JMenuItem importBagInfoItem = new JMenuItem(Context.getMessage("importBagInfoItem.label"));
        
        importCSVItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                CSVWizard wizard = new CSVWizard();
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
                    }
                });
                dialog.showDialog();
            }            
        });
        
        importBagInfoItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                BagInfoImportWizard wizard = new BagInfoImportWizard();
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
                    }
                });
                dialog.showDialog();
            }            
        });
        
        final JPopupMenu importPopupMenu = new JPopupMenu();
        importPopupMenu.add(importCSVItem);
        importPopupMenu.add(importBagInfoItem);                
        
        importButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                importPopupMenu.show(importButton,0,importButton.getHeight());
            }            
        });
        
        //voeg toe
        panel.add(addButton);                
        panel.add(importButton);         
        panel.add(removeButton); 
        
        return panel;
    }
    private void monitor(SwingWorker worker,String title){
        SwingUtils.monitor(worker,title,"working..",getPropertyListeners());        
    }   
    private List<PropertyChangeListener> getPropertyListeners(){        
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {                
                final BagView bagView = BagView.getInstance();
                if(pce.getPropertyName().equals("state") && pce.getNewValue() == SwingWorker.StateValue.STARTED){                    
                    ApplicationContextUtil.addConsoleMessage(Context.getMessage("mdSecTable.addMdSec.start"));
                }else if(pce.getPropertyName().equals("log")){
                    ApplicationContextUtil.addConsoleMessage(pce.getNewValue().toString());                    
                }else if(pce.getPropertyName().equals("send")){
                    getEditDmdSecPropertiesTable().add((MdSec)pce.getNewValue());
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
}
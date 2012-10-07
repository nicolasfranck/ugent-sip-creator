package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import ugent.bagger.dialogs.XMLTransformDialog;
import ugent.bagger.forms.MdSecForm;
import ugent.bagger.forms.MdWrapForm;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.tables.DmdSecPropertiesTable;
import ugent.bagger.tables.MdSecPropertiesTable;
import ugent.bagger.workers.TaskAddMdSecFromFile;
import ugent.bagger.workers.TaskAddMdSecFromImport;

/**
 *
 * @author nicolas
 */
public class MdSecPropertiesPanel extends JPanel{
    private JComponent buttonPanel;
    private MdSecPropertiesTable dmdSecPropertiesTable;         
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
        getDmdSecPropertiesTable().reset(data);
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
    public MdSecPropertiesTable getDmdSecPropertiesTable() {
        if(dmdSecPropertiesTable == null){
            dmdSecPropertiesTable = createMdSecPropertiesTable();
        }
        return dmdSecPropertiesTable;
    }
    public DmdSecPropertiesTable createMdSecPropertiesTable(){                        
        return new DmdSecPropertiesTable(data,new String [] {"namespace","label","MDTYPE","OTHERMDTYPE"},"mdSecTable");
    }
    public void setMdSecPropertiesTable(DmdSecPropertiesTable dmdSecTable) {
        this.dmdSecPropertiesTable = dmdSecTable;
    }  
    protected JComponent createContentPane() {
        JPanel panel = new JPanel(new BorderLayout());        
        panel.add(new JScrollPane(getDmdSecPropertiesTable().getControl()));        
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
        crosswalkButton = new JButton("crosswalk..");                
        
        //action listeners
        crosswalkButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                JDialog dialog = new XMLTransformDialog();                
                dialog.pack();
                dialog.setLocationRelativeTo(panel);
                dialog.setVisible(true);
            }
            
        });
        removeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                getDmdSecPropertiesTable().deleteSelectedMdSec();
                getDmdSecPropertiesTable().refresh();
            }        
        });
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){                                
                monitor(new TaskAddMdSecFromFile(),"importing..");                
            }
        });
        importButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                monitor(new TaskAddMdSecFromImport(),"importing..");
            }
        });
        
        //default niet ingeschakeld
        addButton.setEnabled(false);
        removeButton.setEnabled(false);
        importButton.setEnabled(false);
        crosswalkButton.setEnabled(false);
        
        //voeg toe
        panel.add(addButton);                
        panel.add(importButton); 
        panel.add(crosswalkButton);
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
                    ApplicationContextUtil.addConsoleMessage("adding new mdSec batch");
                }else if(pce.getPropertyName().equals("log")){
                    ApplicationContextUtil.addConsoleMessage(pce.getNewValue().toString());                    
                }else if(pce.getPropertyName().equals("send")){
                    getDmdSecPropertiesTable().addMdSec((MdSec)pce.getNewValue());
                }else if(
                    pce.getPropertyName().equals("report") && 
                    pce.getNewValue().toString().compareTo("success") == 0
                ){
                    getDmdSecPropertiesTable().refresh();
                }
            }
        };
        return Arrays.asList(new PropertyChangeListener [] {listener});        
    }    
}
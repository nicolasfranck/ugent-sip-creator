/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.panels;

import ugent.bagger.dialogs.TextViewDialog;
import ugent.bagger.forms.MdSecForm;
import ugent.bagger.forms.MdWrapForm;
import com.anearalone.mets.MdSec;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;

/**
 *
 * @author nicolas
 */
final public class EditMdSecPanel extends JPanel{
    private JComponent buttonPanel;
    private MdWrapForm mdWrapForm;
    private MdSecForm mdSecForm;
    private MdSec mdSec;    
    
    public EditMdSecPanel(final MdSec mdSec){        
        assert(mdSec != null);
        this.mdSec = mdSec;         
        setLayout(new BorderLayout());
        add(createContentPane());        
    }  
    public MdSec getMdSec() {
        return mdSec;
    }
    public void setMdSec(MdSec mdSec) {
        this.mdSec = mdSec;
    }
    public MdSecForm getMdSecForm(){
        if(mdSecForm == null){
            mdSecForm = new MdSecForm(getMdSec());
        }
        return mdSecForm;
    }
    public void setMdSecForm(MdSecForm mdSecForm) {
        this.mdSecForm = mdSecForm;
    }    
    public MdWrapForm getMdWrapForm() {
        if(mdWrapForm == null){
            mdWrapForm = new MdWrapForm(getMdSec().getMdWrap());
        }
        return mdWrapForm;
    }
    public void setMdWrapForm(MdWrapForm mdWrapForm) {
        this.mdWrapForm = mdWrapForm;
    }    
    public void reset(final MdSec mdSec){
        getMdWrapForm().setFormObject(getMdSec().getMdWrap());        
        this.mdSec = mdSec;        
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
    protected JComponent createContentPane() {
        JPanel panel = new JPanel(new BorderLayout());                        
        panel.add(getButtonPanel(),BorderLayout.NORTH);        
        panel.add(new JScrollPane(getMdSecForm().getControl()),BorderLayout.CENTER);
        panel.add(new JScrollPane(getMdWrapForm().getControl()),BorderLayout.SOUTH);
        return panel;
    }
    public JComponent createButtonPanel(){
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));                
        
        final JButton saveButton = new JButton(Context.getMessage("EditMdSecPanel.saveButton.label"));
        saveButton.setEnabled(true);
               
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(!getMdWrapForm().hasErrors()){
                    getMdWrapForm().commit();
                    saveButton.setEnabled(false);
                }
            }
        });
        
        JButton showButton = new JButton(Context.getMessage("EditMdSecPanel.showButton.label"));          
        showButton.setToolTipText(Context.getMessage("EditMdSecPanel.showButton.toolTip"));        
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(getMdSec().getMdWrap() != null && getMdSec().getMdWrap().getXmlData() != null && !getMdSec().getMdWrap().getXmlData().isEmpty()){                                                            
                    try{                        
                        JDialog dialog = new TextViewDialog(
                            SwingUtils.getFrame(),
                            new String [] {
                                ugent.bagger.helper.XML.NodeToXML(getMdSec().getMdWrap().getXmlData().get(0),true)
                        });
                        dialog.pack();                        
                        dialog.setVisible(true);    
                    }catch(ClassNotFoundException e){                        
                    }catch(InstantiationException e){
                    }catch(IllegalAccessException e){                        
                    }                    
                }
            }
        });       
        getMdWrapForm().addValidationListener(new ValidationListener(){
            @Override
            public void validationResultsChanged(ValidationResults results) {
                saveButton.setEnabled(!results.getHasErrors());
            }
        });        
        panel.add(saveButton);
        panel.add(showButton);        
        return panel;
    }     
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Panels;

import org.springframework.richclient.application.Application;
import Dialogs.TextViewDialog;
import Forms.MdWrapForm;
import com.anearalone.mets.MdSec;
import helper.Context;
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
public class EditMdSecPanel extends JPanel{
    private JComponent buttonPanel;
    private MdWrapForm mdWrapForm;
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
        panel.add(new JScrollPane(getMdWrapForm().getControl()));
        return panel;
    }
    public JComponent createButtonPanel(){
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));                
        JButton showButton = new JButton(Context.getMessage("EditMdSecPanel.showButton.label"));          
        showButton.setToolTipText(Context.getMessage("EditMdSecPanel.showButton.toolTip"));        
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae){
                if(getMdSec().getMdWrap() != null && getMdSec().getMdWrap().getXmlData() != null && !getMdSec().getMdWrap().getXmlData().isEmpty()){                                                            
                    try{                        
                        JDialog dialog = new TextViewDialog(null,new String [] {
                            helper.XML.NodeToXML(getMdSec().getMdWrap().getXmlData().get(0))
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
        getMdWrapForm().addValidationListener(new ValidationListener(){
            @Override
            public void validationResultsChanged(ValidationResults results) {
                saveButton.setEnabled(!results.getHasErrors());
            }
        });
        
        panel.add(showButton);
        panel.add(saveButton);
        return panel;
    }     
}

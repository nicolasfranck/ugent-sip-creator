package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.BagInfoField;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import ugent.bagger.helper.Beans;


public final class BagInfoForm extends AbstractForm {
    
    static final Log log = LogFactory.getLog(BagInfoForm.class);
    public static final String INFO_FORM_PAGE = "infoPage";
    JComponent focusField;    
    HashMap<String,ArrayList<String>> fieldMap;
    JComponent fieldsPanel;
    AddFieldPanel addFieldPanel;
    //LoadFieldsPanel loadFieldsPanel;
    JScrollPane fieldsScrollPane;
    JPanel contentPanel;
    final int FORM_WIDTH = 800;

    public JScrollPane getFieldsScrollPane() {
        if(fieldsScrollPane == null){
            fieldsScrollPane = new JScrollPane(getFieldsPanel());
            fieldsScrollPane.setOpaque(false);
            fieldsScrollPane.getViewport().setOpaque(false);
            fieldsScrollPane.setBorder(null);
            fieldsScrollPane.setWheelScrollingEnabled(true);
            Dimension dim = new Dimension(FORM_WIDTH,500);
            fieldsScrollPane.setPreferredSize(dim);
            fieldsScrollPane.setMaximumSize(dim);
            fieldsScrollPane.setAlignmentX(Container.LEFT_ALIGNMENT);
        }
        return fieldsScrollPane;
    }
    public void setFieldsScrollPane(JScrollPane fieldsScrollPane) {
        this.fieldsScrollPane = fieldsScrollPane;
    }    
    /*
    public LoadFieldsPanel getLoadFieldsPanel() {
        if(loadFieldsPanel == null){
            loadFieldsPanel = new LoadFieldsPanel();                        
            Dimension dim = new Dimension(FORM_WIDTH,35);
            loadFieldsPanel.setPreferredSize(dim);            
            loadFieldsPanel.setMaximumSize(dim);            
            loadFieldsPanel.setAlignmentX(Container.LEFT_ALIGNMENT);
        }
        return loadFieldsPanel;
    }
    public void setLoadFieldsPanel(LoadFieldsPanel loadFieldsPanel) {
        this.loadFieldsPanel = loadFieldsPanel;
    }*/
    public BagInfoForm(FormModel formModel,HashMap<String,ArrayList<String>> fieldMap, boolean enabled) {
    	super(formModel,INFO_FORM_PAGE);        
        setFieldMap(fieldMap);
    }   
    public HashMap<String,ArrayList<String>> getFieldMap() {                
        return fieldMap;
    }
    public void setFieldMap(HashMap<String,ArrayList<String>> fieldMap) {
        this.fieldMap = fieldMap;
    }    
    public BagView getBagView(){
        return BagView.getInstance();
    }
    public DefaultBag getBag(){
        return getBagView().getBag();
    }
    public JComponent getFieldsPanel() {
        if(fieldsPanel == null){
            fieldsPanel = createFormFields();                 
        }
        return fieldsPanel;
    }
    public void setFieldsPanel(JComponent fieldsPanel) {
        this.fieldsPanel = fieldsPanel;
    }   
    public AddFieldPanel getAddFieldPanel() {
        if(addFieldPanel == null){
            addFieldPanel = new AddFieldPanel();
            Dimension dim = new Dimension(FORM_WIDTH,35);
            addFieldPanel.setPreferredSize(dim);            
            addFieldPanel.setMaximumSize(dim);            
            addFieldPanel.setAlignmentX(Container.LEFT_ALIGNMENT);
        }
        return addFieldPanel;
    }
    public void setAddFieldPannel(AddFieldPanel addFieldPannel) {
        this.addFieldPanel = addFieldPannel;
    }    

    protected JPanel getContentPanel() {
        if(contentPanel == null){
            contentPanel = new JPanel();
            contentPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            BoxLayout layout = new BoxLayout(contentPanel,BoxLayout.Y_AXIS);       
            contentPanel.setLayout(layout);            
            //contentPanel.add(getLoadFieldsPanel());
            contentPanel.add(getAddFieldPanel());            
            contentPanel.add(getFieldsScrollPane());
            contentPanel.setAlignmentX(Container.LEFT_ALIGNMENT);            
        }
        return contentPanel;
    }    
    @Override
    protected JComponent createFormControl() {        
        return getContentPanel();
    }
    public void resetFields(){           
        getContentPanel().remove(getFieldsScrollPane());
        setFieldsScrollPane(null);
        setFieldsPanel(null);
        getContentPanel().add(getFieldsScrollPane());
        getContentPanel().revalidate();
    }
    protected JComponent createFormFields() {       
                
    	int rowCount = 0;
        ImageIcon requiredIcon = getBagView().getPropertyImage("bag.required.image");
        BagTableFormBuilder formBuilder = new BagTableFormBuilder(getBindingFactory(),requiredIcon);
        final int index = 2;
        
        formBuilder.row();
        
        ArrayList<String>baginfoReadonlyFields = new ArrayList<String>();
        try{
            baginfoReadonlyFields = (ArrayList<String>)Beans.getBean("baginfoReadonlyFields");
        }catch(Exception e){
            log.error(e.getMessage());
        }
        
        if(fieldMap != null && !fieldMap.isEmpty()){
            Set<String> keys = fieldMap.keySet();
            if (keys != null){
                for(Iterator<String> iter = keys.iterator();iter.hasNext();){
                    final String key = (String) iter.next();                    
                    final ArrayList<String>values = fieldMap.get(key);                 
                    
                    boolean isReadOnly = baginfoReadonlyFields.contains(key);
                    
                    for(int i = 0;i < values.size();i++){
                        final String value = values.get(i);
                        
                        formBuilder.row();
                        rowCount++;
                        ImageIcon imageIcon = getBagView().getPropertyImage("bag.delete.image");
                        
                        
                        JButton removeButton = new JButton(imageIcon);
                        Dimension dimension = removeButton.getPreferredSize();
                        dimension.width = imageIcon.getIconWidth();
                        removeButton.setMaximumSize(dimension);
                        removeButton.setOpaque(false);
                        removeButton.setBorderPainted(false);
                        removeButton.setContentAreaFilled(false);                        
                        
                        removeButton.addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent ae) {                                                                
                                if(fieldMap.containsKey(key)){                                    
                                    fieldMap.get(key).remove(value);
                                    if(fieldMap.get(key).isEmpty()){                                                                                
                                        getBag().removeBagInfoField(key);
                                    }
                                }                                
                                SwingUtilities.invokeLater(new Runnable(){
                                    @Override
                                    public void run() {
                                        resetFields();
                                    }                                    
                                });
                            }                            
                        });
                        removeButton.setEnabled(!isReadOnly);
                        
                        int componentType = BagInfoField.TEXTFIELD_COMPONENT;
                        if(value.length() > 50){
                            componentType = BagInfoField.TEXTAREA_COMPONENT;
                        }
                        
                        final int thisIndex = i;
                        switch(componentType){
                            case BagInfoField.TEXTAREA_COMPONENT:
                                JComponent[] tlist = formBuilder.addTextArea(key,false,key,removeButton, "");
                                JComponent textarea = tlist[index];
                                textarea.setEnabled(!isReadOnly);
                                NoTabTextArea area = (NoTabTextArea)textarea;
                                area.setText(value);
                                area.setBorder(new EmptyBorder(1,1,1,1));
                                area.setLineWrap(true);                                
                                if(rowCount == 1){
                                    focusField = textarea;
                                }
                                if(!isReadOnly){
                                    area.getDocument().addDocumentListener(new DocumentListener(){

                                        @Override
                                        public void insertUpdate(DocumentEvent de) {
                                            Document doc = de.getDocument();
                                            try{                                                
                                                values.set(thisIndex,doc.getText(0,doc.getLength()));
                                            }catch(Exception e){}
                                            
                                        }

                                        @Override
                                        public void removeUpdate(DocumentEvent de) {
                                            Document doc = de.getDocument();
                                            try{                                                
                                                values.set(thisIndex,doc.getText(0,doc.getLength()));
                                            }catch(Exception e){}
                                            
                                        }

                                        @Override
                                        public void changedUpdate(DocumentEvent de) {
                                            
                                        }
                                    
                                    });
                                }
                                break;
                            case BagInfoField.TEXTFIELD_COMPONENT:
                                JComponent[] flist = formBuilder.add(key,false,key,removeButton,"");
                                JComponent comp = flist[index];
                                comp.setEnabled(!isReadOnly);                                
                                ((JTextField) comp).setText(value);
                                if(rowCount == 1){
                                    focusField = comp;
                                }
                                if(!isReadOnly){
                                    JTextField field = (JTextField)comp;
                                    field.getDocument().addDocumentListener(new DocumentListener(){

                                        @Override
                                        public void insertUpdate(DocumentEvent de) {
                                            Document doc = de.getDocument();
                                            try{                                                
                                                values.set(thisIndex,doc.getText(0,doc.getLength()));
                                            }catch(Exception e){}
                                            
                                        }

                                        @Override
                                        public void removeUpdate(DocumentEvent de) {
                                            Document doc = de.getDocument();
                                            try{                                                
                                                values.set(thisIndex,doc.getText(0,doc.getLength()));
                                            }catch(Exception e){}
                                            
                                        }

                                        @Override
                                        public void changedUpdate(DocumentEvent de) {
                                            
                                        }
                                    
                                    });
                                    
                                }
                                break;
                        }  
                        
                    }
                   
                }
                if(focusField != null){
                    focusField.requestFocus();
                }
            }
        }
        return formBuilder.getForm();
    }      
    Component[] getFieldComponents() {
       return getFieldsPanel().getComponents();
    }
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getAddFieldPanel().setEnabled(enabled);
        //getLoadFieldsPanel().setEnabled(enabled);
    }
}
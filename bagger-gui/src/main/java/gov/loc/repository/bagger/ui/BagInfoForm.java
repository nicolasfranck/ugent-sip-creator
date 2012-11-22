package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.BagInfoField;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.util.LayoutUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;


public final class BagInfoForm extends AbstractForm {
    private static final long serialVersionUID = -3231249644435262577L;
    public static final String INFO_FORM_PAGE = "infoPage";
    private JComponent focusField;    
    private HashMap<String,ArrayList<String>> fieldMap;
    private JComponent form;
    private AddFieldPanel addFieldPannel;
    private LoadFieldsPanel loadFieldsPanel;

    public LoadFieldsPanel getLoadFieldsPanel() {
        if(loadFieldsPanel == null){
            loadFieldsPanel = new LoadFieldsPanel();
        }
        return loadFieldsPanel;
    }
    public void setLoadFieldsPanel(LoadFieldsPanel loadFieldsPanel) {
        this.loadFieldsPanel = loadFieldsPanel;
    }
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
    public JComponent getForm() {
        if(form == null){
            form = createFormFields();
        }
        return form;
    }
    public void setForm(JComponent form) {
        this.form = form;
    }   

    public AddFieldPanel getAddFieldPannel() {
        if(addFieldPannel == null){
            addFieldPannel = new AddFieldPanel();
        }
        return addFieldPannel;
    }
    public void setAddFieldPannel(AddFieldPanel addFieldPannel) {
        this.addFieldPannel = addFieldPannel;
    }    
    @Override
    protected JComponent createFormControl() {
        
        Set<String>keys = fieldMap.keySet();
        for(String key:keys){
            for(String value:fieldMap.get(key)){
                System.out.println("key: "+key+", value: "+value);
            }
        }
    	
    	JPanel contentPanel = new JPanel(new GridBagLayout());
    	int row = 0;
    	int col = 0;
        GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(col, row++,1,1,0,0,GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
       
        //load fields panel
        contentPanel.add(getLoadFieldsPanel(),gbc);
        
        // add field panel    	
        gbc = LayoutUtil.buildGridBagConstraints(col, row++, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
    	contentPanel.add(getAddFieldPannel(),gbc);   	
    	gbc = LayoutUtil.buildGridBagConstraints(col, row++, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
    	contentPanel.add(new JSeparator(),gbc);    	
        
    	// bag-info input form    	
    	gbc = LayoutUtil.buildGridBagConstraints(col,row++,1,1,1,1,GridBagConstraints.BOTH, GridBagConstraints.WEST);
    	contentPanel.add(getForm(),gbc);
        
    	return contentPanel;
    }
    protected JComponent createFormFields() {
    	int rowCount = 0;
        ImageIcon requiredIcon = getBagView().getPropertyImage("bag.required.image");
        BagTableFormBuilder formBuilder = new BagTableFormBuilder(getBindingFactory(),requiredIcon);
        int index = 2;

        formBuilder.row();
        if (fieldMap != null && !fieldMap.isEmpty()){
            Set<String> keys = fieldMap.keySet();
            if (keys != null){
                for(Iterator<String> iter = keys.iterator();iter.hasNext();){
                    final String key = (String) iter.next();                    
                    ArrayList<String>values = fieldMap.get(key);                 
                    
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
                                        fieldMap.remove(key);
                                    }
                                }
                                getBagView().getInfoFormsPane().updateInfoFormsPane(true);
                            }                            
                        });
                        
                        int componentType = BagInfoField.TEXTFIELD_COMPONENT;
                        if(value.length() > 30){
                            componentType = BagInfoField.TEXTAREA_COMPONENT;
                        }
                        switch(componentType){
                            case BagInfoField.TEXTAREA_COMPONENT:
                                JComponent[] tlist = formBuilder.addTextArea(key,false,key,removeButton, "");
                                JComponent textarea = tlist[index];
                                textarea.setEnabled(true);                             
                                ((NoTabTextArea) textarea).setText(value);
                                ((NoTabTextArea) textarea).setBorder(new EmptyBorder(1,1,1,1));
                                ((NoTabTextArea) textarea).setLineWrap(true);
                                if (rowCount == 1) {
                                    focusField = textarea;
                                }
                                break;
                            case BagInfoField.TEXTFIELD_COMPONENT:
                                JComponent[] flist = formBuilder.add(key,false,key,removeButton,"");
                                JComponent comp = flist[index];
                                comp.setEnabled(true);                                
                                ((JTextField) comp).setText(value);
                                if (rowCount == 1) {
                                    focusField = comp;
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
    
    public HashMap<String,ArrayList<String>> getBagInfoMap() {
        HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
        String key = "";
        String value = "";
        Component[] components = getFieldComponents();
        for(int i=0; i<components.length; i++){
            Component c;
            c = components[i];
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                key = label.getText();
            }
            i++;
            // Is required component
            c = components[i];
            i++;
            c = components[i];
            if (c instanceof JTextField) {
                JTextField tf = (JTextField) c;
                value = tf.getText();
            }else if (c instanceof JTextArea) {
                JTextArea ta = (JTextArea) c;
                value = ta.getText();
            } else if (c instanceof JComboBox) {
                JComboBox tb = (JComboBox) c;
                value = (String) tb.getSelectedItem();
            }
            
            if(!map.containsKey(key)){
                map.put(key,new ArrayList<String>());
            }
            map.get(key).add(value);
            
            i++;
            c = components[i];
        }
        return map;
    }
    private Component[] getFieldComponents() {
       return getForm().getComponents();
    }
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getAddFieldPannel().setEnabled(enabled);
    }
}
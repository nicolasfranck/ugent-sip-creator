package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.BagInfoField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.Beans;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;

public final class AddFieldPanel extends JPanel {
    
    static final Log log = LogFactory.getLog(AddFieldPanel.class);
    JCheckBox standardCheckBox;
    JComboBox standardFieldsComboBox;
    JTextField customFieldTextField;
    JTextField valueField;
    ArrayList<String>standardFields;   
    ArrayList<String>baginfoReadonlyFields;

    public ArrayList<String> getBaginfoReadonlyFields() {
        if(baginfoReadonlyFields == null){            
            try{
                baginfoReadonlyFields = (ArrayList<String>)Beans.getBean("baginfoReadonlyFields");
            }catch(Exception e){
                log.error(e.getMessage());
            }
            if(baginfoReadonlyFields == null){
                baginfoReadonlyFields = new ArrayList<String>();
            }            
        }
        return baginfoReadonlyFields;
    }
    public ArrayList<String> getStandardFields() {
        if(standardFields == null){
            standardFields = retrieveStandardFields();
        }
        return standardFields;
    }
    public void setStandardFields(ArrayList<String> standardFields) {
        this.standardFields = standardFields;
    }    
    public BagView getBagView(){
        return BagView.getInstance();
    }
    public JCheckBox getStandardCheckBox() {
        if(standardCheckBox == null){
            standardCheckBox = new JCheckBox(Context.getMessage("baginfo.isStandardField.label"));
            standardCheckBox.setSelected(true);
            standardCheckBox.addActionListener(new StandardFieldCheckBoxAction());
        }
        return standardCheckBox;
    }

    public void setStandardCheckBox(JCheckBox standardCheckBox) {
        this.standardCheckBox = standardCheckBox;
    }

    public JComboBox getStandardFieldsComboBox() {
        if(standardFieldsComboBox == null){
            List<String> listModel = getStandardFields();              
            standardFieldsComboBox = new JComboBox(listModel.toArray());            
            standardFieldsComboBox.setName(Context.getMessage("baginfo.field.fieldlist"));
            standardFieldsComboBox.setSelectedItem("");
            standardFieldsComboBox.setToolTipText(Context.getMessage("baginfo.field.fieldlist.help"));
        }
        return standardFieldsComboBox;
    }

    public void setStandardFieldsComboBox(JComboBox standardFieldsComboBox) {
        this.standardFieldsComboBox = standardFieldsComboBox;
    }

    public JTextField getCustomFieldTextField() {
        if(customFieldTextField == null){
            //customFieldTextField = new JTextField(15);
            customFieldTextField = new JTextField();            
            customFieldTextField.setToolTipText(Context.getMessage("baginfo.field.name.help"));
            customFieldTextField.setVisible(false);
            Dimension pdim = getStandardFieldsComboBox().getPreferredSize();
            customFieldTextField.setPreferredSize(new Dimension((int)pdim.getWidth(),(int)pdim.getHeight()));
        }
        return customFieldTextField;
    }

    public void setCustomFieldTextField(JTextField customFieldTextField) {
        this.customFieldTextField = customFieldTextField;
    }

    public JTextField getValueField() {
        if(valueField == null){
            valueField = new JTextField("",15);
        }
        return valueField;
    }

    public void setValueField(JTextField valueField) {
        this.valueField = valueField;
    }  
    
	
    public AddFieldPanel() {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));        
        
        setLayout(new FlowLayout(FlowLayout.LEFT));
        
        add(getStandardCheckBox());
        add(getStandardFieldsComboBox());
        add(getCustomFieldTextField());
        add(new JLabel(" : "));
        add(getValueField());
        
        JButton addFieldButton = new JButton(Context.getMessage("baginfo.addField.label"));        
        add(addFieldButton);
        addFieldButton.addActionListener(new AddFieldAction());        
        
        getValueField().addActionListener(new AddFieldAction());        
    }
	
	
    class StandardFieldCheckBoxAction extends AbstractAction {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox checkbox = (JCheckBox) e.getSource();
            boolean standardFieldSelected = checkbox.isSelected();
            if(standardFieldSelected) {
                getCustomFieldTextField().setVisible(false);
                getStandardFieldsComboBox().setVisible(true);                
            }else{
                getStandardFieldsComboBox().setVisible(false);
                getCustomFieldTextField().setVisible(true);                
            }
        }
    }
	
    ArrayList<String> retrieveStandardFields() {
        ArrayList<String> list = (ArrayList<String>) Beans.getBean("baginfoStandardFields");
        if(list == null){
            list = new ArrayList<String>();
        } 
        Collections.sort(list);
        return list;
    }	
	
    @Override
    public void setEnabled(boolean enabled) {       
        super.setEnabled(enabled);
        for(Component component:getComponents()){
            component.setEnabled(enabled);
        }       
    }
	
	
    class AddFieldAction extends AbstractAction {        

        @Override
        public void actionPerformed(ActionEvent e) {            
            BagInfoField field = createBagInfoField();
            if(field != null) {
                getValueField().setText("");
                getBagView().getBag().addField(field);                                
                getBagView().getInfoFormsPane().getInfoInputPane().getBagInfoForm().resetFields();
            }
        }
    }
    
	
    BagInfoField createBagInfoField() {
        BagView bagView = BagView.getInstance();
       
        BagInfoField field = new BagInfoField();

        String fieldName;
        if (isStandardField()) {
            fieldName = (String)getStandardFieldsComboBox().getSelectedItem();
        } else {
            fieldName = getCustomFieldTextField().getText();
        }

        String dialogTitle = Context.getMessage("baginfo.newFieldDialog.title");
        fieldName = fieldName.trim();
        if(fieldName.isEmpty()){                       
            SwingUtils.ShowError(dialogTitle,Context.getMessage("baginfo.newFieldDialog.error.fieldRequired"));            
            return null;
        }else if(!fieldName.matches("^[a-zA-Z0-9_\\-]+$")){
            SwingUtils.ShowError(dialogTitle,Context.getMessage("baginfo.newFieldDialog.error.fieldInvalid"));            
            return null;
        }else if(getValueField().getText() == null || getValueField().getText().trim().isEmpty()){                     
            SwingUtils.ShowError(dialogTitle,Context.getMessage("baginfo.newFieldDialog.error.valueRequired"));
            return null;
        }else if(getBaginfoReadonlyFields().contains(fieldName)){
            SwingUtils.ShowError(dialogTitle,Context.getMessage("baginfo.newFieldDialog.error.fieldReadonly",new Object []{fieldName}));
            return null;
        }

        field.setName(fieldName);
        field.setLabel(fieldName);        
        field.setValue(getValueField().getText());        
        field.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);
		
    	return field;
    }
    boolean isStandardField() {
        return getStandardCheckBox().isSelected();
    }	        
}

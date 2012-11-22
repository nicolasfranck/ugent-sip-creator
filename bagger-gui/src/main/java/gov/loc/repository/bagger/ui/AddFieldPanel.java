package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.BagInfoField;
import gov.loc.repository.bagger.bag.impl.DefaultBagInfo;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagger.ui.util.LayoutUtil;
import gov.loc.repository.bagit.impl.BagInfoTxtImpl;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
import ugent.bagger.helper.Context;

public final class AddFieldPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(AddFieldPanel.class);
    private JCheckBox standardCheckBox;
    private JComboBox standardFieldsComboBox;
    private JTextField customFieldTextField;
    private JTextField valueField;

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
            List<String> listModel = getStandardBagFields();        
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
            customFieldTextField = new JTextField(17);
            customFieldTextField.setToolTipText(Context.getMessage("baginfo.field.name.help"));
            customFieldTextField.setVisible(false);
        }
        return customFieldTextField;
    }

    public void setCustomFieldTextField(JTextField customFieldTextField) {
        this.customFieldTextField = customFieldTextField;
    }

    public JTextField getValueField() {
        if(valueField == null){
            valueField = new JTextField();
        }
        return valueField;
    }

    public void setValueField(JTextField valueField) {
        this.valueField = valueField;
    }  
    
	
    public AddFieldPanel() {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setLayout(new GridBagLayout());
        int row = 0;
        int col = 0;

        // standard field checkbox        
        GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        add(getStandardCheckBox(), gbc);

        // standard field dropdown menu        
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        add(getStandardFieldsComboBox(), gbc);

        // custom field name        
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        add(getCustomFieldTextField(), gbc);

        // field value
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        add(new JLabel(" : "), gbc);
        
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        add(getValueField(), gbc);

        // add field button
        JButton addFieldButton = new JButton(Context.getMessage("baginfo.addField.label"));
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        add(addFieldButton, gbc);
        addFieldButton.addActionListener(new AddFieldAction());

        
        getValueField().addActionListener(new AddFieldAction());        
    }
	
	
    private class StandardFieldCheckBoxAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
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
	
    private List<String> getStandardBagFields() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("");
        
        // Standard Fields from BagInfoTxt
        // TODO fix it when BIL has the functionality
        Field[] fields = BagInfoTxtImpl.class.getFields();
        for(Field field : fields){
            if(Modifier.isStatic(field.getModifiers()) && field.getName().startsWith("FIELD_")){
                try {
                    String standardFieldName = (String) field.get(null);
                    // Removes BagInfo.txt default values from the Standard item drop down list on the Bag-Info tab.
                    // This would prevent the BagInfo.txt values generated by Bagger to be overwritten
                    if (                    
                        ((standardFieldName.compareTo("Bag-Size") != 0) && (standardFieldName.compareTo("Payload-Oxum") != 0))
                    ) {
                        log.debug("adding standard field: " + standardFieldName);
                        list.add(standardFieldName);
                    }
                }catch(Exception e){
                    log.error("Failed to get value for static field "+field.getName());
                }
            }
        }
        return list;
    }
	
	
    @Override
    public void setEnabled(boolean enabled) {       
        for(Component component:getComponents()){
            component.setEnabled(enabled);
        }       
    }
	
	
    private class AddFieldAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            
            BagInfoField field = createBagInfoField();
            if(field != null) {
                getBagView().getBag().addField(field);
                // TODO use observer pattern
                //verwijdert de volledige infoFormsPane, en hertekent alles
                //nadeel: wijzingen in metsTab zijn weg!
                getBagView().getInfoFormsPane().updateInfoFormsPane(true);                
            }
        }
    }
    
	
    private BagInfoField createBagInfoField() {
        BagView bagView = ApplicationContextUtil.getBagView();
       
        BagInfoField field = new BagInfoField();

        String fieldName;
        if (isStandardField()) {
            fieldName = (String)getStandardFieldsComboBox().getSelectedItem();
        } else {
            fieldName = getCustomFieldTextField().getText();
        }

        String dialogTitle = Context.getMessage("baginfo.newFieldDialog.title");
        if (fieldName.trim().isEmpty()) {
            
            bagView.showWarningErrorDialog(
                dialogTitle,
                Context.getMessage("baginfo.newFieldDialog.error.fieldRequired")
            );
            
            return null;
        }else if(getValueField().getText() == null || getValueField().getText().trim().isEmpty()){
            bagView.showWarningErrorDialog(
                dialogTitle,
                Context.getMessage("baginfo.newFieldDialog.error.valueRequired")
            );          
            return null;
        }

        field.setName(fieldName);
        field.setLabel(fieldName);        
        field.setValue(getValueField().getText());        
        field.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);
		
    	return field;
    }
    private boolean isStandardField() {
        return getStandardCheckBox().isSelected();
    }	    
}

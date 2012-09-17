package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.BagInfoField;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.util.LayoutUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;

public final class BagInfoForm extends AbstractForm implements FocusListener {
    private static final long serialVersionUID = -3231249644435262577L;
    public static final String INFO_FORM_PAGE = "infoPage";
    private JComponent focusField;    
    private HashMap<String, BagInfoField> fieldMap;
    private JComponent form;
    private AddFieldPanel addFieldPannel;
	
    public BagInfoForm(FormModel formModel,HashMap<String, BagInfoField> fieldMap, boolean enabled) {
    	super(formModel, INFO_FORM_PAGE);        
        setFieldMap(fieldMap);
    }   
    public HashMap<String, BagInfoField> getFieldMap() {        
        return fieldMap;
    }
    public void setFieldMap(HashMap<String, BagInfoField> fieldMap) {
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
    	// add field panel
    	JPanel contentPanel = new JPanel(new GridBagLayout());
    	int row = 0;
    	int col = 0;
    	GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(col, row++, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);    	
    	contentPanel.add(getAddFieldPannel(), gbc);    	
    	gbc = LayoutUtil.buildGridBagConstraints(col, row++, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
    	contentPanel.add(new JSeparator(), gbc);    	
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
                    String key = (String) iter.next();
                    BagInfoField field = fieldMap.get(key);
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
                    removeButton.addActionListener(new RemoveFieldHandler());
                    logger.debug("OrganizationInfoForm add: " + field);
                    if(field.getValue() != null && field.getValue().length() > 30){
                        field.setComponentType(BagInfoField.TEXTAREA_COMPONENT);
                    }
                    if (field.isRequired()) {
                        removeButton = new JButton();
                        removeButton.setOpaque(false);
                        removeButton.setBorderPainted(false);
                        removeButton.setContentAreaFilled(false);
                    }
                    switch (field.getComponentType()){
                        case BagInfoField.TEXTAREA_COMPONENT:
                            JComponent[] tlist = formBuilder.addTextArea(field.getName(), field.isRequired(), field.getLabel(), removeButton, "");
                            JComponent textarea = tlist[index];
                            textarea.setEnabled(field.isEnabled());
                            textarea.addFocusListener(this);
                            ((NoTabTextArea) textarea).setText(field.getValue());
                            ((NoTabTextArea) textarea).setBorder(new EmptyBorder(1,1,1,1));
                            ((NoTabTextArea) textarea).setLineWrap(true);
                            if (rowCount == 1) {
                                focusField = textarea;
                            }
                            break;
                        case BagInfoField.TEXTFIELD_COMPONENT:
                            JComponent[] flist = formBuilder.add(field.getName(), field.isRequired(), field.getLabel(), removeButton, "");
                            JComponent comp = flist[index];
                            comp.setEnabled(field.isEnabled());
                            comp.addFocusListener(this);
                            ((JTextField) comp).setText(field.getValue());
                            if (rowCount == 1) {
                                focusField = comp;
                            }
                            break;
                        case BagInfoField.LIST_COMPONENT:
                            List<String> elements = field.getElements();
                            JComponent[] llist = formBuilder.addList(field.getName(), field.isRequired(), field.getLabel(), elements, field.getValue(), removeButton, "");
                            JComponent lcomp = llist[index];
                            lcomp.setEnabled(field.isEnabled());
                            lcomp.addFocusListener(this);
                            if(field.getValue() != null) {
                                ((JComboBox) lcomp).setSelectedItem(field.getValue().trim());
                            }
                            if (rowCount == 1) {
                                focusField = lcomp;
                            }
                            break;                            
                    }
                }
                if (focusField != null) {
                    focusField.requestFocus();
                }
            }
        }
        return formBuilder.getForm();                
    }
    @Override
    public void focusGained(FocusEvent evt){ 
        System.out.println("BagInfoForm::focusGained");
    }    
    @Override
    public void focusLost(FocusEvent evt) {       
        System.out.println("BagInfoForm::focusLost");           	
    	getBagView().getInfoInputPane().getUpdateBagHandler().updateBag();        
        //Nicolas Franck: waarom?
	//getBagView().getInfoInputPane().bagInfoInputPane.setSelectedIndex(0);        
    }
    private class RemoveFieldHandler extends AbstractAction {
       	private static final long serialVersionUID = 1L;
        @Override
    	public void actionPerformed(ActionEvent e) {
            try {
                Component selected = (Component) e.getSource();
                String key = "";
                Component[] components = getFieldComponents();
                for (int i=0; i<components.length; i++) {
                    Component c;
                    // See BagTableFormBuilder.addBinding for component info
                    // Field label
                    c = components[i];
                    if (c instanceof JLabel) {
                        JLabel label = (JLabel) c;
                        key = label.getText();
                    }
                    i++;
                    // Required button
                    c = components[i];
                    i++;
                    // Input text field
                    c = components[i];
                    i++;
                    // Remove button
                    c = components[i];
                    if (c instanceof JButton) {
                        if (c == selected) {
                            BagInfoField field = getField(key);
                            if (field != null) {
                                // remove field
                                getBag().removeBagInfoField(key);
                            }
                        }
                    }
                }
                getBagView().getInfoInputPane().updateInfoFormsPane(true);
            }catch (Exception ex){
                ex.printStackTrace();
            }
       	}
    }
    private BagInfoField getField(String key) {
    	BagInfoField field = null;
        Set<String> keys = fieldMap.keySet();
        if (keys != null) {
            for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
                String keySet = (String) iter.next();
                if (keySet.equalsIgnoreCase(key)) {
                    field = fieldMap.get(key);
                    return field;
                }
            }
        }
    	return field;
    }    
    public HashMap<String,String> getBagInfoMap() {
        HashMap<String,String> map = new HashMap<String,String>();
        String key = "";
        String value = "";
        java.awt.Component[] components = getFieldComponents();
        for (int i=0; i<components.length; i++) {
            java.awt.Component c;
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
            map.put(key, value);
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
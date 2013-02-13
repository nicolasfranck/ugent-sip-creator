package gov.loc.repository.bagger.bag;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class BagInfoField {    
    static final Log log = LogFactory.getLog(BagInfoField.class);
    public static final int TEXTFIELD_COMPONENT = 1;
    public static final int TEXTAREA_COMPONENT = 2;
    public static final int LIST_COMPONENT = 3;
    public static final String TEXTFIELD_CODE = "TF";
    public static final String TEXTAREA_CODE = "TA";
    public static final int MAX_VALUE = 32;
    String name = "";
    String label = "";
    String value = "";
    List<String> elements = new ArrayList<String>();
    int componentType = TEXTFIELD_COMPONENT;
    boolean isEnabled = true;
    boolean isRequired = false;
    boolean isRequiredvalue = false;
    boolean isEditable = true;
    //boolean isProfile = false;

    public BagInfoField() {
    	
    }    

    public void setName(String n) {
    	this.name = n;
    }

    public String getName() {
    	return this.name;
    }
    
    public void setLabel(String l) {
    	this.label = l;
    }
    
    public String getLabel() {
    	return this.label;
    }
    
    public void setValue(String v) {
    	this.value = v;
    }
    
    public String getValue() {
    	return this.value;
    }
    
    public void setElements(List<String> e) {
    	this.elements = e;
    }
    
    public List<String> getElements() {
    	return elements;
    }
        
    public void setComponentType(int type) {
    	this.componentType = type;
    }

    public int getComponentType() {
    	return this.componentType;
    }
    
    public void isEditable(boolean b) {
    	this.isEditable = b;
    }
    
    public boolean isEditable() {
    	return this.isEditable;
    }
    
    public void isEnabled(boolean b) {
    	this.isEnabled = b;
    }

    public boolean isEnabled() {
    	return this.isEnabled;
    }

    public void isRequired(boolean b) {
    	this.isRequired = b;
    }

    public boolean isRequired() {
    	return this.isRequired;
    }
        
    public void isRequiredvalue(boolean b) {
    	this.isRequiredvalue = b;
    }

    public boolean isRequiredvalue() {
    	return this.isRequiredvalue;
    }
    /*
    public void isProfile(boolean b) {
    	this.isProfile = b;
    }    
    public boolean isProfile() {
    	return this.isProfile;
    }*/    
    public void buildElements(List<String> elements){
        this.elements = elements;
    }   
   
    /*
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n");
    	sb.append("Label: ").append(getLabel()).append("\n");
    	sb.append("Name: ").append(getName()).append("\n");
    	sb.append("Value: ").append(getValue()).append("\n");
    	sb.append("Type: ").append(getComponentType()).append("\n");
    	sb.append("Elements: ").append(getElements()).append("\n");
    	sb.append("isRequired: ").append(isRequired()).append("\n");
    	sb.append("isRequiredvalue: ").append(isRequiredvalue()).append("\n");
    	sb.append("isEnabled: ").append(isEnabled()).append("\n");
    	sb.append("isEditable: ").append(isEditable()).append("\n");
    	return sb.toString();
    }*/
}
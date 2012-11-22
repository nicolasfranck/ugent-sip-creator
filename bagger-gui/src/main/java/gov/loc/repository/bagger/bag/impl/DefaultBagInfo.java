package gov.loc.repository.bagger.bag.impl;

import gov.loc.repository.bagger.bag.BagInfoField;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagInfoTxt;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultBagInfo {    
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(DefaultBagInfo.class);
   
    private HashMap<String,ArrayList<String>> fieldMap = new HashMap<String,ArrayList<String>>();

    public DefaultBagInfo(Bag bag) {
        log.debug("DefaultBagInfo");
    }    
    public HashMap<String,ArrayList<String>> getFieldMap() {
        return fieldMap;
    }
    public void addField(BagInfoField field){ 
        if(!fieldMap.containsKey(field.getName())){
            fieldMap.put(field.getName(),new ArrayList<String>());
        }
        fieldMap.get(field.getName()).add(field.getValue());
    }
    public void update(BagInfoTxt bagInfoTxt) {    
        
        updateBagInfoFieldMapFromBilBag(bagInfoTxt);
       
        for (String key : bagInfoTxt.keySet()){ 
            for(String value:bagInfoTxt.getList(key)){
                BagInfoField infoField = new BagInfoField();
                infoField.setLabel(key);
                infoField.setName(key);
                infoField.setValue(value);
                infoField.isEditable(true);
                infoField.isEnabled(true);
                addField(infoField);            
            }
        }		
    }	
    private void updateBagInfoFieldMapFromBilBag(BagInfoTxt bagInfoTxt) {
        if (fieldMap != null) {
            Set<String> keys = fieldMap.keySet();
            for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
                String label = (String) iter.next();
                ArrayList<String>value = fieldMap.get(label);
                ArrayList<String> values = (ArrayList<String>) bagInfoTxt.getList(label);
                fieldMap.put(label,values);
            }
        }
    }	
    
    public void clearFields() {
        fieldMap.clear();        
    }
    public void removeField(String key) {
        fieldMap.remove(key);
    }
   
    public void prepareBilBagInfo(BagInfoTxt bagInfoTxt) {
        bagInfoTxt.clear();
        for (Map.Entry<String,ArrayList<String>> entry : fieldMap.entrySet()) {
            bagInfoTxt.putList(entry.getKey(),entry.getValue());
        }        
    }   
    public void update(Map<String,ArrayList<String>> map) {
        
        Set<String> keys = map.keySet();
        for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            ArrayList<String> value = (ArrayList<String>) map.get(key);                        
            fieldMap.put(key,value);                            
        }
    }	
}
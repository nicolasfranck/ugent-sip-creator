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
        updateBagInfoFieldMapFromBilBag(bag.getBagInfoTxt());
    }    
    public HashMap<String,ArrayList<String>> getFieldMap() {
        return fieldMap;
    }
    //voeg los toe aan fieldMap
    public void addField(BagInfoField field){ 
        if(!fieldMap.containsKey(field.getName())){
            fieldMap.put(field.getName(),new ArrayList<String>());
        }
        fieldMap.get(field.getName()).add(field.getValue());
    }
    public void clearFields() {
        fieldMap.clear();        
    }
    public void removeField(String key) {
        fieldMap.remove(key);
    }
    //fieldMap aanpassen op basis van baginfotxt (start)
    public void updateBagInfoFieldMapFromBilBag(BagInfoTxt bagInfoTxt) {
        if(fieldMap != null){  
            fieldMap.clear();
            Set<String> keys = bagInfoTxt.keySet();
            for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
                String key = (String) iter.next();                
                ArrayList<String>values = (ArrayList<String>) bagInfoTxt.getList(key);
                fieldMap.put(key,values);
            }
        }
    }
    //baginfotxt aanpassen op basis van fieldMap (einde)
    public void prepareBilBagInfo(BagInfoTxt bagInfoTxt) {
        bagInfoTxt.clear();
        for(Map.Entry<String,ArrayList<String>> entry : fieldMap.entrySet()) {            
            bagInfoTxt.putList(entry.getKey(),entry.getValue());
        }        
    } 
    //fieldMap aanpassen op basis van andere map (tijdens)
    public void update(Map<String,ArrayList<String>>map) {
        fieldMap.clear();
        Set<String> keys = map.keySet();
        for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            ArrayList<String> values = (ArrayList<String>) map.get(key);                        
            fieldMap.put(key,values);                            
        }
    }	
}
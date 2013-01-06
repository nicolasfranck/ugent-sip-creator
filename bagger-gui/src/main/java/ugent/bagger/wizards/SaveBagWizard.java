package ugent.bagger.wizards;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.wizard.AbstractWizard;

/**
 *
 * @author nicolas
 */
public class SaveBagWizard extends AbstractWizard {    
    static final Log log = LogFactory.getLog(SaveBagWizard.class);
    SaveBagWizardPage1 saveBagWizardPage1;    
    protected HashMap<String,ArrayList<PropertyChangeListener>>propertyChangeListeners = new HashMap<String,ArrayList<PropertyChangeListener>>();    

    public SaveBagWizard(String wizardId){
        super(wizardId);
    }

    public SaveBagWizardPage1 getSaveBagWizardPage1() {
        if(saveBagWizardPage1 == null){
            saveBagWizardPage1 = new SaveBagWizardPage1("SaveBagWizardPage1");
        }
        return saveBagWizardPage1;
    }    
    @Override
    public void addPages(){     
        addPage(getSaveBagWizardPage1());           
    }
    public void addPropertyChangeListener(String key,PropertyChangeListener l){        
        if(!propertyChangeListeners.containsKey(key)){
            propertyChangeListeners.put(key,new ArrayList<PropertyChangeListener>());
        }
        propertyChangeListeners.get(key).add(l);       
    }
    public void firePropertyChange(String key,Object oldValue,Object newValue){
        if(propertyChangeListeners.containsKey(key)){
            ArrayList<PropertyChangeListener>list = propertyChangeListeners.get(key);
            for(PropertyChangeListener l:list){
                l.propertyChange(new PropertyChangeEvent(this,key,newValue,newValue));               
            }            
        }
    }    
    @Override
    protected boolean onFinish(){
        getSaveBagWizardPage1().getSaveBagPanel().save();
        return true;
    }
} 
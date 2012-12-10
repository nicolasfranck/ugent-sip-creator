package ugent.bagger.wizards;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.richclient.wizard.AbstractWizard;

/**
 *
 * @author nicolas
 */
public class ExportWizard extends AbstractWizard {    
    ExportWizardPage1 exportWizardPage1;    
    ExportWizardPage2 exportWizardPage2;
    HashMap<String,ArrayList<PropertyChangeListener>>propertyChangeListeners = new HashMap<String,ArrayList<PropertyChangeListener>>();

    public ExportWizard(String wizardId){
        super(wizardId);
    }
    public ExportWizardPage1 getExportWizardPage1() {
        if(exportWizardPage1 == null){
            exportWizardPage1 = new ExportWizardPage1("ExportWizardPage1");
        }
        return exportWizardPage1;
    }

    public ExportWizardPage2 getExportWizardPage2() {
        if(exportWizardPage2 == null){
            exportWizardPage2 = new ExportWizardPage2("ExportWizardPage2");
        }
        return exportWizardPage2;
    }    
    
    @Override
    public void addPages(){     
        addPage(getExportWizardPage1());     
        addPage(getExportWizardPage2());
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
    protected boolean onFinish() {        
        return true;
    }
} 
package ugent.bagger.wizards;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.ui.BagView;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.progress.BusyIndicator;
import org.springframework.richclient.wizard.AbstractWizard;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Document;
import ugent.bagger.helper.CSVUtils;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.CSVParseParams;
import ugent.bagger.params.VelocityTemplate;

/**
 *
 * @author nicolas
 */
public class CSVWizard extends AbstractWizard {    
    static Log log = LogFactory.getLog(CSVWizard.class);
    CSVWizardPage1 csv1WizardPage;
    CSVWizardPage2 csv2WizardPage;
    HashMap<String,ArrayList<PropertyChangeListener>>propertyChangeListeners = new HashMap<String,ArrayList<PropertyChangeListener>>();

    public CSVWizard(String wizardId){
        super(wizardId);
    }

    public CSVWizardPage1 getCsv1WizardPage() {
        if(csv1WizardPage == null){
            csv1WizardPage = new CSVWizardPage1("CSVWizardPage1");        
        }
        return csv1WizardPage;
    }    

    public CSVWizardPage2 getCsv2WizardPage() {
        if(csv2WizardPage == null){
            csv2WizardPage = new CSVWizardPage2("CSVWizardPage2");
        }
        return csv2WizardPage;
    }
         
    
    @Override
    public void addPages(){
        
        final HashMap<String,ArrayList<String>>fieldMap = BagView.getInstance().getInfoFormsPane().getInfoInputPane().getBagInfoForm().getFieldMap();        
        getCsv1WizardPage().getCsv1Panel().addPropertyChangeListener("record",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                HashMap<String,String>defaultMap = CSVUtils.createDefaultMap(fieldMap);
                
                HashMap<String,String>newRecord = (HashMap<String,String>) pce.getNewValue();
                HashMap<String,String>record = getCsv2WizardPage().getCsv2Panel().getRecord();                
                record.clear();                        
                
                //set defaults
                for(Entry<String,String>entry:defaultMap.entrySet()){                 
                    record.put(entry.getKey(),entry.getValue());
                }
                //set new values
                for(Entry<String,String>entry:newRecord.entrySet()){                    
                    record.put(entry.getKey(),entry.getValue());
                }                
            }
        });                
        
        addPage(getCsv1WizardPage());
        addPage(getCsv2WizardPage());       
    }

    @Override
    protected boolean onFinish() {        
       
        try{                        
            final VelocityTemplate vt = (VelocityTemplate) getCsv2WizardPage().getCsv2Panel().getTemplateComboBox().getSelectedItem();
            final CSVParseParams csvParseParams = getCsv1WizardPage().getCsv1Panel().getCsvParseParams();
            final File file = csvParseParams.getFiles().size() > 0 ? csvParseParams.getFiles().get(0) : null;
            
            if(file != null){
                Runnable runnable = new Runnable(){
                    @Override
                    public void run() {   
                        try{
                            CsvPreference csvPreference = CSVUtils.createCSVPreference(
                                csvParseParams.getQuoteChar(),
                                csvParseParams.getDelimiterChar(),
                                csvParseParams.getEndOfLineSymbols(),
                                csvParseParams.isSurroundingSpacesNeedQuotes()
                            ); 
                            
                            ICsvMapReader mapReader = new CsvMapReader(new FileReader(file),csvPreference);                       
                            final String [] cols = mapReader.getHeader(true); 
                            
                            HashMap<String,ArrayList<String>>fieldMap = BagView.getInstance().getInfoFormsPane().getInfoInputPane().getBagInfoForm().getFieldMap();
                            HashMap<String,String>defaultMap = CSVUtils.createDefaultMap(fieldMap);
                            HashMap<String,String>map;
                            while((map = (HashMap<String,String>) mapReader.read(cols)) != null){                                
                                //stel default in waar nog niet ingevuld
                                Set<String>keys = defaultMap.keySet();
                                for(String key:keys){
                                    if(!map.containsKey(key)){
                                        String value = defaultMap.get(key);                                    
                                        map.put(key,value);
                                    }
                                }
                                
                                Document document = CSVUtils.templateToDocument(vt,map);
                                MdSec mdSec = MetsUtils.createMdSec(document,false);
                                firePropertyChange("addMdSec",mdSec,mdSec);                                
                            }
                            firePropertyChange("doneMdSec",null,null);                            
                        }catch(Exception e){ 
                            log.error(e.getMessage());
                        }                        
                    }                    
                };                
                
                BusyIndicator.showWhile(SwingUtils.getFrame(),runnable);
                
            }            
            
        }catch(Exception e){
            log.error(e.getMessage());
        }
        
        return true;
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

} 
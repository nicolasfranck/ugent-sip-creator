package ugent.bagger.wizards;

import gov.loc.repository.bagger.ui.BagView;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map.Entry;
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
import ugent.bagger.tables.EditMdSecPropertiesTable;

/**
 *
 * @author nicolas
 */
public class CSVWizard extends AbstractWizard{    
    CSVWizardPage1 csv1WizardPage;
    CSVWizardPage2 csv2WizardPage;
         
    @Override
    public void addPages(){        
        
        csv1WizardPage = new CSVWizardPage1("page1");        
        csv2WizardPage = new CSVWizardPage2("page1");                        
        csv1WizardPage.getCsv1Panel().addPropertyChangeListener("record",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                HashMap<String,String>newRecord = (HashMap<String,String>) pce.getNewValue();
                HashMap<String,String>record = csv2WizardPage.getCsv2Panel().getRecord();
                
                record.clear();
                        
                for(Entry<String,String>entry:newRecord.entrySet()){                    
                    record.put(entry.getKey(),entry.getValue());
                }                
            }
        });                
        
        addPage(csv1WizardPage);
        addPage(csv2WizardPage);       
    }

    @Override
    protected boolean onFinish() {
        
       
        try{            
            final EditMdSecPropertiesTable table = BagView.getInstance().getInfoFormsPane().getInfoInputPane().getMetsPanel().getDmdSecPropertiesPanel().getEditDmdSecPropertiesTable();                        
            final VelocityTemplate vt = (VelocityTemplate) csv2WizardPage.getCsv2Panel().getTemplateComboBox().getSelectedItem();
            final CSVParseParams csvParseParams = csv1WizardPage.getCsv1Panel().getCsvParseParams();
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

                            HashMap<String,String>map;
                            while((map = (HashMap) mapReader.read(cols)) != null){
                                Document document = CSVUtils.templateToDocument(vt,map);
                                table.addMdSec(MetsUtils.createMdSec(document));
                            }

                            table.refresh();
                        }catch(Exception e){                           
                            e.printStackTrace();
                        }                        
                    }                    
                };                
                
                BusyIndicator.showWhile(SwingUtils.getFrame(),runnable);
                
            }            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return true;
    }
} 
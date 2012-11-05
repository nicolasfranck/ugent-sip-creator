package ugent.bagger.wizards;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map.Entry;
import org.springframework.richclient.wizard.AbstractWizard;

/**
 *
 * @author nicolas
 */
public class CSVWizard extends AbstractWizard{
         
    @Override
    public void addPages(){
  
        CSVWizardPage1 csv1WizardPage = new CSVWizardPage1("page1");        
        final CSVWizardPage2 csv2WizardPage = new CSVWizardPage2("page1");        
        
        
        csv1WizardPage.getCsv1Panel().addPropertyChangeListener("record",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                HashMap<String,String>newRecord = (HashMap<String,String>) pce.getNewValue();
                HashMap<String,String>record = csv2WizardPage.getCsv2Panel().getRecord();
                record.clear();
                        
                for(Entry<String,String>entry:newRecord.entrySet()){
                    System.out.println("key: "+entry.getKey()+", value: "+entry.getValue());
                    record.put(entry.getKey(),entry.getValue());
                }
            }
        });
        
        addPage(csv2WizardPage);
        addPage(csv1WizardPage);
        
    }

    @Override
    protected boolean onFinish() {
        return true;
    }
}

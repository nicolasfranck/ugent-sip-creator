package ugent.bagger.wizards;

import com.anearalone.mets.MdSec;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.wizard.AbstractWizard;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.NameValueUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.BagInfoImportParams;
import ugent.bagger.params.VelocityTemplate;
import ugent.bagger.workers.DefaultWorker;
/**
 *
 * @author nicolas
 */
public class BagInfoImportWizard extends AbstractWizard{    
    static final Log log = LogFactory.getLog(BagInfoImportWizard.class);
    BagInfoImportWizardPage1 bagInfoImportWizardPage1;
    HashMap<String,ArrayList<PropertyChangeListener>>propertyChangeListeners = new HashMap<String,ArrayList<PropertyChangeListener>>();
    
    public BagInfoImportWizard(String wizardId){
        super(wizardId);
    }     
    @Override
    public void addPages(){ 
        bagInfoImportWizardPage1 = new BagInfoImportWizardPage1("BagInfoImportWizardPage1");
        addPage(bagInfoImportWizardPage1);
    }
    @Override
    protected boolean onFinish(){                
        SwingUtils.monitor(
            new BagInfoImporterWorker(),
            Context.getMessage("BagInfoImportWizard.monitoring.title"),
            Context.getMessage("BagInfoImportWizard.monitoring.description")
        );
        return true;
    }    
    
    class BagInfoImporterWorker extends DefaultWorker {
        @Override
        protected Void doInBackground() throws Exception {
            
            bagInfoImportWizardPage1.getBagInfoImportForm().commit();
            BagInfoImportParams bagInfoImportParams = bagInfoImportWizardPage1.getBagInfoImportParams();
            
            VelocityTemplate template = bagInfoImportParams.getTemplate(); 
            InputStream templateIS = Context.getResourceAsStream(template.getPath());

            if(template != null){
                ArrayList<File>files = bagInfoImportParams.getFiles();
                int numSuccess = 0;
                int numErrors = 0;
                
                for(int i = 0;i < files.size();i++){
                    File file = files.get(i);
                    try{                   
                        HashMap<String,ArrayList<String>>map = NameValueUtils.readMap(file);
                        Document doc = NameValueUtils.templateToDocument(template,map);
                        MdSec mdSec = MetsUtils.createMdSec(doc,false);                        
                        BagInfoImportWizard.this.firePropertyChange("addMdSec",mdSec,mdSec);
                        numSuccess++;
                    }catch(NoNamespaceException e){
                        numErrors++;
                        log.error(e.getMessage());
                        SwingUtils.ShowError(
                            null,
                            Context.getMessage("bagInfoImportWizard.NoNamespaceException",new Object [] {file,e.getMessage()})
                        );                        
                    }catch(IllegalNamespaceException e){
                        numErrors++;
                        log.error(e.getMessage());
                        SwingUtils.ShowError(
                            null,
                            Context.getMessage("bagInfoImportWizard.IllegalNamespaceException",new Object [] {file,e.getNamespace()})
                        );                        
                    }catch(MalformedURLException e){
                        numErrors++;
                        log.error(e.getMessage());
                        SwingUtils.ShowError(
                            null,
                            Context.getMessage("bagInfoImportWizard.MalformedURLException",new Object [] {file,e.getMessage()})
                        );                       
                    }catch(SAXException e){
                        numErrors++;
                        log.error(e.getMessage());
                        SwingUtils.ShowError(
                            null,
                            Context.getMessage("bagInfoImportWizard.SAXException",new Object [] {file,e.getMessage()})
                        );                        
                    }catch(IOException e){
                        numErrors++;
                        log.error(e.getMessage());
                        SwingUtils.ShowError(
                            null,
                            Context.getMessage("bagInfoImportWizard.IOException",new Object [] {file,e.getMessage()})
                        );                        
                    }catch(TransformerException e){
                        numErrors++;
                        log.error(e.getMessage());
                        SwingUtils.ShowError(
                            null,
                            Context.getMessage("bagInfoImportWizard.TransformerException",new Object [] {file,e.getMessage()})
                        );                        
                    }catch(ParserConfigurationException e){
                        numErrors++;
                        log.error(e.getMessage());
                        SwingUtils.ShowError(
                            null,
                            Context.getMessage("bagInfoImportWizard.ParserConfigurationException",new Object [] {file,e.getMessage()})
                        );                        
                    }catch(Exception e){
                        numErrors++;
                        log.error(e.getMessage());
                        SwingUtils.ShowError(
                            null,
                            Context.getMessage("bagInfoImportWizard.Exception",new Object [] {file,e.getMessage()})
                        );                        
                    }     
                    
                    int percent = (int)Math.floor(
                        (i+1) / ((float)files.size()) * 100
                    );                                                                        
                    setProgress(percent);    
                    
                }

                //report
                String report = Context.getMessage("report.message",new Integer []{
                    files.size(),numSuccess,numErrors
                });
                String reportLog = Context.getMessage("report.log");
                SwingUtils.ShowMessage(null,report+"\n"+reportLog);
                
                BagInfoImportWizard.this.firePropertyChange("doneMdSec",null,null);
            }else{
                //geen importer gevonden
                SwingUtils.ShowError(null,Context.getMessage("bagInfoImportWizard.noImporterFound"));
            }
            return null;
        }
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

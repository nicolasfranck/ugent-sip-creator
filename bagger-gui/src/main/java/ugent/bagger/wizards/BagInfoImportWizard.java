package ugent.bagger.wizards;

import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.springframework.richclient.wizard.AbstractWizard;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.importers.Importer;
import ugent.bagger.importers.NameValueToDCImporter;
import ugent.bagger.importers.NameValueToOAIDCImporter;
import ugent.bagger.params.BagInfoImportParams;
import ugent.bagger.tables.EditMdSecPropertiesTable;
import ugent.bagger.workers.DefaultWorker;
/**
 *
 * @author nicolas
 */
public class BagInfoImportWizard extends AbstractWizard{    
    private BagInfoImportWizardPage1 bagInfoImportWizardPage1;
    
    public void log(String message){
        ApplicationContextUtil.addConsoleMessage(message+"\n");
    }  
    @Override
    public void addPages(){ 
        bagInfoImportWizardPage1 = new BagInfoImportWizardPage1("page1");
        addPage(bagInfoImportWizardPage1);
    }
    @Override
    protected boolean onFinish(){                
        SwingUtils.monitor(new BagInfoImporterWorker(),"","%s");
        return true;
    }    
    
    private class BagInfoImporterWorker extends DefaultWorker {
        @Override
        protected Void doInBackground() throws Exception {
            final EditMdSecPropertiesTable table = BagView.getInstance().getInfoFormsPane().getInfoInputPane().getMetsPanel().getDmdSecPropertiesPanel().getEditDmdSecPropertiesTable();                        
            bagInfoImportWizardPage1.getBagInfoImportForm().commit();
            BagInfoImportParams bagInfoImportParams = bagInfoImportWizardPage1.getBagInfoImportParams();
            
            Importer importer = Class.forName(
                MetsUtils.getBagInfoImporters().get(bagInfoImportParams.getBagInfoConverter())                
            ).asSubclass(Importer.class).newInstance();                        

            if(importer != null){
                ArrayList<File>files = bagInfoImportParams.getFiles();
                int numSuccess = 0;
                int numErrors = 0;
                
                for(int i = 0;i < files.size();i++){
                    File file = files.get(i);
                    Document doc = importer.performImport(file);
                    try{
                        table.addMdSec(MetsUtils.createMdSec(doc,false));
                        numSuccess++;
                    }catch(NoNamespaceException e){
                        numErrors++;
                        log(Context.getMessage("bagInfoImportWizard.NoNamespaceException",new Object [] {file,e.getMessage()}));
                    }catch(IllegalNamespaceException e){
                        numErrors++;
                        log(Context.getMessage("bagInfoImportWizard.IllegalNamespaceException",new Object [] {file,e.getNamespace()}));
                    }catch(MalformedURLException e){
                        numErrors++;
                        log(Context.getMessage("bagInfoImportWizard.MalformedURLException",new Object [] {file,e.getMessage()}));
                    }catch(SAXException e){
                        numErrors++;
                        log(Context.getMessage("bagInfoImportWizard.SAXException",new Object [] {file,e.getMessage()}));
                    }catch(IOException e){
                        numErrors++;
                        log(Context.getMessage("bagInfoImportWizard.IOException",new Object [] {file,e.getMessage()}));
                    }catch(TransformerException e){
                        numErrors++;
                        log(Context.getMessage("bagInfoImportWizard.TransformerException",new Object [] {file,e.getMessage()}));
                    }catch(ParserConfigurationException e){
                        numErrors++;
                        log(Context.getMessage("bagInfoImportWizard.ParserConfigurationException",new Object [] {file,e.getMessage()}));
                    }catch(Exception e){
                        numErrors++;
                        log(Context.getMessage("bagInfoImportWizard.Exception",new Object [] {file,e.getMessage()}));
                    }     
                    
                    int percent = (int)Math.floor(
                        (i+1) / ((float)files.size()) * 100
                    );                                                                        
                    setProgress(percent);    
                    
                }

                //report
                String report = Context.getMessage("report.message",new Integer []{
                    numSuccess,numErrors
                });
                String reportLog = Context.getMessage("report.log");
                SwingUtils.ShowMessage(null,report+"\n"+reportLog);

                table.refresh();
            }else{
                //geen importer gevonden
                SwingUtils.ShowError(null,Context.getMessage("bagInfoImportWizard.noImporterFound"));
            }
            return null;
        }
    }
}

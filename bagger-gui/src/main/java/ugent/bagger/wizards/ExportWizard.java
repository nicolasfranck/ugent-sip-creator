package ugent.bagger.wizards;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.InfoInputPane;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.richclient.progress.BusyIndicator;
import org.springframework.richclient.wizard.AbstractWizard;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.BagitMetsValidationException;
import ugent.bagger.exporters.Exporter;
import ugent.bagger.exporters.MdSecFilter;
import ugent.bagger.forms.ExportParamsForm;
import ugent.bagger.helper.ArrayUtils;
import ugent.bagger.helper.Beans;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.panels.ExportParamsPanel;
import ugent.bagger.params.ExportParams;

/**
 *
 * @author nicolas
 */
public class ExportWizard extends AbstractWizard {    
    static final Log log = LogFactory.getLog(ExportWizard.class);
    ExportWizardPage1 exportWizardPage1;        
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
    @Override
    public void addPages(){     
        addPage(getExportWizardPage1());           
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
        
        InfoInputPane infoInputPane = BagView.getInstance().getInfoFormsPane().getInfoInputPane();
        final Mets mets = infoInputPane.getMets();
        final MetsBag metsBag = infoInputPane.getMetsBag();        
        ExportParamsPanel exportParamsPanel = getExportWizardPage1().getExportParamsPanel();
        final ExportParams exportParams = exportParamsPanel.getExportParams();               
        final ExportParamsForm exportParamsForm = exportParamsPanel.getExportParamsForm();
        
        exportParamsForm.addValidationListener(new ValidationListener() {
            @Override
            public void validationResultsChanged(ValidationResults results) {
                System.out.println("results: ");
                for(Object m:results.getMessages()){
                    System.out.println("message: "+m);
                }
         
            }
        });
        
        
        if(exportParamsForm.hasErrors()){
            System.out.println("errors found: ");            
            return false;
        }
        exportParamsForm.commit();
        
        HashMap<String,HashMap<String,Object>>exportersConfig = null;
        try{
            exportersConfig = (HashMap<String,HashMap<String,Object>>) Beans.getBean("exporters");
            System.out.println("exportersConfig: "+exportersConfig);
            System.out.println("format: "+exportParams.getFormat());
            HashMap<String,Object>econfig = exportersConfig.get(exportParams.getFormat());                
            
            System.out.println("econfig: "+econfig);
            
            /*
             * analyse  -> zit de noodzakelijke metadata er tussen? 
             *          -> 'false' betekent niet noodzakelijkerwijze dat er een probleem is.
             *              Zo aanvaardt DSpace altijd mods, maar sommige servers van DSPace
             *              kunnen geconfigureerd zijn om ook andere metadata-schema's te
             *              aanvaarden.
             *              Dus daarom verder op eigen risico (confirm-dialog)
             */
            if(econfig.containsKey("metadataFilter")){
                MdSecFilter mdSecFilter = (MdSecFilter) econfig.get("metadataFilter");                
                ArrayList<MdSec>filteredMdSec = mdSecFilter.filter((ArrayList<MdSec>) mets.getDmdSec());
                if(filteredMdSec.isEmpty()){
                    boolean proceed = SwingUtils.confirm(                        
                        Context.getMessage("ExportWizard.filteredMdSecEmpty.title"), 
                        Context.getMessage(
                            "ExportWizard.filteredMdSecEmpty.description",new Object []{
                                exportParams.getFormat()
                            }
                        )
                    );
                    if(!proceed){
                        return true;
                    }
                }
            }
            
            //export
            final Exporter exporter = (Exporter) Class.forName((String)econfig.get("class")).newInstance();
            
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        exporter.export(metsBag,mets,new BufferedOutputStream(new FileOutputStream(exportParams.getOutputFile().get(0))));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        log.debug(ex.getMessage());
                    } catch (BagitMetsValidationException ex) {
                        ex.printStackTrace();
                        log.debug(ex.getMessage());
                    } catch (DatatypeConfigurationException ex) {
                        ex.printStackTrace();
                        log.debug(ex.getMessage());
                    } catch (ParserConfigurationException ex) {
                        ex.printStackTrace();
                        log.debug(ex.getMessage());
                    } catch (TransformerException ex) {
                        ex.printStackTrace();
                        log.debug(ex.getMessage());
                    } catch (SAXException ex) {
                        ex.printStackTrace();
                        log.debug(ex.getMessage());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        log.debug(ex.getMessage());
                    } catch(Exception e){}
                }
            };
            BusyIndicator.showWhile(SwingUtils.getFrame(),runnable);            
            
            
        }catch(Exception e){
            e.printStackTrace();            
        }                
        return true;
    }    
} 
package ugent.bagger.wizards;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.InfoInputPane;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedOutputStream;
import java.io.File;
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
import org.springframework.richclient.progress.BusyIndicator;
import org.springframework.richclient.wizard.AbstractWizard;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.BagitMetsValidationException;
import ugent.bagger.exporters.Exporter;
import ugent.bagger.exporters.MdSecFilter;
import ugent.bagger.exporters.MetadataConverter;
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
    static final String EOL = System.getProperty("line.separator");

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
        
        if(exportParamsForm.hasErrors()){        
            return false;
        }
        exportParamsForm.commit();
        
        HashMap<String,HashMap<String,Object>>exportersConfig = null;
        try{
            final File outputFile = exportParams.getOutputFile().get(0);
            
            exportersConfig = (HashMap<String,HashMap<String,Object>>) Beans.getBean("exporters");            
            HashMap<String,Object>econfig = exportersConfig.get(exportParams.getFormat());                
            
            //controle op bestand
            if(!outputFile.getParentFile().canRead()){
                SwingUtils.ShowError(
                    null,
                    Context.getMessage(
                        "ExportWizard.parentFileNotReadable",
                        new Object [] {outputFile.getParentFile().getAbsolutePath()})
                );
                return false;
            }
            if(!outputFile.getParentFile().canWrite()){
                SwingUtils.ShowError(
                    null,
                    Context.getMessage(
                        "ExportWizard.parentFileNotWritable",
                        new Object [] {outputFile.getParentFile().getAbsolutePath()})
                );
                return false;
            }            
            if(outputFile.exists()){
                if(outputFile.isDirectory()){
                    SwingUtils.ShowError(
                        null, 
                        Context.getMessage(
                            "ExportWizard.outputFileExistingDirectory", 
                            new Object [] {
                                outputFile.getAbsolutePath()
                            }
                        )
                    );
                    return false;
                }else{
                    boolean proceed = SwingUtils.confirm(
                        null, 
                        Context.getMessage(
                            "ExportWizard.outputFileExists",
                            new Object [] {outputFile.getAbsolutePath()}
                        )
                    );
                    if(!proceed){
                        return false;
                    }
                }                                    
            }
            
            
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
                                exportParams.getFormat(),
                                ArrayUtils.join(mdSecFilter.getErrors().toArray(),EOL)
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
            
            MetadataConverter metadataConverter = null;
            if(econfig.containsKey("metadataConverter")){
                metadataConverter = (MetadataConverter) econfig.get("metadataConverter");                
                exporter.setMetadataConverter(metadataConverter);
            }
            
            
            Runnable runnable = new Runnable() {                
                @Override
                public void run() {
                    
                    boolean success = false;
                    
                    try {
                        exporter.export(metsBag,mets,new BufferedOutputStream(new FileOutputStream(outputFile)));
                        success = true;
                    } catch (IOException ex) {                                                
                        log.error(ex);
                    } catch (BagitMetsValidationException ex) {                                                
                        log.error(ex);
                    } catch (DatatypeConfigurationException ex) {                    
                        log.error(ex);
                    } catch (ParserConfigurationException ex) {                        
                        log.error(ex);
                    } catch (TransformerException ex) {                        
                        log.error(ex);
                    } catch (SAXException ex) {                        
                        log.error(ex);
                    } catch (ParseException ex) {                        
                        log.error(ex);
                    } catch(Exception ex){                        
                        log.error(ex);
                    }
                    
                    //report                                    
                    SwingUtils.ShowMessage(
                        null,
                        Context.getMessage("ExportWizard.exportSuccessfull.label",new Object []{
                            outputFile
                    }));
                }
            };           
            
            BusyIndicator.showWhile(getExportWizardPage1().getControl(),runnable);            
            
            
        }catch(Exception e){            
            log.error(e);
        }                
        return true;
    }    
} 
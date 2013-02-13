package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.utilities.SimpleResult;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import gov.loc.repository.bagit.verify.impl.ParallelManifestChecksumVerifier;
import gov.loc.repository.bagit.verify.impl.ValidVerifierImpl;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import javax.swing.JDialog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.BagitUtils;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.panels.BagErrorPanel;
import ugent.bagger.params.Failure;
import ugent.bagger.params.FixityFailure;
import ugent.bagger.workers.Handler;
import ugent.bagger.workers.LongTask;

public class ValidateBagHandler extends Handler {
    static final Log log = LogFactory.getLog(ValidateBagHandler.class);   

    public ValidateBagHandler() {
        super();      
    }
    @Override
    public void actionPerformed(ActionEvent e) {        
        validateBag();
    }
    public void validateBag(){        
        execute();    	
    }
    @Override
    public void execute() {        
        SwingUtils.monitor(
            new ValidateBagWorker(),
            Context.getMessage("ValidateBagHandler.monitor.title"),
            Context.getMessage("ValidateBagHandler.monitor.label")
        );
    }
    class ValidateBagWorker extends LongTask{
        SimpleResult result;
        @Override
        protected Object doInBackground() throws Exception {
            
            final BagView bagView = BagView.getInstance();
            MetsBag metsBag = bagView.getBag();  
            
            SwingUtils.getStatusBar().setMessage(
                Context.getMessage(
                    "StatusBar.validateBag.message",
                    new Object []{metsBag.getFile().getAbsolutePath()}
                )
            );
            
            log.error(Context.getMessage("ValidateBagHandler.validationStarted",new Object [] {metsBag.getFile()}));
            
            try {
                CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
                ParallelManifestChecksumVerifier manifestVerifier = new ParallelManifestChecksumVerifier();
                ValidVerifierImpl validVerifier = new ValidVerifierImpl(completeVerifier, manifestVerifier);                
                validVerifier.addProgressListener(this);                
                
                result = metsBag.validateBag(validVerifier); 
                for(String message:result.getMessages()){
                    log.debug("message: "+message);
                }
                   
            }catch (Exception e){   
                log.error(e.getMessage());  
                if(isCancelled()){                                      
                    SwingUtils.ShowError(Context.getMessage("ValidateBagHandler.validationCancelled.title"),Context.getMessage("ValidateBagHandler.validationCancelled.label"));
                }else{
                    log.error(Context.getMessage("ValidateBagHandler.Exception.message",new Object [] {e.getMessage()}));
                    SwingUtils.ShowError(
                        Context.getMessage("ValidateBagHandler.Exception.title"),
                        Context.getMessage("ValidateBagHandler.Exception.message",new Object [] {e.getMessage()})
                    );
                }
            }            
            
            return null;
        }            
        @Override
        public void done(){
            super.done();
            if(result != null){
                if(!result.isSuccess()){
                    
                    MetsBag metsBag = BagView.getInstance().getBag();
                    Manifest payloadManifest = metsBag.getBag().getPayloadManifest(MetsBag.resolveAlgorithm(metsBag.getPayloadManifestAlgorithm()));
                    Manifest tagManifest = metsBag.getBag().getTagManifest(MetsBag.resolveAlgorithm(metsBag.getTagManifestAlgorithm()));
                    
                    ArrayList<String>payloadsMissing = new ArrayList<String>();
                    ArrayList<String>tagsMissing = new ArrayList<String>();
                    ArrayList<String>payloadsFixityFailure = new ArrayList<String>();
                    ArrayList<String>tagsFixityFailure = new ArrayList<String>();               
                    ArrayList<String>filesNotInManifest = new ArrayList<String>();
                    
                    for(String message:result.getMessages()){
                        
                        Matcher m = null;
                        
                        if(
                            (m = BagitUtils.payloadsMissingPattern.matcher(message)) != null &&
                            m.matches()
                        ){
                            payloadsMissing.add(m.group(1));
                        }
                        else if(
                            (m = BagitUtils.tagsMissingPattern.matcher(message)) != null &&
                            m.matches()
                        ){
                            tagsMissing.add(m.group(1));
                        }
                        else if(
                            (m = BagitUtils.payloadsFixityFailurePattern.matcher(message)) != null &&
                            m.matches()
                        ){
                            payloadsFixityFailure.add(m.group(1));
                        }
                        else if(
                            (m = BagitUtils.tagsFixityFailurePattern.matcher(message)) != null &&
                            m.matches()
                        ){
                            tagsFixityFailure.add(m.group(1));
                        }
                        else if(
                            (m = BagitUtils.fileNotInManifestPattern.matcher(message)) != null &&
                            m.matches()
                        ){
                            filesNotInManifest.add(m.group(1));
                        }
                        else if(
                            (m = BagitUtils.fileNotAllowedInBagDirPattern.matcher(message)) != null &&
                            m.matches()
                        ){
                            filesNotInManifest.add(m.group(1));
                        }
                    }
                    
                    //rapport
                    
                    JDialog dialog = new JDialog(SwingUtils.getFrame(),true);
                    dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    
                    ArrayList<Failure>missingFiles = new ArrayList<Failure>();
                    for(String payload:payloadsMissing){
                        missingFiles.add(new Failure(payload));
                    }
                    for(String tag:tagsMissing){
                        missingFiles.add(new Failure(tag));
                    }                    
                    
                    ArrayList<Failure>newFiles = new ArrayList<Failure>();                    
                    for(String nf:filesNotInManifest){
                        newFiles.add(new Failure(nf));
                    }
                    
                    ArrayList<FixityFailure>fixityFailure = new ArrayList<FixityFailure>();
                    for(String payload:payloadsFixityFailure){
                        fixityFailure.add(new FixityFailure(payload,payloadManifest.get(payload)));
                    }
                    for(String tag:tagsFixityFailure){
                        fixityFailure.add(new FixityFailure(tag,tagManifest.get(tag)));
                    }
                    
                    BagErrorPanel panel = new BagErrorPanel(missingFiles,fixityFailure,newFiles);                    
                    
                    dialog.setContentPane(panel);
                    SwingUtils.centerOnParent(dialog,true);
                    dialog.setVisible(true);
                    
                    log.error(Context.getMessage("ValidateBagHandler.title"));
                    
                    if(payloadsMissing.size() > 0){
                        log.error(Context.getMessage("ValidateBagHandler.validation.payloadsMissing.title"));
                        for(String filename:payloadsMissing){
                            log.error("\t"+filename);
                        }
                    }
                    if(tagsMissing.size() > 0){
                        log.error(Context.getMessage("ValidateBagHandler.validation.tagsMissing.title"));
                        for(String filename:tagsMissing){
                            log.error("\t"+filename);
                        }
                    }
                    if(payloadsFixityFailure.size() > 0){
                        log.error(Context.getMessage("ValidateBagHandler.validation.payloadsFixityFailure.title"));
                        for(String filename:payloadsFixityFailure){
                            log.error("\t"+filename);
                        }
                    }
                    if(tagsFixityFailure.size() > 0){
                        log.error(Context.getMessage("ValidateBagHandler.validation.tagsFixityFailure.title"));
                        for(String filename:tagsFixityFailure){
                            log.error("\t"+filename);
                        }
                    }                    
                    if(filesNotInManifest.size() > 0){
                        log.error(Context.getMessage("ValidateBagHandler.validation.filesNotInManifest.title"));
                        for(String filename:filesNotInManifest){
                            log.error("\t"+filename);
                        }
                    }
                    
                }else{
                    SwingUtils.ShowMessage(
                        Context.getMessage("ValidateBagHanddler.validationSuccessfull.title"),
                        Context.getMessage("ValidateBagHanddler.validationSuccessfull.label")
                    );                    
                    log.error(Context.getMessage("ValidateBagHanddler.validationSuccessfull.label"));
                }
                
                log.error(
                    Context.getMessage(
                        "ValidateBagHandler.validationDone",
                        new Object [] {BagView.getInstance().getBag().getFile()}
                    )
                );
                
                SwingUtils.getStatusBar().setMessage("");
            }
        }
    }
}
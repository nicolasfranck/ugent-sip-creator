package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.utilities.SimpleResult;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import gov.loc.repository.bagit.verify.impl.ParallelManifestChecksumVerifier;
import gov.loc.repository.bagit.verify.impl.ValidVerifierImpl;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.BagitUtils;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Handler;
import ugent.bagger.workers.LongTask;

public class ValidateBagHandler extends Handler {
    static final Log log = LogFactory.getLog(ValidateBagHandler.class);
    static final long serialVersionUID = 1L;       
    /*Pattern tagsMissingPattern = Pattern.compile("File (\\S+) in manifest tagmanifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");
    Pattern payloadsMissingPattern = Pattern.compile("File (\\S+) in manifest manifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");
    Pattern tagsFixityFailurePattern = Pattern.compile("Fixity failure in manifest tagmanifest-(?:md5|sha1|sha256|sha512)\\.txt: (\\S+)");
    Pattern payloadsFixityFailurePattern = Pattern.compile("Fixity failure in manifest manifest-(?:md5|sha1|sha256|sha512)\\.txt: (\\S+)");*/

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
    private class ValidateBagWorker extends LongTask{
        SimpleResult result;
        @Override
        protected Object doInBackground() throws Exception {
            
            final BagView bagView = BagView.getInstance();
            DefaultBag bag = bagView.getBag();            
            
            log.error(Context.getMessage("ValidateBagHandler.validationStarted",new Object [] {bag.getFile()}));
            
            try {
                CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
                ParallelManifestChecksumVerifier manifestVerifier = new ParallelManifestChecksumVerifier();
                ValidVerifierImpl validVerifier = new ValidVerifierImpl(completeVerifier, manifestVerifier);                
                validVerifier.addProgressListener(this);                
                
                result = bag.validateBag(validVerifier);                               
                   
            }catch (Exception e){                                                
                if(isCancelled()){
                    log.error(e.getMessage());
                    //log(Context.getMessage("ValidateBagHandler.validationCancelled.label"));
                    SwingUtils.ShowError(Context.getMessage("ValidateBagHandler.validationCancelled.title"),Context.getMessage("ValidateBagHandler.validationCancelled.label"));
                }else{
                    //log(Context.getMessage("ValidateBagHandler.validationFailed.label",new Object [] {e.getMessage()}));                    
                    SwingUtils.ShowError(
                        Context.getMessage("ValidateBagHandler.validationFailed.title"), 
                        Context.getMessage("ValidateBagHandler.validationFailed.label",new Object [] {e.getMessage()})
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
                    
                    ArrayList<String>payloadsMissing = new ArrayList<String>();
                    ArrayList<String>tagsMissing = new ArrayList<String>();
                    ArrayList<String>payloadsFixityFailure = new ArrayList<String>();
                    ArrayList<String>tagsFixityFailure = new ArrayList<String>();               
                    ArrayList<String>filesNotInManifest = new ArrayList<String>();
                    
                    for(String message:result.getMessages()){
                        Matcher m1 = BagitUtils.payloadsMissingPattern.matcher(message);
                        Matcher m2 = BagitUtils.tagsMissingPattern.matcher(message);
                        Matcher m3 = BagitUtils.payloadsFixityFailurePattern.matcher(message);
                        Matcher m4 = BagitUtils.tagsFixityFailurePattern.matcher(message);
                        Matcher m5 = BagitUtils.fileNotInManifestPattern.matcher(message);
                        
                        if(m1.matches()){
                            payloadsMissing.add(m1.group(1));
                        }else if(m2.matches()){
                            tagsMissing.add(m2.group(1));
                        }else if(m3.matches()){
                            payloadsFixityFailure.add(m3.group(1));
                        }else if(m4.matches()){
                            tagsFixityFailure.add(m4.group(1));
                        }else if(m5.matches()){
                            filesNotInManifest.add(m5.group(1));
                        }
                    }
                    
                    SwingUtils.ShowError(
                        Context.getMessage("ValidateBagHandler.validationFailed.title"),
                        Context.getMessage(
                            "ValidateBagHandler.validationFailed.label",new Object [] {
                                payloadsMissing.size(),
                                tagsMissing.size(),
                                payloadsFixityFailure.size(),
                                tagsFixityFailure.size(),
                                filesNotInManifest.size()
                            }
                        )
                    );
                    
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
            }
        }
    }
}
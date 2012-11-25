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
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Handler;
import ugent.bagger.workers.LongTask;

public class ValidateBagHandler extends Handler {

    private static final long serialVersionUID = 1L;       
    private Pattern tagsMissingPattern = Pattern.compile("File (\\S+) in manifest tagmanifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");
    private Pattern payloadsMissingPattern = Pattern.compile("File (\\S+) in manifest manifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");
    private Pattern tagsFixityFailurePattern = Pattern.compile("Fixity failure in manifest tagmanifest-(?:md5|sha1|sha256|sha512)\\.txt: (\\S+)");
    private Pattern payloadsFixityFailurePattern = Pattern.compile("Fixity failure in manifest manifest-(?:md5|sha1|sha256|sha512)\\.txt: (\\S+)");

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
        @Override
        protected Object doInBackground() throws Exception {
            
            SwingUtils.ShowBusy();            
            
            final BagView bagView = BagView.getInstance();
            DefaultBag bag = bagView.getBag();            
            
            try {
                CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
                ParallelManifestChecksumVerifier manifestVerifier = new ParallelManifestChecksumVerifier();
                ValidVerifierImpl validVerifier = new ValidVerifierImpl(completeVerifier, manifestVerifier);                
                validVerifier.addProgressListener(this);                
                
                SimpleResult result = bag.validateBag(validVerifier);                                
               
                if(!result.isSuccess()){
                    
                    ArrayList<String>payloadsMissing = new ArrayList<String>();
                    ArrayList<String>tagsMissing = new ArrayList<String>();
                    ArrayList<String>payloadsFixityFailure = new ArrayList<String>();
                    ArrayList<String>tagsFixityFailure = new ArrayList<String>();               
                    
                    for(String message:result.getMessages()){
                        Matcher m1 = payloadsMissingPattern.matcher(message);
                        Matcher m2 = tagsMissingPattern.matcher(message);
                        Matcher m3 = payloadsFixityFailurePattern.matcher(message);
                        Matcher m4 = tagsFixityFailurePattern.matcher(message);
                        
                        if(m1.matches()){
                            payloadsMissing.add(m1.group(1));
                        }else if(m2.matches()){
                            tagsMissing.add(m2.group(1));
                        }else if(m3.matches()){
                            payloadsFixityFailure.add(m3.group(1));
                        }else if(m4.matches()){
                            tagsFixityFailure.add(m4.group(1));
                        }
                    }
                    
                    SwingUtils.ShowError(
                        Context.getMessage("ValidateBagHandler.validationFailed.title"),
                        Context.getMessage(
                            "ValidateBagHandler.validationFailed.label",new Object [] {
                                payloadsMissing.size(),tagsMissing.size(),payloadsFixityFailure.size(),tagsFixityFailure.size()
                            }
                        )
                    );
                    
                    log(Context.getMessage("ValidateBagHandler.title"));
                    
                    if(payloadsMissing.size() > 0){
                        log(Context.getMessage("ValidateBagHandler.validation.payloadsMissing.title"));
                        for(String filename:payloadsMissing){
                            log("\t"+filename);
                        }
                    }
                    if(tagsMissing.size() > 0){
                        log(Context.getMessage("ValidateBagHandler.validation.tagsMissing.title"));
                        for(String filename:tagsMissing){
                            log("\t"+filename);
                        }
                    }
                    if(payloadsFixityFailure.size() > 0){
                        log(Context.getMessage("ValidateBagHandler.validation.payloadsFixityFailure.title"));
                        for(String filename:payloadsFixityFailure){
                            log("\t"+filename);
                        }
                    }
                    if(tagsFixityFailure.size() > 0){
                        log(Context.getMessage("ValidateBagHandler.validation.tagsFixityFailure.title"));
                        for(String filename:tagsFixityFailure){
                            log("\t"+filename);
                        }
                    }                    
                    
                }else{
                    SwingUtils.ShowMessage(
                        Context.getMessage("ValidateBagHanddler.validationSuccessfull.title"),
                        Context.getMessage("ValidateBagHanddler.validationSuccessfull.label")
                    );
                    log(Context.getMessage("ValidateBagHanddler.validationSuccessfull.label"));
                }
                   
            }catch (Exception e){                                                
                if(isCancelled()){
                    log(Context.getMessage("ValidateBagHandler.validationCancelled.label"));
                    SwingUtils.ShowError(Context.getMessage("ValidateBagHandler.validationCancelled.title"),Context.getMessage("ValidateBagHandler.validationCancelled.label"));
                }else{
                    log(Context.getMessage("ValidateBagHandler.validationFailed.label",new Object [] {e.getMessage()}));                    
                    SwingUtils.ShowError(
                        Context.getMessage("ValidateBagHandler.validationFailed.title"), 
                        Context.getMessage("ValidateBagHandler.validationFailed.label",new Object [] {e.getMessage()})
                    );
                }
            }
            
            SwingUtils.ShowDone();
            
            return null;
        }            
    }
}
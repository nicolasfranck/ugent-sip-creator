package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.utilities.SimpleResult;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Handler;
import ugent.bagger.workers.LongTask;

public class CompleteBagHandler extends Handler /*implements Loggable*/ {
    static final long serialVersionUID = 1L;   
    static final Log log = LogFactory.getLog(CompleteBagHandler.class);
    Pattern tagsMissingPattern = Pattern.compile("File (\\S+) in manifest tagmanifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");
    Pattern payloadsMissingPattern = Pattern.compile("File (\\S+) in manifest manifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");

    public CompleteBagHandler() {
        super();           
    }

    @Override
    public void actionPerformed(ActionEvent e) {        
        completeBag();
    }    
    public void completeBag(){        
        execute();    	
    }
    @Override
    public void execute() {        
        SwingUtils.monitor(
            new CompleteBagWorker(),
            Context.getMessage("CompleteBagHandler.validating.title"),
            Context.getMessage("CompleteBagHandler.validating.description")
        );
    }
    private class CompleteBagWorker extends LongTask {       
        SimpleResult result;
        BagView bagView = BagView.getInstance();
        
        @Override
        protected Object doInBackground() throws Exception {           
                        
            MetsBag metsBag = bagView.getBag();
            log.error(Context.getMessage("CompleteBagHandler.validationStarted",new Object [] {metsBag.getFile()}));
            
            try {
                CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
                completeVerifier.addProgressListener(this);

                result = metsBag.completeBag(completeVerifier);                                           

            }catch(Exception e) {
                log.error(e.getMessage());
                            
                if (isCancelled()) {
                    log.error(Context.getMessage("defaultBag.checkCompleteCancelled"));
                    SwingUtils.ShowError(Context.getMessage("defaultBag.checkCompleteCancelled"),Context.getMessage("defaultBag.checkCompleteCancelled"));
                }else{
                    log.error(Context.getMessage("defaultBag.checkComplete.error.label",new Object [] {e.getMessage()}));
                    SwingUtils.ShowError(
                        Context.getMessage("defaultBag.checkComplete.error.title"),
                        Context.getMessage("defaultBag.checkComplete.error.label",new Object [] {e.getMessage()})
                    );
                }
            }            
            
            return null;
        }     
        @Override
        public void done(){
            super.done();
            
            if(result != null){
                if (!result.isSuccess()) {
                    ArrayList<String>payloadsMissing = new ArrayList<String>();
                    ArrayList<String>tagsMissing = new ArrayList<String>();

                    for(String message:result.getMessages()){            
                        Matcher m1 = payloadsMissingPattern.matcher(message);
                        Matcher m2 = tagsMissingPattern.matcher(message);
                        if(m1.matches()){
                            payloadsMissing.add(m1.group(1));                            
                        }else if(m2.matches()){
                            tagsMissing.add(m2.group(1));                            
                        }
                    }

                    log.error(Context.getMessage("CompleteBagHandler.validation.title"));

                    if(payloadsMissing.size() >  0){
                        log.error(Context.getMessage("CompleteBagHandler.validation.payloadsMissing.title"));
                        for(String filename:payloadsMissing){
                            log.error("\t"+filename);
                        }
                    }
                    if(tagsMissing.size() > 0){
                        log.error(Context.getMessage("CompleteBagHandler.validation.tagsMissing.title"));
                        for(String filename:tagsMissing){
                            log.error("\t"+filename);                     
                        }
                    }               

                    SwingUtils.ShowError(
                        Context.getMessage("CompleteBagHandler.validationFailed.title"),
                        Context.getMessage("CompleteBagHandler.validationFailed.label",new Object [] {
                            payloadsMissing.size(),tagsMissing.size()
                        })
                    );                  
                }else {
                    String message = Context.getMessage("defaultBag.bagIsComplete");
                    log.error(message);
                    SwingUtils.ShowMessage(message,message);                  
                }             
                
                log.debug(result.toString());
                
                log.error(Context.getMessage("CompleteBagHandler.validationDone",new Object [] {BagView.getInstance().getBag().getFile()}));

            }            
        }
    }
}
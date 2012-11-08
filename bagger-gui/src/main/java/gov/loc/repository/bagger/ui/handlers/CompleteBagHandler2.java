package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
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
import ugent.bagger.workers.Loggable;
import ugent.bagger.workers.LongTask2;

public class CompleteBagHandler2 extends Handler implements Loggable {
    private static final long serialVersionUID = 1L;   
    private static final Log log = LogFactory.getLog(CompleteBagHandler2.class);
    private Pattern tagsMissingPattern = Pattern.compile("File (\\S+) in manifest tagmanifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");
    private Pattern payloadsMissingPattern = Pattern.compile("File (\\S+) in manifest manifest-(?:md5|sha1|sha256|sha512)\\.txt missing from bag\\.");

    public CompleteBagHandler2() {
        super();           
    }

    @Override
    public void actionPerformed(ActionEvent e) {        
        completeBag();
    }
    @Override
    public void log(String message){
        ApplicationContextUtil.addConsoleMessage(message);
    }
    public void completeBag(){        
        execute();    	
    }
    @Override
    public void execute() {        
        SwingUtils.monitor(new CompleteBagWorker(),"checking bag complete","");
    }
    private class CompleteBagWorker extends LongTask2 {
        @Override
        protected Object doInBackground() throws Exception {
            SwingUtils.ShowBusy();            
            
            BagView bagView = BagView.getInstance();
            DefaultBag bag = bagView.getBag();
            try {
                CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
                completeVerifier.addProgressListener(this);

                final SimpleResult result = bag.completeBag(completeVerifier);

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
                    
                    log(Context.getMessage("CompleteBagHandler.validation.title"));
                    
                    if(payloadsMissing.size() >  0){
                        log(Context.getMessage("CompleteBagHandler.validation.payloadsMissing.title"));
                        for(String filename:payloadsMissing){
                            log("\t"+filename);
                        }
                    }
                    if(tagsMissing.size() > 0){
                        log(Context.getMessage("CompleteBagHandler.validation.tagsMissing.title"));
                        for(String filename:tagsMissing){
                            log("\t"+filename);                     
                        }
                    }               
                    
                    SwingUtils.ShowError(
                        Context.getMessage("CompleteBagHandler.validationFailed.title"),
                        Context.getMessage("CompleteBagHandler.validationFailed.label",new Object [] {
                            payloadsMissing.size(),tagsMissing.size()
                        })
                    );                  
                }
                else {
                    String message = Context.getMessage("defaultBag.bagIsComplete");
                    log(message);
                    SwingUtils.ShowMessage(message,message);                  
                }
                
                log(result.toString());                           

            }catch(Exception e) {
                log.debug(e.getMessage());
                            
                if (isCancelled()) {
                    log(Context.getMessage("defaultBag.checkCompleteCancelled"));
                    SwingUtils.ShowError(Context.getMessage("defaultBag.checkCompleteCancelled"),Context.getMessage("defaultBag.checkCompleteCancelled"));
                }else{
                    log(Context.getMessage("defaultBag.checkComplete.error.label",new Object [] {e.getMessage()}));
                    SwingUtils.ShowError(
                        Context.getMessage("defaultBag.checkComplete.error.title"),
                        Context.getMessage("defaultBag.checkComplete.error.label",new Object [] {e.getMessage()})
                    );
                }
            }
            
            SwingUtils.ShowDone();
            
            return null;
        }               
    }
}
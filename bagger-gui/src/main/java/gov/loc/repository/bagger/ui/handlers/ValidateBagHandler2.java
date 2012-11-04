package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import gov.loc.repository.bagit.verify.impl.ParallelManifestChecksumVerifier;
import gov.loc.repository.bagit.verify.impl.ValidVerifierImpl;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Handler;
import ugent.bagger.workers.LongTask2;

public class ValidateBagHandler2 extends Handler {

    private static final long serialVersionUID = 1L;   
    private String messages;

    public ValidateBagHandler2() {
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
        SwingUtils.monitor(new ValidateBagWorker(),"validating bag..","");
    }
    private class ValidateBagWorker extends LongTask2{
        @Override
        protected Object doInBackground() throws Exception {
            
            BusyIndicator.showAt(SwingUtils.getFrame());
            
            final BagView bagView = BagView.getInstance();
            DefaultBag bag = bagView.getBag();
            try {
                CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
                ParallelManifestChecksumVerifier manifestVerifier = new ParallelManifestChecksumVerifier();
                ValidVerifierImpl validVerifier = new ValidVerifierImpl(completeVerifier, manifestVerifier);                
                validVerifier.addProgressListener(this);                
                messages = bag.validateBag(validVerifier);                
               
                if (messages != null && !messages.trim().isEmpty()){                      
                    SwingUtils.ShowError("Warning - validation failed","Validation result: " + messages);
                    log("Validation result: " + messages);
                }else{
                    SwingUtils.ShowMessage("Validation Dialog","Validation successful.");
                    log("Validation successful.");
                }
                   
            }catch (Exception e){                                                
                if (isCancelled()) {
                    log("Validation cancelled.");
                    bagView.showWarningErrorDialog("Validation cancelled", "Validation cancelled.");
                } else {
                    log("Error trying validate bag: " + e.getMessage());
                    bagView.showWarningErrorDialog("Warning - validation interrupted", "Error trying validate bag: " + e.getMessage());
                }
            }
            
            BusyIndicator.clearAt(SwingUtils.getFrame());
            
            return null;
        }               
    }
}
package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Handler;
import ugent.bagger.workers.Loggable;
import ugent.bagger.workers.LongTask2;

public class CompleteBagHandler2 extends Handler implements Loggable {
    private static final long serialVersionUID = 1L;   
    private String messages;

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
            BusyIndicator.showAt(SwingUtils.getFrame());
            
            BagView bagView = BagView.getInstance();
            DefaultBag bag = bagView.getBag();
            try {
                CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
                completeVerifier.addProgressListener(this);

                messages = bag.completeBag(completeVerifier);

                if (messages != null && !messages.trim().isEmpty()) {
                    String error = "Is complete result: " + messages;
                    log(error);
                    SwingUtils.ShowError("Is complete result: ",error);                  
                }
                else {
                    String message = "Bag is complete.";
                    log(message);
                    SwingUtils.ShowMessage("Is Complete Dialog",message);                  
                }

                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        log(messages);
                    }
                });            

            }catch(Exception e) {
                e.printStackTrace();                
                if (isCancelled()) {
                    log("Completion check cancelled.");
                    SwingUtils.ShowError("Check cancelled", "Completion check cancelled.");
                } else {
                    log("Error checking bag completeness: " + e.getMessage());
                    SwingUtils.ShowError("Warning - complete check interrupted", "Error checking bag completeness: " + e.getMessage());
                }
            }
            
            BusyIndicator.clearAt(SwingUtils.getFrame());
            return null;
        }               
    }
}
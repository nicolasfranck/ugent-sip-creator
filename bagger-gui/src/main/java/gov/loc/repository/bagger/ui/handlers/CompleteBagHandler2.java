package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Handler;
import ugent.bagger.workers.LongTask2;

public class CompleteBagHandler2 extends Handler {
    private static final long serialVersionUID = 1L;   
    private String messages;

    public CompleteBagHandler2() {
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
        SwingUtils.monitor(new CompleteBagWorker(),"checking bag complete","");
    }
    private class CompleteBagWorker extends LongTask2 {
        @Override
        protected Object doInBackground() throws Exception {
            BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());
            
            BagView bagView = BagView.getInstance();
            DefaultBag bag = bagView.getBag();
            try {
                CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
                completeVerifier.addProgressListener(this);

                messages = bag.completeBag(completeVerifier);

                if (messages != null && !messages.trim().isEmpty()) {
                    bagView.showWarningErrorDialog("Warning - incomplete", "Is complete result: " + messages);
                }
                else {
                    bagView.showWarningErrorDialog("Is Complete Dialog", "Bag is complete.");
                }

                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        ApplicationContextUtil.addConsoleMessage(messages);
                    }
                });            

            }catch(Exception e) {
                e.printStackTrace();
                if (isCancelled()) {
                    bagView.showWarningErrorDialog("Check cancelled", "Completion check cancelled.");
                } else {
                    bagView.showWarningErrorDialog("Warning - complete check interrupted", "Error checking bag completeness: " + e.getMessage());
                }
            }
            
            BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());
            return null;
        }
        @Override
        public void cancel(){  
            BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());
        }        
    }
}
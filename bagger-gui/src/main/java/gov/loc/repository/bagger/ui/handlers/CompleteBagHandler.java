package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.utilities.SimpleResult;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
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

public class CompleteBagHandler extends Handler {
    static final Log log = LogFactory.getLog(CompleteBagHandler.class);    

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
    class CompleteBagWorker extends LongTask {       
        SimpleResult result;
        BagView bagView = BagView.getInstance();
        
        @Override
        protected Object doInBackground() throws Exception {           
                        
            MetsBag metsBag = bagView.getBag();
            log.error(Context.getMessage("CompleteBagHandler.validationStarted",new Object [] {metsBag.getFile()}));
            
            SwingUtils.getStatusBar().setMessage(
                Context.getMessage(
                    "StatusBar.completeBag.message",
                    new Object []{metsBag.getFile().getAbsolutePath()}
                )
            );
            
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
                    ArrayList<String>filesNotInManifest = new ArrayList<String>();

                    for(String message:result.getMessages()){    
                        
                        /*
                        Matcher m1 = BagitUtils.payloadsMissingPattern.matcher(message);
                        Matcher m2 = BagitUtils.tagsMissingPattern.matcher(message);
                        Matcher m3 = BagitUtils.fileNotInManifestPattern.matcher(message);
                        if(m1.matches()){
                            payloadsMissing.add(m1.group(1));                            
                        }else if(m2.matches()){
                            tagsMissing.add(m2.group(1));                            
                        }else if(m3.matches()){
                            filesNotInManifest.add(m3.group(1));
                        }*/
                        
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
                    if(filesNotInManifest.size() > 0){
                        log.error(Context.getMessage("CompleteBagHandler.validation.filesNotInManifest.title"));
                        for(String filename:filesNotInManifest){
                            log.error("\t"+filename);
                        }
                    }

                                        
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
                    
                    BagErrorPanel panel = new BagErrorPanel(missingFiles,fixityFailure,newFiles);                    
                    
                    dialog.setContentPane(panel);
                    SwingUtils.centerOnParent(dialog,true);
                    dialog.setVisible(true);
                    
                }else {
                    String message = Context.getMessage("defaultBag.bagIsComplete");
                    log.error(message);
                    SwingUtils.ShowMessage(message,message);                  
                }             
                
                log.debug(result.toString());
                
                log.error(Context.getMessage("CompleteBagHandler.validationDone",new Object [] {BagView.getInstance().getBag().getFile()}));
                
                SwingUtils.getStatusBar().setMessage("");

            }            
        }
    }
}
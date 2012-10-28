package ugent.bagger.helper;

import gov.loc.repository.bagger.ui.BagView;
import javax.swing.SwingWorker;
import org.springframework.richclient.application.statusbar.support.StatusBarProgressMonitor;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.wizards.FTPWizard;
import ugent.bagger.wizards.FTPWizardDialog;




/**
 *
 * @author nicolas
 */
public class Monitoring {
    public static void main(String [] args){
        
        FTPWizardDialog wizard = new FTPWizardDialog();
        
        wizard.showDialog();
        
        
        final StatusBarProgressMonitor monitor = (StatusBarProgressMonitor)SwingUtils.getStatusBar().getProgressMonitor();        
        monitor.setDelayProgress(0);
        BusyIndicator.showAt(SwingUtils.getFrame());
      
        monitor.taskStarted("long work",100);        
        new SwingWorker(){
            @Override
            protected Object doInBackground() throws Exception {
                for(int i = 1;i <= 10;i++){
                    
                    System.out.println("subtask "+i+" started");
                    monitor.subTaskStarted("subtask "+i+" started");
                    try{
                        Thread.sleep(2000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    monitor.worked(i*10); 
                   
                    try{
                        Thread.sleep(2000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println("subtask "+i+" done");
                }
                monitor.done();
                BusyIndicator.clearAt(SwingUtils.getFrame());
                
                return null;
            }
        }.execute();
        
        
    }
}

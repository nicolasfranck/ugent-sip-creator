package ugent.bagger.helper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.statusbar.support.StatusBarProgressMonitor;
import org.springframework.richclient.progress.BusyIndicator;
import org.springframework.richclient.progress.ProgressBarProgressMonitor;
import org.springframework.richclient.progress.ProgressMonitor;




/**
 *
 * @author nicolas
 */
public class Monitoring {
    public static void main(String [] args){
        
        
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

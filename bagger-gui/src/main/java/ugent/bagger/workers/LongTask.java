package ugent.bagger.workers;

import gov.loc.repository.bagit.ProgressListener;
import javax.swing.SwingWorker;
import ugent.bagger.helper.SwingUtils;

/**
 *
 * @author nicolas
 */
public abstract class LongTask extends SwingWorker implements ProgressListener /*,Loggable*/{       
    String lastNote = "";    
    public LongTask(){
        SwingUtils.ShowBusy();
        SwingUtils.getStatusBar().clear();
        SwingUtils.getStatusBar().getProgressMonitor().done();
        SwingUtils.getStatusBar().getProgressMonitor().taskStarted("",100);
    }
    @Override
    final public void reportProgress(String activity, Object o, Long count, Long total) {                
        if(count == null || total == null){            
            return;
        }
        if(activity.compareTo(lastNote) != 0){            
            firePropertyChange("note",lastNote,activity);
            lastNote = activity;
        }        
        int percent = (int)Math.floor( (count / ((float)total))*100);                
        if(!isDone()){
            setProgress(percent);               
        }        
    }
    /*
    @Override
    final public void log(String message){
        ApplicationContextUtil.addConsoleMessage(message);
    }*/   
    @Override 
    public void done(){
        super.done();
        SwingUtils.ShowDone();
        SwingUtils.getStatusBar().getProgressMonitor().done();
    }
}
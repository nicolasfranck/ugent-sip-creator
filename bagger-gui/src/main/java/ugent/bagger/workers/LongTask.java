package ugent.bagger.workers;

import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.ProgressListener;
import javax.swing.SwingWorker;

/**
 *
 * @author nicolas
 */
public abstract class LongTask extends SwingWorker implements ProgressListener,Loggable{       
    private String lastNote = "";
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
    @Override
    final public void log(String message){
        ApplicationContextUtil.addConsoleMessage(message);
    }    
}
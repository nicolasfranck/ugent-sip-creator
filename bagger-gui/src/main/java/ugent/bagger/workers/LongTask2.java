package ugent.bagger.workers;

import gov.loc.repository.bagit.Cancellable;
import gov.loc.repository.bagit.ProgressListener;
import javax.swing.SwingWorker;

/**
 *
 * @author nicolas
 */
public abstract class LongTask2 extends SwingWorker implements ProgressListener,Cancellable{       
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
        /*
        if(percent == 100){            
            //indien percent == 100, dan sluit de monitor, en dat willen we zelf doen
            percent = 99;
        }*/
        setProgress(percent);       
    }
}
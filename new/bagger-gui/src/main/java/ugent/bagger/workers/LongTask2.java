package ugent.bagger.workers;

import gov.loc.repository.bagit.Cancellable;
import gov.loc.repository.bagit.ProgressListener;
import javax.swing.SwingWorker;

/**
 *
 * @author nicolas
 */
public abstract class LongTask2 extends SwingWorker implements ProgressListener,Cancellable{       
    @Override
    final public void reportProgress(String string, Object o, Long count, Long total) {
        if(count == null || total == null){
            return;
        }
        System.out.println("progress:"+count+"/"+total);
        int percent = (int)Math.floor( (count / ((float)total))*100);        
        setProgress(percent);        
    }
}
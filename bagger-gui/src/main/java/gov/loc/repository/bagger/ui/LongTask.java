package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagit.ProgressListener;
import java.text.MessageFormat;
import javax.swing.ProgressMonitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LongTask implements ProgressListener {
	
    private static final Log log = LogFactory.getLog(LongTask.class);	
    private boolean done = false;
    private Progress progress;
    private ProgressMonitor progressMonitor;    
    private String activityMonitored;

    public LongTask() {       
        //Compute length of task...
        //In a real program, this would figure out
        //the number of bytes to read or whatever.
    }
    
    public void setMonitor(ProgressMonitor monitor) {
    	this.progressMonitor = monitor;
    }

    public void setProgress(Progress progress) {
    	this.progress = progress;
    }

    /**
     * Called from ProgressBarDemo to start the task.
     */
    public void go() {
    	final SwingWorker worker = new SwingWorker(this) {
            @Override
            public Object construct() {
                done = false;
                /*
                 * Nicolas Franck
                 * => LongTask.this.progress.execute();
                 * => progress.execute();
                 * Progress => interface, geïmplementeerd door vb. SaveBagHandler
                 */                
                longTask.progress.execute();
                return new Object();
            }
            
            @Override
            public void finished() {
            	// update UI
            }
        };
        worker.start();
    }


    public boolean hasUserTriedToCancel() {
    	return progressMonitor.isCanceled();
    }

	/**
     * Called from ProgressBarDemo to find out if the task has completed.
     */
    public boolean isDone() {
        return done;
    }
    
    public void done() {
    	this.done = true;
    	progressMonitor.close();
    }

    // should be thread-safe
    @Override
    public synchronized void reportProgress(String activity, Object item, Long count, Long total) {
        if (count == null || total == null) {
            log.error("reportProgress received null info: count=" + count + ", total=" + total);
        } else {
            if (activityMonitored == null || activityMonitored.equals(activity)) {
                String message = MessageFormat.format("{0} ({2} of {3}) {1} ", activity, item, count, total);
                progressMonitor.setNote(message);
                progressMonitor.setMaximum(total.intValue());
                progressMonitor.setProgress(count.intValue());
            }
        }
    }

    public String getActivityMonitored() {
        return activityMonitored;
    }
    public void setActivityMonitored(String activityMonitored) {
        this.activityMonitored = activityMonitored;
    }	
}
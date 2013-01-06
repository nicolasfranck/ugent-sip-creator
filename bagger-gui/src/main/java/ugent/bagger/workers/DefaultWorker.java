package ugent.bagger.workers;

import javax.swing.SwingWorker;
import ugent.bagger.helper.SwingUtils;

/**
 *
 * @author nicolas
 */
public class DefaultWorker extends SwingWorker<Void, Void> implements /*Loggable,*/Sendable,Reportable{
    boolean success = false;        
    public DefaultWorker(){        
        SwingUtils.ShowBusy();   
        SwingUtils.getStatusBar().clear();
        SwingUtils.getStatusBar().getProgressMonitor().done();
        SwingUtils.getStatusBar().getProgressMonitor().taskStarted("",100);                   
    }
    @Override
    public void done(){
        super.done();
        SwingUtils.ShowDone();
        SwingUtils.getStatusBar().getProgressMonitor().done();
    }
    @Override
    protected Void doInBackground() throws Exception {        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /*
    @Override
    public void log(String message){
        firePropertyChange("log",null,message);        
    }*/
    @Override
    public void send(Object o){
        firePropertyChange("send",null,o);        
    }
    @Override
    public void success(boolean success){
        firePropertyChange("report",null,"success");                
    }    
}
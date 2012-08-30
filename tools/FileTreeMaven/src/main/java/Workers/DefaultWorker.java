/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Workers;

import javax.swing.SwingWorker;

/**
 *
 * @author nicolas
 */
public class DefaultWorker extends SwingWorker<Void, Void> implements Loggable,Sendable,Reportable{
    private boolean success = false;
    @Override
    protected Void doInBackground() throws Exception {        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void log(String message){
        firePropertyChange("log",null,message);        
    }
    @Override
    public void send(Object o){
        firePropertyChange("send",null,o);        
    }
    @Override
    public void success(boolean success){
        firePropertyChange("report",null,"success");                
    }
}

package ugent.bagger.dialogs;

import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import ugent.bagger.panels.CrosswalkParamsPanel;

/**
 *
 * @author nicolas
 */
public class CDialog extends JDialog {      
    public CDialog(Frame parentFrame,boolean isModal){
        super(parentFrame,true);     
        final CrosswalkParamsPanel panel = new CrosswalkParamsPanel();        
        
        panel.addPropertyChangeListener("close",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                CDialog.this.dispose();
            }
        });
        panel.addPropertyChangeListener("ok",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                panel.getCrosswalkParamsForm().commit();                
                CDialog.this.dispose();
            }
        });
        setResizable(false);
        setContentPane(panel);
    }    
}
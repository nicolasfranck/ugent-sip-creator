package ugent.bagger.dialogs;

import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import ugent.bagger.panels.CSVPanel;

/**
 *
 * @author nicolas
 */
public class CSVDialog extends JDialog{
    public CSVDialog(Frame parentFrame){
        super(parentFrame,true);        
        JPanel panel = new CSVPanel();
        setContentPane(panel);
        PropertyChangeListener l = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if(pce.getPropertyName().equals("ok") || pce.getPropertyName().equals("cancel")){
                    CSVDialog.this.dispose();
                }
            }
        };
        panel.addPropertyChangeListener(l);        
    }
}
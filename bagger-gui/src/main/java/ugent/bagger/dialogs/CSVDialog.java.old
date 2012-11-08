package ugent.bagger.dialogs;

import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import ugent.bagger.panels.CSV1Panel;

/**
 *
 * @author nicolas
 */
public class CSVDialog extends JDialog{
    public CSVDialog(Frame parentFrame){
        super(parentFrame,true);        
        JPanel panel = new CSV1Panel();
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
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
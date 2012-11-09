package ugent.bagger.dialogs;

import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import ugent.bagger.panels.BagValidationResultPanel;

/**
 *
 * @author nicolas
 */
public class BagValidationResultDialog extends JDialog {      
    public BagValidationResultDialog(Frame parentFrame,boolean isModal){
        super(parentFrame,isModal);        
        JPanel panel = new BagValidationResultPanel();
        panel.addPropertyChangeListener("ok",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                dispose();
            }
        });
        setContentPane(panel);        
    }    
}
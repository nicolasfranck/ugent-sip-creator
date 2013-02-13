package ugent.bagger.dialogs;

import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import ugent.bagger.panels.ValidateManifestPanel;

/**
 *
 * @author nicolas
 */
public class ValidateManifestDialog extends JDialog {      
    public ValidateManifestDialog(Frame parentFrame,boolean isModal){
        super(parentFrame,true);        
        JPanel panel = new ValidateManifestPanel();
        panel.addPropertyChangeListener("cancel",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                ValidateManifestDialog.this.dispose();
            }
        });
        setContentPane(panel);
    }    
}
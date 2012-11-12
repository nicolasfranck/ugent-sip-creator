package ugent.bagger.dialogs;

import java.awt.Frame;
import javax.swing.JDialog;
import ugent.bagger.panels.ValidateManifestPanel;

/**
 *
 * @author nicolas
 */
public class ValidateManifestDialog extends JDialog {      
    public ValidateManifestDialog(Frame parentFrame,boolean isModal){
        super(parentFrame,true);        
        setContentPane(new ValidateManifestPanel());
    }    
}
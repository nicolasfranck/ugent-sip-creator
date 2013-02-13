package ugent.bagger.dialogs;

import com.anearalone.mets.MdSec;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import ugent.bagger.panels.EditMdSecPanel;

/**
 *
 * @author nicolas
 */
public class EditMdSecDialog extends JDialog {      
    public EditMdSecDialog(Frame parentFrame,boolean isModal,MdSec mdSec){
        super(parentFrame,isModal);
        assert(mdSec != null);
        JPanel panel = new EditMdSecPanel(mdSec);
        panel.addPropertyChangeListener("save",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                dispose();
            }
        });
        setContentPane(panel);        
    }    
}
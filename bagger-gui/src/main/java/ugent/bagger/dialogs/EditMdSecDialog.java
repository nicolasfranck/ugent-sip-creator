package ugent.bagger.dialogs;

import com.anearalone.mets.MdSec;
import java.awt.Frame;
import javax.swing.JDialog;
import ugent.bagger.panels.EditMdSecPanel;

/**
 *
 * @author nicolas
 */
public class EditMdSecDialog extends JDialog {      
    public EditMdSecDialog(Frame parentFrame,boolean isModal,MdSec mdSec){
        super(parentFrame,isModal);
        assert(mdSec != null);
        setContentPane(new EditMdSecPanel(mdSec));        
    }    
}
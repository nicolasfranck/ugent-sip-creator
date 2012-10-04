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
    public EditMdSecDialog(Frame parentFrame,MdSec mdSec){
        super(parentFrame,true);
        assert(mdSec != null);
        setContentPane(new EditMdSecPanel(mdSec));        
    }    
}
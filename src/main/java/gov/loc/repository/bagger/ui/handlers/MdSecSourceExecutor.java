package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.ui.BagView;
import java.awt.Dialog;
import javax.swing.JDialog;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.dialogs.MdSecSourceDialog;
import ugent.bagger.helper.SwingUtils;

/**
 *
 * @author nicolas
 */
public class MdSecSourceExecutor extends AbstractActionCommandExecutor{
    @Override
    public void execute(){ 
        BagView bagView = BagView.getInstance();
        Mets mets = bagView.getInfoFormsPane().getInfoInputPane().getMets();
        JDialog dialog = new MdSecSourceDialog(SwingUtils.getFrame(),mets);        
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);                    
        dialog.pack();
        dialog.setVisible(true);        
    }
}

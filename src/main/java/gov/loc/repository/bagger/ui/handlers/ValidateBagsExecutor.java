package gov.loc.repository.bagger.ui.handlers;

import java.awt.Dialog;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.dialogs.BagValidationResultDialog;
import ugent.bagger.helper.SwingUtils;

/**
 *
 * @author nicolas
 */
public class ValidateBagsExecutor extends AbstractActionCommandExecutor{
    @Override
    public void execute(){          
        BagValidationResultDialog dialog = new BagValidationResultDialog(SwingUtils.getFrame(),true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);  
        SwingUtils.centerOnParent(dialog,true);
        dialog.setVisible(true);
    }
}
package gov.loc.repository.bagger.ui.handlers;

import java.awt.Dialog;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.dialogs.ValidateManifestDialog;
import ugent.bagger.helper.SwingUtils;

public class ValidateManifestExecutor extends AbstractActionCommandExecutor {      
    public ValidateManifestExecutor() {
        super();        
    }
    @Override
    public void execute() {        
        ValidateManifestDialog dialog = new ValidateManifestDialog(SwingUtils.getFrame(),true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);  
        dialog.pack();
        dialog.setVisible(true);
    }
}
package gov.loc.repository.bagger.ui.handlers;

import java.awt.Dialog;
import javax.swing.JDialog;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.wizards.CSVWizardDialog;

/**
 *
 * @author nicolas
 */
public class CSVExecutor extends AbstractActionCommandExecutor {
    @Override
    public void execute() {        
        CSVWizardDialog wdialog = new CSVWizardDialog();
        JDialog dialog = wdialog.getDialog();
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);                    
        dialog.pack();
        dialog.setVisible(true);
    }
}

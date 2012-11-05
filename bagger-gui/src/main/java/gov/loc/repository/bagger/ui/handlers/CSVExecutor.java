package gov.loc.repository.bagger.ui.handlers;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.wizards.CSVWizardDialog;

/**
 *
 * @author nicolas
 */
public class CSVExecutor extends AbstractActionCommandExecutor {
    @Override
    public void execute() {        
        new CSVWizardDialog().showDialog();                
    }
}
package gov.loc.repository.bagger.ui.handlers;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.wizards.ExportWizardDialog;

public class ExportExecutor extends AbstractActionCommandExecutor { 
    public ExportExecutor() {
        super();        
    }   
    @Override
    public void execute() {        
        new ExportWizardDialog("ExportWizardDialog").showDialog();                
    }
}
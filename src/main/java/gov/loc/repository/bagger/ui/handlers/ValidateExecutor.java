package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class ValidateExecutor extends AbstractActionCommandExecutor{
    
    public ValidateExecutor() {
        super();           
    }
    @Override
    public void execute() {       
        BagView.getInstance().validateBagHandler.validateBag();
    }
}
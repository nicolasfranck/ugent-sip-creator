package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class CreateBagsExecutor extends AbstractActionCommandExecutor {    
    public CreateBagsExecutor() {
        super();
        setEnabled(true);            
    }
    @Override
    public void execute() {
        BagView.getInstance().createBagsHandler.createBags();
    }
}
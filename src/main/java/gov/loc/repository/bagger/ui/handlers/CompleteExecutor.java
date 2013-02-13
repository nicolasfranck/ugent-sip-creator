package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class CompleteExecutor extends AbstractActionCommandExecutor {    
    public CompleteExecutor() {
        super();		        
    }
    @Override
    public void execute(){        
        BagView.getInstance().completeBagHandler.completeBag();
    }
}
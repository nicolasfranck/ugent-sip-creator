package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class OpenExecutor extends AbstractActionCommandExecutor {    

    public OpenExecutor() {
        super();
        setEnabled(true);            
    }
    @Override
    public void execute(){                
        BagView.getInstance().openBagHandler.openBag();
    }
}
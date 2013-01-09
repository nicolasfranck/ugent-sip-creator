package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class StartExecutor extends AbstractActionCommandExecutor {
    static final long serialVersionUID = 1L;       
 
    public StartExecutor() {
        super();
        setEnabled(true);        
    }
    @Override
    public void execute() {
        BagView.getInstance().startNewBagHandler.newBag();
    }
}
package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class OpenExecutor extends AbstractActionCommandExecutor {
    private static final long serialVersionUID = 1L;

    public OpenExecutor() {
        super();
        setEnabled(true);            
    }
    @Override
    public void execute(){                
        BagView.getInstance().openBagHandler.openBag();
    }
}
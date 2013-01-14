package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class SaveBagAsExecutor extends AbstractActionCommandExecutor {
    public SaveBagAsExecutor() {
        super();        
    }
    @Override
    public void execute() {
        BagView.getInstance().saveBagAsHandler.openSaveBagAsFrame();        
    }
}
package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class SaveBagExecutor extends AbstractActionCommandExecutor {
    private static final long serialVersionUID = 1L;       
 
    public SaveBagExecutor() {
        super();        
    }

    @Override
    public void execute() {
        BagView bagView = BagView.getInstance();
    
        if(bagView.getBagRootPath().exists()) {
            bagView.saveBagHandler.setTmpRootPath(bagView.getBagRootPath());
            bagView.saveBagHandler.confirmWriteBag();
        } else {
            bagView.saveBagHandler.saveBag(bagView.getBagRootPath());
        }
    }
}
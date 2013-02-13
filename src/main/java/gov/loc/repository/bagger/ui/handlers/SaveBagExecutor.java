package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import java.io.File;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class SaveBagExecutor extends AbstractActionCommandExecutor {   
    public SaveBagExecutor() {
        super();        
    }
    @Override
    public void execute() {
        BagView bagView = BagView.getInstance();
        File file = bagView.getBag().getFile();
    
        if(file != null){            
            bagView.saveBagHandler.saveBag(file);            
        }        
    }
}
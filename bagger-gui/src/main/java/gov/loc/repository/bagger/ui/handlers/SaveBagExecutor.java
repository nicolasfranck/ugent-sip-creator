package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import java.io.File;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class SaveBagExecutor extends AbstractActionCommandExecutor {
    private static final long serialVersionUID = 1L;       
 
    public SaveBagExecutor() {
        super();        
    }

    @Override
    public void execute() {
        BagView bagView = BagView.getInstance();
        File file = bagView.getBag().getFile();
    
        if(file != null){
            if(file.exists()) {            
                bagView.saveBagHandler.confirmWriteBag();
            } else {
                bagView.saveBagHandler.saveBag(file);
            }
        }        
    }
}
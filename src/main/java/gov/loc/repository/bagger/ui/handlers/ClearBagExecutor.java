package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class ClearBagExecutor extends AbstractActionCommandExecutor {
    static final Log log = LogFactory.getLog(ClearBagExecutor.class);        
 
    public ClearBagExecutor() {
        super();       
    }
    @Override
    public void execute() {
        try{
            BagView.getInstance().clearBagHandler.closeExistingBag();
        }catch(Exception e) {
            log.error(e.getMessage());           
        }
    }
}
package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class ClearBagExecutor extends AbstractActionCommandExecutor {
    private static final Log log = LogFactory.getLog(ClearBagExecutor.class);    
    private static final long serialVersionUID = 1L; 
 
    public ClearBagExecutor() {
        super();       
    }
    @Override
    public void execute() {
        try {
            BagView.getInstance().clearBagHandler.closeExistingBag();
        } catch (Exception ex) {
            log.error(ex);
            ex.printStackTrace();
        }
    }
}

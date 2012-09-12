package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class ValidateExecutor extends AbstractActionCommandExecutor{
    private static final long serialVersionUID = 1L;

    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
    public ValidateExecutor() {
        super();           
    }
    @Override
    public void execute() {
        BagView.getInstance().validateBagHandler.validateBag();
    }
}
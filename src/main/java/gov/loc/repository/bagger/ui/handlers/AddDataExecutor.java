package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class AddDataExecutor extends AbstractActionCommandExecutor {    

    public AddDataExecutor(){
        super();       
    }
    @Override
    public void execute() {  
        BagView.getInstance().addDataHandler.addData();
    }
}
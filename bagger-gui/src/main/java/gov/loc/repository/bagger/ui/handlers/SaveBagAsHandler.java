package gov.loc.repository.bagger.ui.handlers;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import ugent.bagger.wizards.SaveBagWizardDialog;

public class SaveBagAsHandler extends AbstractAction {
    
    public SaveBagAsHandler() {
        super();        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        openSaveBagAsFrame();
    }
    public void openSaveBagAsFrame(){        
        new SaveBagWizardDialog("SaveBagWizard").showDialog();        
    }
}
package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class UpdateBagHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
  
    public UpdateBagHandler() {
        super();            
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        updateBag();
    }
    public void updateBag() {        
        BagView.getInstance().getInfoFormsPane().getInfoInputPane().updateForms();
    }
}
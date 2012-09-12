package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class UpdateBagHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
    public UpdateBagHandler() {
        super();            
    }    

    @Override
    public void actionPerformed(ActionEvent e) {
        updateBag(BagView.getInstance().getBag());
    }

    public void updateBag(DefaultBag bag) {
        BagView.getInstance().infoInputPane.bagInfoInputPane.updateForms(bag);
    }
}
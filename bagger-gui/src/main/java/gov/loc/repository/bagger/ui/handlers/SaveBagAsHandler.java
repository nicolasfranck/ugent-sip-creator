package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.SaveBagFrame2;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class SaveBagAsHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;    

    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
    public SaveBagAsHandler() {
        super();        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        openSaveBagAsFrame();
    }
    public void openSaveBagAsFrame(){
        BagView bagView = BagView.getInstance();        
        SaveBagFrame2 saveBagFrame = new SaveBagFrame2(bagView.getPropertyMessage("bag.frame.save"));
        saveBagFrame.setBag(bagView.getBag());
        saveBagFrame.setVisible(true);
    }
}
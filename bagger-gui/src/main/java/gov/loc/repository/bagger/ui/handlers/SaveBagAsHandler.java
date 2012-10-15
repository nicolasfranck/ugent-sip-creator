package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.SaveBagDialog;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import ugent.bagger.helper.SwingUtils;

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
        
        SaveBagDialog dialog = new SaveBagDialog(SwingUtils.getFrame(),true,bagView.getPropertyMessage("bag.frame.save"));
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);               
        dialog.setLocationRelativeTo(SwingUtils.getFrame());         
        SwingUtils.centerAt(SwingUtils.getFrame(),dialog);
        dialog.pack();
        dialog.setVisible(true);
        
        //Nicolas Franck
        /*
        SaveBagFrame2 saveBagFrame = new SaveBagFrame2(bagView.getPropertyMessage("bag.frame.save"));
        saveBagFrame.setBag(bagView.getBag());
        saveBagFrame.setVisible(true);*/
    }
}
package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.SaveBagDialog;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import ugent.bagger.dialogs.SDialog;
import ugent.bagger.helper.SwingUtils;

public class SaveBagAsHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;    

    public SaveBagAsHandler() {
        super();        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        openSaveBagAsFrame();
    }
    public void openSaveBagAsFrame(){
        BagView bagView = BagView.getInstance();    
        
        /*
        SaveBagDialog dialog = new SaveBagDialog(SwingUtils.getFrame(),true,bagView.getPropertyMessage("bag.frame.save"));
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);                               
        dialog.pack();     
        dialog.setLocationRelativeTo(SwingUtils.getFrame());                      
        dialog.setVisible(true);*/
        
        SDialog dialog = new SDialog(SwingUtils.getFrame(),true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);                               
        dialog.pack();     
        dialog.setLocationRelativeTo(SwingUtils.getFrame());                      
        dialog.setVisible(true);
    }
}
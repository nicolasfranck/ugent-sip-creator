package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.Progress;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.dialogs.CreateBagsDialog;
import ugent.bagger.helper.SwingUtils;

//Nicolas Franck: based on code of CreateBagInPlaceHandler, but for creation of multiple bagits at once
//profile uitgeschakeld

public class CreateBagsHandler extends AbstractAction implements Progress/*,Loggable*/ {    
    static final Log log = LogFactory.getLog(CreateBagsHandler.class);   
    
    public CreateBagsHandler() {
        super();           
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        execute();
    }
    @Override
    public void execute() {        
        createBags();        
    }
    public void createBags() {
        BagView bagView = BagView.getInstance();        
    
        CreateBagsDialog dialog = new CreateBagsDialog(SwingUtils.getFrame(),true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);  
        SwingUtils.centerOnParent(dialog,true);
        dialog.setVisible(true);
    }
}
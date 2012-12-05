package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import java.awt.Dialog;
import java.io.File;
import java.util.ArrayList;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.dialogs.RenameDialog;
import ugent.bagger.helper.SwingUtils;

public class RenameExecutor extends AbstractActionCommandExecutor {
    private static final long serialVersionUID = 1L;  
    private RenameDialog renameDialog;
 
    public RenameExecutor() {
        super();        
    }

    public RenameDialog getRenameDialog() {
        if(renameDialog == null){
            renameDialog = new RenameDialog(SwingUtils.getFrame(),true);
            renameDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);                          
            renameDialog.pack();
        }
        return renameDialog;
    }
    
    @Override
    public void execute() {        
        File file = BagView.getInstance().getBagRootPath();        
        ArrayList<File>forbiddenFiles = new ArrayList<File>();
        forbiddenFiles.add(file);
        getRenameDialog().setForbiddenFiles(forbiddenFiles);                
        getRenameDialog().setVisible(true);
    }
}
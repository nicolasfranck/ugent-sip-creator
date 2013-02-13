package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import java.awt.Dialog;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.dialogs.RenameDialog;
import ugent.bagger.helper.SwingUtils;

public class RenameExecutor extends AbstractActionCommandExecutor {    
    RenameDialog renameDialog;
 
    public RenameExecutor() {
        super();        
    }

    public RenameDialog getRenameDialog() {
        if(renameDialog == null){
            renameDialog = new RenameDialog(SwingUtils.getFrame(),true);
            renameDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            renameDialog.setBounds(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()
            );
        }
        return renameDialog;
    }
    
    @Override
    public void execute() {        
        SwingUtils.ShowWhile(new Runnable(){
            @Override
            public void run() {
                File file = BagView.getInstance().getBag().getFile();                   
                if(file != null){
                    ArrayList<File>forbiddenFiles = new ArrayList<File>();
                    forbiddenFiles.add(file);
                    getRenameDialog().setForbiddenFiles(forbiddenFiles);                
                }        
                getRenameDialog().setVisible(true);        
            }            
        });
    }
}
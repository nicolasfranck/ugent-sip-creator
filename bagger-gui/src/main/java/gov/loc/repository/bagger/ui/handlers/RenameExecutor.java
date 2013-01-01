package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import java.awt.Dialog;
import java.awt.Dimension;
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
            Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();  
            renameDialog.setBounds(0,0,(int)dim.getWidth(),(int)dim.getHeight());            
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
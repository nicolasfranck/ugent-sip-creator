package ugent.bagger.popupmenus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import ugent.bagger.dialogs.RenameDialog;

/**
 *
 * @author nicolas
 */
public class FileManipulatePopupMenu extends JPopupMenu{
    public FileManipulatePopupMenu(final File [] files,boolean enabled){
        JMenuItem renameItem = new JMenuItem("rename..");
        renameItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                RenameDialog dialog = new RenameDialog();
                dialog.setFiles(files);
                dialog.setVisible(true);
                dialog.pack();
            }            
        });
        renameItem.setEnabled(enabled);
        JMenuItem renumberItem = new JMenuItem("renumber..");
        renumberItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                
            }            
        });
        renumberItem.setEnabled(enabled);
        
        add(renameItem);
        add(renumberItem);
    }
}
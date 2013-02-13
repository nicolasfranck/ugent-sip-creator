package ugent.bagger.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import ugent.bagger.helper.Context;
import ugent.bagger.panels.RenamePanel;

/**
 *
 * @author nicolas
 */
public class RenameDialog extends JDialog {      
    RenamePanel renamePanel;
    JLabel statusBar;
    ArrayList<File>forbiddenFiles;

    public ArrayList<File> getForbiddenFiles() {
        if(forbiddenFiles == null){
            forbiddenFiles = new ArrayList<File>();
        }
        return forbiddenFiles;
    }
    public void setForbiddenFiles(ArrayList<File> forbiddenFiles) {        
        this.forbiddenFiles = forbiddenFiles;
        getRenamePanel().setForbiddenFiles(forbiddenFiles);
    }

    public RenamePanel getRenamePanel() {
        if(renamePanel == null){
            renamePanel = new RenamePanel();
            renamePanel.addPropertyChangeListener("statusMessage",new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent pce) {
                    String message = (String) pce.getNewValue();
                    setStatusMessage(message != null && !message.isEmpty() ? message:" ");
                }
            });
            renamePanel.addPropertyChangeListener("statusError",new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent pce) {
                    String error = (String) pce.getNewValue();
                    setStatusError(error != null && !error.isEmpty() ? error:" ");
                }
            });            
        }
        return renamePanel;
    }
    public JLabel getStatusBar() {
        if(statusBar == null){
            statusBar = new JLabel();                        
            statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));            
        }
        return statusBar;
    }
    public RenameDialog(Frame parentFrame,boolean isModal){
        super(parentFrame,true);   
        setTitle(Context.getMessage("RenameDialog.title"));
        setContentPane(new JScrollPane(createContentPanel()));        
    }    
    public JPanel createContentPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        
        panel.add(getRenamePanel());
        panel.add(getStatusBar(),BorderLayout.SOUTH);
        
        return panel;
    }
    public void setStatusMessage(String message){
        getStatusBar().setText(message);
        getStatusBar().setForeground(Color.BLACK);
    }
    public void setStatusError(String error){
        getStatusBar().setText(error);
        getStatusBar().setForeground(Color.RED);
    }
}
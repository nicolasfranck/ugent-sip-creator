/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author nicolas
 */
public class SwingUtils {
     private static JFileChooser createFileChooser(String title,FileFilter filter,int mode,boolean multiSelectionEnabled){
        JFileChooser fileChooser = new JFileChooser();              
        fileChooser.setDialogTitle(title);            
        fileChooser.setFileFilter(filter);            
        fileChooser.setFileSelectionMode(mode);
        fileChooser.setMultiSelectionEnabled(multiSelectionEnabled);        
        return fileChooser;
    }        
    public static File [] chooseFiles(String title,FileFilter filter,int mode,boolean multiSelectionEnabled){        
        JFileChooser fchooser = createFileChooser(title,filter,mode,multiSelectionEnabled);
        int freturn = fchooser.showOpenDialog(null);
        File [] files = {};
        if(freturn == JFileChooser.APPROVE_OPTION) {
            if(multiSelectionEnabled){
                files = fchooser.getSelectedFiles();
            }else{
                //when multiSelectionEnabled == false, then getSelectedFiles() returns an empty array!
                files = new File [] {fchooser.getSelectedFile()};
            }
            
        }        
        System.out.println("num choosen:"+files.length);
        return files;
    }
    public static void setJComponentsEnabled(JComponent [] components,boolean enabled){
        for(JComponent component:components){
            setJComponentEnabled(component,enabled);
        }
    }
    public static void setJComponentEnabled(JComponent component,boolean enabled){
        component.setEnabled(enabled);        
        for(Component c:component.getComponents()) {
            c.setEnabled(enabled);
        }
    }
    public static void removeAllActionListeners(AbstractButton button){
        for(ActionListener l:button.getActionListeners()){
            button.removeActionListener(l);
        }
    }
}

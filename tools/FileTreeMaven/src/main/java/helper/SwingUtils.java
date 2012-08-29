/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import com.anearalone.mets.StructMap;
import com.anearalone.mets.StructMap.Div;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.springframework.richclient.application.Application;

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
        return chooseFiles(title,filter,mode,multiSelectionEnabled,SwingUtils.getFrame());
    }
    public static File [] chooseFiles(String title,FileFilter filter,int mode,boolean multiSelectionEnabled,Component component){        
        JFileChooser fchooser = createFileChooser(title,filter,mode,multiSelectionEnabled);        
        
        int freturn = fchooser.showOpenDialog(component);
        File [] files = {};
        if(freturn == JFileChooser.APPROVE_OPTION) {
            if(multiSelectionEnabled){
                files = fchooser.getSelectedFiles();                
            }else{
                //when multiSelectionEnabled == false, then getSelectedFiles() returns an empty array!
                files = new File [] {fchooser.getSelectedFile()};
            }
            if(files == null){
                files = new File [] {};
            }            
        }               
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
    public static void monitor(Component component,SwingWorker worker,String title){
        ProgressMonitor progressMonitor = new ProgressMonitor(component,title,"",0,100);        
        progressMonitor.setProgress(0);
        progressMonitor.setMillisToDecideToPopup(100);
        progressMonitor.setMillisToPopup(0);
        worker.addPropertyChangeListener(getProgressListener(progressMonitor));                
        worker.execute();
    }
    private static PropertyChangeListener getProgressListener(final ProgressMonitor progressMonitor){
        return new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {                        
                System.out.println("property change event: "+evt.getPropertyName());
                System.out.println("property value:"+evt.getNewValue());                
                if("progress".compareTo(evt.getPropertyName())==0){                            
                    int progress = (Integer) evt.getNewValue();                            
                    progressMonitor.setProgress(progress);                              
                    progressMonitor.setNote(progress+"%");                    
                }else if(
                    "state".compareTo(evt.getPropertyName()) == 0 && evt.getNewValue().toString().compareTo("DONE") == 0
                ){
                    progressMonitor.close();
                }
            }
        };
    }
    public static DefaultMutableTreeNode pathToTreeNode(Object [] objects){
        return pathToTreeNode(objects,0);
    }
    private static DefaultMutableTreeNode pathToTreeNode(Object [] objects,int offset){
        if(objects == null || objects.length == 0 || offset >= objects.length || offset < 0){
            return null;
        }
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(objects[offset]);
        DefaultMutableTreeNode child = pathToTreeNode(objects,offset+1);
        if(child != null){
            node.add(child);
        }        
        return node;
    }
    public static DefaultMutableTreeNode structMapToTreeNode(StructMap struct){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(struct.getLabel());
        
        return node; 
    }
    public static DefaultMutableTreeNode divToTreeNode(Div div){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(div.getLabel());
        return node; 
    }
    public static JFrame getFrame(){
        return Application.instance().getActiveWindow().getControl();
    }
}

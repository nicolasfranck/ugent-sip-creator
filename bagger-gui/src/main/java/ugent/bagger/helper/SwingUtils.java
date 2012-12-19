package ugent.bagger.helper;

import com.anearalone.mets.StructMap;
import com.anearalone.mets.StructMap.Div;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.statusbar.StatusBar;
import org.springframework.richclient.progress.BusyIndicator;


/**
 *
 * @author nicolas
 */
public class SwingUtils {
    static Log log = LogFactory.getLog(SwingUtils.class);
    static HashMap<String,String>uiManagerMessages;
    static File lastDirectory;

    protected static File getLastDirectory() {
        if(lastDirectory == null){
            String home = System.getProperty("user.home");
            lastDirectory = home != null && !home.isEmpty() ? new File(home) : File.listRoots()[0];            
        }
        return lastDirectory;
    }
    protected static void setLastDirectory(File lastDirectory) {
       SwingUtils.lastDirectory = lastDirectory;
    }
    
    public static JFileChooser createFileChooser(String title,FileFilter filter,int mode,boolean multiSelectionEnabled,int dialogType){
        JFileChooser fileChooser = new JFileChooser();           
        fileChooser.setDialogTitle(title);            
        fileChooser.setFileFilter(filter);            
        fileChooser.setFileSelectionMode(mode);        
        fileChooser.setDialogType(dialogType);       
        fileChooser.setMultiSelectionEnabled(multiSelectionEnabled);        
        fileChooser.setCurrentDirectory(getLastDirectory());
        return fileChooser;
    }        
    public static File [] chooseFiles(String title,FileFilter filter,int mode,boolean multiSelectionEnabled){
        return chooseFiles(title,filter,mode,multiSelectionEnabled,SwingUtils.getFrame(),JFileChooser.OPEN_DIALOG);
    }
    public static File [] chooseFiles(String title,FileFilter filter,int mode,boolean multiSelectionEnabled,Component component,int dialogType){        
        return chooseFiles(
            createFileChooser(title,filter,mode,multiSelectionEnabled,dialogType),
            component
        );        
    }
    public static File [] chooseFiles(JFileChooser fchooser,Component component){        
        fchooser.setCurrentDirectory(getLastDirectory());
        int freturn = 
            fchooser.getDialogType() == JFileChooser.SAVE_DIALOG ? 
                fchooser.showSaveDialog(component) : 
                fchooser.showOpenDialog(component);
        
        File [] files = {};
        if(freturn == JFileChooser.APPROVE_OPTION) {
            if(fchooser.isMultiSelectionEnabled()){
                files = fchooser.getSelectedFiles();                
            }else{
                //when multiSelectionEnabled == false, then getSelectedFiles() returns an empty array!
                files = new File [] {fchooser.getSelectedFile()};
            }
            if(files == null){
                files = new File [] {};
            }            
        }          
        setLastDirectory(fchooser.getCurrentDirectory() != null ? fchooser.getCurrentDirectory():lastDirectory);
        
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
    public static void monitor(SwingWorker worker,String title,String note){
        monitor(getFrame(),worker,title,note);
    }
    public static void monitor(SwingWorker worker,String title,String note,List<PropertyChangeListener>listeners){
        monitor(getFrame(),worker,title,note,listeners);
    }
    public static void monitor(Component component,SwingWorker worker,String title,String note){
        monitor(component,worker,title,note,new ArrayList<PropertyChangeListener>());
    }
    public static void monitor(Component component,SwingWorker worker,String title,String note,List<PropertyChangeListener>listeners){
        ProgressMonitor progressMonitor = new ProgressMonitor(component,title,note,0,100);                                
        /*progressMonitor.setMillisToDecideToPopup(0);
        progressMonitor.setMillisToPopup(0);*/
        progressMonitor.setProgress(0);        
        
        worker.addPropertyChangeListener(getProgressListener(progressMonitor));                
        if(listeners != null){
            for(PropertyChangeListener listener:listeners){
                worker.addPropertyChangeListener(listener);
            }            
        }        
        worker.execute();
    }
    private static PropertyChangeListener getProgressListener(final ProgressMonitor progressMonitor){
        return new PropertyChangeListener() {
            boolean cancelRemoved = false;
            @Override
            public void propertyChange(PropertyChangeEvent evt) {                                        
                if("progress".compareTo(evt.getPropertyName())==0){ 
                    int progress = (Integer) evt.getNewValue();                     
                    progressMonitor.setProgress(progress);                              
                    progressMonitor.setNote(progress+"%");                    
                }else if(
                    "state".compareTo(evt.getPropertyName()) == 0 && evt.getNewValue() == SwingWorker.StateValue.DONE
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
    public static void expandTreeNode(JTree tree,DefaultMutableTreeNode node){
        tree.expandPath(new TreePath(node.getPath()));        
        for(int i = 0;i < node.getChildCount();i++){
            expandTreeNode(tree,(DefaultMutableTreeNode) node.getChildAt(i));
        }        
    }
    public static void ShowMessage(String title,String message){
        ShowMessage(title,message,JOptionPane.INFORMATION_MESSAGE);
    }
    public static void ShowError(String title,String message){
        ShowMessage(title,message,JOptionPane.ERROR_MESSAGE);
    }
    public static void ShowMessage(String title,String message,int type){
        JOptionPane.showMessageDialog(getFrame(),message,title,type);
    }
    public static StatusBar getStatusBar(){
        return Application.instance().getActiveWindow().getStatusBar();
    }
    public static void StatusMessage(String message){
        getStatusBar().setMessage(message);        
    }
    public static void StatusErrorMessage(String errorMessage){
        getStatusBar().setErrorMessage(errorMessage);
    }
    public static void centerAt(Component parent,Component child){        
        Dimension parentDim = parent.getPreferredSize();
        Dimension childDim = child.getPreferredSize();
        int innerX = (int) Math.ceil((parentDim.getWidth() - childDim.getWidth()) / 2.0);
        int innerY = (int) Math.ceil((parentDim.getHeight() - childDim.getHeight()) / 2.0);                        
        child.setLocation(innerX,innerY);
    }
    // Center on parent ( absolute true/false (exact center or 25% upper left) )
    static public void centerOnParent(final Window child,final boolean absolute) {
        child.pack();
        boolean useChildsOwner = child.getOwner() != null ? ((child.getOwner() instanceof JFrame) || (child.getOwner() instanceof JDialog)) : false;
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension parentSize = useChildsOwner ? child.getOwner().getSize() : screenSize ;
        final Point parentLocationOnScreen = useChildsOwner ? child.getOwner().getLocationOnScreen() : new Point(0,0) ;
        final Dimension childSize = child.getSize();
        childSize.width = Math.min(childSize.width, screenSize.width);
        childSize.height = Math.min(childSize.height, screenSize.height);
        child.setSize(childSize);        
        int x;
        int y;
        if ((child.getOwner() != null) && child.getOwner().isShowing()) {
            x = (parentSize.width - childSize.width) / 2;
            y = (parentSize.height - childSize.height) / 2;
            x += parentLocationOnScreen.x;
            y += parentLocationOnScreen.y;
        } else {
            x = (screenSize.width - childSize.width) / 2;
            y = (screenSize.height - childSize.height) / 2;
        }
        if (!absolute) {
            x /= 2;
            y /= 2;
        }
        child.setLocation(x, y);
    }
    public static ComponentLocation locateComponent(JComponent ancestor,JComponent child){
        for(int i = 0;i < ancestor.getComponentCount();i++){
            if(!(ancestor.getComponent(i) instanceof JComponent)){
                continue;
            }
            JComponent sub = (JComponent)ancestor.getComponent(i);
            if(sub == child){
                return new ComponentLocation(ancestor,i);
            }
            
            ComponentLocation l = locateComponent(sub,child);
            if(l != null){
                return l;
            }
        }
       
        return null;
    }
    public static void ShowBusy(){
        BusyIndicator.showAt(getFrame());
    }
    public static void ShowDone(){
        BusyIndicator.clearAt(getFrame());
    }
    public static HashMap<String,String> getUIManagerMessages(){
        if(uiManagerMessages == null){
            uiManagerMessages = new HashMap<String, String>();
            try{                
                Properties props = new Properties();
                props.load(Context.getResourceAsStream("gov/loc/repository/bagger/ui/manager.properties"));
                Enumeration it = props.propertyNames();
                while(it.hasMoreElements()){
                    String key = (String) it.nextElement();
                    uiManagerMessages.put(key,props.getProperty(key));
                }
            }catch(Exception e){                
                log.debug(e);
            }
        }
        return uiManagerMessages;
    }
    public static boolean confirm(String title,String text){
        return confirm(getFrame(),title,text);
    }
    public static boolean confirm(Component parent,String title,String text){               
        return JOptionPane.showConfirmDialog(parent,text,title,JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;        
    }
}
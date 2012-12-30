package ugent.bagger.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import ugent.bagger.panels.SaveBagPanel;

/**
 *
 * @author nicolas
 */
public class SDialog extends JDialog {      
    
    public SDialog(Frame parentFrame,boolean isModal){
        super(parentFrame,true);           
        setContentPane(createContentPanel());        
    }    
    public JPanel createContentPanel(){
        JPanel panel = new JPanel(new BorderLayout());        
       
        SaveBagPanel cpanel = new SaveBagPanel();
        panel.add(cpanel);
        
        cpanel.addPropertyChangeListener("close",new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                SDialog.this.dispose();
            }                    
        });
        
        
        return panel;
    }  
}
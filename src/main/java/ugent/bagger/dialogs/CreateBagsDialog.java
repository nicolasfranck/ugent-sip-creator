package ugent.bagger.dialogs;

import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import ugent.bagger.panels.CreateBagsPanel;

/**
 *
 * @author nicolas
 */
public class CreateBagsDialog extends JDialog {      
    public CreateBagsDialog(Frame parentFrame,boolean isModal){
        super(parentFrame,true);  
        JPanel panel = new CreateBagsPanel();
        panel.addPropertyChangeListener("cancel",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                CreateBagsDialog.this.dispose();
            }
        });
        setContentPane(panel);
    }    
}
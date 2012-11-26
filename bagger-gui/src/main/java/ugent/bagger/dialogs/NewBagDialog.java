package ugent.bagger.dialogs;

import gov.loc.repository.bagger.ui.BagView;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import ugent.bagger.panels.NewBagPanel;

/**
 *
 * @author nicolas
 */
public class NewBagDialog extends JDialog {      
    public NewBagDialog(Frame parentFrame,boolean isModal){
        super(parentFrame,true);     
        final NewBagPanel panel = new NewBagPanel();        
        
        panel.addPropertyChangeListener("cancel",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                NewBagDialog.this.dispose();
            }
        });
        panel.addPropertyChangeListener("ok",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                panel.getNewBagParamsForm().commit();
                BagView.getInstance().startNewBagHandler.createNewBag(
                    panel.getNewBagParams()
                );
                NewBagDialog.this.dispose();
            }
        });
        setContentPane(panel);
    }    
}
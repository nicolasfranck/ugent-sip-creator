package ugent.bagger.dialogs;

import com.anearalone.mets.Mets;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import ugent.bagger.panels.MdSecSourcePanel;

/**
 *
 * @author nicolas
 */
public class MdSecSourceDialog extends JDialog{
    public MdSecSourceDialog(Frame parentFrame,Mets mets){
        super(parentFrame,true);        
        JPanel panel = new MdSecSourcePanel(mets);
        setContentPane(panel);
        PropertyChangeListener l = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if(pce.getPropertyName().equals("ok") || pce.getPropertyName().equals("cancel")){
                    MdSecSourceDialog.this.dispose();
                }
            }
        };
        panel.addPropertyChangeListener(l);        
    }
}

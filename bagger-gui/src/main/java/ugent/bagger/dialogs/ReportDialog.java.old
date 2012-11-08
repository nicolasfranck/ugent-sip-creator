package ugent.bagger.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JPanel;
import ugent.bagger.panels.ReportPanel;

/**
 *
 * @author nicolas
 */
public class ReportDialog extends JDialog{
    public ReportDialog(Frame parentFrame,int numSuccess,ArrayList<String>errors,String labelSuccess,String labelErrors){
        super(parentFrame,true); 
        JPanel panel = new ReportPanel(numSuccess,errors,labelSuccess,labelErrors);
        panel.addPropertyChangeListener("ok",new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                ReportDialog.this.dispose();
            }
        });
        setLayout(new BorderLayout());
        add(panel);
    }
}
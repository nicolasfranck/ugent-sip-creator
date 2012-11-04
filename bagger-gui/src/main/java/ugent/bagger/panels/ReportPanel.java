package ugent.bagger.panels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author nicolas
 */
public class ReportPanel extends JPanel{
    private int numSuccess = 0;
    private ArrayList<String>errors;
    private String labelSuccess;
    private String labelErrors;
    public ReportPanel(int numSuccess,ArrayList<String>errors,String labelSuccess,String labelErrors){
        this.numSuccess = numSuccess >= 0 ? numSuccess : 0;
        this.errors = errors != null ? errors : new ArrayList<String>();
        this.labelSuccess = labelSuccess != null ? labelSuccess: "";
        this.labelErrors = labelErrors != null ? labelErrors: "";
        init();
    }    
    protected void init(){
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        add(constructPair(labelSuccess,""+numSuccess));
        add(constructPair(labelErrors,""+errors.size()));
        if(errors.size() > 0){
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            add(textArea);
        }
        JButton okButton = new JButton("ok");
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                firePropertyChange("ok",null,null);
            }            
        });
        add(okButton);
    }
    protected static JComponent constructPair(String key,String value){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(key));
        panel.add(new JLabel(value));
        return panel;
    }
}

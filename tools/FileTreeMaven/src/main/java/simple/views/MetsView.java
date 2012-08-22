/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.views;

import com.anearalone.mets.Mets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import org.springframework.richclient.application.support.AbstractView;

/**
 *
 * @author nicolas
 */
public class MetsView extends AbstractView{
    private Mets mets;    
    
    @Override
    protected JComponent createControl() {
        return createForm();
    }
    private JComponent createForm(){
        JPanel panel = new JPanel(new SpringLayout()); 
        panel.add(new JLabel("voornaam:"));
        panel.add(new JTextField(15));                
        return panel;
    }
   
}


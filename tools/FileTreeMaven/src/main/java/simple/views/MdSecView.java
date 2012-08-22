/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.views;

import Dialogs.MdSecDialog;
import com.anearalone.mets.MdSec;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.springframework.richclient.application.support.AbstractView;

/**
 *
 * @author nicolas
 */
public class MdSecView extends AbstractView{
    
    private ArrayList<MdSec>data = new ArrayList<MdSec>();
    private boolean isShowing = false;
    @Override
    protected JComponent createControl() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        JButton button = new JButton("show mdSec");                
        panel.add(button);                
        
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(isShowing) {
                    return;
                }
                isShowing = true;
                JDialog dialog = new MdSecDialog((Frame)getWindowControl(),data);                
                dialog.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent ev){                        
                        isShowing = false;                                               
                    }
                });
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });
        return panel;
    }      
   
}

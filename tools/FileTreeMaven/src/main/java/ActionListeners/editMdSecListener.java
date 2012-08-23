/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ActionListeners;

import Dialogs.MdSecDialog;
import com.anearalone.mets.MdSec;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JDialog;

/**
 *
 * @author nicolas
 */
public class editMdSecListener implements ActionListener{
    private ArrayList<MdSec>data;
    private Frame frame;
    public editMdSecListener(Frame frame,final ArrayList<MdSec>data){
        this.data = data;
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent ae) {                    
        JDialog dialog = new MdSecDialog(frame,data);                               
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
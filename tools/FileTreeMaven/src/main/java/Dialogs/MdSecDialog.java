/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dialogs;

import Panels.MdSecPanel;
import com.anearalone.mets.MdSec;
import java.awt.Frame;
import java.util.ArrayList;
import javax.swing.JDialog;

/**
 *
 * @author nicolas
 */
public class MdSecDialog extends JDialog {      
    public MdSecDialog(Frame parentFrame,ArrayList<MdSec>data){
        super(parentFrame,true);
        assert(data != null);
        setContentPane(new MdSecPanel(data));        
    }    
}
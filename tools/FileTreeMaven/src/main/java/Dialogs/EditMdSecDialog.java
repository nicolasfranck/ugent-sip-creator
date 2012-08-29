/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dialogs;

import Panels.EditMdSecPanel;
import com.anearalone.mets.MdSec;
import java.awt.Frame;
import javax.swing.JDialog;

/**
 *
 * @author nicolas
 */
public class EditMdSecDialog extends JDialog {      
    public EditMdSecDialog(Frame parentFrame,MdSec mdSec){
        super(parentFrame,true);
        assert(mdSec != null);
        setContentPane(new EditMdSecPanel(mdSec));        
    }    
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.dialogs;

import com.anearalone.mets.MetsHdr.Agent;
import java.awt.Frame;
import javax.swing.JDialog;
import ugent.bagger.forms.AgentForm;

/**
 *
 * @author nicolas
 */
public class AgentDialog extends JDialog {      
    public AgentDialog(Frame parentFrame,Agent agent){
        super(parentFrame,true);
        assert(agent != null);        
        setContentPane(new AgentForm(agent).getControl());        
    }    
}
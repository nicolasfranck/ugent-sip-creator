/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.views;

import Forms.AgentForm;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsHdr.Agent;
import javax.swing.JComponent;
import org.springframework.richclient.application.support.AbstractView;

/**
 *
 * @author nicolas
 */
public class AgentView extends AbstractView{ 
    @Override
    protected JComponent createControl(){
        Mets mets = new Mets();
        Agent agent = new Agent(Agent.ROLE.ARCHIVIST,"nicolas");
       
        AgentForm form = new AgentForm(agent);
        return form.getControl();        
    }
}

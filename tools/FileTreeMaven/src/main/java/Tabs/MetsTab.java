/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tabs;

import Panels.AgentPanel;
import Panels.DmDSecPanel;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsHdr.Agent;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author nicolas
 */
public class MetsTab extends JPanel{
    public MetsTab(Mets mets){
        JTabbedPane tabs = new JTabbedPane();
        
        AgentPanel agentPanel = new AgentPanel((ArrayList<Agent>)mets.getMetsHdr().getAgent());
        tabs.add(agentPanel,"agent");
        
        DmDSecPanel dmdSecPanel = new DmDSecPanel(((ArrayList<MdSec>)mets.getDmdSec()));
        tabs.add(dmdSecPanel,"dmdSec");
        
        add(tabs);
    }
}

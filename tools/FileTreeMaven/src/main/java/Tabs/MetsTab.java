/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tabs;

import Panels.AgentPanel;
import Panels.DmDSecPanel;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsHdr;
import com.anearalone.mets.MetsHdr.Agent;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author nicolas
 */
public class MetsTab extends JPanel{
    private AgentPanel agentPanel;
    private DmDSecPanel dmdSecPanel;
    private Mets mets;
    public MetsTab(Mets mets){
        assert(mets != null);
        init(mets);
    }
    public void init(Mets mets){
        JTabbedPane tabs = new JTabbedPane();
        
        if(mets.getMetsHdr() == null){            
            mets.setMetsHdr(new MetsHdr());
        }        
        setMets(mets);
        
        tabs.add(getAgentPanel(),"agent");
        
        DmDSecPanel dmdSecPanel = new DmDSecPanel(((ArrayList<MdSec>)mets.getDmdSec()));
        tabs.add(getDmdSecPanel(),"dmdSec");
        
        add(tabs);
    }
    public AgentPanel getAgentPanel() {
        if(agentPanel == null){
            agentPanel = new AgentPanel((ArrayList<Agent>)mets.getMetsHdr().getAgent());
        }
        return agentPanel;
    }
    public void setAgentPanel(AgentPanel agentPanel) {
        this.agentPanel = agentPanel;
    }
    public DmDSecPanel getDmdSecPanel() {
        if(dmdSecPanel == null){
            dmdSecPanel = new DmDSecPanel(((ArrayList<MdSec>)mets.getDmdSec()));
        }
        return dmdSecPanel;
    }
    public void setDmdSecPanel(DmDSecPanel dmdSecPanel) {
        this.dmdSecPanel = dmdSecPanel;
    }    

    public Mets getMets() {        
        return mets;
    }

    public void setMets(Mets mets) {
        this.mets = mets;
    }
    public void reset(Mets mets){
        System.out.println("resetting from MetsTab");
        System.out.println("mets.OBJID:"+mets.getOBJID());
        System.out.println("num agents:"+getMets().getMetsHdr().getAgent().size());
        getAgentPanel().reset((ArrayList<Agent>)mets.getMetsHdr().getAgent());
        getDmdSecPanel().reset((ArrayList<MdSec>)mets.getDmdSec());
        setMets(mets);
    }
    
}

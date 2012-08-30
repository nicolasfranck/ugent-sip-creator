/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Panels;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsHdr;
import com.anearalone.mets.MetsHdr.Agent;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author nicolas
 */
public class MetsPanel extends JPanel{
    private AgentPanel agentPanel;
    private DmdSecPanel dmdSecPanel;
    private Mets mets;
    public MetsPanel(Mets mets){
        assert(mets != null);
        init(mets);
    }
    private void init(Mets mets){
        setLayout(new BorderLayout());
        
        JTabbedPane tabs = new JTabbedPane();
        
        if(mets.getMetsHdr() == null){            
            mets.setMetsHdr(new MetsHdr());
        }        
        setMets(mets);        
        
        setDmdSecPanel(new DmdSecPanel(((ArrayList<MdSec>)mets.getDmdSec())));        
        tabs.add(getDmdSecPanel(),"dmdSec");        
        tabs.add(getAgentPanel(),"agent");        
        
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
    public DmdSecPanel getDmdSecPanel() {
        if(dmdSecPanel == null){
            dmdSecPanel = new DmdSecPanel(((ArrayList<MdSec>)mets.getDmdSec()));
        }
        return dmdSecPanel;
    }
    public void setDmdSecPanel(DmdSecPanel dmdSecPanel) {
        this.dmdSecPanel = dmdSecPanel;
    }    
    public Mets getMets() {        
        return mets;
    }
    public void setMets(Mets mets) {
        this.mets = mets;
    }
    public void reset(Mets mets){       
        getAgentPanel().reset((ArrayList<Agent>)mets.getMetsHdr().getAgent());
        getDmdSecPanel().reset((ArrayList<MdSec>)mets.getDmdSec());
        setMets(mets);
    }    
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.mets;

import com.anearalone.mets.MetsHdr.Agent;

/**
 *
 * @author nicolas
 */
public class AgentAdapter extends Agent{
    public AgentAdapter(Agent.ROLE role,String name){
        super(role,name);
    }  
    public static Agent clone(Agent agent){
        Agent n = new Agent(agent.getROLE(),agent.getName());
        n.setID(agent.getID());
        n.setAGENTTYPE(agent.getAGENTTYPE());
        n.setOTHERROLE(agent.getOTHERROLE());
        n.setOTHERTYPE(agent.getOTHERTYPE());
        return n;        
    }
    public static void dump(Agent agent){
        System.out.println("ID: "+agent.getID());
        System.out.println("name: "+agent.getName());
        System.out.println("role: "+agent.getROLE());
        System.out.println("otherrole: "+agent.getOTHERROLE());
        System.out.println("othertype: "+agent.getOTHERTYPE());
        System.out.println("agenttype: "+agent.getAGENTTYPE());
        System.out.println("note: "+agent.getNote());
    }
}

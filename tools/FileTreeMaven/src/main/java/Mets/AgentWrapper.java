/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mets;

import com.anearalone.mets.MetsHdr.Agent;

/**
 *
 * @author nicolas
 */
public class AgentWrapper extends Agent{
    public AgentWrapper(Agent.ROLE role,String name){
        super(role,name);
    }    
}

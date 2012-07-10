/**
 * 
 * Copyright 2008 The Australian National University (ANU)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package au.edu.apsr.mtk.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class representing a METS metsHdr element
 * 
 * @author Scott Yeadon
 *
 */
public class MetsHdr extends METSElement
{
    private List<Agent> agents = new ArrayList<Agent>();
    private List<AltRecordID> altRecordIDs = new ArrayList<AltRecordID>();
    private MetsDocumentID metsDocumentID = null;
    
    
    /**
     * Construct a METS element
     * 
     * @param n 
     *        A w3c Node, typically an Element
     *        
     * @exception METSException
     *
     */ 
    public MetsHdr(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_METSHDR);
        initStructures();
    }

    
    /**
     * Return an empty agent.
     * 
     * The returned object has no properties or content and is not part
     * of the METS document, it is essentially a constructor of a METS element
     * which is owned by the METS document. The returned object needs to be
     * "filled out" (e.g. with id, additional sub-elements, etc) before being
     * added to the METS document.
     * 
     * @exception METSException
     *
     */
    public Agent newAgent() throws METSException
    {
        return new Agent(this.newElement(Constants.ELEMENT_AGENT));
    }

    
    /**
     * Return an empty altRecordId.
     * 
     * The returned object has no properties or content and is not part
     * of the METS document, it is essentially a constructor of a METS element
     * which is owned by the METS document. The returned object needs to be
     * "filled out" (e.g. with id, additional sub-elements, etc) before being
     * added to the METS document.
     * 
     * @exception METSException
     *
     */
    public AltRecordID newAltRecordID() throws METSException
    {
        return new AltRecordID(this.newElement(Constants.ELEMENT_ALTRECORDID));
    }

    
    /**
     * Return an empty metsDocumentId.
     * 
     * The returned object has no properties or content and is not part
     * of the METS document, it is essentially a constructor of a METS element
     * which is owned by the METS document. The returned object needs to be
     * "filled out" (e.g. with id, additional sub-elements, etc) before being
     * added to the METS document.
     * 
     * @exception METSException
     *
     */
    public MetsDocumentID newMetsDocumentID() throws METSException
    {
        return new MetsDocumentID(this.newElement(Constants.ELEMENT_METSDOCUMENTID));
    }

    
    /**
     * Obtain the ID
     * 
     * @return String 
     *      The ID attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getID()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_ID);
    }
    

    /**
     * Set the ID
     * 
     * @param id 
     *      The ID attribute value
     */
    public void setID(String id)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_ID, id);
    }    

    
    /**
     * Remove the ID attribute
     * 
     */
    public void removeID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_ID);
    }
    
    
    /**
     * Set the adm id
     * 
     * @param admID 
     *      The ADMID attribute value
     */  
    public void setAdmID(String admID)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_ADMID, admID);
    }
    
    
    /**
     * Obtain the adm id
     * 
     * @return String 
     *      The ADMID attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getAdmID()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_ADMID);
    }
    
    
    /**
     * Obtain the adm ids
     * 
     * @return String[] 
     *      The ADMID attribute value split into separate id strings
     *      else a single element array with an empty string
     */      
    public String[] getAdmIDs()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_ADMID).split("\\s+");
    }

    
    /**
     * Remove the ADMID attribute
     * 
     */
    public void removeAdmID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_ADMID);
    }


    /**
     * Obtain the create date
     * 
     * @return String
     *      The CREATEDATE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getCreateDate()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_CREATEDATE);
    }


    /**
     * Set the create date
     * 
     * @param createDate
     *      The CREATEDATE attribute value
     */  
    public void setCreateDate(String createDate)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_CREATEDATE, createDate);
    }


    /**
     * Remove the CREATEDATE attribute
     */  
    public void removeCreateDate()
    {
        super.removeAttribute(Constants.ATTRIBUTE_CREATEDATE);
    }

    
    /**
     * Obtain the last modified date
     * 
     * @return String
     *      The LASTMODDATE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getLastModDate()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_LASTMODDATE);
    }

    
    /**
     * Set the last modified date
     * 
     * @param lastModDate
     *      The LASTMODDATE attribute value
     */  
    public void setLastModDate(String lastModDate)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_LASTMODDATE, lastModDate);
    }


    /**
     * Remove the LASTMODDATE date attribute
     */  
    public void removeLastModDate()
    {
        super.removeAttribute(Constants.ATTRIBUTE_LASTMODDATE);
    }
    

    /**
     * Obtain the record status
     * 
     * @return String
     *      The RECORDSTATUS attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getRecordStatus()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_RECORDSTATUS);
    }
    

    /**
     * Set the record status
     * 
     * @param status
     *      The RECORDSTATUS attribute value
     */  
    public void setRecordStatus(String status)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_RECORDSTATUS, status);
    }
    

    /**
     * Remove the RECORDSTATUS attribute
     */  
    public void removeRecordStatus()
    {
        super.removeAttribute(Constants.ATTRIBUTE_RECORDSTATUS);
    }

    
    /**
     * Obtain the agents from the metsHdr
     * 
     * @return List<Agent> 
     *      A list of all agents within the metsHdr
     *      Empty list if none are found.
     */          
    public List<Agent> getAgents() throws METSException
    {
        return agents;
    }
    
    
    /**
     * Add an agent 
     * 
     * @param agent
     *    an Agent object      
     * 
     * @return Agent 
     *      Any existing agent having an ID attribute value matching
     *      the id of the agent provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public Agent addAgent(Agent agent)
    {
        if (agent.getID().equals(""))
        {
            this.getElement().appendChild(agent.getElement());
            agents.add(agent);
            return null;
        }
        
        for (Iterator<Agent> i = agents.iterator(); i.hasNext();)
        {
            Agent a = i.next();
            if (a.getID().equals(agent.getID()))
            {
                this.getElement().replaceChild(agent.getElement(), a.getElement());
                agents.add(agent);
                return a;
            }
        }

        this.getElement().appendChild(agent.getElement());
        agents.add(agent);

        return null;
    }
    
    
    /**
     * Remove an agent with the ID provided
     * 
     * @param id
     *    the id of the agent to remove
     *
     * @return Agent 
     *      The deleted agent. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public Agent removeAgent(String id)
    {
        for (Iterator<Agent> i = agents.iterator(); i.hasNext();)
        {
            Agent agent = i.next();
            if (agent.getID().equals(id))
            {
                this.getElement().removeChild(agent.getElement());
                i.remove();
                return agent;
            }
        }

        return null;
    }

    
    /**
     * Remove an agent at the position provided
     * 
     * @param pos
     *    the position of the agent element in relation to
     *    other agent elements
     */
    public Agent removeAgent(int pos)
    {
        for (int i = 0; i < agents.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(agents.get(i).getElement());
                return agents.remove(i);
            }
        }

        return null;
    }
    
    
    /**
     * Obtain the altRecordIds
     * 
     * @return List<AltRecordID> 
     *      A list of all altRecordIDs within the metsHdr
     *      Empty list if none are found.
     */          
    public List<AltRecordID> getAltRecordIDs() throws METSException
    {
        return altRecordIDs;
    }
    
    
    /**
     * Add an altRecordID 
     * 
     * @param altRecordID
     *    an AltRecordID object      
     * 
     * @return AltRecordID 
     *      Any existing altRecordID having an ID attribute value matching
     *      the id of the altRecordID provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public AltRecordID addAltRecordID(AltRecordID altRecordID)
    {
        if (altRecordID.getID().equals(""))
        {
            this.getElement().appendChild(altRecordID.getElement());
            altRecordIDs.add(altRecordID);
            return null;
        }
        
        for (Iterator<AltRecordID> i = altRecordIDs.iterator(); i.hasNext();)
        {
            AltRecordID a = i.next();
            if (a.getID().equals(altRecordID.getID()))
            {
                this.getElement().replaceChild(altRecordID.getElement(), a.getElement());
                altRecordIDs.add(altRecordID);
                return a;
            }
        }
        
        this.getElement().appendChild(altRecordID.getElement());
        altRecordIDs.add(altRecordID);

        return null;
    }
    
    
    /**
     * Remove an altRecordID with the ID provided
     * 
     * @param id
     *    the id of the altRecordID to remove
     *
     * @return AltRecordID 
     *      The deleted altRecordID. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public AltRecordID removeAltRecordID(String id)
    {
        for (Iterator<AltRecordID> i = altRecordIDs.iterator(); i.hasNext();)
        {
            AltRecordID a = i.next();
            if (a.getID().equals(id))
            {
                this.getElement().removeChild(a.getElement());
                i.remove();
                return a;
            }
        }

        return null;
    }

    
    /**
     * Remove an altRecordID at the position provided
     * 
     * @param pos
     *    the position of the altRecordID element in relation to
     *    other altRecordID elements
     */
    public AltRecordID removeAltRecordID(int pos)
    {
        for (int i = 0; i < altRecordIDs.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(altRecordIDs.get(i).getElement());
                return altRecordIDs.remove(i);
            }
        }

        return null;
    }
    
    
    /**
     * Add a metsDocumentID 
     * 
     * @param metsDocumentID
     *    a metsDocumentID object      
     * 
     * @return metsDocumentID 
     *      Any existing metsDocunentID. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public MetsDocumentID addMetsDocumentID(MetsDocumentID metsDocumentID) throws METSException
    {        
        if (this.metsDocumentID != null)
        {
            MetsDocumentID temp = new MetsDocumentID(this.getElement().replaceChild(metsDocumentID.getElement(), this.metsDocumentID.getElement()));
            this.metsDocumentID = metsDocumentID;
            return temp; 
        }
        
        NodeList nl = super.getElements(Constants.ELEMENT_METSDOCUMENTID); 
        if (nl.getLength() > 0)
        {
            this.metsDocumentID = new MetsDocumentID(nl.item(0));
            MetsDocumentID temp = new MetsDocumentID(this.getElement().replaceChild(metsDocumentID.getElement(), this.metsDocumentID.getElement()));
            return temp;
        }
        else
        {
            this.getElement().appendChild(metsDocumentID.getElement());
        }
        
        return null;
    }

    
    /**
     * Obtain the metsDocumentID 
     * 
     * @return metsDocumentID 
     *      Any existing metsDocunentID. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public MetsDocumentID getMetsDocumentID() throws METSException
    {        
        if (this.metsDocumentID != null)
        {
            return this.metsDocumentID;
        }
        
        List<Node> l = super.getChildElements(Constants.ELEMENT_METSDOCUMENTID);
        if (!l.isEmpty())
        {
            return new MetsDocumentID(l.get(0));
        }
        
        return null;
    }

    
    /**
     * Remove the MetsDocumentID
     */
    public void removeMetsDocumentID()
    {
        if (metsDocumentID != null)
        {
            this.getElement().removeChild(metsDocumentID.getElement());
            this.metsDocumentID = null;
        }
    }

    /**
     * Initialise the main METSHdr structures
     *       
     * @exception METSException
     */
    private void initStructures() throws METSException
    {
        initMetsDocumentID();
        initAgents();
        initAltRecordIDs();       
    }
 
    
    private void initMetsDocumentID() throws METSException
    {
        NodeList nl = super.getElements(Constants.ELEMENT_METSDOCUMENTID); 
        if (nl.getLength() > 0)
        {
            this.metsDocumentID = new MetsDocumentID(nl.item(0));
        }
    }
    
    
    private void initAgents() throws METSException
    {
        agents = new ArrayList<Agent>();
        List<Node> l = super.getChildElements(Constants.ELEMENT_AGENT);
        for (Iterator<Node> i = l.iterator(); i.hasNext();)
        {
            Node n = i.next();
            agents.add(new Agent(n));
        }        
    }
    
    
    private void initAltRecordIDs() throws METSException
    {
        altRecordIDs = new ArrayList<AltRecordID>();

        List<Node> l = super.getChildElements(Constants.ELEMENT_ALTRECORDID);
        for (Iterator<Node> i = l.iterator(); i.hasNext();)
        {
            Node n = i.next();
            altRecordIDs.add(new AltRecordID(n));
        }        
    }
}
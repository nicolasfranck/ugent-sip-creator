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
 */

package au.edu.apsr.mtk.base;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * Class representing the METS Agent element
 * 
 * @author Scott Yeadon
 *
 */
public class Agent extends METSElement
{
    /**
     * Construct a METS Agent
     * 
     * @param n 
     *        A w3c Node, typically an Element
     *        
     * @exception METSException
     */ 
    public Agent(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_AGENT);
    }
    
    
    /**
     * Obtain the Agent ID
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
     * Set the Agent ID
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
     */
    public void removeID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_ID);
    }

    
    /**
     * Obtain the Agent role
     * 
     * @return String 
     *     The ROLE attribute value  or empty string if
     *     attribute is empty or not present
     */ 
    public String getRole()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_ROLE);
    }

    
    /**
     * Set the Agent role
     * 
     * @param role 
     *     The ROLE attribute value
     */ 
    public void setRole(String role)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_ROLE, role);
    }

    
    /**
     * Obtain the Agent other role
     * 
     * @return String 
     *     The OTHERROLE attribute value or empty string if
     *     attribute is empty or not present
     */ 
    
    public String getOtherRole()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_OTHERROLE);
    }

    
    /**
     * Set the Agent other role
     * 
     * @param role 
     *     The OTHERROLE attribute value
     */ 
    
    public void setOtherRole(String role)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_OTHERROLE, role);
    }

    
    /**
     * Remove the Agent OTHERROLE attribute
     */ 
    
    public void removeOtherRole()
    {
        super.removeAttribute(Constants.ATTRIBUTE_OTHERROLE);
    }

    
    /**
     * Obtain the Agent type
     * 
     * @return String 
     *         The TYPE attribute value or empty string if
     *         attribute is empty or not present
     */     
    public String getType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_TYPE);
    }

    
    /**
     * Set the Agent type
     * 
     * @param type 
     *         The TYPE attribute value
     */     
    public void setType(String type)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_TYPE, type);
    }
    
    
    /**
     * Remove the TYPE attribute
     */
    public void removeType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_TYPE);
    }
    
    
    /**
     * Obtain the Agent other type
     * 
     * @return String 
     *         The OTHERTYPE attribute value or empty string if
     *         attribute is empty or not present
     */ 
    public String getOtherType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_OTHERTYPE);
    }
    
    
    /**
     * Set the Agent other type
     * 
     * @param type 
     *         The OTHERTYPE attribute value
     */ 
    public void setOtherType(String type)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_OTHERTYPE, type);
    }
    
    
    /**
     * Remove the OTHERTYPE attribute
     */
    public void removeOtherType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_OTHERTYPE);
    }
    
    
    /**
     * Obtain the Agent name
     * 
     * @return String 
     *         The NAME element value
     *        
     * @exception METSException
     *         if the name element is not found
     */ 
    public String getName() throws METSException
    {
        List<Node> l = super.getChildElements(Constants.ELEMENT_NAME);
        if (l.size()==0)
        {
            throw new METSException("Name element not found within agent. The name element is a required element.");
        }
        
        return l.get(0).getTextContent();
    }
    
    
    /**
     * Set the Agent name
     * 
     * @param name 
     *         The NAME element value
     */ 
    public void setName(String name)
    {
        Element n = super.newElement(Constants.ELEMENT_NAME);
        n.setTextContent(name);
        
        this.getElement().appendChild(n);
    }
    

    /**
     * Obtain the Agent notes
     * 
     * @return String[] 
     *         An array of strings, each string the value
     *         of a single note element. Empty array if no
     *         note elements.
     */     
    public String[] getNotes() throws METSException
    {
        List<Node> l = super.getChildElements(Constants.ELEMENT_NOTE);
        
        String[] notes = new String[l.size()];
        
        Iterator<Node> it = l.iterator();
        
        for (int i = 0; i < l.size(); i++)
        {
            notes[i] = it.next().getTextContent();
        }
        
        return notes;
    }
    
    
    /**
     * Add an Agent note
     * 
     * @param note 
     *         the value of a single note element
     */     
    public void setNote(String note)
    {
        Element n = super.newElement(Constants.ELEMENT_NOTE);
        n.setTextContent(note);
        
        this.getElement().appendChild(n);        
    }
}
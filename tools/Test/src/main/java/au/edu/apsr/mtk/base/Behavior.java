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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class representing the METS behavior element
 * 
 * @author Scott Yeadon
 *
 */
public class Behavior extends METSElement
{
    private InterfaceDef interfaceDef = null;
    private Mechanism mechanism = null;

    /**
     * Construct a METS behavior
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */ 
    public Behavior(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_BEHAVIOR);
        init();
    }


    /**
     * Return an empty InterfaceDef.
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
    public InterfaceDef newInterfaceDef() throws METSException
    {
        return new InterfaceDef(this.newElement(Constants.ELEMENT_INTERFACEDEF));
    }
   
    
    /**
     * Return an empty mechanism.
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
    public Mechanism newMechanism() throws METSException
    {
        return new Mechanism(this.newElement(Constants.ELEMENT_MECHANISM));
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
     * Obtain the struct id
     * 
     * @return String 
     *      The STRUCTID attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getStructID()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_STRUCTID);
    }

    
    /**
     * Set the struct id
     * 
     * @param structID 
     *      The STRUCTID attribute value
     */  
    public void setStructID(String structID)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_STRUCTID, structID);
    }
    
    
    /**
     * Obtain the struct ids
     * 
     * @return String[] 
     *      The STRUCTID attribute value split into separate struct
     *      id strings else a single element array with an empty string
     */      
    public String[] getStructIDs()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_STRUCTID).split("\\s+");
    }
    

    /**
     * Obtain the behavior type
     * 
     * @return String 
     *      The BTYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getBType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_BTYPE);
    }
    

    /**
     * Set the behavior type
     * 
     * @param bType 
     *      The BTYPE attribute value
     */  
    public void setBType(String bType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_BTYPE, bType);
    }
    

    /**
     * Remove the BTYPE attribute
     */  
    public void removeBType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_BTYPE);
    }

    
    /**
     * Obtain the group id
     * 
     * @return String 
     *      The GROUPID attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getGroupID()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_GROUPID);
    }

    
    /**
     * Set the group id
     * 
     * @param groupID 
     *      The GROUPID attribute value
     */  
    public void setGroupID(String groupID)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_GROUPID, groupID);
    }
    
    
    /**
     * Remove the GROUPID attribute
     */  
    public void removeGroupID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_GROUPID);
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
     * Set the admID
     * 
     * @param admID 
     *      The ADMID attribute value. Multiple IDs are set by passing a
     *      space separated string of IDs.
     */  
    public void setAdmID(String admID)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_ADMID, admID);
    }
    
    
    /**
     * Remove the ADMID attribute
     */  
    public void removeAdmID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_ADMID);
    }
    
    
    /**
     * Obtain the date created
     * 
     * @return String 
     *      The CREATED attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getCreated()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_CREATED);
    }
    
    
    /**
     * Set the date created
     * 
     * @param created 
     *      The CREATED attribute value
     */  
    public void setCreated(String created)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_CREATED, created);
    }

    
    /**
     * Remove the CREATED attribute
     */  
    public void removeCreated()
    {
        super.removeAttribute(Constants.ATTRIBUTE_CREATED);
    }
    
    
    /**
     * Obtain the label
     * 
     * @return String 
     *      The LABEL attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getLabel()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_LABEL);
    }
    
    
    /**
     * Set the label
     * 
     * @param label 
     *      The LABEL attribute value
     */  
    public void setLabel(String label)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_LABEL, label);
    }
    
    
    /**
     * Remove the LABEL attribute
     */  
    public void removeLabel()
    {
        super.removeAttribute(Constants.ATTRIBUTE_LABEL);
    }
    

    /**
     * Obtain the interface definition
     * 
     * @return InterfaceDef 
     *      An InterfaceDef object or null if no interface
     *      definition could be found
     *      
     * @exception METSException     
     */  
    public InterfaceDef getInterfaceDef() throws METSException
    {
        return interfaceDef;
    }
    

    /**
     * Set the interface definition
     * 
     * @param iDef 
     *      An InterfaceDef object
     *      
     * @exception METSException     
     */  
    public void setInterfaceDef(InterfaceDef iDef)
    {
        interfaceDef = iDef;
        this.getElement().appendChild(iDef.getElement());
    }
    
    
    /**
     * Obtain the behavior mechanism
     * 
     * @return Mechanism 
     *      A Mechanism object or null if no mechanism could
     *      be found
     *      
     * @exception METSException     
     */  
    public Mechanism getMechanism() throws METSException
    {
        return mechanism;
    }
    

    /**
     * Set the mechanism
     * 
     * @param mech 
     *      A Mechanism object
     *      
     * @exception METSException     
     */  
    public void setMechanism(Mechanism mech)
    {
        mechanism = mech;
        this.getElement().appendChild(mechanism.getElement());
    }
    
    
    /**
     * Initialise the sub-structures
     *       
     * @exception METSException
     */          
    private void init() throws METSException
    {
        initInterfaceDef();
        initMechanism();
    }
    
    
    /**
     * Initialise the interfaceDef
     *       
     * @exception METSException
     */          
    private void initInterfaceDef() throws METSException
    {
        NodeList nl = super.getElements(Constants.ELEMENT_INTERFACEDEF);

        if (nl.getLength() == 1)
        {
            interfaceDef = new InterfaceDef(nl.item(0));
        }        
    }
    
    
    /**
     * Initialise the mechanism
     *       
     * @exception METSException
     */          
    private void initMechanism() throws METSException
    {
        NodeList nl = super.getElements(Constants.ELEMENT_MECHANISM);

        if (nl.getLength() == 1)
        {
            mechanism = new Mechanism(nl.item(0));
        }        
    }
}
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
 * Class representing the METS mdSec element
 * 
 * @author Scott Yeadon
 *
 */
public class MdSec extends METSElement
{
    MdWrap mdWrap = null;
    MdRef mdRef = null;
    
    
    /**
     * Construct an mdSec
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    protected MdSec(Node n,
                    String name) throws METSException
    {
        super(n, name);
        getMdWrap();
        getMdRef();
    }

    
    /**
     * Return an empty mdWrap.
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
    public MdWrap newMdWrap() throws METSException
    {
        return new MdWrap(this.newElement(Constants.ELEMENT_MDWRAP));
    }

    
    /**
     * Return an empty mdRef.
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
    public MdRef newMdRef() throws METSException
    {
        return new MdRef(this.newElement(Constants.ELEMENT_MDREF));
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
     * Obtain the status
     * 
     * @return String 
     *      The STATUS attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getStatus()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_STATUS);
    }
    
    
    /**
     * Set the status
     * 
     * @param status 
     *      The STATUS attribute value
     */  
    public void setStatus(String status)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_STATUS, status);
    }
    
    
    /**
     * Remove the STATUS attribute
     */  
    public void removeStatus()
    {
        super.removeAttribute(Constants.ATTRIBUTE_STATUS);
    }
    
    
    /**
     * Obtain the mdRef
     * 
     * @return MdRef 
     *      an MdRef object or <code>null</code> if no mdRef
     *      elements exist
     */  
    public MdRef getMdRef() throws METSException
    {
        if (mdRef == null)
        {
            NodeList nl = super.getElements(Constants.ELEMENT_MDREF);
            if (nl.getLength() == 1)
            {
                mdRef = new MdRef(nl.item(0));
            }
        }
        
        return mdRef;
    }
    
    
    /**
     * Set the mdRef
     * 
     * @param newMdRef 
     *      an MdRef object
     */  
    public void setMdRef(MdRef newMdRef) throws METSException
    {
        mdRef = newMdRef;
        this.getElement().appendChild(mdRef.getElement());
    }
    
    
    /**
     * Obtain the mdWrap
     * 
     * @return MdWrap
     *      an MdWrap object or <code>null</code> if no mdWrap
     *      elements exist
     */  
    public MdWrap getMdWrap() throws METSException
    {
        if (mdWrap == null)
        {
            NodeList nl = super.getElements(Constants.ELEMENT_MDWRAP);
            if (nl.getLength() == 1)
            {
                mdWrap = new MdWrap(nl.item(0));
            }
        }

        return mdWrap;
    }
    
    
    /**
     * Set the mdWrap
     * 
     * @param newMdWrap
     *      an MdWrap object
     */  
    public void setMdWrap(MdWrap newMdWrap) throws METSException
    {
        mdWrap = newMdWrap;
        this.getElement().appendChild(mdWrap.getElement());
    }
    
    
    /**
     * Convenience method to obtain an xmlData node
     * 
     * @return org.w3c.dom.Node 
     *      The xmlData Node of this file or <code>null</code>
     *      if none exists      
     */  
    public Node getXmlData()
    {
        if (mdWrap != null)
        {
            return mdWrap.getXmlData();
        }

        return null;
    }


    /**
     * Convenience method to obtain the mdType
     * 
     * @return String 
     *      The MDTYPE of the mdSec or <code>null</code>
     *      if none exists      
     */  
    public String getMDType()
    {
        if (mdWrap != null)
        {
            return mdWrap.getMDType();
        }
        
        if (mdRef != null)
        {
            return mdRef.getMDType();
        }
        
        return null;
    }
}
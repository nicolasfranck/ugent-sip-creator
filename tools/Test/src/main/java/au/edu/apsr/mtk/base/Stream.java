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

import java.util.List;

import org.w3c.dom.Node;


/**
 * Class representing the METS stream element
 * 
 * @author Scott Yeadon
 *
 */
public class Stream extends METSElement
{
    /**
     * Construct a METS stream
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public Stream(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_STREAM);
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
     */
    public void removeID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_ID);
    }
    

    /**
     * Obtain the stream type
     * 
     * @return String 
     *      The STREAMTYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getStreamType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_STREAMTYPE);
    }
    

    /**
     * Set the stream type
     * 
     * @param type 
     *      The STREAMTYPE attribute value
     */  
    public void setStreamType(String type)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_STREAMTYPE, type);
    }
    

    /**
     * Remoave the STREAMTYPE attribute
     */  
    public void removeStreamType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_STREAMTYPE);
    }


    /**
     * Obtain the owner id
     * 
     * @return String 
     *      The OWNERID attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getOwnerID()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_OWNERID);
    }


    /**
     * Set the owner id
     * 
     * @param ownerID 
     *      The OWNERID attribute value
     */  
    public void setOwnerID(String ownerID)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_OWNERID, ownerID);
    }


    /**
     * Remove the OWNERID attribute
     */  
    public void removeOwnerID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_OWNERID);
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
     * Obtain the dmd id
     * 
     * @return String 
     *      The DMDID attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getDmdID()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_DMDID);
    }
    
    
    /**
     * Set the dmd id
     * 
     * @param dmdID 
     *      The DMDID attribute value
     */  
    public void setDmdID(String dmdID)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_DMDID, dmdID);
    }
    
    
    /**
     * Obtain the dmd ids
     * 
     * @return String[] 
     *      The DMDID attribute value split into separate id strings
     *      else a single element array with an empty string
     */      
    public String[] getDmdIDs()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_DMDID).split("\\s+");
    }

    
    /**
     * Remove the DMDID attribute
     */  
    public void removeDmdID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_DMDID);
    }
    

    /**
     * Obtain the BEGIN attribute value
     * 
     * @return String 
     *      The BEGIN attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getBegin()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_BEGIN);
    }


    /**
     * Set the BEGIN attribute value
     * 
     * @param begin 
     *      The BEGIN attribute value
     */  
    public void setBegin(String begin)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_BEGIN, begin);
    }

    
    /**
     * Remove the BEGIN attribute
     */  
    public void removeBegin()
    {
        super.removeAttribute(Constants.ATTRIBUTE_BEGIN);
    }
    

    /**
     * Obtain the END attribute value
     * 
     * @return String 
     *      The END attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getEnd()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_END);
    }


    /**
     * Set the END attribute value
     * 
     * @param end 
     *      The END attribute value
     */  
    public void setEnd(String end)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_END, end);
    }

    
    /**
     * Remove the END attribute
     */  
    public void removeEnd()
    {
        super.removeAttribute(Constants.ATTRIBUTE_END);
    }
    

    /**
     * Obtain the BETYPE attribute value
     * 
     * @return String 
     *      The BETYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getBEType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_BETYPE);
    }


    /**
     * Set the BETYPE attribute value
     * 
     * @param beType 
     *      The BETYPE attribute value
     */  
    public void setBEType(String beType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_BETYPE, beType);
    }

    
    /**
     * Remove the BETYPE attribute
     */  
    public void removeBEType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_BETYPE);
    }


    /**
     * Obtain the stream content
     * 
     * @return List<Node>
     *      a list of child nodes holding stream (meta)data
     */      
    public List<Node> getContent()
    {
        return super.getChildNodes();
    }


    /**
     * Set the stream content
     * 
     * @param n
     *      a node representing the stream.  Note that the
     *      importNode method is used to copy the XML into the new
     *      METS structure, the source from which it comes is not
     *      modified in any way.
     */      
    public void setContent(Node n)
    {
        Node n2 = this.getElement().getOwnerDocument().importNode(n, true);
        this.getElement().appendChild(n2);
    }
}
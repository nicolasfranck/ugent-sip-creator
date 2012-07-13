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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class representing the METS FLocat element
 * 
 * @author Scott Yeadon
 *
 */
public class MdWrap extends METSElement
{
    /**
     * Construct a METS mdWrap
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public MdWrap(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_MDWRAP);
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
     * Obtain the mdType
     * 
     * @return String 
     *      The MDTYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getMDType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_MDTYPE);
    }
    

    /**
     * Obtain the mdType version
     * 
     * @return String 
     *      The MDTYPEVERSION attribute value or empty string if attribute
     *      is empty or not present
     */         
    public String getMDTypeVersion()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_MDTYPEVERSION);
    }

    
    /**
     * Set the mdType
     * 
     * @param mdType 
     *      The MDTYPE attribute value
     */  
    public void setMDType(String mdType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_MDTYPE, mdType);
    }
    

    /**
     * Set the mdType version
     * 
     * @param mdTypeVersion 
     *      The MDTYPEVERSION attribute value
     */         
    public void setMDTypeVersion(String mdTypeVersion)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_MDTYPEVERSION, mdTypeVersion);
    }

    
    /**
     * Set the mdType and mdType version (convenience method)
     * 
     * @param mdType 
     *      The MDTYPE attribute value
     * @param mdTypeVersion 
     *      The MDTYPEVERSION attribute value
     */         
    public void setMDType(String mdType,
                          String mdTypeVersion)
    {
        this.setMDType(mdType);
        this.setMDTypeVersion(mdTypeVersion);
    }

    
    /**
     * Obtain the other mdType
     * 
     * @return String 
     *      The OTHERMDTYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getOtherMDType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_OTHERMDTYPE);
    }

    
    /**
     * Set the other mdType
     * 
     * @param mdType 
     *      The OTHERMDTYPE attribute value
     */  
    public void setOtherMDType(String mdType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_OTHERMDTYPE, mdType);
    }

    
    /**
     * Remove the OTHERMDTYPE attribute
     */  
    public void removeOtherMDType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_OTHERMDTYPE);
    }
    
    
    /**
     * Obtain the MIME type
     * 
     * @return String 
     *      The MIMETYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getMIMEType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_MIMETYPE);
    }
    
    
    /**
     * Set the MIME type
     * 
     * @param mimeType 
     *      The MIMETYPE attribute value
     */  
    public void setMIMEType(String mimeType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_MIMETYPE, mimeType);
    }
    
    
    /**
     * Remove the MIMETYPE attribute
     */  
    public void removeMIMEType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_MIMETYPE);
    }
    
    
    /**
     * Obtain the checksum
     * 
     * @return String 
     *      The CHECKSUM attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getChecksum()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_CHECKSUM);
    }
    
    
    /**
     * Set the checksum
     * 
     * @param checksum 
     *      The CHECKSUM attribute value
     */  
    public void setChecksum(String checksum)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_CHECKSUM, checksum);
    }
    
    
    /**
     * Remove the CHECKSUM attribute
     */  
    public void removeChecksum()
    {
        super.removeAttribute(Constants.ATTRIBUTE_CHECKSUM);
    }
    
    
    /**
     * Obtain the checksum type
     * 
     * @return String 
     *      The CHECKSUMTYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getChecksumType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_CHECKSUMTYPE);
    }
    
    
    /**
     * Set the checksum type
     * 
     * @param cType 
     *      The CHECKSUMTYPE attribute value
     */  
    public void setChecksumType(String cType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_CHECKSUMTYPE, cType);
    }
    
    
    /**
     * Remove the CHECKSUMTYPE attribute
     */  
    public void removeChecksumType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_CHECKSUMTYPE);
    }
    
    
    /**
     * Obtain the file size
     * 
     * @return String 
     *      The SIZE attribute value or -1 if attribute
     *      is empty or not present
     */  
    public long getSize()
    {
        String s = super.getAttributeValue(Constants.ATTRIBUTE_SIZE);
        
        if (s.length()==0)
        {
            return -1;
        }
        return Long.valueOf(s);
    }
    
    
    /**
     * Set the file size
     * 
     * @param size 
     *      The SIZE attribute value
     */  
    public void setSize(long size)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_SIZE, String.valueOf(size));
    }    
    
    
    /**
     * Remove the SIZE attribute
     */  
    public void removeSize()
    {
        super.removeAttribute(Constants.ATTRIBUTE_SIZE);
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
     * Obtain base64 encoded data from the binData element.
     * 
     * @return String 
     *       the encoded data
     * 
     */          
    public String getEncodedData()
    {
        NodeList nl = super.getElements(Constants.ELEMENT_BINDATA);
        if (nl.getLength() == 1)
        {
            return nl.item(0).getTextContent();
        }
        
        return null;
    }
    
    
    /**
     * Create a binData element and set the base64 content.
     * 
     * @param base64 
     *       the encoded data
     * 
     */          
    public void setEncodedData(String base64)
    {
        Element binData = this.newElement(Constants.ELEMENT_BINDATA);
        binData.setTextContent(base64);
        this.getElement().appendChild(binData);
    }

    
    /**
     * Obtain the xmlData wrapper node
     * 
     * @return org.w3c.dom.Node 
     *      The xmlData Node of this file or <code>null</code>
     *      if none exists      
     */          
    public Node getXmlData()
    {
        NodeList nl = super.getElements(Constants.ELEMENT_XMLDATA);
        if (nl.getLength() == 1)
        {
            return nl.item(0);
        }
        
        return null;
    }

    
    /**
     * Create the xmlData wrapper and add the XML
     * 
     * @param n 
     *      The root node of any metadata structure. Note that the
     *      importNode method is used to copy the XML into the new
     *      METS structure, the source from which it comes is not
     *      modified in any way.      
     */          
    public void setXmlData(Node n)
    {
        Element xmlData = this.newElement(Constants.ELEMENT_XMLDATA);
        this.getElement().appendChild(xmlData);
        Node n2 = this.getElement().getOwnerDocument().importNode(n, true);
        xmlData.appendChild(n2);
    }
}
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

/**
 * Class representing the METS mdRef element
 * 
 * @author Scott Yeadon
 *
 */
public class MdRef extends METSElement
{
    /**
     * Construct a METS mdRef
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public MdRef(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_MDREF);
        super.setAttributeValue(Constants.ATTRIBUTE_XLINK_TYPE, Constants.ATTRIBUTE_XLINK_TYPE_VALUE_SIMPLE);
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
     * Obtain the xptr
     * 
     * @return String 
     *      The XPTR attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getXptr()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_XPTR);
    }
    
    
    /**
     * Set the xptr
     * 
     * @param xptr 
     *      The XPTR attribute value
     */  
    public void setXptr(String xptr)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_XPTR, xptr);
    }
    
    
    /**
     * Remove the XPTR attribute
     */  
    public void removeXptr()
    {
        super.removeAttribute(Constants.ATTRIBUTE_XPTR);
    }
    
    
    /**
     * Obtain the loctype
     * 
     * @return String 
     *      The LOCTYPE attribute value or empty string if attribute
     *      is empty or not present
     */     
    public String getLocType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_LOCTYPE);
    }

    
    /**
     * Set the loctype
     * 
     * @param locType
     *      The LOCTYPE attribute value
     */     
    public void setLocType(String locType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_LOCTYPE, locType);
    }


    /**
     * Obtain the otherloctype
     * 
     * @return String 
     *      The OTHERLOCTYPE attribute value or empty string if attribute
     *      is empty or not present
     */     
    public String getOtherLocType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_OTHERLOCTYPE);
    }

    
    /**
     * Set the otherLocType
     * 
     * @param locType
     *      The OTHERLOCTYPE attribute value
     */     
    public void setOtherLocType(String locType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_OTHERLOCTYPE, locType);
    }

    
    /**
     * Remove the OTHERLOCTYPE attribute
     */     
    public void removeOtherLocType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_OTHERLOCTYPE);
    }


    /**
     * Obtain the type
     * 
     * @return String 
     *      The xlink:type attribute value or empty string if attribute
     *      is empty or not present
     */     
    public String getType()
    {
        return Constants.ATTRIBUTE_XLINK_TYPE_VALUE_SIMPLE;
    }


    /**
     * Set the type
     * 
     * @param type 
     *      The xlink:type attribute value
     */     
    public void setType(String type)
    {
        super.setAttributeValueNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_TYPE, Constants.ATTRIBUTE_XLINK_TYPE_VALUE_SIMPLE);
    }


    /**
     * Remove the xlink:type attribute
     */     
    public void removeType()
    {
        super.removeAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_TYPE);
    }

    
    /**
     * Obtain the href
     * 
     * @return String 
     *      The xlink:href attribute value or empty string if attribute
     *      is empty or not present
     */         
    public String getHref()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_HREF);
    }

    
    /**
     * Set the href
     * 
     * @param href 
     *      The xlink:href attribute value
     */         
    public void setHref(String href)
    {
        super.setAttributeValueNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_HREF, href);
    }

    
    /**
     * Remove the xlink:href attribute
     */         
    public void removeHref()
    {
        super.removeAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_HREF);
    }

    
    /**
     * Obtain the role
     * 
     * @return String 
     *      The xlink:role attribute value or empty string if attribute
     *      is empty or not present
     */         
    public String getRole()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_ROLE);
    }

    
    /**
     * Set the role
     * 
     * @param role 
     *      The xlink:role attribute value
     */         
    public void setRole(String role)
    {
        super.setAttributeValueNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ROLE, role);
    }

    
    /**
     * Remove the xlink:role attribute
     */         
    public void removeRole()
    {
        super.removeAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ROLE);
    }

    
    /**
     * Obtain the arcrole
     * 
     * @return String 
     *      The xlink:arcrole attribute value or empty string if attribute
     *      is empty or not present
     */         
    public String getArcRole()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_ARCROLE);
    }

    
    /**
     * Set the arcrole
     * 
     * @param arcRole 
     *      The xlink:arcrole attribute value
     */         
    public void setArcRole(String arcRole)
    {
        super.setAttributeValueNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ARCROLE, arcRole);
    }

    
    /**
     * Remove the xlink:arcrole attribute
     */         
    public void removeArcRole()
    {
        super.removeAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ARCROLE);
    }
    
    
    /**
     * Obtain the title
     * 
     * @return String 
     *      The xlink:title attribute value or empty string if attribute
     *      is empty or not present
     */         
    public String getTitle()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_TITLE);
    }
    
    
    /**
     * Set the title
     * 
     * @param title 
     *      The xlink:title attribute value
     */         
    public void setTitle(String title)
    {
        super.setAttributeValueNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_TITLE, title);
    }
    
    
    /**
     * Remove the xlink:title attribute
     */         
    public void removeTitle()
    {
        super.removeAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_TITLE);
    }
    
    
    /**
     * Obtain the show
     * 
     * @return String 
     *      The xlink:show attribute value or empty string if attribute
     *      is empty or not present
     */         
    public String getShow()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_SHOW);
    }
    
    
    /**
     * Set the show
     * 
     * @param show 
     *      The xlink:show attribute value
     */         
    public void setShow(String show)
    {
        super.setAttributeValueNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_SHOW, show);
    }
    
    
    /**
     * Remove the xlink:show attribute
     */         
    public void removeShow()
    {
        super.removeAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_SHOW);
    }

    
    /**
     * Obtain the actuate
     * 
     * @return String 
     *      The xlink:actuate attribute value or empty string if attribute
     *      is empty or not present
     */         
    public String getActuate()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_ACTUATE);
    }    

    
    /**
     * Set the actuate
     * 
     * @param actuate 
     *      The xlink:actuate attribute value
     */         
    public void setActuate(String actuate)
    {
        super.setAttributeValueNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ACTUATE, actuate);
    }

    
    /**
     * Remove the xlink:actuate attribute
     */         
    public void removeActuate()
    {
        super.removeAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ACTUATE);
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
     * Obtain the othermdType
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
     * Set the otherMDType
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
}
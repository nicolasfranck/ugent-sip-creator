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

import org.w3c.dom.Node;


/**
 * Class representing the METS mptr element
 * 
 * @author Scott Yeadon
 *
 */
public class Mptr extends METSElement
{
    /**
     * Construct a METS fptr
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public Mptr(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_MPTR);
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
     * Obtain the content ids
     * 
     * @return String 
     *      The CONTENTIDS attribute value or empty string if attribute
     *      is empty or not present
     */     
    public String getContentIDs()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_CONTENTIDS);
    }
    

    /**
     * Set the content ids
     * 
     * @param contentIDs 
     *      The CONTENTIDS attribute value
     */     
    public void setContentIDs(String contentIDs)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_CONTENTIDS, contentIDs);
    }

    
    /**
     * Remove the CONTENTIDS attribute
     */      
    public void removeContentIDs()
    {
        super.removeAttribute(Constants.ATTRIBUTE_CONTENTIDS);
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
        return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_TYPE);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_HREF_LOCAL);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_HREF_LOCAL);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ROLE_LOCAL);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ROLE_LOCAL);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ARCROLE_LOCAL);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ARCROLE_LOCAL);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_TITLE_LOCAL);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_TITLE_LOCAL);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_SHOW_LOCAL);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_SHOW_LOCAL);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ACTUATE_LOCAL);
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
        //return super.getAttributeValue(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ACTUATE_LOCAL);
    }

    
    /**
     * Remove the xlink:actuate attribute
     */         
    public void removeActuate()
    {
        super.removeAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_ACTUATE);
    }
}
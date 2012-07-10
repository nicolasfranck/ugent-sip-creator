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
 * Class representing the METS smLocatorLink element
 * 
 * @author Scott Yeadon
 *
 */
public class SmLocatorLink extends METSElement
{
    /**
     * Construct a METS smLink
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public SmLocatorLink(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_SMLOCATORLINK);
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
     * Remove the xlink:title attribute
     */         
    public void removeTitle()
    {
        super.removeAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_TITLE);
    }

    
    /**
     * Obtain the xlink:label
     * 
     * @return String 
     *      The xlink:label attribute value or empty string if attribute
     *      is empty or not present
     */      
    public String getXLinkLabel()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_LABEL);
    }

    
    /**
     * Set the xlink:label
     * 
     * @param label 
     *      The xlink:label attribute value
     */      

    public void setXLinkLabel(String label)
    {
        super.setAttributeValueNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_LABEL, label);
    }

    
    /**
     * Remove the xlink:label attribute
     */      

    public void removeXLinkLabel()
    {
        super.removeAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_LABEL);
    }
}
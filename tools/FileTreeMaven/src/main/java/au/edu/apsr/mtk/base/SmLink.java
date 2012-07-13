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
 * Class representing the METS smLink element
 * 
 * @author Scott Yeadon
 *
 */
public class SmLink extends METSElement
{
    /**
     * Construct a METS smLink
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public SmLink(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_SMLINK);
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

    
    /**
     * Obtain the link target
     * 
     * @return String 
     *      The xlink:to attribute value or empty string if attribute
     *      is empty or not present
     */         
    public String getTo()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_TO);
    }
    
    
    /**
     * Set the link target
     * 
     * @param to 
     *      The xlink:to attribute value
     */         
    public void setTo(String to)
    {
        super.setAttributeValueNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_TO, to);
    }


    /**
     * Obtain the link source
     * 
     * @return String 
     *      The xlink:from attribute value or empty string if attribute
     *      is empty or not present
     */         
    
    public String getFrom()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_FROM);
    }


    /**
     * Set the link source
     * 
     * @param from 
     *      The xlink:from attribute value
     */         
    
    public void setFrom(String from)
    {
        super.setAttributeValueNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_FROM, from);
    }    
}
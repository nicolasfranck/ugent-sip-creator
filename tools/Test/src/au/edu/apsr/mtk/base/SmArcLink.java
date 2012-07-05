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
public class SmArcLink extends METSElement
{
    /**
     * Construct a METS SmArcRef
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public SmArcLink(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_SMARCLINK);
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
     * Obtain the ArcType
     * 
     * @return String 
     *      The ARCTYPE attribute value or empty string if attribute
     *      is empty or not present
     */     
    public String getArcType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_ARCTYPE);
    }
    

    /**
     * Set the ArcType
     * 
     * @param arcType 
     *      The ARCTYPE attribute value
     */
    public void setArcType(String arcType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_ARCTYPE, arcType);
    }    

    
    /**
     * Remove the ARCTYPE attribute
     */
    public void removeArcType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_ARCTYPE);
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
     * Set the amd id
     * 
     * @param amdID 
     *      The AMDID attribute value
     */  
    public void setAmdID(String amdID)
    {
        super.getAttributeValue(Constants.ATTRIBUTE_ADMID, amdID);
    }
    
    
    /**
     * Remove the ADMID attribute
     */  
    public void removeAdmID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_ADMID);
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
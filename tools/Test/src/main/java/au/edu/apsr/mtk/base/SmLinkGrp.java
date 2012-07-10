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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

/**
 * Class representing the METS smLinkGrp element
 * 
 * @author Scott Yeadon
 *
 */
public class SmLinkGrp extends METSElement
{
    private List<SmLocatorLink> smLocatorLinks = null;
    private List<SmArcLink> smArcLinks = null;

    /**
     * Construct a METS smLinkGrp
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public SmLinkGrp(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_SMLINKGRP);
    }

    
    /**
     * Return an empty smLocatorLink.
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
    public SmLocatorLink newSmLocatorLink() throws METSException
    {
        return new SmLocatorLink(this.newElement(Constants.ELEMENT_SMLOCATORLINK));
    }

    
    /**
     * Return an empty smArcLink.
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
    public SmArcLink newSmArcLink() throws METSException
    {
        return new SmArcLink(this.newElement(Constants.ELEMENT_SMARCLINK));
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
     * Obtain the arcLinkOrder
     * 
     * @return String 
     *      The ARCLINKORDER attribute value or empty string if attribute
     *      is empty or not present
     */         
    public String getArcLinkOrder()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_ARCLINKORDER);
    }

    
    /**
     * Set the arcLinkOrder
     * 
     * @param arcLinkOrder
     *      The ARCLINKORDER attribute value
     */         
    public void setArcLinkOrder(String arcLinkOrder)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_ARCLINKORDER, arcLinkOrder);
    }

    
    /**
     * Remove the ARCLINKORDER attribute
     */         
    public void removeArcLinkOrder()
    {
        super.removeAttribute(Constants.ATTRIBUTE_ARCLINKORDER);
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
     * Obtain the smLocatorLinks of this smLinkGrp
     * 
     * @return List<SmLocatorLink> 
     *      A list of all smLocatorLinks within this smLinkGrp
     *      Empty list if none are found.
     *      
     * @exception METSException
     */          
    public List<SmLocatorLink> getSmLocatorLinks() throws METSException
    {
        if (smLocatorLinks == null)
        {
            smLocatorLinks = new ArrayList<SmLocatorLink>();

            List<Node> l = super.getChildElements(Constants.ELEMENT_SMLOCATORLINK);
            for (Iterator<Node> i = l.iterator(); i.hasNext();)
            {
                Node n = i.next();
                smLocatorLinks.add(new SmLocatorLink(n));
            }
        }
        
        return smLocatorLinks;
    }
    
    
    /**
     * Add an smLocatorLink 
     * 
     * @param smLocatorLink
     *    an SmLocatorLink object      
     * 
     * @return SmLocatorLink 
     *      Any existing smLocatorLink having an ID attribute value matching
     *      the id of the smLocatorLink provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public SmLocatorLink addSmLocatorLink(SmLocatorLink smLocatorLink)
    {
        if (smLocatorLink.getID().equals(""))
        {
            this.getElement().appendChild(smLocatorLink.getElement());
            smLocatorLinks.add(smLocatorLink);
            return null;
        }
        
        for (Iterator<SmLocatorLink> i = smLocatorLinks.iterator(); i.hasNext();)
        {
            SmLocatorLink s = i.next();
            if (s.getID().equals(smLocatorLink.getID()))
            {
                this.getElement().replaceChild(smLocatorLink.getElement(), s.getElement());
                smLocatorLinks.add(smLocatorLink);
                return s;
            }
        }

        this.getElement().appendChild(smLocatorLink.getElement());
        smLocatorLinks.add(smLocatorLink);
        
        return null;
    }
    
    
    /**
     * Remove an smLocatorLink with the ID provided
     * 
     * @param id
     *    the id of the smLocatorLink to remove
     *
     * @return SmLocatorLink 
     *      The deleted smLocatorLink. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public SmLocatorLink removeSmLocatorLink(String id)
    {
        for (Iterator<SmLocatorLink> i = smLocatorLinks.iterator(); i.hasNext();)
        {
            SmLocatorLink s = i.next();
            if (s.getID().equals(id))
            {
                this.getElement().removeChild(s.getElement());
                i.remove();
                return s;
            }
        }

        return null;
    }

    
    /**
     * Remove an smLocatorLink at the position provided
     * 
     * @param pos
     *    the position of the smLocatorLink element in relation to
     *    other smLocatorLink elements
     */
    public SmLocatorLink removeSmLocatorLink(int pos)
    {
        for (int i = 0; i < smLocatorLinks.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(smLocatorLinks.get(i).getElement());
                return smLocatorLinks.remove(i);
            }
        }

        return null;
    }
    
    
    /**
     * Obtain the smArcLinks of this smLinkGrp
     * 
     * @return List<SmArcLink> 
     *      A list of all smArcLinks within this smLinkGrp
     *      Empty list if none are found.
     *      
     * @exception METSException
     */          
    public List<SmArcLink> getSmArcLinks() throws METSException
    {
        if (smArcLinks == null)
        {
            smArcLinks = new ArrayList<SmArcLink>();

            List<Node> l = super.getChildElements(Constants.ELEMENT_SMARCLINK);
            for (Iterator<Node> i = l.iterator(); i.hasNext();)
            {
                Node n = i.next();
                smArcLinks.add(new SmArcLink(n));
            }
        }
        
        return smArcLinks;
    }
    
    
    /**
     * Add an smArcLink 
     * 
     * @param smArcLink
     *    an SmArcLink object      
     * 
     * @return SmArcLink 
     *      Any existing smArcLink having an ID attribute value matching
     *      the id of the smArcLink provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public SmArcLink addSmArcLink(SmArcLink smArcLink)
    {
        if (smArcLink.getID().equals(""))
        {
            this.getElement().appendChild(smArcLink.getElement());
            smArcLinks.add(smArcLink);
            return null;
        }
        
        for (Iterator<SmArcLink> i = smArcLinks.iterator(); i.hasNext();)
        {
            SmArcLink s = i.next();
            if (s.getID().equals(smArcLink.getID()))
            {
                this.getElement().replaceChild(smArcLink.getElement(), s.getElement());
                smArcLinks.add(smArcLink);
                return s;
            }
        }

        this.getElement().appendChild(smArcLink.getElement());
        smArcLinks.add(smArcLink);
        
        return null;
    }
    
    
    /**
     * Remove an smArcLink with the ID provided
     * 
     * @param id
     *    the id of the smArcLink to remove
     *
     * @return SmArcLink 
     *      The deleted smArcLink. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public SmArcLink removeSmArcLink(String id)
    {
        for (Iterator<SmArcLink> i = smArcLinks.iterator(); i.hasNext();)
        {
            SmArcLink s = i.next();
            if (s.getID().equals(id))
            {
                this.getElement().removeChild(s.getElement());
                i.remove();
                return s;
            }
        }

        return null;
    }

    
    /**
     * Remove an smArcLink at the position provided
     * 
     * @param pos
     *    the position of the smArcLink element in relation to
     *    other smArcLink elements
     */
    public SmArcLink removeSmArcLink(int pos)
    {
        for (int i = 0; i < smArcLinks.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(smArcLinks.get(i).getElement());
                return smArcLinks.remove(i);
            }
        }

        return null;
    }
}
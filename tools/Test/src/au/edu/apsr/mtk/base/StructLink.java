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
 * Class representing the METS structLink element
 * 
 * @author Scott Yeadon
 *
 */
public class StructLink extends METSElement
{
    private List<SmLink> smLinks = null;
    private List<SmLinkGrp> smLinkGrps = null;
    
    /**
     * Construct a METS structLink
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public StructLink(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_STRUCTLINK);
    }

    
    /**
     * Return an empty smLink.
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
    public SmLink newSmLink() throws METSException
    {
        return new SmLink(this.newElement(Constants.ELEMENT_SMLINK));
    }

    
    /**
     * Return an empty smLinkGrp.
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
    public SmLinkGrp newSmLinkGrp() throws METSException
    {
        return new SmLinkGrp(this.newElement(Constants.ELEMENT_SMLINKGRP));
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
     * Obtain the smLinks of this structLink
     * 
     * @return List<SmLink> 
     *      A list of all smLinks within this structLink
     *      Empty list if none are found.
     *      
     * @exception METSException
     */          
    public List<SmLink> getSmLinks() throws METSException
    {
        if (smLinks == null)
        {
            smLinks = new ArrayList<SmLink>();

            List<Node> l = super.getChildElements(Constants.ELEMENT_SMLINK);
            for (Iterator<Node> i = l.iterator(); i.hasNext();)
            {
                Node n = i.next();
                smLinks.add(new SmLink(n));
            }
        }
        
        return smLinks;
    }
    
    
    /**
     * Add an smLink 
     * 
     * @param smLink
     *    an SmLink object      
     * 
     * @return SmLink 
     *      Any existing smLink having an ID attribute value matching
     *      the id of the smLink provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public SmLink addSmLink(SmLink smLink)
    {
        if (smLink.getID().equals(""))
        {
            this.getElement().appendChild(smLink.getElement());
            smLinks.add(smLink);
            return null;
        }
        
        for (Iterator<SmLink> i = smLinks.iterator(); i.hasNext();)
        {
            SmLink s = i.next();
            if (s.getID().equals(smLink.getID()))
            {
                this.getElement().replaceChild(smLink.getElement(), s.getElement());
                smLinks.add(smLink);
                return s;
            }
        }

        this.getElement().appendChild(smLink.getElement());
        smLinks.add(smLink);
        
        return null;
    }
    
    
    /**
     * Remove an smLink with the ID provided
     * 
     * @param id
     *    the id of the smLink to remove
     *
     * @return SmLink 
     *      The deleted smLink. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public SmLink removeSmLink(String id)
    {
        for (Iterator<SmLink> i = smLinks.iterator(); i.hasNext();)
        {
            SmLink s = i.next();
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
     * Remove an smLink at the position provided
     * 
     * @param pos
     *    the position of the smLink element in relation to
     *    other smLink elements
     */
    public SmLink removeSmLink(int pos)
    {
        for (int i = 0; i < smLinks.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(smLinks.get(i).getElement());
                return smLinks.remove(i);
            }
        }

        return null;
    }
    
    
    /**
     * Obtain the smLinkGrps of this structLink
     * 
     * @return List<SmLinkGrp> 
     *      A list of all smLinkGrps within this structLink
     *      Empty list if none are found.
     *      
     * @exception METSException
     */          
    public List<SmLinkGrp> getSmLinkGrps() throws METSException
    {
        if (smLinkGrps == null)
        {
            smLinkGrps = new ArrayList<SmLinkGrp>();

            List<Node> l = super.getChildElements(Constants.ELEMENT_SMLINKGRP);
            for (Iterator<Node> i = l.iterator(); i.hasNext();)
            {
                Node n = i.next();
                smLinkGrps.add(new SmLinkGrp(n));
            }
        }
        
        return smLinkGrps;
    }
    
    
    /**
     * Add an smLinkGrp 
     * 
     * @param smLinkGrp
     *    an SmLinkGrp object      
     * 
     * @return SmLinkGrp
     *      Any existing smLinkGrp having an ID attribute value matching
     *      the id of the smLinkGrp provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public SmLinkGrp addSmLinkGrp(SmLinkGrp smLinkGrp)
    {
        if (smLinkGrp.getID().equals(""))
        {
            this.getElement().appendChild(smLinkGrp.getElement());
            smLinkGrps.add(smLinkGrp);
            return null;
        }
        
        for (Iterator<SmLinkGrp> i = smLinkGrps.iterator(); i.hasNext();)
        {
            SmLinkGrp s = i.next();
            if (s.getID().equals(smLinkGrp.getID()))
            {
                this.getElement().replaceChild(smLinkGrp.getElement(), s.getElement());
                smLinkGrps.add(smLinkGrp);
                return s;
            }
        }

        this.getElement().appendChild(smLinkGrp.getElement());
        smLinkGrps.add(smLinkGrp);
        
        return null;
    }
    
    
    /**
     * Remove an smLinkGrp with the ID provided
     * 
     * @param id
     *    the id of the smLinkGrp to remove
     *
     * @return SmLinkGrp 
     *      The deleted smLinkGrp. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public SmLinkGrp removeSmLinkGrp(String id)
    {
        for (Iterator<SmLinkGrp> i = smLinkGrps.iterator(); i.hasNext();)
        {
            SmLinkGrp s = i.next();
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
     * Remove an smLinkGrp at the position provided
     * 
     * @param pos
     *    the position of the smLinkGrp element in relation to
     *    other smLinkGrp elements
     */
    public SmLinkGrp removeSmLinkGrp(int pos)
    {
        for (int i = 0; i < smLinkGrps.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(smLinkGrps.get(i).getElement());
                return smLinkGrps.remove(i);
            }
        }

        return null;
    }
}
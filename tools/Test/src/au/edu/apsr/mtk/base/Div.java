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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

/**
 * Class representing the METS div element
 * 
 * @author Scott Yeadon
 *
 */
public class Div extends METSElement
{
    private List<Div> divs = null;
    private List<Fptr> fptrs = null;
    private List<Mptr> mptrs = null;
    
    /**
     * Construct a METS div
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public Div(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_DIV);
        init();
    }

    
    /**
     * Return an empty div.
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
    public Div newDiv() throws METSException
    {
        return new Div(this.newElement(Constants.ELEMENT_DIV));
    }

    
    /**
     * Return an empty mptr.
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
    public Mptr newMptr() throws METSException
    {
        return new Mptr(this.newElement(Constants.ELEMENT_MPTR));
    }
    

    /**
     * Return an empty fptr.
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
    public Fptr newFptr() throws METSException
    {
        return new Fptr(this.newElement(Constants.ELEMENT_FPTR));
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
     * Obtain the type
     * 
     * @return String 
     *      The TYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_TYPE);
    }
    

    /**
     * Set the type
     * 
     * @param type 
     *      The TYPE attribute value
     */  
    public void setType(String type)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_TYPE, type);
    }
    
    
    /**
     * Remove the TYPE attribute
     */
    public void removeType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_TYPE);
    }
    
    
    /**
     * Obtain the order
     * 
     * @return String 
     *      The ORDER attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getOrder()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_ORDER);
    }
    
    
    /**
     * Set the order
     * 
     * @param order 
     *      The ORDER attribute value
     */  
    public void setOrder(String order)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_ORDER, order);
    }
    
    
    /**
     * Remove the ORDER attribute
     */  
    public void removeOrder()
    {
        super.removeAttribute(Constants.ATTRIBUTE_ORDER);
    }
    
    
    /**
     * Obtain the order label
     * 
     * @return String 
     *      The ORDERLABEL attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getOrderLabel()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_ORDERLABEL);
    }
    
    
    /**
     * Set the order label
     * 
     * @param orderLabel 
     *      The ORDERLABEL attribute value
     */  
    public void setOrderLabel(String orderLabel)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_ORDERLABEL, orderLabel);
    }
    
    
    /**
     * Remove the ORDERLABEL attribute
     */  
    public void removeOrderLabel()
    {
        super.removeAttribute(Constants.ATTRIBUTE_ORDERLABEL);
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
     * Remove the DMDID attribute
     */  
    public void removeDmdID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_DMDID);
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
     * Set the admid
     * 
     * @param admID 
     *      The ADMID attribute value
     */  
    public void setAdmID(String admID)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_ADMID, admID);
    }
    
    
    /**
     * Remove the ADMID attribute
     */  
    public void removeAdmID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_ADMID);
    }

    
    /**
     * Obtain the contentIDs
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
     * Set the contentIDs
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
     * Obtain the xlink:label
     * 
     * @return String 
     *      The xlink:label attribute value or empty string if attribute
     *      is empty or not present
     */      

    public String getXLinkLabel()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_LABEL);
        //return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_LABEL_LOCAL, Constants.ATTRIBUTE_XLINK_LABEL_LOCAL);
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
        //return super.getAttributeValue(Constants.ATTRIBUTE_XLINK_LABEL_LOCAL, Constants.ATTRIBUTE_XLINK_LABEL_LOCAL);
    }

    
    /**
     * Remove the xlink:label attribute
     */      

    public void removeXLinkLabel()
    {
        super.removeAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_LABEL);
    }

    
    /**
     * Obtain the child divs of this div with the specified type
     * 
     * @param type
     *      A string whose value will be used to select
     *      child divs with a TYPE attribute of matching
     *      value
     *      
     * @return List<Div> 
     *      A list of child divs of this div whose TYPE attribute
     *      matches the type parameter. Empty list if none are found.
     *      
     * @exception METSException
     */              
    public List<Div> getDivs(String type) throws METSException
    {
        ArrayList<Div> al = new ArrayList<Div>();

        for (Iterator<Div> i = divs.iterator(); i.hasNext();)
        {
            Div div = i.next();
            if (div.getType().equals(type))
            {
                al.add(div);
            }
        }

        return al;        
    }


    /**
     * Obtain the child divs of this div
     * 
     * @return List<Div> 
     *      A list of all child divs of this div.
     *      Empty list if none are found.
     *      
     * @exception METSException
     */          
    public List<Div> getDivs() throws METSException
    {
        return divs;
    }
    
    
    /**
     * Add a div 
     * 
     * @param div
     *    a Div object      
     * 
     * @return Div 
     *      Any existing div having an ID attribute value matching
     *      the id of the div provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public Div addDiv(Div div)
    {
        if (div.getID().equals(""))
        {
            this.getElement().appendChild(div.getElement());
            divs.add(div);
            return null;
        }
        
        for (Iterator<Div> i = divs.iterator(); i.hasNext();)
        {
            Div d = i.next();
            if (d.getID().equals(div.getID()))
            {
                this.getElement().replaceChild(div.getElement(), d.getElement());
                divs.add(div);
                return d;
            }
        }
        
        this.getElement().appendChild(div.getElement());
        divs.add(div);
        
        return null;
    }
    
    
    /**
     * Remove a div with the ID provided
     * 
     * @param id
     *    the id of the div to remove
     *
     * @return Div 
     *      The deleted div. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public Div removeDiv(String id)
    {
        for (Iterator<Div> i = divs.iterator(); i.hasNext();)
        {
            Div div = i.next();
            if (div.getID().equals(id))
            {
                this.getElement().removeChild(div.getElement());
                i.remove();
                return div;
            }
        }

        return null;
    }

    
    /**
     * Remove a div with the TYPE provided
     * 
     * @param type
     *    the type of the div to remove
     * @param all
     *    If <code>true</code> all divs at the same level with the provided
     *    type will be removed, else only the first encountered
     */    
    public void removeDivByType(String type,
                                boolean all)
    {
        for (Iterator<Div> i = divs.iterator(); i.hasNext();)
        {
            Div div = i.next();
            if (div.getType().equals(type))
            {
                this.getElement().removeChild(div.getElement());
                i.remove();
                if (!all)
                {
                    break;
                }
            }
        }
    }

    
    /**
     * Remove a div at the position provided
     * 
     * @param pos
     *    the position of the div element in relation to
     *    other div elements
     */
    public Div removeDiv(int pos)
    {
        for (int i = 0; i < divs.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(divs.get(i).getElement());
                return divs.remove(i);
            }
        }

        return null;
    }

            
    /**
     * Obtain the fptrs of this div
     * 
     * @return List<Fptr> 
     *      A list of all fptrs within this div.
     *      Empty list if none are found.
     *      
     * @exception METSException
     */          
    public List<Fptr> getFptrs() throws METSException
    {
        return fptrs;
    }
    
    
    /**
     * Add an fptr 
     * 
     * @param fptr
     *    an Fptr object      
     * 
     * @return Fptr 
     *      Any existing fptr having an ID attribute value matching
     *      the id of the fptr provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public Fptr addFptr(Fptr fptr)
    {
        if (fptr.getID().equals(""))
        {
            this.getElement().appendChild(fptr.getElement());
            fptrs.add(fptr);
            return null;
        }
        
        for (Iterator<Fptr> i = fptrs.iterator(); i.hasNext();)
        {
            Fptr f = i.next();
            if (f.getID().equals(fptr.getID()))
            {
                this.getElement().replaceChild(fptr.getElement(), f.getElement());
                fptrs.add(fptr);
                return f;
            }
        }

        this.getElement().appendChild(fptr.getElement());
        fptrs.add(fptr);

        return null;
    } 
    
    
    /**
     * Remove an fptr with the ID provided
     * 
     * @param id
     *    the id of the fptr to remove
     *
     * @return FPtr 
     *      The deleted fptr. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public Fptr removeFptr(String id)
    {
        for (Iterator<Fptr> i = fptrs.iterator(); i.hasNext();)
        {
            Fptr fptr = i.next();
            if (fptr.getID().equals(id))
            {
                this.getElement().removeChild(fptr.getElement());
                i.remove();
                return fptr;
            }
        }

        return null;
    }

    
    /**
     * Remove an fptr at the position provided
     * 
     * @param pos
     *    the position of the fptr element in relation to
     *    other fptr elements
     */
    public Fptr removeFptr(int pos)
    {
        for (int i = 0; i < fptrs.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(fptrs.get(i).getElement());
                return fptrs.remove(i);
            }
        }

        return null;
    }
    
    
    /**
     * Obtain the mptrs of this div
     * 
     * @return List<Mptr> 
     *      A list of all mptrs within this div.
     *      Empty list if none are found.
     *      
     * @exception METSException
     */          
    public List<Mptr> getMptrs() throws METSException
    {
        return mptrs;
    }
    
    
    /**
     * Add an mptr 
     * 
     * @param mptr
     *    an Mptr object      
     * 
     * @return Mptr 
     *      Any existing mptr having an ID attribute value matching
     *      the id of the mptr provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public Mptr addMptr(Mptr mptr)
    {
        if (mptr.getID().equals(""))
        {
            this.getElement().appendChild(mptr.getElement());
            mptrs.add(mptr);
            return null;
        }
        
        for (Iterator<Mptr> i = mptrs.iterator(); i.hasNext();)
        {
            Mptr m = i.next();
            if (m.getID().equals(mptr.getID()))
            {
                this.getElement().replaceChild(mptr.getElement(), m.getElement());
                mptrs.add(mptr);
                return m;
            }
        }

        this.getElement().appendChild(mptr.getElement());
        mptrs.add(mptr);

        return null;
    } 
    
    
    /**
     * Remove an mptr with the ID provided
     * 
     * @param id
     *    the id of the mptr to remove
     *
     * @return MPtr 
     *      The deleted mptr. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public Mptr removeMptr(String id)
    {
        for (Iterator<Mptr> i = mptrs.iterator(); i.hasNext();)
        {
            Mptr mptr = i.next();
            if (mptr.getID().equals(id))
            {
                this.getElement().removeChild(mptr.getElement());
                i.remove();
                return mptr;
            }
        }

        return null;
    }

    
    /**
     * Remove an mptr at the position provided
     * 
     * @param pos
     *    the position of the mptr element in relation to
     *    other mptr elements
     */
    public Mptr removeMptr(int pos)
    {
        for (int i = 0; i < mptrs.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(mptrs.get(i).getElement());
                return mptrs.remove(i);
            }
        }

        return null;
    }
        
    
    /**
     * Initialise the sub-structures
     *       
     * @exception METSException
     */          
    private void init() throws METSException
    {
        initDivs();
        initFptrs();
        initMptrs();
    }
    
    
    /**
     * Initialise the list of child divs
     *       
     * @exception METSException
     */          
    private void initDivs() throws METSException
    {
        if (divs == null)
        {
            divs = new ArrayList<Div>();
            List<Node> l = super.getChildElements(Constants.ELEMENT_DIV);
            for (Iterator<Node> i = l.iterator(); i.hasNext();)
            {
                Node n = i.next();
                divs.add(new Div(n));
            }
        }
    }
    
    
    /**
     * Initialise the list of fptrs
     *       
     * @exception METSException
     */          
    private void initFptrs() throws METSException
    {
        if (fptrs == null)
        {
            fptrs = new ArrayList<Fptr>();
            List<Node> l = super.getChildElements(Constants.ELEMENT_FPTR);
            for (Iterator<Node> i = l.iterator(); i.hasNext();)
            {
                Node n = i.next();
                fptrs.add(new Fptr(n));
            }
        }
    }
    
    
    /**
     * Initialise the list of mptrs
     *       
     * @exception METSException
     */          
    private void initMptrs() throws METSException
    {
        if (mptrs == null)
        {
            mptrs = new ArrayList<Mptr>();
            List<Node> l = super.getChildElements(Constants.ELEMENT_MPTR);
            for (Iterator<Node> i = l.iterator(); i.hasNext();)
            {
                Node n = i.next();
                mptrs.add(new Mptr(n));
            }
        }
    }
}
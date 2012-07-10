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
 * Class representing the METS structMap element
 * 
 * @author Scott Yeadon
 *
 */
public class StructMap extends METSElement
{
    private List<Div> divs = new ArrayList<Div>();
    
    /**
     * Construct a METS structMap
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public StructMap(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_STRUCTMAP);
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
        getDivs();

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
        divs = new ArrayList<Div>();
        List<Node> l = super.getChildElements(Constants.ELEMENT_DIV);
        for (Iterator<Node> i = l.iterator(); i.hasNext();)
        {
            Node n = i.next();
            divs.add(new Div(n));
        }
        
        return divs;
    }
}
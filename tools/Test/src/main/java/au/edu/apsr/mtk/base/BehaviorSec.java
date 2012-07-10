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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class representing the METS behavior element
 * 
 * @author Scott Yeadon
 *
 */
public class BehaviorSec extends METSElement
{
    private List<BehaviorSec> behaviorSecs = null;
    //private List<Behavior> behaviors = null;    
    private HashMap<String,Behavior> behaviors = null;
    
    /**
     * Construct a METS behaviorSec
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */ 
    public BehaviorSec(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_BEHAVIORSEC);
        init();
    }
    
    
    /**
     * Return an empty BehaviorSec.
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
    public BehaviorSec newBehaviorSec() throws METSException
    {
        return new BehaviorSec(this.newElement(Constants.ELEMENT_BEHAVIORSEC));
    }
    
    
    /**
     * Return an empty Behavior.
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
    public Behavior newBehavior() throws METSException
    {
        return new Behavior(this.newElement(Constants.ELEMENT_BEHAVIOR));
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
     * Obtain the behaviors
     * 
     * @return List<BehaviorSec> 
     *      A list of all behaviorSecs within this document
     *      Empty list if none are found.
     *      
     * @exception METSException
     */
    public List<BehaviorSec> getBehaviorSecs() throws METSException
    {
        return behaviorSecs;
    }
    
    
    /**
     * Obtain the behaviorSec with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return BehaviorSec 
     *      The behaviorSec having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     */
    public BehaviorSec getBehaviorSec(String id) throws METSException
    {
        for (Iterator<BehaviorSec> i = behaviorSecs.iterator(); i.hasNext();)
        {
            BehaviorSec bs = i.next();
            if (bs.getID().equals(id))
            {
                return bs;
            }
        }

        return null;
    }
    
    
    /**
     * Add a behaviorSec 
     * 
     * @param behaviorSec
     *    a behaviorSec object      
     * 
     * @return boolean
     *    true if added successfully, false if a behaviorSec with an identical
     *    is found.
     */
    public BehaviorSec addBehaviorSec(BehaviorSec behaviorSec)
    {
        if (behaviorSec.getID().equals(""))
        {
            this.getElement().appendChild(behaviorSec.getElement());
            behaviorSecs.add(behaviorSec);
            return null;
        }
        
        for (Iterator<BehaviorSec> i = behaviorSecs.iterator(); i.hasNext();)
        {
            BehaviorSec bs = i.next();
            if (bs.getID().equals(behaviorSec.getID()))
            {
                this.getElement().replaceChild(behaviorSec.getElement(), bs.getElement());
                behaviorSecs.add(behaviorSec);
                return bs;
            }
        }

        this.getElement().appendChild(behaviorSec.getElement());
        behaviorSecs.add(behaviorSec);

        return null;
    }
    
    
    /**
     * Remove a beahviorSec with the ID provided
     * 
     * @param id
     *    the id of the behaviorSec to remove
     *
     * @return BehaviorSec 
     *      The deleted beaviorSec. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public BehaviorSec removeBehaviorSec(String id)
    {
        for (Iterator<BehaviorSec> i = behaviorSecs.iterator(); i.hasNext();)
        {
            BehaviorSec bs = i.next();
            if (bs.getID().equals(id))
            {
                this.getElement().removeChild(bs.getElement());
                i.remove();
                return bs;
            }
        }

        return null;
    }

    
    /**
     * Remove a behaviorSec with the ID provided
     * 
     * @param pos
     *    the position of the behaviorSec element in relation to
     *    other behaviorSec elements
     */
    public BehaviorSec removeBehaviorSec(int pos)
    {
        for (int i = 0; i < behaviorSecs.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(behaviorSecs.get(i).getElement());
                return behaviorSecs.remove(i);
            }
        }

        return null;
    }
    
    
    /**
     * Obtain the behavior with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return Behavior
     *      The behavior having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     */          
    public Behavior getBehavior(String id) throws METSException
    {
        return behaviors.get(id);
    }
    
    
    /**
     * Initialise the sub-structures
     *       
     * @exception METSException
     */          
    private void init() throws METSException
    {
        initBehaviorSecs();
        initBehaviors();
    }

    
    /**
     * Initialise the list of behaviorSecs
     *       
     * @exception METSException
     */          
    private void initBehaviorSecs() throws METSException
    {
        behaviorSecs = new ArrayList<BehaviorSec>();
        List<Node> l = super.getChildElements(Constants.ELEMENT_BEHAVIORSEC);
        for (Iterator<Node> i = l.iterator(); i.hasNext();)
        {
            Node n = i.next();
            behaviorSecs.add(new BehaviorSec(n));
        }
    }
    
    
    /**
     * Initialise the list of behaviors
     *       
     * @exception METSException
     */          
    private void initBehaviors() throws METSException
    {
        behaviors = new HashMap<String,Behavior>();
        
        NodeList nl = super.getElements(Constants.ELEMENT_BEHAVIOR);
        
        for (int i = 0; i < nl.getLength(); i++)
        {
            Behavior behavior = new Behavior(nl.item(i));
            if (behavior.getID().length()==0)
            {
                throw new METSException("Found Behavior with null ID");
            }
            behaviors.put(behavior.getID(), behavior);
        }
    }
}
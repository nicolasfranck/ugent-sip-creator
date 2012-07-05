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

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * <p>Class representing the METS root element</p>
 * 
 * <p>When needing to create a <code>METS</code> object from an existing source stream,
 *  use the <code>METSReader</code> class to create a DOM object, wrap the DOM in the 
 *  <code>METSWrapper</code> class, then obtain the <code>METS</code> object from the 
 *  <code>METSWrapper</code>. For example:
 *  <p><code>METSReader mr = new METSReader();<br></br>
 *     mr.mapToDOM(inputStream);<br></br>
 *     METSWrapper mw = new METSWrapper(mr.getMETSDocument());<br></br>            
 *     METS mets = mw.getMETSObject();</code></p>
 *  </p>
 * 
 * @author Scott Yeadon
 *
 */
public class METS extends METSElement
{
    private MetsHdr metsHdr = null;
    private HashMap<String,DmdSec> dmdSecs = null;
    private List<AmdSec> amdSecs = null;
    private FileSec fileSec = null;
    private StructLink structLink = null;
    private List<BehaviorSec> behaviorSecs = null;
    private List<StructMap> structMaps = null;

    
    /**
     * Construct a METS Document
     * 
     * @param n 
     *        A w3c Node, typically a Document
     * 
     * @exception METSException
     */
    public METS(Node n) throws METSException
    {
        super(n);
        initStructures();
    }

    
    /**
     * Construct an empty METS document comprising of only a mets element
     * 
     * @exception ParserConfigurationException
     *
     */
    public METS() throws ParserConfigurationException, METSException
    {
        super();
        initStructures();
    }
    
    
    /**
     * Return an empty MetsHdr
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
    public MetsHdr newMetsHdr() throws METSException
    {
        return new MetsHdr(this.newElement(Constants.ELEMENT_METSHDR));
    }

    
    /**
     * Return an empty DmdSec.
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
    public DmdSec newDmdSec() throws METSException
    {
        return new DmdSec(this.newElement(Constants.ELEMENT_DMDSEC));
    }
    
    
    /**
     * Return an empty AmdSec.
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
    public AmdSec newAmdSec() throws METSException
    {
        return new AmdSec(this.newElement(Constants.ELEMENT_AMDSEC));
    }


    /**
     * Return an empty FileSec.
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
    public FileSec newFileSec() throws METSException
    {
        return new FileSec(this.newElement(Constants.ELEMENT_FILESEC));
    }

    
    /**
     * Return an empty StructMap.
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
    public StructMap newStructMap() throws METSException
    {
        return new StructMap(this.newElement(Constants.ELEMENT_STRUCTMAP));
    }

    
    /**
     * Return an empty StructLink.
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
    public StructLink newStructLink() throws METSException
    {
        return new StructLink(this.newElement(Constants.ELEMENT_STRUCTLINK));
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
     * Obtain the object id
     * 
     * @return String 
     *      The OBJID attribute value or empty string if attribute
     *      is empty or not present
     */
    public String getObjID()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_OBJID);
    }
    
    
    /**
     * Set the object id
     * 
     * @param objID 
     *      The OBJID attribute value
     */
    public void setObjID(String objID)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_OBJID, objID);
    }

    
    /**
     * Remove the object id attribute
     * 
     */
    public void removeObjID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_OBJID);
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
     *      The LABEL attribute value or empty string if attribute
     *      is empty or not present
     */     
    public void setLabel(String label)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_LABEL, label);
    }

    
    /**
     * Remove the label attribute
     * 
     */     
    public void removeLabel()
    {
        super.removeAttribute(Constants.ATTRIBUTE_LABEL);
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
     * Remove the type attribute
     */
    public void removeType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_TYPE);
    }
    
    
    /**
     * Obtain the profile
     * 
     * @return String 
     *      The PROFILE attribute value or empty string if attribute
     *      is empty or not present
     */
    public String getProfile()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_PROFILE);
    }
    
    
    /**
     * Set the profile
     * 
     * @param profile 
     *      The PROFILE attribute value
     */
    public void setProfile(String profile)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_PROFILE, profile);
    }
    
    
    /**
     * Remove the profile attribute
     */
    public void removeProfile()
    {
        super.removeAttribute(Constants.ATTRIBUTE_PROFILE);
    }
    
    
    /**
     * Obtain the metsHdr if available
     * 
     * @return MetsHdr 
     *      a MetsHdr object or <code>null</code> if none exists
     *      
     * @exception METSException
     */
    public MetsHdr getMetsHdr() throws METSException
    {
        return metsHdr;
    }
    
    
    /**
     * Set the metsHdr
     * 
     * @param hdr 
     *      a MetsHdr object
     */
    public void setMetsHdr(MetsHdr hdr)
    {
        if (metsHdr == null)
        {
            this.getElement().appendChild(hdr.getElement());
        }
        else
        {
            this.getElement().replaceChild(hdr.getElement(), metsHdr.getElement());            
        }
        
        metsHdr = hdr;
    }
    
    
    /**
     * Remove the MetsHdr
     */
    public void removeMetsHdr()
    {
        if (metsHdr != null)
        {
            this.getElement().removeChild(metsHdr.getElement());
            metsHdr = null;
        }
    }
    
    
    /**
     * Obtain the dmdSec with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return DmdSec
     *      The dmdSec having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     */
    public DmdSec getDmdSec(String id) throws METSException
    {
        return dmdSecs.get(id);
    }
    
    
    /**
     * Obtain a dmdSec with the given type from an array
     * of ids
     * 
     * @param ids
     *          an array of id strings
     * @param type
     *      the TYPE to match
     * 
     * @return DmdSec 
     *      The dmdSec having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     */
    public DmdSec getDmdSec(String[] ids,
                            String type) throws METSException
    {
        for (int i=0; i<ids.length; i++)
        {
            DmdSec dmdSec = getDmdSec(ids[i]);
            if (dmdSec.getMDType().equals(type))
            {
                return dmdSec;
            }
        }

        return null;
    }
    
    
    /**
     * Add a dmdSec. If a dmdSec with the same id already
     * exists it is replaced with the new one and returned to the caller 
     * 
     * @param dmdSec
     *    a DmdSec object      
     * 
     * @return DmdSec 
     *      Any existing dmdSec having an ID attribute value matching
     *      the id of the dmdSec to add. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public DmdSec addDmdSec(DmdSec dmdSec)
    {
        DmdSec existingDmdSec = dmdSecs.get(dmdSec.getID());
        
        if(existingDmdSec != null)
        {
            //this.getElement().removeChild(dmdSecs.get(dmdSec.getID()).getElement());
            // add new dmdSec element
            //this.getElement().appendChild(dmdSec.getElement());
            this.getElement().replaceChild(dmdSec.getElement(), existingDmdSec.getElement());
        }
        else
        {
            this.getElement().appendChild(dmdSec.getElement());
        }
        
        dmdSecs.put(dmdSec.getID(), dmdSec);
        
        return existingDmdSec;
    }
    
    
    /**
     * Remove a dmdSec with the ID provided
     * 
     * @param id
     *    the id of the DmdSec to remove
     * 
     * @return DmdSec
     *      The dmdSec having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     */
    public DmdSec removeDmdSec(String id)
    {
        DmdSec d = dmdSecs.get(id);
        if (d != null)
        {
            this.getElement().removeChild(dmdSecs.get(id).getElement());
            dmdSecs.remove(id);
        }
        return d;
    }
    
    
    /**
     * Return whether a dmdSec with a given id exists
     * 
     * @param id
     *    the id of the DmdSec to search for
     * 
     * @return boolean
     *      <code>true</code> if a dmdSec with the given id is found
     *      else <code>false</false>
     */
    public boolean hasDmdSec(String id)
    {
        if (dmdSecs.containsKey(id))
        {
            return true;
        }
        
        return false;
    }

    
    /**
     * Obtain the amdSecs
     * 
     * @return List<AmdSec> 
     *      A list of all amdSecs within this document
     *      Empty list if none are found.
     *      
     * @exception METSException
     */
    public List<AmdSec> getAmdSecs() throws METSException
    {
        return amdSecs;
    }
    
    
    /**
     * Obtain the amdSec with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return AmdSec 
     *      The amdSec having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     */
    public AmdSec getAmdSec(String id) throws METSException
    {
        for (Iterator<AmdSec> i = amdSecs.iterator(); i.hasNext();)
        {
            AmdSec am = i.next();
            if (am.getID().equals(id))
            {
                return am;
            }
        }

        return null;
    }
    
    
    /**
     * Add an amdSec 
     * 
     * @param amdSec
     *    an AmdSec object      
     * 
     * @return AmdSec 
     *      Any existing amdSec having an ID attribute value matching
     *      the id of the amdSec provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public AmdSec addAmdSec(AmdSec amdSec)
    {
        if (amdSec.getID().equals(""))
        {
            this.getElement().appendChild(amdSec.getElement());
            amdSecs.add(amdSec);
            return null;
        }
        
        for (Iterator<AmdSec> i = amdSecs.iterator(); i.hasNext();)
        {
            AmdSec am = i.next();
            if (am.getID().equals(amdSec.getID()))
            {
                this.getElement().replaceChild(amdSec.getElement(), am.getElement());
                amdSecs.add(amdSec);
                return am;
            }
        }
        
        return null;
    } 
    
    
    /**
     * Remove an amdSec with the ID provided
     * 
     * @param id
     *    the id of the amdSec to remove
     *
     * @return AmdSec 
     *      The deleted amdSec. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public AmdSec removeAmdSec(String id)
    {
        for (Iterator<AmdSec> i = amdSecs.iterator(); i.hasNext();)
        {
            AmdSec am = i.next();
            if (am.getID().equals(id))
            {
                this.getElement().removeChild(am.getElement());
                i.remove();
                return am;
            }
        }

        return null;
    }

    
    /**
     * Remove an amdSec with at the position provided
     * 
     * @param pos
     *    the position of the amdSec element in relation to
     *    other amdSec elements
     */
    public AmdSec removeAmdSec(int pos)
    {
        for (int i = 0; i < amdSecs.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(amdSecs.get(i).getElement());
                return amdSecs.remove(i);
            }
        }

        return null;
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
     * Obtain the structLink
     * 
     * @return StructLink 
     *      a StructLink object or <code>null</code> if none exists
     */
    public StructLink getStructLink() throws METSException
    {
        return structLink;
    }
    
    
    /**
     * Set the structLink
     * 
     * @param sl 
     *      a StructLink object
     */
    public void setStructLink(StructLink sl)
    {        
        if (structLink == null)
        {
            this.getElement().appendChild(sl.getElement());
        }
        else
        {
            this.getElement().replaceChild(sl.getElement(), structLink.getElement());            
        }
        
        structLink = sl;
    }
    
    
    /**
     * Remove the structLink
     */
    public void removeStructLink()
    {
        if (structLink != null)
        {
            this.getElement().removeChild(structLink.getElement());
            structLink = null;
        }
    }

    
    /**
     * Obtain the fileSec
     * 
     * @return FileSec 
     *      a FileSec object or <code>null</code> if none exists
     */
    public FileSec getFileSec() throws METSException
    {
        return fileSec;
    }    
    
    
    /**
     * Set the fileSec
     * 
     * @param fs
     *      a FileSec object
     */
    public void setFileSec(FileSec fs)
    {        
        if (fileSec == null)
        {
            this.getElement().appendChild(fs.getElement());
        }
        else
        {
            this.getElement().replaceChild(fs.getElement(), fileSec.getElement());            
        }
        
        fileSec = fs;
    }
    
    
    /**
     * Remove the fileSec
     */
    public void removeFileSec()
    {
        if (fileSec != null)
        {
            this.getElement().removeChild(fileSec.getElement());
            fileSec = null;
        }        
    }

    
    /**
     * Obtain the structMaps
     * 
     * @return List<StructMap> 
     *      A list of all structMaps within this document
     *      Empty list if none are found.
     *      
     * @exception METSException
     */
    public List<StructMap> getStructMaps() throws METSException
    {
        return structMaps;
    }
    
    
    /**
     * Obtain the structMap with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return StructMap 
     *      The structMap having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     */
    public StructMap getStructMap(String id) throws METSException
    {
        for (Iterator<StructMap> i = structMaps.iterator(); i.hasNext();)
        {
            StructMap sm = i.next();
            if (sm.getID().equals(id))
            {
                return sm;
            }
        }

        return null;
    }
    
    
    /**
     * Add a structMap
     * 
     * @param structMap
     *    a structMap object      
     * 
     * @return StructMap 
     *      Any existing structMap having an ID attribute value matching
     *      the id of the structMap passed in. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public StructMap addStructMap(StructMap structMap)
    {
        if (structMap.getID().equals(""))
        {
            this.getElement().appendChild(structMap.getElement());
            structMaps.add(structMap);
            return null;
        }
        
        for (Iterator<StructMap> i = structMaps.iterator(); i.hasNext();)
        {
            StructMap sm = i.next();
            if (sm.getID().equals(structMap.getID()))
            {
                this.getElement().replaceChild(structMap.getElement(), sm.getElement());
                structMaps.add(structMap);
                return sm;
            }
        }
        
        return null;
    }

    
    /**
     * Obtain structMaps of a given type
     * 
     * @param type
     *      the TYPE to match
     * 
     * @return List<StructMap> 
     *      A list of StructMap objects of TYPE matching
     *      the type passed in. If not found an empty list is
     *      returned.
     *      
     * @exception METSException
     */
    public List<StructMap> getStructMapByType(String type) throws METSException
    {
        ArrayList<StructMap> al = new ArrayList<StructMap>();
        
        for (Iterator<StructMap> i = structMaps.iterator(); i.hasNext();)
        {
            StructMap sm = i.next();
            if (sm.getType().equals(type))
            {
                al.add(sm);
            }
        }
        
        return al;
    }
    

    /**
     * Initialise the main METS structures
     *       
     * @exception METSException
     */
    private void initStructures() throws METSException
    {
        // get the MetsHdr
        initMetsHdr();
        
        // get any *Sec elements
        initDmdSecs();
        initAmdSecs();
        initBehaviorSecs();
        
        // get the StructLink
        initStructLink();
        
        // get the FileSec 
        initFileSec();
        
        // get any StructMaps
        initStructMaps();
    }
    
    /**
     * Initialise the list of dmdSecs
     *       
     * @exception METSException
     */
    private void initDmdSecs() throws METSException
    {
        dmdSecs = new HashMap<String,DmdSec>();
        
        NodeList nl = super.getElements(Constants.ELEMENT_DMDSEC);
        
        for (int i = 0; i < nl.getLength(); i++)
        {
            DmdSec dmdSec = new DmdSec(nl.item(i));
            if (dmdSec.getID().length()==0)
            {
                throw new METSException("Found DmdSec with null ID");
            }
            dmdSecs.put(dmdSec.getID(), dmdSec);
        }
    }
    
    
    /**
     * Initialise the list of amdSecs
     *       
     * @exception METSException
     */
    private void initAmdSecs() throws METSException
    {
        amdSecs = new ArrayList<AmdSec>();
        
        NodeList nl = super.getElements(Constants.ELEMENT_AMDSEC);
        for (int i = 0; i < nl.getLength(); i++)
        {
            AmdSec as = new AmdSec(nl.item(i));
            amdSecs.add(as);
        }
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
     * Initialise the list of structMaps
     *       
     * @exception METSException
     */
    private void initStructMaps() throws METSException
    {
        structMaps = new ArrayList<StructMap>();
        
        NodeList nl = super.getElements(Constants.ELEMENT_STRUCTMAP);
        for (int i = 0; i < nl.getLength(); i++)
        {
            StructMap sm = new StructMap(nl.item(i));
            structMaps.add(sm);
        }
    }

    
    /**
     * Initialise the MetsHdr
     *       
     * @exception METSException
     */
    private void initMetsHdr() throws METSException
    {
        NodeList nl = super.getElements(Constants.ELEMENT_METSHDR);
    
        if (nl.getLength() == 1)
        {
            metsHdr = new MetsHdr(nl.item(0));
        }
    }
    

    /**
     * Initialise the StructLink
     *       
     * @exception METSException
     */
    private void initStructLink() throws METSException
    {
        NodeList nl = super.getElements(Constants.ELEMENT_STRUCTLINK);
    
        if (nl.getLength() == 1)
        {
            structLink = new StructLink(nl.item(0));
        }
    }
    

    /**
     * Initialise the FileSec
     *       
     * @exception METSException
     */
    private void initFileSec() throws METSException
    {
        NodeList nl = super.getElements(Constants.ELEMENT_FILESEC);
    
        if (nl.getLength() == 1)
        {
            fileSec = new FileSec(nl.item(0));
        }
    }
}
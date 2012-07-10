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

import java.util.HashMap;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class representing the METS amdSec element
 * 
 * @author Scott Yeadon
 *
 */
public class AmdSec extends METSElement
{
    //private List<TechMD> techMDs = null;
    private HashMap<String,TechMD> techMDs = null;
    //private List<SourceMD> sourceMDs = null;
    private HashMap<String,SourceMD> sourceMDs = null;
    //private List<RightsMD> rightsMDs = null;
    private HashMap<String,RightsMD> rightsMDs = null;
    //private List<DigiprovMD> digiprovMDs = null;
    private HashMap<String,DigiprovMD> digiprovMDs = null;
    
    /**
     * Construct a METS amdSec
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */ 
    public AmdSec(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_AMDSEC);
        initStructures();
    }
    
    
    /**
     * Return an empty TechMD.
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
    public TechMD newTechMD() throws METSException
    {
        return new TechMD(this.newElement(Constants.ELEMENT_TECHMD));
    }
   
    
    /**
     * Return an empty SourceMD.
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
    public SourceMD newSourceMD() throws METSException
    {
        return new SourceMD(this.newElement(Constants.ELEMENT_SOURCEMD));
    }
   
    
    /**
     * Return an empty RightsMD.
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
    public RightsMD newRightsMD() throws METSException
    {
        return new RightsMD(this.newElement(Constants.ELEMENT_RIGHTSMD));
    }
   
    
    /**
     * Return an empty DigiprovMD.
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
    public DigiprovMD newDigiprovMD() throws METSException
    {
        return new DigiprovMD(this.newElement(Constants.ELEMENT_DIGIPROVMD));
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
    
/*    
    /**
     * Obtain the techMDs from this amdSec
     * 
     * @return List<TechMD> 
     *      A list of all techMDs within this amdSec.
     *      Empty list if none are found.
     *      
     * @exception METSException
     *      
    public List<TechMD> getTechMDs() throws METSException
    {
        if (techMDs == null)
        {
            initTechMDs();
        }
        
        return techMDs;
    }
    
    
    **
     * Obtain the techMD with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return TechMD 
     *      The techMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     *          
    public TechMD getTechMD(String id) throws METSException
    {
        if (techMDs == null)
        {
            initTechMDs();
        }

        TechMD tm = null;
        
        for (Iterator<TechMD> i = techMDs.iterator(); i.hasNext();)
        {
            TechMD techMD = i.next();
            if (techMD.getID().equals(id))
            {
                tm = techMD;
                break;
            }
        }
        
        return tm;
    }
*/    
    
    /**
     * Obtain the techMD with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return TechMD
     *      The TechMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     */
    public TechMD getTechMD(String id)
    {
        return techMDs.get(id);
    }
    
    
    /**
     * Obtain a techMD with the given type from an array
     * of ids
     * 
     * @param ids
     *          an array of id strings
     * @param type
     *      the TYPE to match
     * 
     * @return TechMD 
     *      The TechMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     */
    public TechMD getTechMD(String[] ids,
                            String type)
    {
        for (int i=0; i<ids.length; i++)
        {
            TechMD tm = getTechMD(ids[i]);
            if (tm.getMDType().equals(type))
            {
                return tm;
            }
        }

        return null;
    }
        
    
    /**
     * Add a techMD. If a techMD with the same id already
     * exists it is replaced with the new one and returned to the caller 
     * 
     * @param techMD
     *    a TechMD object      
     * 
     * @return TechMD 
     *      Any existing techMD having an ID attribute value matching
     *      the id of the techMD to add. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public TechMD addTechMD(TechMD techMD)
    {
        TechMD existingTechMD = techMDs.get(techMD.getID());
        
        if(existingTechMD != null)
        {
            this.getElement().replaceChild(techMD.getElement(), existingTechMD.getElement());
        }
        else
        {
            this.getElement().appendChild(techMD.getElement());
        }
        
        techMDs.put(techMD.getID(), techMD);
        
        return existingTechMD;
    }
    
    
    /**
     * Remove a techMD with the ID provided
     * 
     * @param id
     *    the id of the DmdSec to remove
     * 
     * @return TechMD
     *      The techMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     */
    public TechMD removeTechMD(String id)
    {
        TechMD t = techMDs.get(id);
        if (t != null)
        {
            this.getElement().removeChild(techMDs.get(id).getElement());
            techMDs.remove(id);
        }
        return t;
    }
    
    
    /**
     * Return whether a techMD with a given id exists
     * 
     * @param id
     *    the id of the techMD to search for
     * 
     * @return boolean
     *      <code>true</code> if a techMD with the given id is found
     *      else <code>false</false>
     */
    public boolean hasTechMD(String id)
    {
        if (techMDs.containsKey(id))
        {
            return true;
        }
        
        return false;
    }

/*    
    **
     * Obtain the sourceMDs from this amdSec
     * 
     * @return List<SourceMD> 
     *      A list of all sourceMDs within this amdSec.
     *      Empty list if none are found.
     *      
     * @exception METSException
     *          
    public List<SourceMD> getSourceMDs() throws METSException
    {
*        if (sourceMDs == null)
        {
            initSourceMDs();
        }
*        
        return sourceMDs;
    }
    

    **
     * Obtain the sourceMD with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return SourceMD
     *      The sourceMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     *          
    public SourceMD getSourceMD(String id) throws METSException
    {
*        if (sourceMDs == null)
        {
            initSourceMDs();
        }
*
        SourceMD sm = null;
        
        for (Iterator<SourceMD> i = sourceMDs.iterator(); i.hasNext();)
        {
            SourceMD sourceMD = i.next();
            if (sourceMD.getID().equals(id))
            {
                sm = sourceMD;
                break;
            }
        }
        
        return sm;
    }
*/
    
    /**
     * Obtain the SourceMD with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return SourceMD
     *      The SourceMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     */
    public SourceMD getSourceMD(String id)
    {
        return sourceMDs.get(id);
    }
    
    
    /**
     * Obtain a sourceMD with the given type from an array
     * of ids
     * 
     * @param ids
     *          an array of id strings
     * @param type
     *      the TYPE to match
     * 
     * @return SourceMD 
     *      The SourceMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     */
    public SourceMD getSourceMD(String[] ids,
                            String type)
    {
        for (int i=0; i<ids.length; i++)
        {
            SourceMD tm = getSourceMD(ids[i]);
            if (tm.getMDType().equals(type))
            {
                return tm;
            }
        }

        return null;
    }
        
    
    /**
     * Add a sourceMD. If a sourceMD with the same id already
     * exists it is replaced with the new one and returned to the caller 
     * 
     * @param sourceMD
     *    a SourceMD object      
     * 
     * @return SourceMD 
     *      Any existing sourceMD having an ID attribute value matching
     *      the id of the sourceMD to add. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public SourceMD addSourceMD(SourceMD sourceMD)
    {
        SourceMD existingSourceMD = sourceMDs.get(sourceMD.getID());
        
        if(existingSourceMD != null)
        {
            this.getElement().replaceChild(sourceMD.getElement(), existingSourceMD.getElement());
        }
        else
        {
            this.getElement().appendChild(sourceMD.getElement());
        }
        
        sourceMDs.put(sourceMD.getID(), sourceMD);
        
        return existingSourceMD;
    }
    
    
    /**
     * Remove a sourceMD with the ID provided
     * 
     * @param id
     *    the id of the DmdSec to remove
     * 
     * @return SourceMD
     *      The sourceMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     */
    public SourceMD removeSourceMD(String id)
    {
        SourceMD sourceMD = sourceMDs.get(id);
        if (sourceMD != null)
        {
            this.getElement().removeChild(sourceMDs.get(id).getElement());
            sourceMDs.remove(id);
        }
        return sourceMD;
    }
    
    
    /**
     * Return whether a sourceMD with a given id exists
     * 
     * @param id
     *    the id of the sourceMD to search for
     * 
     * @return boolean
     *      <code>true</code> if a sourceMD with the given id is found
     *      else <code>false</false>
     */
    public boolean hasSourceMD(String id)
    {
        if (sourceMDs.containsKey(id))
        {
            return true;
        }
        
        return false;
    }

    
    /*
     * Obtain the rightsMDs from this amdSec
     * 
     * @return List<RightsMD> 
     *      A list of all rightsMDs within this amdSec.
     *      Empty list if none are found.
     *      
     * @exception METSException
     *          
    public List<RightsMD> getRightsMDs() throws METSException
    {
        return rightsMDs;
    }
    
    
    **
     * Obtain the rightsMD with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return RightsMD
     *      The rightsMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     *          
    public RightsMD getRightsMD(String id) throws METSException
    {
        RightsMD rm = null;
        
        for (Iterator<RightsMD> i = rightsMDs.iterator(); i.hasNext();)
        {
            RightsMD rightsMD = i.next();
            if (rightsMD.getID().equals(id))
            {
                rm = rightsMD;
                break;
            }
        }
        
        return rm;
    }
*/
    /**
     * Obtain the RightsMD with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return RightsMD
     *      The RightsMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     */
    public RightsMD getRightsMD(String id)
    {
        return rightsMDs.get(id);
    }
    
    
    /**
     * Obtain a rightsMD with the given type from an array
     * of ids
     * 
     * @param ids
     *          an array of id strings
     * @param type
     *      the TYPE to match
     * 
     * @return RightsMD 
     *      The RightsMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     */
    public RightsMD getRightsMD(String[] ids,
                            String type)// throws METSException
    {
        for (int i=0; i<ids.length; i++)
        {
            RightsMD tm = getRightsMD(ids[i]);
            if (tm.getMDType().equals(type))
            {
                return tm;
            }
        }

        return null;
    }
        
    
    /**
     * Add a rightsMD. If a rightsMD with the same id already
     * exists it is replaced with the new one and returned to the caller 
     * 
     * @param rightsMD
     *    a RightsMD object      
     * 
     * @return RightsMD 
     *      Any existing rightsMD having an ID attribute value matching
     *      the id of the rightsMD to add. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public RightsMD addRightsMD(RightsMD rightsMD)
    {
        RightsMD existingRightsMD = rightsMDs.get(rightsMD.getID());
        
        if(existingRightsMD != null)
        {
            this.getElement().replaceChild(rightsMD.getElement(), existingRightsMD.getElement());
        }
        else
        {
            this.getElement().appendChild(rightsMD.getElement());
        }
        
        rightsMDs.put(rightsMD.getID(), rightsMD);
        
        return existingRightsMD;
    }
    
    
    /**
     * Remove a rightsMD with the ID provided
     * 
     * @param id
     *    the id of the DmdSec to remove
     * 
     * @return RightsMD
     *      The rightsMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     */
    public RightsMD removeRightsMD(String id)
    {
        RightsMD rightsMD = rightsMDs.get(id);
        if (rightsMD != null)
        {
            this.getElement().removeChild(rightsMDs.get(id).getElement());
            rightsMDs.remove(id);
        }
        return rightsMD;
    }
    
    
    /**
     * Return whether a rightsMD with a given id exists
     * 
     * @param id
     *    the id of the rightsMD to search for
     * 
     * @return boolean
     *      <code>true</code> if a rightsMD with the given id is found
     *      else <code>false</false>
     */
    public boolean hasRightsMD(String id)
    {
        if (rightsMDs.containsKey(id))
        {
            return true;
        }
        
        return false;
    }

    /*
    /**
     * Obtain the digiprovMDs from this amdSec
     * 
     * @return List<DigiprovMD> 
     *      A list of all digiprovMDs within this amdSec.
     *      Empty list if none are found.
     *      
     * @exception METSException
     *          
    public List<DigiprovMD> getDigiprovMDs() throws METSException
    {
        return digiprovMDs;
    }
    
    
    **
     * Obtain the digiprovMD with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return DigiprovMD
     *      The digiprovMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     *              
    public DigiprovMD getDigiprovMD(String id) throws METSException
    {

        DigiprovMD dm = null;
        
        for (Iterator<DigiprovMD> i = digiprovMDs.iterator(); i.hasNext();)
        {
            DigiprovMD digiprovMD = i.next();
            if (digiprovMD.getID().equals(id))
            {
                dm = digiprovMD;
                break;
            }
        }
        
        return dm;
    }
*/
    
    
    /**
     * Obtain the DigiprovMD with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return DigiprovMD
     *      The DigiprovMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     */
    public DigiprovMD getDigiprovMD(String id)
    {
        return digiprovMDs.get(id);
    }
    
    
    /**
     * Obtain a digiprovMD with the given type from an array
     * of ids
     * 
     * @param ids
     *          an array of id strings
     * @param type
     *      the TYPE to match
     * 
     * @return DigiprovMD 
     *      The DigiprovMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     */
    public DigiprovMD getDigiprovMD(String[] ids,
                            String type)// throws METSException
    {
        for (int i=0; i<ids.length; i++)
        {
            DigiprovMD tm = getDigiprovMD(ids[i]);
            if (tm.getMDType().equals(type))
            {
                return tm;
            }
        }

        return null;
    }
        
    
    /**
     * Add a digiprovMD. If a digiprovMD with the same id already
     * exists it is replaced with the new one and returned to the caller 
     * 
     * @param digiprovMD
     *    a DigiprovMD object      
     * 
     * @return DigiprovMD 
     *      Any existing digiprovMD having an ID attribute value matching
     *      the id of the digiprovMD to add. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public DigiprovMD addDigiprovMD(DigiprovMD digiprovMD)
    {
        DigiprovMD existingDigiprovMD = digiprovMDs.get(digiprovMD.getID());
        
        if(existingDigiprovMD != null)
        {
            this.getElement().replaceChild(digiprovMD.getElement(), existingDigiprovMD.getElement());
        }
        else
        {
            this.getElement().appendChild(digiprovMD.getElement());
        }
        
        digiprovMDs.put(digiprovMD.getID(), digiprovMD);
        
        return existingDigiprovMD;
    }
    
    
    /**
     * Remove a digiprovMD with the ID provided
     * 
     * @param id
     *    the id of the DmdSec to remove
     * 
     * @return DigiprovMD
     *      The digiprovMD having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     */
    public DigiprovMD removeDigiprovMD(String id)
    {
        DigiprovMD digiprovMD = digiprovMDs.get(id);
        if (digiprovMD != null)
        {
            this.getElement().removeChild(digiprovMDs.get(id).getElement());
            digiprovMDs.remove(id);
        }
        return digiprovMD;
    }
    
    
    /**
     * Return whether a digiprovMD with a given id exists
     * 
     * @param id
     *    the id of the digiprovMD to search for
     * 
     * @return boolean
     *      <code>true</code> if a digiprovMD with the given id is found
     *      else <code>false</false>
     */
    public boolean hasDigiprovMD(String id)
    {
        if (digiprovMDs.containsKey(id))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * Initialise the main METS structures
     *       
     * @exception METSException
     */          
    private void initStructures() throws METSException
    {
        initTechMDs();
        initSourceMDs();
        initDigiprovMDs();
        initRightsMDs();
    }
    
    
    /**
     * Initialise the list of techMDs
     *       
     * @exception METSException
     */          
    private void initTechMDs() throws METSException
    {
        techMDs = new HashMap<String,TechMD>();

        NodeList nl = super.getElements(Constants.ELEMENT_TECHMD);
        
        for (int i = 0; i < nl.getLength(); i++)            
        {
            TechMD techMD = new TechMD(nl.item(i));
            if (techMD.getID().length()==0)
            {
                throw new METSException("Element techMD with null ID");
            }
            techMDs.put(techMD.getID(), techMD);
        }
    }
    
    
    /**
     * Initialise the list of sourceMDs
     *       
     * @exception METSException
     */          
    private void initSourceMDs() throws METSException
    {
        sourceMDs = new HashMap<String,SourceMD>();

        NodeList nl = super.getElements(Constants.ELEMENT_SOURCEMD);
        
        for (int i = 0; i < nl.getLength(); i++)
        {
            SourceMD sourceMD = new SourceMD(nl.item(i));
            if (sourceMD.getID().length()==0)
            {
                throw new METSException("Element sourceMD with null ID");
            }
            sourceMDs.put(sourceMD.getID(), sourceMD);
        }
    }
    
    
    /**
     * Initialise the list of digiprovMDs
     *       
     * @exception METSException
     */          
    private void initDigiprovMDs() throws METSException
    {
        digiprovMDs = new HashMap<String,DigiprovMD>();

        NodeList nl = super.getElements(Constants.ELEMENT_DIGIPROVMD);
        
        for (int i = 0; i < nl.getLength(); i++)
        {
            DigiprovMD digiprovMD = new DigiprovMD(nl.item(i));
            if (digiprovMD.getID().length()==0)
            {
                throw new METSException("Element digiprovMD with null ID");
            }
            digiprovMDs.put(digiprovMD.getID(),digiprovMD);
        }
    }
    
    
    /**
     * Initialise the list of rightsMDs
     *       
     * @exception METSException
     */          
    private void initRightsMDs() throws METSException
    {
       rightsMDs = new HashMap<String,RightsMD>();
       
       NodeList nl = super.getElements(Constants.ELEMENT_RIGHTSMD);
        
        for (int i = 0; i < nl.getLength(); i++)
        {
            RightsMD rightsMD = new RightsMD(nl.item(i));
            if (rightsMD.getID().length()==0)
            {
                throw new METSException("Element rightsMD with null ID");
            }
            rightsMDs.put(rightsMD.getID(),rightsMD);
        }
    }
}
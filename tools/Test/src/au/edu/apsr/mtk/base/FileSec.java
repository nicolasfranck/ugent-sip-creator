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
 * Class representing the METS fileSec element
 * 
 * @author Scott Yeadon
 *
 */
public class FileSec extends METSElement
{
    private List<FileGrp> fileGrps = null;
    private HashMap<String,File> allFiles = null;
    
    
    /**
     * Construct a METS div
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
   public FileSec(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_FILESEC);
        init();
    }
   
   
   /**
    * Return an empty FileGrp.
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
   public FileGrp newFileGrp() throws METSException
   {
       return new FileGrp(this.newElement(Constants.ELEMENT_FILEGRP));
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
     * Obtain the child fileGrps
     * 
     * @return List<FileGrp> 
     *      A list of all child fileGrps of this fileGrp
     *      Empty list if none are found.
     *      
     * @exception METSException
     */          
    public List<FileGrp> getFileGrps() throws METSException
    {
        return fileGrps;
    }
    
    
    /**
     * Obtain the child fileGrp with the USE provided
     * 
     * @param use
     *      a USE string
     * 
     * @return List<FileGrp> 
     *      FileGrps having a USE attribute value matching
     *      the use passed in. If not found an empty list is
     *      returned.
     *      
     * @exception METSException
     */
    public List<FileGrp> getFileGrpByUse(String use) throws METSException
    {
        ArrayList<FileGrp> al = new ArrayList<FileGrp>();
        
        for (Iterator<FileGrp> i = fileGrps.iterator(); i.hasNext();)
        {
            FileGrp fileGrp = i.next();
            if (fileGrp.getUse().equals(use))
            {
                al.add(fileGrp);
            }
        }
        
        return al;
    }

    
    /**
     * Add a fileGrp 
     * 
     * @param fileGrp
     *    a FileGrp object      
     * 
     * @return FileGrp 
     *      Any existing fileGrp having an ID attribute value matching
     *      the id of the fileGrp provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public FileGrp addFileGrp(FileGrp fileGrp)
    {
        if (fileGrp.getID().equals(""))
        {
            this.getElement().appendChild(fileGrp.getElement());
            fileGrps.add(fileGrp);
            return null;
        }
        
        for (Iterator<FileGrp> i = fileGrps.iterator(); i.hasNext();)
        {
            FileGrp fg = i.next();
            if (fg.getID().equals(fileGrp.getID()))
            {
                this.getElement().replaceChild(fileGrp.getElement(), fg.getElement());
                fileGrps.add(fileGrp);
                return fg;
            }
        }

        this.getElement().appendChild(fileGrp.getElement());
        fileGrps.add(fileGrp);

        return null;
    } 
    
    
    /**
     * Remove a fileGrp with the ID provided
     * 
     * @param id
     *    the id of the fileGrp to remove
     *
     * @return FileGrp
     *      The deleted fileGrp. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public FileGrp removeFileGrp(String id)
    {
        for (Iterator<FileGrp> i = fileGrps.iterator(); i.hasNext();)
        {
            FileGrp fg = i.next();
            if (fg.getID().equals(id))
            {
                this.getElement().removeChild(fg.getElement());
                i.remove();
                return fg;
            }
        }

        return null;
    }

    
    /**
     * Remove a fileGrp at the position provided
     * 
     * @param pos
     *    the position of the fileGrp element in relation to
     *    other fileGrp elements
     */
    public FileGrp removeFileGrp(int pos)
    {
        for (int i = 0; i < fileGrps.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(fileGrps.get(i).getElement());
                return fileGrps.remove(i);
            }
        }

        return null;
    }

    
    /**
     * Obtain the file with the ID provided
     * 
     * @param id
     *      an ID string
     * 
     * @return File 
     *      The file having an ID attribute value matching
     *      the id passed in. If not found <code>null</code> is
     *      returned.
     *      
     * @exception METSException
     */          
    public File getFile(String id) throws METSException
    {
        return allFiles.get(id);
    }
    
    
    /**
     * Refreshes the file list cache. Should be called whenever files
     * are added or removed to ensure cache is up to date.
     *       
     * @exception METSException
     */          
    public void refresh() throws METSException
    {
        initAllFiles();
    }

    
    /**
     * Initialise the sub-structures
     *       
     * @exception METSException
     */          
    private void init() throws METSException
    {
        initAllFiles();
        initFileGrps();
    }
        

    /**
     * Initialise the list of files
     *       
     * @exception METSException
     */          
    private void initAllFiles() throws METSException
    {
        allFiles = new HashMap<String,File>();
        NodeList nl = super.getElements(Constants.ELEMENT_FILE);
        for (int i = 0; i < nl.getLength(); i++)
        {
            File f = new File(nl.item(i));
            allFiles.put(f.getID(), f);
        }
    }
    
    
    /**
     * Initialise the list of fileGrps
     *       
     * @exception METSException
     */          
    private void initFileGrps() throws METSException
    {
        fileGrps = new ArrayList<FileGrp>();
        List<Node> l = super.getChildElements(Constants.ELEMENT_FILEGRP);
        for (Iterator<Node> i = l.iterator(); i.hasNext();)
        {
            Node n = i.next();
            fileGrps.add(new FileGrp(n));
        }
    }
}
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
 * Class representing the METS fileGrp element
 * 
 * @author Scott Yeadon
 *
 */
public class FileGrp extends METSElement
{
    private List<FileGrp> fileGrps = null;
    // although ID is required, order may be important so use List
    private List<File> files = null;
    
    /**
     * Construct a METS fileGrp
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public FileGrp(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_FILEGRP);
        init();
    }
    
    
    /**
     * Return an empty File.
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
    public File newFile() throws METSException
    {
        return new File(this.newElement(Constants.ELEMENT_FILE));
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
     * Obtain the use
     * 
     * @return String 
     *      The USE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getUse()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_USE);
    }
    
    
    /**
     * Set the use
     * 
     * @param use 
     *      The USE attribute value
     */  
    public void setUse(String use)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_USE, use);
    }
    
    
    /**
     * Remove the USE attribute
     */  
    public void removeUse()
    {
        super.removeAttribute(Constants.ATTRIBUTE_USE);
    }
    
    
   /**
    * Obtain the version date
    * 
    * @return String 
    *      The VERSDATE attribute value or empty string if attribute
    *      is empty or not present
    */  
    public String getVersDate()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_VERSDATE);
    }
    
    
    /**
     * Set the version date
     * 
     * @param versDate 
     *      The VERSDATE attribute value
     */  
     public void setVersDate(String versDate)
     {
         super.setAttributeValue(Constants.ATTRIBUTE_VERSDATE, versDate);
     }
     
     
     /**
      * Remove the VERSDATE attribute
      */  
      public void removeVersDate()
      {
          super.removeAttribute(Constants.ATTRIBUTE_VERSDATE);
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
       * Set the adm id
       * 
       * @param admID 
       *      The ADMID attribute value
       */  
      public void setAdmID(String admID)
      {
          super.setAttributeValue(Constants.ATTRIBUTE_ADMID, admID);
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
       * Remove the ADMID attribute
       */  
      public void removeAdmID()
      {
          super.removeAttribute(Constants.ATTRIBUTE_ADMID);
      }
    

    /**
     * Obtain the child files
     * 
     * @return List<File> 
     *      A list of all child files of this fileGrp
     *      Empty list if none are found.
     *      
     * @exception METSException
     */          
    public List<File> getFiles() throws METSException
    {
        return files;
    }
    
    
    /**
     * Obtain the child file with the ID provided
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
        if (files == null)
        {
            initChildFiles();
        }

        File file = null;
        
        for (Iterator<File> i = files.iterator(); i.hasNext();)
        {
            File f = i.next();
            if (f.getID().equals(id))
            {
                file = f;
                break;
            }
        }
        
        return file;
    }
   
   
   /**
    * Add a file 
    * 
    * @param file
    *    a File object      
    * 
    * @return File 
    *      Any existing file having an ID attribute value matching
    *      the id of the file provided. If an entry does not exist <code>null</code> is
    *      returned.       
    */
   public File addFile(File file)
   {
       for (Iterator<File> i = files.iterator(); i.hasNext();)
       {
           File f = i.next();
           if (f.getID().equals(file.getID()))
           {
               this.getElement().replaceChild(file.getElement(), f.getElement());
               files.add(file);
               return f;
           }
       }
       
       this.getElement().appendChild(file.getElement());
       files.add(file);
       
       return null;
   } 
   
   
   /**
    * Remove a File with the ID provided
    * 
    * @param id
    *    the id of the File to remove
    *
    * @return File 
    *      The deleted file. If an entry does not exist <code>null</code> is
    *      returned.       
    */    
   public File removeFile(String id)
   {
       for (Iterator<File> i = files.iterator(); i.hasNext();)
       {
           File f = i.next();
           if (f.getID().equals(id))
           {
               this.getElement().removeChild(f.getElement());
               i.remove();
               return f;
           }
       }

       return null;
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
        if (fileGrps == null)
        {
            initChildFileGrps();
        }

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
     * Initialise the sub-structures
     *       
     * @exception METSException
     */          
    private void init() throws METSException
    {
        initChildFiles();
        initChildFileGrps();
    }
    
    
    /**
     * Initialise the list of child files
     *       
     * @exception METSException
     */          
    private void initChildFiles() throws METSException
    {
        files = new ArrayList<File>();
        List<Node> l = super.getChildElements(Constants.ELEMENT_FILE);
        for (Iterator<Node> i = l.iterator(); i.hasNext();)
        {
            Node n = i.next();
            files.add(new File(n));
        }
    }
    
    
    /**
     * Initialise the list of child fileGrps
     *       
     * @exception METSException
     */          
    private void initChildFileGrps() throws METSException
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
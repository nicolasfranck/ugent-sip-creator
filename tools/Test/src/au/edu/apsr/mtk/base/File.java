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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class representing the METS file element
 * 
 * @author Scott Yeadon
 *
 */
public class File extends METSElement
{
    List<FLocat> flocats = null;
    List<File> files = null;
    List<TransformFile> tfiles = null;
    List<Stream> streams = null;
    FContent fContent = null;
    
    /**
     * Construct a METS file
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public File(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_FILE);
        init();
    }
    
    
    /**
     * Return an empty FLocat.
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
    public FLocat newFLocat() throws METSException
    {
        return new FLocat(this.newElement(Constants.ELEMENT_FLOCAT));
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
     * Return an empty TransformFile.
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
    public TransformFile newTransformFile() throws METSException
    {
        return new TransformFile(this.newElement(Constants.ELEMENT_TRANSFORMFILE));
    }
    
    
    /**
     * Return an empty Stream.
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
    public Stream newStream() throws METSException
    {
        return new Stream(this.newElement(Constants.ELEMENT_STREAM));
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
        super.getAttributeValue(Constants.ATTRIBUTE_DMDID, dmdID);
    }

    
    /**
     * Remove the DMDID attribute
     */  
    public void removeDmdID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_DMDID);
    }
    
    
    /**
     * Obtain the group id
     * 
     * @return String 
     *      The GROUPID attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getGroupID()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_GROUPID);
    }

    
    /**
     * Set the group id
     * 
     * @param groupID 
     *      The GROUP attribute value
     */  
    public void setGroupID(String groupID)
    {
        super.getAttributeValue(Constants.ATTRIBUTE_GROUPID, groupID);
    }
    
    
    /**
     * Remove the GROUPID attribute
     */  
    public void removeGroupID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_GROUPID);
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
     * Obtain the owner id
     * 
     * @return String 
     *      The OWNERID attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getOwnerID()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_OWNERID);
    }

    
    /**
     * Set the owner id
     * 
     * @param ownerID 
     *      The OWNERID attribute value
     */  
    public void setOwnerID(String ownerID)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_OWNERID, ownerID);
    }

    
    /**
     * Remove the OWNERID attribute
     */  
    public void removeOwnerID()
    {
        super.removeAttribute(Constants.ATTRIBUTE_OWNERID);
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
     * Obtain the sequence
     * 
     * @return String 
     *      The SEQ attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getSeq()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_SEQ);
    }
    
    
    /**
     * Set the sequence
     * 
     * @param seq 
     *      The SEQ attribute value
     */  
    public void setSeq(String seq)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_SEQ, seq);
    }
    
    
    /**
     * Remove the SEQ attribute
     */  
    public void removeSeq()
    {
        super.removeAttribute(Constants.ATTRIBUTE_SEQ);
    }
    
    
    /**
     * Obtain the MIME type
     * 
     * @return String 
     *      The MIMETYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getMIMEType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_MIMETYPE);
    }
    
    
    /**
     * Set the MIME type
     * 
     * @param mimeType 
     *      The MIMETYPE attribute value
     */  
    public void setMIMEType(String mimeType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_MIMETYPE, mimeType);
    }
    
    
    /**
     * Remove the MIMETYPE attribute
     */  
    public void removeMIMEType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_MIMETYPE);
    }
    
    
    /**
     * Obtain the checksum
     * 
     * @return String 
     *      The CHECKSUM attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getChecksum()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_CHECKSUM);
    }
    
    
    /**
     * Set the checksum
     * 
     * @param checksum 
     *      The CHECKSUM attribute value
     */  
    public void setChecksum(String checksum)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_CHECKSUM, checksum);
    }
    
    
    /**
     * Remove the CHECKSUM attribute
     */  
    public void removeChecksum()
    {
        super.removeAttribute(Constants.ATTRIBUTE_CHECKSUM);
    }
    
    
    /**
     * Obtain the checksum type
     * 
     * @return String 
     *      The CHECKSUMTYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getChecksumType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_CHECKSUMTYPE);
    }
    
    
    /**
     * Set the checksum type
     * 
     * @param cType 
     *      The CHECKSUMTYPE attribute value
     */  
    public void setChecksumType(String cType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_CHECKSUMTYPE, cType);
    }
    
    
    /**
     * Remove the CHECKSUMTYPE attribute
     */  
    public void removeChecksumType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_CHECKSUMTYPE);
    }
    

    /**
     * Obtain the BEGIN attribute value
     * 
     * @return String 
     *      The BEGIN attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getBegin()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_BEGIN);
    }


    /**
     * Set the BEGIN attribute value
     * 
     * @param begin 
     *      The BEGIN attribute value
     */  
    public void setBegin(String begin)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_BEGIN, begin);
    }

    
    /**
     * Remove the BEGIN attribute
     */  
    public void removeBegin()
    {
        super.removeAttribute(Constants.ATTRIBUTE_BEGIN);
    }
    

    /**
     * Obtain the END attribute value
     * 
     * @return String 
     *      The END attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getEnd()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_END);
    }


    /**
     * Set the END attribute value
     * 
     * @param end 
     *      The END attribute value
     */  
    public void setEnd(String end)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_END, end);
    }

    
    /**
     * Remove the END attribute
     */  
    public void removeEnd()
    {
        super.removeAttribute(Constants.ATTRIBUTE_END);
    }
    

    /**
     * Obtain the BETYPE attribute value
     * 
     * @return String 
     *      The BETYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getBEType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_BETYPE);
    }


    /**
     * Set the BETYPE attribute value
     * 
     * @param beType 
     *      The BETYPE attribute value
     */  
    public void setBEType(String beType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_BETYPE, beType);
    }

    
    /**
     * Remove the BETYPE attribute
     */  
    public void removeBEType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_BETYPE);
    }

    
    /**
     * Obtain the file size
     * 
     * @return String 
     *      The SIZE attribute value or -1 if attribute
     *      is empty or not present
     */  
    public long getSize()
    {
        String s = super.getAttributeValue(Constants.ATTRIBUTE_SIZE);
        
        if (s.length()==0)
        {
            return -1;
        }
        return Long.valueOf(s);
    }
    
    
    /**
     * Set the file size
     * 
     * @param size 
     *      The SIZE attribute value
     */  
    public void setSize(long size)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_SIZE, String.valueOf(size));
    }    
    
    
    /**
     * Remove the SIZE attribute
     */  
    public void removeSize()
    {
        super.removeAttribute(Constants.ATTRIBUTE_SIZE);
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
     * Obtain the xmlData wrapper node
     * 
     * @return org.w3c.dom.Node 
     *      The xmlData Node of this file or <code>null</code>
     *      if none exists      
     */          
    public Node getXMLData()
    {
        NodeList nl = super.getElements(Constants.ELEMENT_XMLDATA);
        if (nl.getLength() == 1)
        {
            return nl.item(0).getFirstChild();
        }
        
        return null;
    }
    
    
    /**
     * Set the xmlData (also creates an FContent for the data)
     * 
     * @param content 
     *      The xmlData content      
     */          
    public void setXMLData(Element content) throws METSException
    {
        FContent fc = new FContent(this.newElement(Constants.ELEMENT_FCONTENT));
        fc.setXMLData(content);
        this.getElement().appendChild(fc.getElement());
    }
    
    
    /**
     * Obtain base64 encoded data from the binData element.
     * it is expected that users of this class would use FLocat
     * rather than binData
     * 
     * @return String 
     *       the encoded data
     * 
     */          
    public String getEncodedData()
    {
        NodeList nl = super.getElements(Constants.ELEMENT_BINDATA);
        if (nl.getLength() == 1)
        {
            return nl.item(0).getTextContent();
        }
        
        return null;
    }
    
    
    /**
     * Set the binData (also creates an FContent for the data)
     * 
     * @param content 
     *      The base64-encoded binData content      
     */          
    public void setBinData(String content) throws METSException
    {
        FContent fc = new FContent(this.newElement(Constants.ELEMENT_FCONTENT));
        fc.setEncodedData(content);
        this.getElement().appendChild(fc.getElement());
    }
    
    
    /**
     * Check if this file contains base64 encoded data 
     * 
     * @return boolean
     *  <code>true</code> if this file has encoded data else
     *  <code>false</code> 
     */          
    public boolean hasEncodedData()
    {
        NodeList nl = super.getElements(Constants.ELEMENT_BINDATA);
        if (nl.getLength() > 0)
        {
            return true;
        }

        return false;
    }

    
    /**
     * Obtain the FLocats of this file
     * 
     * @return List<FLocat> 
     *      A list of all FLocats within this file
     *      Empty list if none are found.
     *      
     * @exception METSException
     */
    public List<FLocat> getFLocats() throws METSException
    {
/*        if (flocats == null)
        {
            initFLocats();
        }
*/        
        return flocats;
    }    
    
    
    /**
     * Add an FLocat 
     * 
     * @param flocat
     *    an FLocat object      
     * 
     * @return FLocat 
     *      Any existing FLocat having an ID attribute value matching
     *      the id of the FLocat provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public FLocat addFLocat(FLocat flocat)
    {
        if (flocat.getID().equals(""))
        {
            this.getElement().appendChild(flocat.getElement());
            flocats.add(flocat);
            return null;
        }
        
        for (Iterator<FLocat> i = flocats.iterator(); i.hasNext();)
        {
            FLocat f = i.next();
            if (f.getID().equals(flocat.getID()))
            {
                this.getElement().replaceChild(flocat.getElement(), f.getElement());
                flocats.add(flocat);
                return f;
            }
        }

        this.getElement().appendChild(flocat.getElement());
        flocats.add(flocat);

        return null;
    } 
    
    
    /**
     * Remove an FLocat with the ID provided
     * 
     * @param id
     *    the id of the FLocat to remove
     *
     * @return FLocat 
     *      The deleted FLocat. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public FLocat removeFLocat(String id)
    {
        for (Iterator<FLocat> i = flocats.iterator(); i.hasNext();)
        {
            FLocat flocat = i.next();
            if (flocat.getID().equals(id))
            {
                this.getElement().removeChild(flocat.getElement());
                i.remove();
                return flocat;
            }
        }

        return null;
    }

    
    /**
     * Remove an FLocat at the position provided
     * 
     * @param pos
     *    the position of the FLocat element in relation to
     *    other FLocat elements
     */
    public FLocat removeFLocat(int pos)
    {
        for (int i = 0; i < flocats.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(flocats.get(i).getElement());
                return flocats.remove(i);
            }
        }

        return null;
    }
    
    
    /**
     * Obtain the transform files
     * 
     * @return List<TransformFile> 
     *      A list of all transform files in this file
     *      Empty list if none are found.
     *      
     * @exception METSException
     */          
    public List<TransformFile> getTransformFiles() throws METSException
    {
        return tfiles;
    }
    
    
    /**
     * Add a transformFile 
     * 
     * @param transformFile
     *    a TransformFile object      
     * 
     * @return TransformFile 
     *      Any existing TransformFile having an ID attribute value matching
     *      the id of the TransformFile provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public TransformFile addTransformFile(TransformFile transformFile)
    {
        if (transformFile.getID().equals(""))
        {
            this.getElement().appendChild(transformFile.getElement());
            tfiles.add(transformFile);
            return null;
        }
        
        for (Iterator<TransformFile> i = tfiles.iterator(); i.hasNext();)
        {
            TransformFile tf = i.next();
            if (tf.getID().equals(transformFile.getID()))
            {
                this.getElement().replaceChild(transformFile.getElement(), tf.getElement());
                tfiles.add(transformFile);
                return tf;
            }
        }
        
        this.getElement().appendChild(transformFile.getElement());
        tfiles.add(transformFile);

        return null;
    } 
    
    
    /**
     * Remove a TransformFile with the ID provided
     * 
     * @param id
     *    the id of the TransformFile to remove
     *
     * @return TransformFile 
     *      The deleted TransformFile. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public TransformFile removeTransformFile(String id)
    {
        for (Iterator<TransformFile> i = tfiles.iterator(); i.hasNext();)
        {
            TransformFile tf = i.next();
            if (tf.getID().equals(id))
            {
                this.getElement().removeChild(tf.getElement());
                i.remove();
                return tf;
            }
        }

        return null;
    }

    
    /**
     * Remove a TransformFile at the position provided
     * 
     * @param pos
     *    the position of the transformFile element in relation to
     *    other transformFile elements
     */
    public TransformFile removeTransformFile(int pos)
    {
        for (int i = 0; i < tfiles.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(tfiles.get(i).getElement());
                return tfiles.remove(i);
            }
        }

        return null;
    }
    
    
    /**
     * Obtain the streams
     * 
     * @return List<Stream> 
     *      A list of all streams in this file
     *      Empty list if none are found.
     *      
     * @exception METSException
     */          
    public List<Stream> getStreams() throws METSException
    {
        return streams;
    }
    
    
    /**
     * Add a stream 
     * 
     * @param stream
     *    a Stream object      
     * 
     * @return Stream 
     *      Any existing Stream having an ID attribute value matching
     *      the id of the Stream provided. If an entry does not exist <code>null</code> is
     *      returned.       
     */
    public Stream addStream(Stream stream)
    {
        if (stream.getID().equals(""))
        {
            this.getElement().appendChild(stream.getElement());
            streams.add(stream);
            return null;
        }
        
        for (Iterator<Stream> i = streams.iterator(); i.hasNext();)
        {
            Stream s = i.next();
            if (s.getID().equals(stream.getID()))
            {
                this.getElement().replaceChild(stream.getElement(), s.getElement());
                streams.add(stream);
                return s;
            }
        }
        
        this.getElement().appendChild(stream.getElement());
        streams.add(stream);

        return null;
    } 
    
    
    /**
     * Remove a Stream with the ID provided
     * 
     * @param id
     *    the id of the Stream to remove
     *
     * @return Stream 
     *      The deleted Stream. If an entry does not exist <code>null</code> is
     *      returned.       
     */    
    public Stream removeStream(String id)
    {
        for (Iterator<Stream> i = streams.iterator(); i.hasNext();)
        {
            Stream s = i.next();
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
     * Remove a Stream at the position provided
     * 
     * @param pos
     *    the position of the stream element in relation to
     *    other stream elements
     */
    public Stream removeStream(int pos)
    {
        for (int i = 0; i < streams.size(); i++)
        {
            if (i == pos)
            {
                this.getElement().removeChild(streams.get(i).getElement());
                return streams.remove(i);
            }
        }

        return null;
    }
    
    
    /**
     * Obtain the child files
     * 
     * @return List<File> 
     *      A list of all child files of this file
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
     * Initialise the sub-structures
     *       
     * @exception METSException
     */          
    private void init() throws METSException
    {
        initFLocats();
        initChildFiles();
        initTransformFiles();
        initStreams();
    }

    
    /**
     * Initialise the list of FLocats
     *       
     * @exception METSException
     */          
    private void initFLocats() throws METSException
    {
        flocats = new ArrayList<FLocat>();
        
        NodeList nl = super.getElements(Constants.ELEMENT_FLOCAT);
        for (int i = 0; i < nl.getLength(); i++)
        {
            FLocat fl = new FLocat(nl.item(i));
            flocats.add(fl);
        }
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
     * Initialise the list of transform files
     *       
     * @exception METSException
     */          
    private void initTransformFiles() throws METSException
    {
        tfiles = new ArrayList<TransformFile>();
        List<Node> l = super.getChildElements(Constants.ELEMENT_TRANSFORMFILE);
        for (Iterator<Node> i = l.iterator(); i.hasNext();)
        {
            Node n = i.next();
            tfiles.add(new TransformFile(n));
        }
    }
    
    
    /**
     * Initialise the list of streams
     *       
     * @exception METSException
     */          
    private void initStreams() throws METSException
    {
        streams = new ArrayList<Stream>();
        List<Node> l = super.getChildElements(Constants.ELEMENT_STREAM);
        for (Iterator<Node> i = l.iterator(); i.hasNext();)
        {
            Node n = i.next();
            streams.add(new Stream(n));
        }
    }
}
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

import org.w3c.dom.Node;

/**
 * Class representing the METS Area element
 * 
 * @author Scott Yeadon
 *
 */
public class Area extends METSElement
{    
    /**
     * Construct a METS area
     * 
     * @param n
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */ 
    public Area(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_AREA);
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
     * Obtain the FileID
     * 
     * @return String 
     *      The FILEID attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getFileID()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_FILEID);
    }
    

    /**
     * Set the FileID
     * 
     * @param id 
     *      The FILEID attribute value
     */  
    public void setFileID(String id)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_FILEID, id);
    }


    /**
     * Obtain the shape
     * 
     * @return String 
     *      The SHAPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getShape()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_SHAPE);
    }


    /**
     * Set the shape
     * 
     * @param shape 
     *      The SHAPE attribute value
     */  
    public void setShape(String shape)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_SHAPE, shape);
    }


    /**
     * Remove the SHAPE attribute
     */  
    public void removeShape()
    {
        super.removeAttribute(Constants.ATTRIBUTE_SHAPE);
    }


    /**
     * Obtain the coordinates
     * 
     * @return String 
     *      The COORDS attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getCoords()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_COORDS);
    }


    /**
     * Set the coordinates
     * 
     * @param coords 
     *      The COORDS attribute value
     */  
    public void setCoords(String coords)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_COORDS, coords);
    }


    /**
     * Remove the COORDS attribute
     */  
    public void removeCoords()
    {
        super.removeAttribute(Constants.ATTRIBUTE_COORDS);
    }


    /**
     * Obtain the begin range value
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
     * Set the begin range value
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
     * Obtain the end range value
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
     * Set the end range value
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
     * Obtain the begin/end type
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
     * Set the begin/end type
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
     * Obtain the extent
     * 
     * @return String 
     *      The EXTENT attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getExtent()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_EXTENT);
    }

    
    /**
     * Set the extent
     * 
     * @param extent 
     *      The EXTENT attribute value
     */  
    public void setExtent(String extent)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_EXTENT, extent);
    }

    
    /**
     * Remove the EXTENT attribute
     */  
    public void removeExtent()
    {
        super.removeAttribute(Constants.ATTRIBUTE_EXTENT);
    }
    
    
    /**
     * Obtain the extent type
     * 
     * @return String 
     *      The EXTTYPE attribute value or empty string if attribute
     *      is empty or not present
     */  
    public String getExtType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_EXTTYPE);
    }
    
    
    /**
     * Set the extent type
     * 
     * @param extType 
     *      The EXTTYPE attribute value
     */  
    public void setExtType(String extType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_EXTTYPE, extType);
    }
    
    
    /**
     * Remove the EXTTYPE attribute
     */  
    public void removeExtType()
    {
        super.removeAttribute(Constants.ATTRIBUTE_EXTTYPE);
    }

    
    /**
     * Obtain the admID
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
     * Set the admID
     * 
     * @param admID 
     *      The ADMID attribute value. Multiple IDs are set by passing a
     *      space separated string of IDs.
     */  
    public void setAdmID(String admID)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_ADMID, admID);
    }

    
    /**
     * Obtain the admIDs
     * 
     * @return String[] 
     *      The ADMID attribute value split into separate admIDs
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
}
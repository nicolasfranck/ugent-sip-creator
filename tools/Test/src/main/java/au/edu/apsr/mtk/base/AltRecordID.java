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

import org.w3c.dom.Node;

/**
 * Class representing the METS AltRecordID element
 * 
 * @author Scott Yeadon
 *
 */
public class AltRecordID extends METSElement
{
    /**
     * Construct a METS altRecordID
     * 
     * @param n 
     *        A w3c Node, typically an Element
     *        
     * @exception METSException
     */ 
    public AltRecordID(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_ALTRECORDID);
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
     *         The TYPE attribute value or empty string if
     *         attribute is empty or not present
     */     
    public String getType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_TYPE);
    }

    
    /**
     * Set the type
     * 
     * @param type 
     *         The TYPE attribute value
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
     * Obtain the value
     * 
     * @return String 
     *         The value of the altRecordID element
     */         
    public String getValue()
    {
        return super.getText();
    }
    
    
    /**
     * Set the value
     * 
     * @param value 
     *         The value of the altRecordID element
     */         
    public void setValue(String value)
    {
        super.setText(value);
    }
}
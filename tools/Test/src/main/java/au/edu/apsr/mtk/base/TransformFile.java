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

import java.util.List;

import org.w3c.dom.Node;


/**
 * Class representing the METS transformFile element
 * 
 * @author Scott Yeadon
 *
 */
public class TransformFile extends METSElement
{
    /**
     * Construct a METS transformFile
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */     
    public TransformFile(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_TRANSFORMFILE);
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
     * Obtain the transform type
     * 
     * @return String 
     *      The TRANSFORMTYPE attribute value or empty string if attribute
     *      is empty or not present
     */     
    public String getTransformType()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_TRANSFORMTYPE);
    }
    

    /**
     * Set the transform type
     * 
     * @param txType 
     *      The TRANSFORMTYPE attribute value
     */     
    public void setTransformType(String txType)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_TRANSFORMTYPE, txType);
    }

    
    /**
     * Obtain the transform algorithm
     * 
     * @return String 
     *      The TRANSFORMALGORITHM attribute value or empty string if attribute
     *      is empty or not present
     */     
    public String getTransformAlgorithm()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_TRANSFORMALGORITHM);
    }

    
    /**
     * Set the transform algorithm
     * 
     * @param txAlg 
     *      The TRANSFORMALGORITHM attribute value
     */     
    public void setTransformAlgorithm(String txAlg)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_TRANSFORMALGORITHM, txAlg);
    }


    /**
     * Obtain the transform key
     * 
     * @return String 
     *      The TRANSFORMKEY attribute value or empty string if attribute
     *      is empty or not present
     */     
    public String getTransformKey()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_TRANSFORMKEY);
    }


    /**
     * Set the transform key
     * 
     * @param txKey 
     *      The TRANSFORMKEY attribute value
     */     
    public void setTransformKey(String txKey)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_TRANSFORMKEY, txKey);
    }


    /**
     * Remove the TRANSFORMKEY attribute
     */     
    public void removeTransformKey()
    {
        super.removeAttribute(Constants.ATTRIBUTE_TRANSFORMKEY);
    }

    
    /**
     * Obtain the transform behavior
     * 
     * @return String 
     *      The TRANSFORMBEHAVIOR attribute value or empty string if attribute
     *      is empty or not present
     */     
    public String getTransformBehavior()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_TRANSFORMBEHAVIOR);
    }

    
    /**
     * Set the transform behavior
     * 
     * @param txBehavior 
     *      The TRANSFORMBEHAVIOR attribute value
     */     
    public void setTransformBehavior(String txBehavior)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_TRANSFORMBEHAVIOR, txBehavior);
    }

    
    /**
     * Remove the TRANSFORMBEHAVIOR attribute
     */     
    public void removeTransformBehavior()
    {
        super.removeAttribute(Constants.ATTRIBUTE_TRANSFORMBEHAVIOR);
    }

    
    /**
     * Obtain the transform order
     * 
     * @return String 
     *      The TRANSFORMORDER attribute value or empty string if attribute
     *      is empty or not present
     */     
    public String getTransformOrder()
    {
        return super.getAttributeValue(Constants.ATTRIBUTE_TRANSFORMORDER);
    }

    
    /**
     * Set the transform order
     * 
     * @param txOrder 
     *      The TRANSFORMORDER attribute value
     */     
    public void setTransformOrder(String txOrder)
    {
        super.setAttributeValue(Constants.ATTRIBUTE_TRANSFORMORDER, txOrder);
    }
    
    
    /**
     * Obtain the stream content
     * 
     * @return List<Node>
     *      a list of child nodes holding stream (meta)data
     */      
    public List<Node> getContent()
    {
        return super.getChildNodes();
    }


    /**
     * Set the stream content
     * 
     * @param n
     *      a node representing the stream.  Note that the
     *      importNode method is used to copy the XML into the new
     *      METS structure, the source from which it comes is not
     *      modified in any way.
     */      
    public void setContent(Node n)
    {
        Node n2 = this.getElement().getOwnerDocument().importNode(n, true);
        this.getElement().appendChild(n2);
    }
}
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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * super class of all METS elements
 * 
 * @author Scott Yeadon
 *
 */
public class METSElement
{
    private Element e = null;
    
    /**
     * Construct a METS element
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * @param name 
     *        The name of the METS Element
     *        
     * @exception METSException
     *
     */ 
    protected METSElement(Node n,
                          String name) throws METSException
    {
        if (n == null)
        {
            throw new METSException("Null Node passed to constructor");
        }

        if (n instanceof Element)
        {
           if (!n.getNodeName().endsWith(name))
           {
               throw new METSException("Mismatch tag name. Node tag is: " + n.getNodeName() + ", expected: " + name);
           }
        }
        else
        {
            throw new METSException("Node of type Element required in constructor");
        }
        
        e = (Element)n;
    }
    
    
    /**
     * Construct a METS element
     * 
     * @param n 
     *        A w3c Node, typically a Document
     *        
     * @exception METSException
     *
     */ 
    protected METSElement(Node n) throws METSException
    {
        if (n == null)
        {
            throw new METSException("Null Node passed to constructor");
        }

        if (n instanceof Document)
        {
           if (!n.getFirstChild().getNodeName().endsWith(Constants.ELEMENT_METS))
           {
               throw new METSException("Mismatch tag name. Node tag is: " + n.getFirstChild().getNodeName() + ", expected: " + Constants.ELEMENT_METS);
           }
        }
        else
        {
            throw new METSException("Node of type Document required in constructor");
        }
        
        e = ((Document)n).getDocumentElement();
    }


    /**
     * Construct an empty METS document comprising of only a mets element
     * 
     * @exception ParserConfigurationException
     *
     */ 
    protected METSElement() throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        e = doc.createElementNS(Constants.NS_METS, Constants.ELEMENT_METS);
        doc.appendChild(e);
    }


    /**
     * Obtain an attribute value
     * 
     * @param name
     *      The name of the attribute
     *      
     * @return String 
     *      The attribute value or empty string if attribute
     *      is empty or not present
     */  
    protected String getAttributeValue(String name)
    {
        return e.getAttribute(name);
    }


    /**
     * Set an attribute value
     * 
     * @param name
     *      The name of the attribute
     *      
     * @param value 
     *      The attribute value
     */  
    protected void setAttributeValue(String name,
                                     String value)
    {
        e.setAttribute(name, value);
    }


    /**
     * Set an attribute value with namespace
     * 
     * @param namespace
     *      The namespace URL of the attribute
     * @param name 
     *      The attribute name
     * @param value 
     *      The attribute value
     */  
    protected void setAttributeValueNS(String ns,
                                       String name,
                                       String value)
    {
        e.setAttributeNS(ns, name, value);
    }


    /**
     * Remove an attribute value
     * 
     * @param name
     *      The name of the attribute
     */  
    protected void removeAttribute(String name)
    {
        e.removeAttribute(name);
    }


    /**
     * Remove an attribute value with namespace
     * 
     * @param namespace
     *      The namespace URL of the attribute
     * @param name 
     *      The attribute name
     */  
    protected void removeAttributeNS(String ns,
                                     String name)
    {
        e.removeAttributeNS(ns, name);
    }
    

    /**
     * Obtain an attribute value where the attribute has a
     * namespace
     * 
     * @param ns
     *      The attribute namespace URL
     * @param localName
     *      The unqualified attribute name
     *      
     * @return String 
     *      The attribute value or empty string if attribute
     *      is empty or not present
     */  
    protected String getAttributeValue(String ns,
                                       String localName)
    {
        return e.getAttributeNS(ns, localName);
    }
    
    
    /**
     * Obtain the text content of the METS Element
     * 
     * @return String 
     *      The text content of the element
     */  
    protected String getText()
    {
        return e.getTextContent();
    }
    
    
    /**
     * Set the text content of the METS Element
     * 
     * @param value 
     *      The text content of the element
     */  
    protected void setText(String value)
    {
        e.setTextContent(value);
    }

    
    /**
     * Obtain a list of descendant METS elements with the given name
     * 
     * @param localName
     *      Obtain a list of descendant METS elements
     *      
     * @return org.w3c.dom.NodeList 
     *      A list of descendant elements matchin the local name provided
     */  
    protected NodeList getElements(String localName)
    {
        return e.getElementsByTagNameNS(Constants.NS_METS, localName);
    }
    
    
    /**
     * Obtain a list of child METS elements
     * 
     * @param localName
     *      An element name
     *      
     * @return org.w3c.dom.NodeList 
     *      A list of METS elements whose tag name matches
     *      the localName
     */  
    protected List<Node> getChildElements(String localName)
    {
        NodeList nl = e.getChildNodes();
        List<Node> l = new ArrayList<Node>();
        for (int i = 0; i < nl.getLength(); i++)
        {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE &&
                nl.item(i).getLocalName().equals(localName))
            {
                l.add(nl.item(i));
            }
        }
        
        return l;
    }
    
    
    // specifically to handle xsd:anyType, ignores immediate child
    // attributes (i.e. attributes of the transform file element
    // and returns everything else directly under the transformFile element
    /**
     * Obtain all child nodes
     * 
     * @return List<Node> 
     *      A list of child nodes. This method is used to handle
     *      xsd:anyType content to return all non-attribute children
     *      of the METS element
     */  
    protected List<Node> getChildNodes()
    {
        NodeList nl = e.getChildNodes();
        List<Node> l = new ArrayList<Node>();
        for (int i = 0; i < nl.getLength(); i++)
        {
            if (nl.item(i).getNodeType() != Node.ATTRIBUTE_NODE)
            {
                l.add(nl.item(i));
            }
        }
        
        return l;
    }
    
    
    /**
     * Obtain the w3c dom element this object represents
     * 
     * @return Element 
     *      A w3c dom element
     */  
    protected Element getElement()
    {
        return e;
    }
    
    
    /**
     * Return null, this class should be overridden by subclasses if sub-elements
     * are permitted
     * 
     * @return Element
     *      an element with the given name
     */  
    protected Element newElement(String elementName)
    {
        return this.getElement().getOwnerDocument().createElementNS(Constants.NS_METS, elementName);
    }
}
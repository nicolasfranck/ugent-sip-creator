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

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.XMLConstants;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import au.edu.apsr.mtk.base.Constants;

import org.xml.sax.SAXException;

/**
 * Class for wrapping METS documents.
 * 
 * Because different profiles will have different requirements only a DOM
 * Document object get is currently supported.
 * 
 * @author Scott Yeadon
 *
 */
public class METSWrapper
{
    private Document doc = null;
    private METS mets = null;
    
    
    /**
     * Construct a METS wrapper containing an empty METS document.
     * The METS document will consist only of a METS element with
     * no attributes set and no sub-elements. Used when creating a 
     * new METS document.
     * 
     * @exception METSException
     */     
    public METSWrapper() throws METSException
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            Element root = doc.createElementNS(Constants.NS_METS, Constants.ELEMENT_METS);
            doc.appendChild(root);
            mets = new METS(doc);
        }
        catch (ParserConfigurationException pce)
        {
            throw new METSException(pce);
        }
    }
    
    
    /**
     * Construct a METS wrapper for an existing METS document.
     * 
     * @param d 
     *        A w3c Document representing a METS DOM
     *        
     * @exception METSException
     */
    public METSWrapper(Document d) throws METSException
    {
        this.doc = d;
        mets = new METS(d);
    }
    
    
    /**
     * Obtain the DOM document
     * 
     * @return org.w3c.dom.Document 
     *        A w3c Document representing a METS DOM
     */
    public Document getMETSDocument()
    {
       return this.doc;
    }
    
    
    /**
     * Obtain a METS object
     * 
     * @return METS 
     *        a METS object representing a METS root element
     */     
    public METS getMETSObject()
    {
        return this.mets;
    }
    
    
    /**
     * Write a METS document to an output stream
     * 
     * @param os 
     *        The OutputStream to write the data to
     */
    public void write(OutputStream os)
    {
        DOMImplementation impl = doc.getImplementation();
        DOMImplementationLS implLS = (DOMImplementationLS)impl.getFeature("LS","3.0");
        
        LSOutput lso = implLS.createLSOutput();
        lso.setByteStream(os);
        LSSerializer writer = implLS.createLSSerializer();
        
        writer.write(doc, lso);
    }
    
    
    /**
     * Output a METS document in string form. Note the string will be UTF-16
     * 
     * @return String 
     *        The METS document in string form
     */     
    public String toString()
    {
        DOMImplementation impl = doc.getImplementation();
        DOMImplementationLS implLS = (DOMImplementationLS) impl.getFeature("LS","3.0");        
        LSSerializer writer = implLS.createLSSerializer();
        return writer.writeToString(doc);
    }
    
    
    /**
     * Validate against the most recent mets schema. The schema is
     * accessed remotely so may hang or be delayed. If wanting to use a
     * local cached schema use the other validate method
     * 
     * @exception SAXException
     *      if document is invalid
     * @exception MalformedURLException
     *      if schema URL is invalid
     *  @exception IOException
     *      if URL stream cannot be accessed
     */     
    public void validate() throws SAXException, MalformedURLException, IOException
    {
        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        URL metsSchema = new URL(Constants.SCHEMA_METS);
        // load a WXS schema, represented by a Schema instance
        Source schemaFile = new StreamSource(metsSchema.openStream());
        Schema schema = factory.newSchema(schemaFile);

        // create a Validator instance, which can be used to validate an instance document
        Validator validator = schema.newValidator();

        validator.validate(new DOMSource(doc));
    }


    /**
     * Validate against the most recent mets schema. The schema is
     * accessed remotely so may hang or be delayed. If wanting to use a
     * local cached schema use the other validate method
     * 
     * @param schemaUrl
     *          the URL of a schema to validate the METS document against
     *          
     * @exception SAXException
     *      if document is invalid
     * @exception MalformedURLException
     *      if schema URL is invalid
     *  @exception IOException
     *      if URL stream cannot be accessed
     */     
    public void validate(String schemaUrl) throws SAXException, MalformedURLException, IOException
    {
        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        URL metsSchema = new URL(schemaUrl);
        // load a WXS schema, represented by a Schema instance
        Source schemaFile = new StreamSource(metsSchema.openStream());

        Schema schema = factory.newSchema(schemaFile);
        
        // create a Validator instance, which can be used to validate an instance document
        Validator validator = schema.newValidator();
        
        validator.validate(new DOMSource(doc));       
    }
}
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
package au.edu.apsr.mtk.ch;

import java.io.InputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * A METS Reader which takes a METS input stream and maps it to a DOM
 * Document
 * 
 * @author Scott Yeadon
 */
public class METSReader
{
    private Document doc = null;
    
    /**
     * Create a METS Reader
     * 
     */
    public METSReader()
    {
        // do nothing constructor
    }
    

    /**
     * Map a METS InputStream to DOM via SAX
     * 
     * @param is
     *      METS InputStream
     * 
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     * 
     */
    public void mapToDOM(InputStream is) throws SAXException, ParserConfigurationException, IOException
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature("http://xml.org/sax/features/namespaces", true);
        spf.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        SAXParser sp = spf.newSAXParser();
        DefaultMETSHandler ch = new DefaultMETSHandler();
        
        InputSource source = new InputSource(is);

        sp.parse(source, ch);

        doc = ch.getDocument();        
    }
    
    
    /**
     * Map a METS InputStream to DOM via SAX
     * 
     * @param is
     *      METS InputStream
     * @param ch
     *      METS SAX Content Handler
     * 
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     * 
     */
    public void mapToDOM(InputStream is,
                DefaultMETSHandler ch) throws SAXException, ParserConfigurationException, IOException
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature("http://xml.org/sax/features/namespaces", true);
        spf.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        SAXParser sp = spf.newSAXParser();
        
        InputSource source = new InputSource(is);
        sp.parse(source, ch);
        doc = ch.getDocument();
    }
    
    
    /**
     * Get a DOM document resulting from a SAX parse
     *  
     *  @return Document
     *      The DOM document. May be null if called before parsing and empty
     *      if parsing exception caught.
     */
    public Document getMETSDocument()
    {
       return this.doc; 
    }
}
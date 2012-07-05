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
 * The decode and associated private methods are a modified version of the w3c base64
 * decoder found at http://www.docjar.com/html/api/org/w3c/tools/codec/Base64Decoder.java.html
 * and the following notice applies to these code fragments:
 * 
 * W3C SOFTWARE NOTICE AND LICENSE
 * http://www.w3.org/Consortium/Legal/2002/copyright-software-20021231
 * 
 * This work (and included software, documentation such as READMEs, or other
 * related items) is being provided by the copyright holders under the following
 * license. By obtaining, using and/or copying this work, you (the licensee) 
 * agree that you have read, understood, and will comply with the following 
 * terms and conditions.
 * 
 * Permission to copy, modify, and distribute this software and its documentation,
 * with or without modification, for any purpose and without fee or royalty is
 * hereby granted, provided that you include the following on ALL copies of the
 * software and documentation or portions thereof, including modifications:
 * <ol>
 *    <li>The full text of this NOTICE in a location viewable to users of the
 *    redistributed or derivative work.</li>
 *    <li>Any pre-existing intellectual property disclaimers, notices, or terms
 *    and conditions. If none exist, the W3C Software Short Notice should be
 *    included (hypertext is preferred, text is permitted) within the body of
 *    any redistributed or derivative code.</li>
 *    <li>Notice of any changes or modifications to the files, including the date 
 *    changes were made. (We recommend you provide URIs to the location from
 *    which the code is derived.)</li>
 * </ol>
 * THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT HOLDERS
 * MAKE NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY OR FITNESS FOR ANY PARTICULAR
 * PURPOSE OR THAT THE USE OF THE SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE 
 * ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.
 * 
 * COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT, SPECIAL 
 * OR CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE SOFTWARE OR DOCUMENTATION.
 * 
 * The name and trademarks of copyright holders may NOT be used in advertising 
 * or publicity pertaining to the software without specific, written prior 
 * permission. Title to copyright in this software and any associated documentation
 * will at all times remain with copyright holders.
 * 
 */
package au.edu.apsr.mtk.ch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.apache.log4j.Logger;

import au.edu.apsr.mtk.base.Constants;

/**
 * The default METS SAX Handler.
 * 
 * This class processed events from a METS document and builds a 
 * DOM. In the instance where file binData is encountered this is decoded to
 * a file and an FLocat element with a file URL (i.e. file://) is substituted.
 * The file is stored in the <code>System.io.temp</code> directory. It is
 * the responsibility of the class making use of this file to clean it up
 * once processing is complete, i.e. this content handler will <strong>not</strong>
 * delete it. Metadata binData is left encoded within the DOM.
 * 
 * Ensure both http://xml.org/sax/features/namespaces and 
 * http://xml.org/sax/features/namespace-prefixes are set to true in the 
 * SAXParserFactory object
 * 
 * @author Scott Yeadon
 */
public class DefaultMETSHandler extends DefaultHandler implements METSHandler
{
    private static Logger log = Logger.getLogger(DefaultMETSHandler.class);

    /** Prefix used for temp files */
    private static final String TEMP_PREFIX = "mets";

    /** character buffer size */
    private static final int BUFFER_SIZE = 4096;
    
    /** the DOM document */
    private Document doc = null;
    
    /** Element stack to assist in building the DOM */
    private Stack<Element> elements = new Stack<Element>();
    
    /** Locator (for future use) */
    private Locator locator;
    
    /** used when creating temporary files from binData content */
    private File currFile = null;
    
    /** output stream for writing temporary files to */
    private FileOutputStream fos = null;


    /**
     * Set the  locator
     * 
     * @param locator 
     *        The Locator object used to track the parsing location
     *        
     */ 
    @Override
    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }
        
    
    /**
     * Create an empty DOM document when the startDocument event is received
     * 
     * @exception SAXException
     *         
     */ 
    @Override
    public void startDocument() throws SAXException
    {
        try
        {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        }
        catch (Exception e)
        {
            throw new SAXException(e);
        }
    }

    
    /**
     * Processing for the startElement event.
     * 
     * Create a DOM element and push it on the stack. If an FContent element
     * is encountered return immediately. If a binData element is encountered
     * create a FLocat element and a temporary file for holding the decoded
     * content.
     * 
     * @param uri
     *      The element namespace
     * @param localName
     *      The unqualified element name
     * @param qName
     *      The qualified element name
     * @param attributes
     *      Attributes associated with the element
     * 
     * @exception SAXException
     *         
     */ 
    @Override
    public void startElement(String uri,
                            String localName,
                            String qName,
                            Attributes attributes) throws SAXException
    {
        if (qName.endsWith(Constants.ELEMENT_FCONTENT))
        {
            return;
        }

        Element e = null;
        String prefix=null;
        
        if (uri.length()>0)
        {
            if (qName.endsWith(Constants.ELEMENT_BINDATA) && !elements.peek().getTagName().endsWith(Constants.ELEMENT_MDWRAP))
            {
                try
                {
                    currFile = File.createTempFile(TEMP_PREFIX, null);
                    fos = new FileOutputStream(currFile);
                    e = createFLocat(uri, qName.split(":")[0]);
                }
                catch (IOException ioe)
                {
                    throw new SAXException(ioe);
                }
            }
            else
            {
                e = doc.createElementNS(uri,qName);
            }
        }
        else
        {
            if (qName.endsWith(Constants.ELEMENT_BINDATA)&& !elements.peek().getTagName().endsWith(Constants.ELEMENT_MDWRAP))
            {
                e = createFLocat();                    
            }
            else
            {
                e = doc.createElement(qName);
            }
        }
        
        for (int i=0; i<attributes.getLength(); i++)
        {
            e.setAttribute(attributes.getQName(i),attributes.getValue(i));
        }
        
        elements.push(e);
    }

    
    /**
     * Processing for characters.
     * 
     * For all elements other than binData characters are simply echoed to
     * the DOM. For encoded data the encoded data is written to a temporary file.
     *  
     * Where binData has been encountered, a FLocat has been previously
     * created and pushed on the stack in the startElement() method.
     * Under normal circumstances the FLocat will never be returned from a peek()
     * as it is an empty element so should never be encountered in the characters
     * method(). Thus if a FLocat is found, we know we're really processing
     * binData.
     * 
     * @param chars
     *      An array of characters
     * @param start
     *      The start position of the first in the array
     * @param length
     *      The length of the character data being passed
     * 
     * @exception SAXException
     *         
     */ 
    @Override
    public void characters(char[] chars,
                           int start,
                           int length) throws SAXException
    {
        String s = new String(chars, start, length);
        if (!s.matches("\\s+"))
        {
            Element e = elements.peek();
            if (e.getTagName().contains(Constants.ELEMENT_FLOCAT))
            {
                try
                {
                    fos.write(s.getBytes());
                    fos.flush();
                }
                catch (IOException ioe)
                {
                    throw new SAXException(ioe);
                }
                return;
            }

            if (e.getTextContent().length()==0)
            {
                e.setTextContent(s);
            }
            else
            {
                e.setTextContent(e.getTextContent() + s);
            }
        }
    }


    /**
     * Processing for skipped entities.
     * 
     * To avoid loss of skipped entities just recreate them and pass
     * to the characters() method.
     * 
     * @param name
     *      The entity name
     * 
     * @exception SAXException
     *         
     */
    @Override
    public void skippedEntity(String name) throws SAXException
    {
        String s = "&" + name + ";";
        char[] text = s.toCharArray();
        this.characters(text, 0, text.length);      
    }
        
        
    /**
     * Processing for the endElement event.
     * 
     * For most elements this results in popping the DOM element from the stack
     * and inserting it into the DOM document. For FContent we simply return as
     * FContent and binData are being replaced with FLocat in startElement(). For
     * binData the temporary file is closed.
     * 
     * @param uri
     *      The element namespace
     * @param localName
     *      The unqualified element name
     * @param qName
     *      The qualified element name
     * 
     * @exception SAXException
     *         
     */ 
    @Override
    public void endElement(String uri,
                            String localName,
                            String qName) throws SAXException
    {
        if (qName.endsWith(Constants.ELEMENT_FCONTENT))
        {
            return;
        }
        
        if (qName.endsWith(Constants.ELEMENT_BINDATA) && !elements.peek().getTagName().endsWith(Constants.ELEMENT_MDWRAP))
        {
            try
            {
                fos.close();
                decode();
            }
            catch (IOException ioe)
            {
                throw new SAXException(ioe);
            }
        }
        
        Element e = elements.pop();
        if (!elements.empty())
        {
            elements.peek().appendChild(e);
        }
        else
        {
            doc.appendChild(e);
        }
    }

        
    /**
     * Print parser location. This may be used in future for debugging
     * purposes
     *  
     * @param s
     *      Parser message text
     *
     * @return String
     *      Parser message with line/column location information
     * 
     */
    private String printLocation(String s)
    { 
        int line = locator.getLineNumber();
        int column = locator.getColumnNumber();
        return s + " at line " + line + "; column " + column;
    }
        
        

    /**
     * Create a FLocat element with href to temporary file.
     *  
     * @param uri
     *      The FLocat element namespace being used
     * @param prefix
     *      The FLocat namespace prefix being used
     *      
     * @return Element
     *      The new FLocat element
     * 
     * @exception IOException
     */
    private Element createFLocat(String uri,
                                 String prefix)
    {
        Element e = doc.createElementNS(uri, prefix + ":" + Constants.ELEMENT_FLOCAT);
        e.setAttribute(Constants.ATTRIBUTE_LOCTYPE, Constants.ATTRIBUTE_VALUE_URL);
        e.setAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_HREF, "file://" + currFile.getParentFile() + File.separator + "dec-" + currFile.getName());                
        return e;
    }
        
        
    /**
     * Create a FLocat element with href to temporary file.
     *      
     * @return Element
     *      The new FLocat element
     *  
     */
    private Element createFLocat()
    {
        Element e = doc.createElement(Constants.ELEMENT_FLOCAT);
        e.setAttribute(Constants.ATTRIBUTE_LOCTYPE, Constants.ATTRIBUTE_VALUE_URL);
        e.setAttributeNS(Constants.NS_XLINK, Constants.ATTRIBUTE_XLINK_HREF, "file://" + currFile.getParentFile() + File.separator + "dec-" + currFile.getName());                
        return e;
    }
        
        
    /**
     * Decode the base64 encoded temporary file and write to new temporary file
     *  
     * @exception IOException, SAXException
     */
    private void decode() throws IOException, SAXException
    {
        File f = new File(currFile.getParentFile() + File.separator + "dec-" + currFile.getName());
        FileInputStream fis = new FileInputStream(currFile);
        
        //FileOutputStream fos = new FileOutputStream(f);
        fos = new FileOutputStream(f);
        byte buffer[] = new byte[BUFFER_SIZE];
        byte chunk[] = new byte[4];
        int got = -1;
        int ready = 0;

        fill : while ((got = fis.read(buffer)) > 0)
        {
            int skipped = 0;
            while (skipped < got)
            {
                // Check for non-understood characters:
                while (ready < 4)
                {
                    if (skipped >= got)
                    {
                        continue fill;
                    }
                    
                    int ch = check(buffer[skipped++]);
                    if (ch >= 0)
                    {
                        chunk[ready++] = (byte) ch;
                    }
                }
            
                if (chunk[2] == 65)
                {
                    fos.write(get1(chunk, 0));
                    return;
                }
                else if (chunk[3] == 65)
                {
                    fos.write(get1(chunk, 0));
                    fos.write(get2(chunk, 0));
                    return;
                }
                else
                {
                    fos.write(get1(chunk, 0));
                    fos.write(get2(chunk, 0));
                    fos.write(get3(chunk, 0));
                }
                ready = 0;
            }
        }
        
        if (ready != 0)
        {
            throw new SAXException("Encoded data has invalid length");
        }
        
        fos.close();
        fis.close();
    }
        
        
    /**
     * Check if known base64 character and return result
     *  
     * @param ch
     *      the character to check
     *      
     * @return int
     *      the checked character
     *      
     */
    private final int check(int ch)
    {
        if ((ch >= 'A') && (ch <= 'Z'))
        {
            return ch - 'A';
        }
        else if ((ch >= 'a') && (ch <= 'z'))
        { 
            return ch - 'a' + 26;
        } 
        else if ((ch >= '0') && (ch <= '9'))
        { 
            return ch - '0' + 52;
        } 
        else
        { 
            switch (ch) 
            {
                case '=' :
                    return 65;
                case '+' :
                    return 62;
                case '/' :
                    return 63;
                default :
                    return -1;
            }
        }
    }


    /**
     * Get the first 8-bit input group
     *  
     * @param buf
     *      byte array of encoded data
     * @param offset
     *      offset into the byte array
     *      
     * @return int
     *      the first 8-bit input group
     */
    private final int get1(byte buf[], int off)
    { 
        return ((buf[off] & 0x3f) << 2) | ((buf[off + 1] & 0x30) >>> 4);
    }
    
    
    /**
     * Get the second 8-bit input group
     *  
     * @param buf
     *      byte array of encoded data
     * @param offset
     *      offset into the byte array

     * @return int
     *      the second 8-bit input group
     *      
     */
    private final int get2(byte buf[], int off)
    { 
        return ((buf[off + 1] & 0x0f) << 4) | ((buf[off + 2] & 0x3c) >>> 2);
    }

    
    /**
     * Get the third 8-bit input group
     *  
     * @param buf
     *      byte array of encoded data
     * @param offset
     *      offset into the byte array
     *      
     * @return int
     *      the third 8-bit input group
     */
    private final int get3(byte buf[], int off)
    { 
        return ((buf[off + 2] & 0x03) << 6) | (buf[off + 3] & 0x3f);
    }
        

    
    /**
     * Get the DOM document
     *  
     *  @return Document
     *      The DOM document. May be null if called before parsing and empty
     *      if parsing exception caught.
     */
    public Document getDocument()
    {
        return this.doc;
    }
}
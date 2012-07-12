/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import au.edu.apsr.mtk.ch.DefaultMETSHandler;
import java.io.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SchemaValidator2 {

    public static void main(String[] args) throws SAXException, IOException {
        if(args.length < 2)System.exit(1);
        try{
            // 1. Lookup a factory for the W3C XML Schema language
            //SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // 2. Compile the schema.
            // Here the schema is loaded from a java.io.File, but you could use
            // a java.net.URL or a javax.xml.transform.Source instead.
            File schemaLocation = new File(args[0]);
            Schema schema = factory.newSchema(schemaLocation);

            // 3. Get a validator from the schema.
            Validator validator = schema.newValidator();

            /*SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature("http://xml.org/sax/features/namespaces", true);
            spf.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            SAXParser sp = spf.newSAXParser();
            DefaultMETSHandler ch = new DefaultMETSHandler();            
            InputSource source = new InputSource(new FileInputStream(args[1]));
            sp.parse(source, ch);*/
           
            
            //Document doc = ch.getDocument();

            // 4. Parse the document you want to check.
            Source source = new StreamSource(args[1]);                  
          
            // 5. Check the document -> DOMSource: oorzaak van alle problemen!! Of is het de DefaultMETSHandler
            //validator.validate(new DOMSource(doc));
            
            //validator.validate(new StreamSource(args[1]));
            validator.validate(source);
            System.out.println(args[0] + " is valid.");

        }catch (Exception ex) {
            System.out.println(args[0] + " is not valid because ");
            System.out.println(ex.getMessage());
        }

    }

}
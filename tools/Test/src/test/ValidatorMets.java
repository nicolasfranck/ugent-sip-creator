/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import au.edu.apsr.mtk.ch.DefaultMETSHandler;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author nicolas
 */
public class ValidatorMets {
    public static void main(String[]args){
        if(args.length < 2)return;
        try{
            String pathSchema = args[0];
            String pathSource = args[1];

            InputStream in = new FileInputStream(pathSource);

            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature("http://xml.org/sax/features/namespaces", true);
            spf.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            SAXParser sp = spf.newSAXParser();
            DefaultMETSHandler ch = new DefaultMETSHandler();
            InputSource source = new InputSource(in);
            sp.parse(source, ch);
            Document doc = ch.getDocument();

            // create a SchemaFactory capable of understanding WXS schemas
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            URL metsSchema = new URL("file://"+pathSchema);
            // load a WXS schema, represented by a Schema instance
            Source schemaFile = new StreamSource(metsSchema.openStream());
            Schema schema = factory.newSchema(schemaFile);

            // create a Validator instance, which can be used to validate an instance document
            Validator validator = schema.newValidator();
            
            validator.validate(new DOMSource(doc));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

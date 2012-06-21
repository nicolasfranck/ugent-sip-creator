package gov.loc.repository.bagger.ui.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class XML {

    private static SchemaFactory sf = null;
    private static DocumentBuilderFactory dbf = null;
    private static DocumentBuilder db = null;   
    
    public static DocumentBuilderFactory getDocumentBuilderFactory(){
        if(dbf == null){
            dbf = DocumentBuilderFactory.newInstance();
        }
        return dbf;
    }    
    public static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException{
        if(db == null){
            db = getDocumentBuilderFactory().newDocumentBuilder();
        }
        return db;
    }
    public static SchemaFactory getSchemaFactory(){
        if(sf == null){
            sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        }
        return sf;
    }
    /*
     * check against schema (XSD or DTD)
     */
    public static void validate(URL sourceURL,URL schemaURL) throws SAXException, IOException{
        validate(
            new BufferedReader(new InputStreamReader(sourceURL.openStream())),
            new BufferedReader(new InputStreamReader(schemaURL.openStream()))
        );
    }
    /*
     * warning: do not reuse your inputstream or readers afterwards, for the parsers close them
     * this will give a 'Bad File Descriptor'
     */
    public static void validate(InputStream sourceInputStream,InputStream schemaInputStream) throws SAXException, IOException{
        StreamSource sourceStreamSource = new StreamSource(sourceInputStream);
        StreamSource schemaStreamSource = new StreamSource(schemaInputStream);
        Schema schema = getSchemaFactory().newSchema(schemaStreamSource);
        validate(sourceStreamSource,schema);
    }
    public static void validate(Reader sourceReader,Reader schemaReader) throws SAXException, IOException{        
        StreamSource sourceStreamSource = new StreamSource(sourceReader);
        StreamSource schemaStreamSource = new StreamSource(schemaReader);
        Schema schema = getSchemaFactory().newSchema(schemaStreamSource);
        validate(sourceStreamSource,schema);
    }
    public static void validate(Source source,Schema schema) throws SAXException, IOException{
        Validator validator = schema.newValidator();
        validator.validate(source);
    }
    /*
     * check for well formedness
     */
    public static void validate(String sourceURL) throws ParserConfigurationException, MalformedURLException, IOException, SAXException{
        validate(new URL(sourceURL));        
    }
    public static void validate(URL sourceURL) throws ParserConfigurationException, MalformedURLException, IOException, SAXException{
        validate(sourceURL.openStream());
    }
    public static void validate(InputStream sourceIS) throws IOException, SAXException, ParserConfigurationException{
        toDocument(sourceIS);
    }
    /*
     * convert XML input to W3C Document object
     */
    public static Document toDocument(URL sourceURL) throws ParserConfigurationException, SAXException, IOException{
        return toDocument(sourceURL.openStream());
    }
    public static Document toDocument(InputStream sourceIS) throws ParserConfigurationException, SAXException, IOException{
        return getDocumentBuilder().parse(sourceIS);
    }
}

package helper;

import Importers.Importer;
import Importers.ImporterFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class XML {

    private static SchemaFactory sf = null;
    private static DocumentBuilderFactory dbf = null;
    private static DocumentBuilder db = null;
    private static DOMImplementationRegistry registry = null;
    private static DOMImplementationLS impl = null;

    public static void free(){
        sf = null;
        dbf = null;
        db = null;
        registry = null;
        impl = null;
    }
    /*
     * W3C DOM helper functions
     */
    public static DOMImplementationRegistry getDOMImplementationRegistry() throws Exception {
        if(registry == null){
            registry = DOMImplementationRegistry.newInstance();            
        }
        return registry;
    }

    public static DOMImplementationLS getDOMImplementationLS() throws Exception {
        if(impl == null){
            impl = (DOMImplementationLS)(getDOMImplementationRegistry().getDOMImplementation("LS"));
        }
        return impl;
    }
    public static LSSerializer createLSSerializer() throws Exception{
        return getDOMImplementationLS().createLSSerializer();
    }
    public static LSOutput createLSOutput(OutputStream out) throws Exception{
        LSOutput lsout = getDOMImplementationLS().createLSOutput();
        lsout.setByteStream(out);
        return lsout;
    }
    public static LSOutput createLSOutput(Writer out) throws Exception{
        LSOutput lsout = getDOMImplementationLS().createLSOutput();
        lsout.setCharacterStream(out);
        return lsout;
    }
    public static DocumentBuilderFactory getDocumentBuilderFactory(){
        if(dbf == null){
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);                  
            dbf.setValidating(false);
            dbf.setIgnoringComments(true);
            dbf.setIgnoringElementContentWhitespace(true);
        }
        return dbf;
    }    
    public static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException{
        if(db == null){
            db = getDocumentBuilderFactory().newDocumentBuilder();   
            db.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId)throws SAXException, IOException {
                    System.out.println("Ignoring " + publicId + ", " + systemId);
                    return new InputSource(new StringReader(""));
                }
            });
        }
        return db;
    }
    public static Document createDocument() throws ParserConfigurationException{
        return getDocumentBuilder().newDocument();
    }
    /*
     * Schema helper functions
     */
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
    public static void validate(Document doc,URL schemaURL) throws SAXException, IOException{
        validate(
            doc,createSchema(schemaURL)            
        );
    }
    public static void validate(Document doc,Schema schema) throws SAXException, IOException{
        validate(new DOMSource(doc),schema);
    }
    public static void validate(Source source,Schema schema) throws SAXException, IOException{
        Validator validator = schema.newValidator();
        validator.validate(source);
    }
    public static Schema createSchema(URL schemaURL) throws SAXException, IOException{        
        return getSchemaFactory().newSchema(new StreamSource(
            new BufferedReader(new InputStreamReader(schemaURL.openStream()))                
        ));
    }
    public static Schema createSchema(File schemaFile) throws SAXException{
        return getSchemaFactory().newSchema(schemaFile);
    }


    /*
     * check for well formedness
     */    
    public static void validate(URL sourceURL) throws ParserConfigurationException, MalformedURLException, IOException, SAXException{
        validate(sourceURL.openStream());
    }
    public static void validate(java.io.File sourceF) throws ParserConfigurationException, MalformedURLException, IOException, SAXException{
        validate(new java.io.FileInputStream(sourceF));
    }
    public static void validate(InputStream sourceIS) throws IOException, SAXException, ParserConfigurationException{
        XMLToDocument(sourceIS);
    }
    public static void validate(Reader sourceR) throws IOException, SAXException, ParserConfigurationException{
        XMLToDocument(sourceR);
    }

    /*
     * convert XML input to W3C Document object
     */
    public static Document XMLToDocument(java.io.File file) throws ParserConfigurationException, SAXException, IOException{      
        return XMLToDocument(new java.io.FileInputStream(file));
    }
    public static Document XMLToDocument(URL sourceURL) throws ParserConfigurationException, SAXException, IOException{
        return XMLToDocument(sourceURL.openStream());
    }
    public static Document XMLToDocument(InputStream sourceIS) throws ParserConfigurationException, SAXException, IOException{
        return getDocumentBuilder().parse(sourceIS);
    }
    public static Document XMLToDocument(Reader sourceR) throws SAXException, ParserConfigurationException, IOException{
        return getDocumentBuilder().parse(new InputSource(sourceR));
    }

    /*
     * convert W3C Document object to XML
     */
    public static void DocumentToXML(Document doc,OutputStream out) throws Exception{
        DocumentToXML(doc,createLSOutput(out));
    }
     public static void DocumentToXML(Document doc,OutputStream out,boolean pretty) throws Exception{
        DocumentToXML(doc,createLSOutput(out),pretty);
    }
    public static void DocumentToXML(Document doc,Writer out) throws Exception{
        DocumentToXML(doc,createLSOutput(out));
    }
    public static void DocumentToXML(Document doc,Writer out,boolean pretty) throws Exception{
        DocumentToXML(doc,createLSOutput(out),pretty);
    }
    public static void DocumentToXML(Document doc,LSOutput lsout) throws Exception{
        DocumentToXML(doc,lsout,false);
    }    
    public static void DocumentToXML(Document doc,LSOutput lsout,boolean pretty) throws Exception{
        LSSerializer serializer = createLSSerializer();
        DOMConfiguration conf = serializer.getDomConfig();                
        conf.setParameter("format-pretty-print",new Boolean(pretty));
        serializer.write(doc,lsout);
    }
    public static void main(String [] args){
        try{
            File file = new File("/tmp/bag-info.txt");
            Importer importer = ImporterFactory.createImporter(file);
            if(importer == null){
                System.out.println("no importer found for "+file);                    
            }
            System.out.println("importing from file "+file);
            Document doc = importer.performImport(file);
           
            System.out.println("importing from file "+file+" successfull");
            try{
                XML.DocumentToXML(doc,new java.io.FileOutputStream(new File("/tmp/dc.xml")),true);
            }catch(Exception e){
                e.printStackTrace();
            }           
            Schema schema = XML.createSchema(new URL("http://www.openarchives.org/OAI/2.0/oai_dc.xsd"));
            System.out.println("schema created");
            XML.validate(
                doc,
                schema
            );
            System.out.println("validation successfull");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

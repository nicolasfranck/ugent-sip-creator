package ugent.bagger.helper;

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
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.*;

/**
 *
 * @author nicolas
 */
public class XML {

    private static SchemaFactory sf = null;
    private static DOMImplementationRegistry registry = null;
    private static DOMImplementationLS impl = null;    
    private static Logger log = Logger.getLogger(XML.class);

    
    /*
     * W3C DOM helper functions
     */
    public static ErrorHandler getErrorHandler(){
        return new ErrorHandler(){
            @Override
            public void warning(SAXParseException e) throws SAXException {
                throw e;
            }
            @Override
            public void error(SAXParseException e) throws SAXException {
                throw e;
            }
            @Override
            public void fatalError(SAXParseException e) throws SAXException {
                throw e;
            }            
        };
    }
    public static DOMImplementationRegistry getDOMImplementationRegistry() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        if(registry == null){
            registry = DOMImplementationRegistry.newInstance();            
        }
        return registry;
    }

    public static DOMImplementationLS getDOMImplementationLS() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        if(impl == null){
            impl = (DOMImplementationLS)(getDOMImplementationRegistry().getDOMImplementation("LS"));
        }
        return impl;
    }
    public static LSSerializer createLSSerializer() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        return getDOMImplementationLS().createLSSerializer();
    }
    public static LSOutput createLSOutput(OutputStream out) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        LSOutput lsout = getDOMImplementationLS().createLSOutput();
        lsout.setByteStream(out);
        return lsout;
    }
    public static LSOutput createLSOutput(Writer out) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        LSOutput lsout = getDOMImplementationLS().createLSOutput();
        lsout.setCharacterStream(out);
        return lsout;
    }
    public static DocumentBuilderFactory getDocumentBuilderFactory(boolean validate){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);                  
        dbf.setValidating(validate);
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);                
        return dbf;
    }
    public static DocumentBuilderFactory getDocumentBuilderFactory(){
        return getDocumentBuilderFactory(false);        
    }    
    public static DocumentBuilder getDocumentBuilder(boolean validate) throws ParserConfigurationException{
        DocumentBuilder db = getDocumentBuilderFactory(validate).newDocumentBuilder();
        //default error-handler simply prints out first 10 errors when validating dtd or xsd on creation
        db.setErrorHandler(getErrorHandler());
        if(!validate){            
            db.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId)throws SAXException, IOException {
                    //avoid schema validating while building xml documents                    
                    return new InputSource(new StringReader(""));
                }
            }); 
        }               
        return db;
    }
    public static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException{
        return getDocumentBuilder(false);        
    }
    public static Document createDocument(boolean validate) throws ParserConfigurationException{
        return getDocumentBuilder(validate).newDocument();
    }
    public static Document createDocument() throws ParserConfigurationException{
        return createDocument(false);
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
     * check against schema (XSD or DTD):
     *  1. well formed? (always)
     *  2. schema valid?
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
        return createSchema(schemaURL.openStream());        
    }
    public static Schema createSchema(InputStream in) throws SAXException, IOException{
        return getSchemaFactory().newSchema(new StreamSource(
            new BufferedReader(new InputStreamReader(in))                
        ));
    }
    public static Schema createSchema(File schemaFile) throws SAXException{
        return getSchemaFactory().newSchema(schemaFile);
    }


    /*
     * validation based on information found in the xml document (references to xsd or dtd):
     *  1. well formed? (always)
     *  2. optional validation of schema referenced in document
     */    
    public static void validate(URL sourceURL,boolean schemaValid) throws ParserConfigurationException, MalformedURLException, IOException, SAXException{
        validate(sourceURL.openStream(),schemaValid);
    }
    public static void validate(URL sourceURL) throws ParserConfigurationException, MalformedURLException, IOException, SAXException{
        validate(sourceURL.openStream(),false);
    }
    public static void validate(File sourceF,boolean schemaValid) throws ParserConfigurationException, MalformedURLException, IOException, SAXException{
        validate(new FileInputStream(sourceF));
    }
    public static void validate(File sourceF) throws ParserConfigurationException, MalformedURLException, IOException, SAXException{
        validate(sourceF,false);
    }
    public static void validate(InputStream sourceIS,boolean schemaValid) throws IOException, SAXException, ParserConfigurationException{
        XMLToDocument(sourceIS,schemaValid);
    }
    public static void validate(InputStream sourceIS) throws IOException, SAXException, ParserConfigurationException{
        validate(sourceIS,false);
    }
    public static void validate(Reader sourceR,boolean schemaValid) throws IOException, SAXException, ParserConfigurationException{
        XMLToDocument(sourceR,schemaValid);
    }
    public static void validate(Reader sourceR) throws IOException, SAXException, ParserConfigurationException{
        validate(sourceR,false);
    }

    /*
     * convert XML input to W3C Document object
     */
    public static Document XMLToDocument(File file,boolean validate) throws ParserConfigurationException, SAXException, IOException{      
        return XMLToDocument(new FileInputStream(file),validate);
    }
    public static Document XMLToDocument(File file) throws ParserConfigurationException, SAXException, IOException{      
        return XMLToDocument(file,false);
    }
    public static Document XMLToDocument(URL sourceURL,boolean validate) throws IOException, ParserConfigurationException, SAXException{
        return XMLToDocument(sourceURL.openStream(),false);
    }
    public static Document XMLToDocument(URL sourceURL) throws ParserConfigurationException, SAXException, IOException{
        return XMLToDocument(sourceURL,false);
    }
    public static Document XMLToDocument(InputStream sourceIS,boolean validate) throws ParserConfigurationException, SAXException, IOException{
        return getDocumentBuilder(validate).parse(sourceIS);
    }
    public static Document XMLToDocument(InputStream sourceIS) throws ParserConfigurationException, SAXException, IOException{
        return XMLToDocument(sourceIS,false);
    }
    public static Document XMLToDocument(Reader sourceR,boolean validate) throws SAXException, ParserConfigurationException, IOException{
        return getDocumentBuilder(validate).parse(new InputSource(sourceR));
    }
    public static Document XMLToDocument(Reader sourceR) throws SAXException, ParserConfigurationException, IOException{
        return XMLToDocument(sourceR,false);
    }

    /*
     * convert W3C Document object to XML
     */
    public static void ElementToXML(Element element,OutputStream out) throws ParserConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        ElementToXML(element,out,false);              
    }
    public static void ElementToXML(Element element,OutputStream out,boolean pretty) throws ParserConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        Document doc = createDocument();
        Node node = doc.importNode(element,true);
        doc.appendChild(node);
        DocumentToXML(doc,out,pretty);
    }
    public static void ElementToXML(Element element,Writer out) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException{
        ElementToXML(element,out,false);
    }
    public static void ElementToXML(Element element,Writer out,boolean pretty) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException{
        Document doc = createDocument();
        Node node = doc.importNode(element,true);
        doc.appendChild(node);
        DocumentToXML(doc,out,pretty);
    }
    public static void DocumentToXML(Document doc,OutputStream out) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        DocumentToXML(doc,out,false);
    }
     public static void DocumentToXML(Document doc,OutputStream out,boolean pretty) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        DocumentToXML(doc,createLSOutput(out),pretty);
    }
    public static void DocumentToXML(Document doc,Writer out) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        DocumentToXML(doc,createLSOutput(out));
    }
    public static void DocumentToXML(Document doc,Writer out,boolean pretty) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        DocumentToXML(doc,createLSOutput(out),pretty);
    }
    public static void DocumentToXML(Document doc,LSOutput lsout) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        DocumentToXML(doc,lsout,false);
    }    
    public static void DocumentToXML(Document doc,LSOutput lsout,boolean pretty) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        LSSerializer serializer = createLSSerializer();
        DOMConfiguration conf = serializer.getDomConfig();                
        conf.setParameter("format-pretty-print",pretty);
        serializer.write(doc,lsout);
    }
    public static String NodeToXML(Node node) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        return NodeToXML(node,false);
    }
    public static String NodeToXML(Node node,boolean pretty) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        LSSerializer serializer = createLSSerializer();
        DOMConfiguration conf = serializer.getDomConfig();                
        conf.setParameter("format-pretty-print",pretty);
        return serializer.writeToString(node);
    }
    public static void main(String [] args){      
     
        try{
            //File file = new File("/home/nicolas/EAD/VEA_000005_A/ead.xml");
            File file = new File("/home/nicolas/dtd1.xml");
            System.out.println("creating document");
            Document doc = XMLToDocument(file,false);
            System.out.println("document created"); 
            
            DocumentType docType = doc.getDoctype();
            
            System.out.println("doctype name: "+docType.getName());
            System.out.println("doctype public id: "+docType.getPublicId());
            System.out.println("doctype system id: "+docType.getSystemId());
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}

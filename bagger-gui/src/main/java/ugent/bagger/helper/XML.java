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
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSResourceResolver;
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
            try{
                sf.setResourceResolver(new ClasspathResourceResolver());
            }catch(Exception e){    
                log.debug(e);
                e.printStackTrace();
            }
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
        validate(doc,createSchema(schemaURL));
    }
    public static void validate(Document doc,Schema schema) throws SAXException, IOException{
        validate(new DOMSource(doc),schema);
    }
    public static void validate(Source source,Schema schema) throws SAXException, IOException{        
        schema.newValidator().validate(source);
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
    /*
    public static void main(String [] args){      
     
        try{
            HashMap emptyNSReplacements = new HashMap();
            emptyNSReplacements.put("ead","urn:isbn:1-931666-22-9");
            
            File xsdFile = new File("/home/nicolas/Bagger-LC/bagger-gui/src/main/resources/metadata/xsd/ead.xsd");            
            File file = new File("/tmp/ead2.xml");
            
            Document doc = XMLToDocument(file,false);                        
            DocumentType docType = doc.getDoctype();
            String namespace = doc.getDocumentElement().getNamespaceURI();    
            
            if(namespace == null || namespace.isEmpty()){                
                String name = docType.getName() != null ? docType.getName() : doc.getDocumentElement().getTagName();                
                if(!emptyNSReplacements.containsKey(name)){
                    throw new Exception("could not find replacement for '"+name+"'");
                }                
                ErrorListener el = new ErrorListener(){
                    @Override
                    public void warning(TransformerException exception) throws TransformerException {
                        throw exception;
                    }
                    @Override
                    public void error(TransformerException exception) throws TransformerException {
                        throw exception;
                    }
                    @Override
                    public void fatalError(TransformerException exception) throws TransformerException {
                        throw exception;
                    }
                };
                TransformerFactory tf = TransformerFactory.newInstance();
                tf.setErrorListener(el);
                Transformer trans = tf.newTransformer();
                trans.setErrorListener(el);                                                
                doc.getDocumentElement().setAttributeNS(
                    "http://www.w3.org/2000/xmlns/",
                    "xmlns:xsi",
                    "http://www.w3.org/1999/XMLSchema-instance"
                );           
                doc.getDocumentElement().setAttributeNS(
                    "http://www.w3.org/2000/xmlns/",
                    "xmlns:xlink",
                    "http://www.w3.org/1999/xlink"
                );                       
                doc.getDocumentElement().setAttribute("xmlns",(String)emptyNSReplacements.get(name));
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                trans.transform(new DOMSource(doc),new StreamResult(bout));
                doc = XMLToDocument(new ByteArrayInputStream(bout.toByteArray()));
            }
            validate(doc,xsdFile.toURI().toURL());
            
            DocumentToXML(doc,new FileOutputStream(new File("/tmp/fixed_ead.xml")));
            
        }catch(Exception e){
            e.printStackTrace();
        }

    }*/
    private static class ClasspathResourceResolver implements LSResourceResolver {        
        private ClasspathResourceResolver() throws ClassNotFoundException, IllegalAccessException, InstantiationException {         
        }        
        @Override
        public LSInput resolveResource(String type, String namespaceURI,String publicId, String systemId,String baseURI) {
            LSInput lsInput = null;
            /*System.out.println("ClasspathResourceResolver::resolveResource");
            System.out.println("type: "+type);
            System.out.println("namespaceURI: "+namespaceURI);
            System.out.println("publicId: "+publicId);
            System.out.println("systemId: "+systemId);
            System.out.println("baseURI: "+baseURI);*/
            try {
                lsInput = getDOMImplementationLS().createLSInput();
                URL url = Context.getResource(systemId);                
                //System.out.println("url: "+url);
                InputStream is = Context.getResourceAsStream(systemId);;                
                lsInput.setByteStream(is);
                lsInput.setSystemId(systemId);
            } catch (ClassNotFoundException ex) {
                log.debug(ex);                
            } catch (InstantiationException ex) {
                log.debug(ex);
            } catch (IllegalAccessException ex) {
                log.debug(ex);
            }            
            return lsInput;
        }
    }
}

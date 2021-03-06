package ugent.bagger.helper;

import com.anearalone.mets.*;
import com.anearalone.mets.StructMap.Div;
import com.anearalone.mets.StructMap.Div.Fptr;       
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.validation.Schema;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.DocumentCreationFailedException;
import ugent.bagger.exceptions.DtdNoFixFoundException;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.MdRefException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.params.VelocityTemplate;

/**
 *
 * @author nicolas
 */
public class MetsUtils {
    
    static HashMap<String,HashMap<String,Object>>crosswalk;
    static HashMap<String,HashMap<String,Object>> xsdMap = null;    
    static HashMap<String,String> baginfoMap = null;    
    static HashMap<String,String> namespaceMap = null;
    static HashMap<String,String> typeMap = null;
    static HashMap<String,String> rootNameMapping = null;
    static HashMap<String,String> docTypeMapping = null;
    static ArrayList<String>forbiddenNamespaces = null;
    static Pattern ncname_forbidden = Pattern.compile("[^a-zA-Z0-9_-]");    
    static final Log log = LogFactory.getLog(MetsUtils.class);
    public static final String NAMESPACE_DC = "http://purl.org/dc/elements/1.1/";
    public static final String NAMESPACE_OAI_DC = "http://www.openarchives.org/OAI/2.0/oai_dc/";     
    public static ArrayList<VelocityTemplate>baginfoTemplates;
    public static HashMap<String,String>dtdTransformations;

    public static HashMap<String, String> getDocTypeMapping() {
        if(docTypeMapping == null){
            docTypeMapping = (HashMap<String,String>) Beans.getBean("docTypeMapping");
        }
        return docTypeMapping;
    }
    public static void setDocTypeMapping(HashMap<String, String> docTypeMapping) {
        MetsUtils.docTypeMapping = docTypeMapping;
    }
    public static HashMap<String, String> getRootNameMapping() {
        if(rootNameMapping == null){
            rootNameMapping = (HashMap<String,String>) Beans.getBean("rootNameMapping");
        }
        return rootNameMapping;
    }

    public static void setRootNameMapping(HashMap<String, String> rootNameMapping) {
        MetsUtils.rootNameMapping = rootNameMapping;
    }
    public static HashMap<String, String> getBaginfoMap() {
        if(baginfoMap == null){
            baginfoMap = (HashMap<String,String>) Beans.getBean("baginfoMap");
        }
        return baginfoMap;
    }
    public static void setBaginfoMap(HashMap<String, String> baginfoMap) {
        MetsUtils.baginfoMap = baginfoMap;
    }
    public static HashMap<String, HashMap<String, Object>> getCrosswalk() {
        if(crosswalk == null){
            crosswalk = (HashMap<String,HashMap<String,Object>>) Beans.getBean("crosswalk");
        }
        return crosswalk;
    }
    public static void setCrosswalk(HashMap<String, HashMap<String, Object>> crosswalk) {
        MetsUtils.crosswalk = crosswalk;
    }
    public static HashMap<String, String> getTypeMap() {
        if(typeMap == null){
            typeMap = (HashMap<String,String>) Beans.getBean("typeMap");
        }
        return typeMap;
    }
    public static void setTypeMap(HashMap<String, String> typeMap) {
        MetsUtils.typeMap = typeMap;
    }    
    
    public static HashMap<String,HashMap<String,Object>> getXsdMap() {
        if(xsdMap == null){
            xsdMap = (HashMap<String,HashMap<String,Object>>) Beans.getBean("xsdMap");
        }        
        return xsdMap;
    }
    public static void setXsdMap(HashMap<String,HashMap<String,Object>> xsdMap) {
        MetsUtils.xsdMap = xsdMap;
    }  
    public static HashMap<String, String> getNamespaceMap(){
        if(namespaceMap == null){
            namespaceMap = (HashMap<String,String>) Beans.getBean("namespaceMap");
        }
        return namespaceMap;
    }
    public static void setNamespaceMap(HashMap<String, String> namespaceMap) {
        MetsUtils.namespaceMap = namespaceMap;
    }
    public static ArrayList<String> getForbiddenNamespaces(){
        if(forbiddenNamespaces == null){
            forbiddenNamespaces = (ArrayList<String>) Beans.getBean("forbiddenNamespaces");
        }
        return forbiddenNamespaces;
    }
    public static void setForbiddenNamespaces(ArrayList<String> forbiddenNamespaces) {
        MetsUtils.forbiddenNamespaces = forbiddenNamespaces;
    }
    public static Mets readMets(File file) throws ParserConfigurationException, DatatypeConfigurationException, FileNotFoundException, SAXException, ParseException, IOException{
        return new MetsReader().read(new FileInputStream(file));
    }
    public static Mets readMets(URL url) throws SAXException, ParseException, DatatypeConfigurationException, ParserConfigurationException, IOException{
        return new MetsReader().read(url.openStream());
    }
    public static Mets readMets(InputStream in) throws ParserConfigurationException, ParserConfigurationException, ParseException, ParserConfigurationException, ParserConfigurationException, ParserConfigurationException, SAXException, IOException, ParserConfigurationException, ParserConfigurationException, DatatypeConfigurationException{
        return new MetsReader().read(in);
    }
    public static void writeMets(Mets mets,File file) throws FileNotFoundException, TransformerException, DatatypeConfigurationException, ParserConfigurationException{
        writeMets(mets,new FileOutputStream(file));
    }
    public static void writeMets(Mets mets,Writer writer) throws ParserConfigurationException, DatatypeConfigurationException, TransformerException{
        writeMets(mets,new WriterOutputStream(writer));
    }
    public static void writeMets(Mets mets,OutputStream out) throws ParserConfigurationException, DatatypeConfigurationException, TransformerException{                
        new MetsWriter().writeToOutputStream(mets,out);
    }
    public static Mets documentToMets(Document doc){
        Mets mets = new Mets();        
        mets.unmarshal(doc.getDocumentElement());
        return mets;
    }
    public static StructMap toStructMap(DefaultMutableTreeNode node){
        return toStructMap(node,new DefaultMetsCallback());
    }
    public static StructMap toStructMap(DefaultMutableTreeNode node,MetsCallback metsCallback){
        StructMap struct = new StructMap();                
        struct.setDiv(toDiv(node,metsCallback));                
        metsCallback.onCreateStructMap(struct,node);
        return struct;
    } 
    public static Div toDiv(DefaultMutableTreeNode node){        
        return toDiv(node,new DefaultMetsCallback());
    }
    public static Div toDiv(DefaultMutableTreeNode node,MetsCallback metsCallback){
        Div div = new Div();
        div.setLabel(node.getUserObject().toString());        
        Enumeration enumeration = node.children();
        while(enumeration.hasMoreElements()){            
            DefaultMutableTreeNode n = (DefaultMutableTreeNode)enumeration.nextElement();            
            if(n.isLeaf()){
                Fptr filePointer = new Fptr(); 
                String fileID = ArrayUtils.join(n.getUserObjectPath(),File.separator);                
                filePointer.setFILEID(fileID);
                div.getFptr().add(filePointer);
            }else{
                div.getDiv().add(toDiv(n,metsCallback));
            }
        }                        
        metsCallback.onCreateDiv(div,node);
        return div;
    }
    /*
     * TODO: check binData within mdWrap (not possible in Mets api)
     */
    public static void validate(Mets mets) throws MdRefException{
        for(MdSec mdSec:mets.getDmdSec()){
           validate(mdSec);
        }
        for(AmdSec amdSec:mets.getAmdSec()){           
           for(MdSec m:amdSec.getDigiprovMD()){
               validate(m);
           }
           for(MdSec m:amdSec.getRightsMD()){
               validate(m);
           }
           for(MdSec m:amdSec.getSourceMD()){
               validate(m);
           }
           for(MdSec m:amdSec.getTechMD()){
               validate(m);
           }
        }
    }
    public static void validate(MdSec mdSec) throws MdRefException{        
        if(mdSec.getMdRef() != null) {
            throw new MdRefException("MdRef not allowed. XML need to be wrapped in a xmlData element");
        }
    }
   
    public static MdSec createMdSec(File file) throws IOException, SAXException, ParserConfigurationException, IllegalNamespaceException, NoNamespaceException, MalformedURLException, TransformerException, TransformerConfigurationException, ClassNotFoundException, IllegalAccessException, DtdNoFixFoundException, InstantiationException, DocumentCreationFailedException{        
        return createMdSec(XML.XMLToDocument(file));        
    }
    public static String createID(){
        //xsd NCName: moet starten met letter of _, colon (':') mag niet voorkomen
        return "_"+UUID.randomUUID().toString();
    }
    public static MdSec createMdSec(Document doc) throws NoNamespaceException, IllegalNamespaceException, MalformedURLException, SAXException, IOException, TransformerException, ParserConfigurationException, TransformerConfigurationException, ClassNotFoundException, IllegalAccessException, InstantiationException, DtdNoFixFoundException, DocumentCreationFailedException{
        return createMdSec(doc,true);
    }
    public static MdSec createMdSec(Document doc,boolean validate) throws NoNamespaceException, IllegalNamespaceException, MalformedURLException, SAXException, IOException, TransformerException, ParserConfigurationException, TransformerConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException, DtdNoFixFoundException, DocumentCreationFailedException{
        MdSec mdSec = new MdSec(createID());                
        mdSec.setMdWrap(createMdWrap(doc,validate));
        mdSec.setGROUPID(mdSec.getMdWrap().getMDTYPE().toString());         
        if(mdSec.getCREATED() == null){
            try{
                mdSec.setCREATEDATE(DateUtils.DateToGregorianCalender());
            }catch(Exception e){
                log.error(e.getMessage());
            }            
        }
        return mdSec;
    }
    public static MdSec.MdWrap createMdWrap(Document doc) throws NoNamespaceException, IllegalNamespaceException, MalformedURLException, SAXException, IOException, TransformerException, ParserConfigurationException, TransformerConfigurationException, InstantiationException, ClassNotFoundException, IllegalAccessException, DtdNoFixFoundException, DocumentCreationFailedException{
        return createMdWrap(doc,true);
    }
    public static MdSec.MdWrap createMdWrap(Document doc,boolean validate) throws NoNamespaceException, IllegalNamespaceException, MalformedURLException, SAXException, IOException, TransformerException, ParserConfigurationException, TransformerConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException, DtdNoFixFoundException, DocumentCreationFailedException{
        String namespace = doc.getDocumentElement().getNamespaceURI();                    
        
        //elke xml moet namespace bevatten (probeer te herstellen, indien mogelijk)        
        if(namespace == null || namespace.isEmpty()){           
            try{
                doc = fixNamespace(doc);
                namespace = doc.getDocumentElement().getNamespaceURI();            
            }catch(DtdNoFixFoundException e){
                log.error(e.getMessage());                
            }            
        } 
        if(namespace != null && !namespace.isEmpty()){  
            //sommige xml mag niet in mdWrap: vermijd METS binnen METS!
            if(getForbiddenNamespaces().contains(namespace)){
                throw new IllegalNamespaceException("namespace "+namespace+" is forbidden in mdWrap",namespace);
            }
            //indien XSD bekend, dan validatie hierop       
            String schemaPath = getSchemaPath(doc);        
            if(validate && schemaPath != null){      
                URL schemaURL = Context.getResource(schemaPath);
                log.debug("validating against "+schemaPath);
                log.debug("schemaURL: "+schemaURL);
                Schema schema = XML.createSchema(schemaURL);                        
                log.debug("validating done!");
                XML.validate(doc,schema);            
            } 
        }
        
        
        MdSec.MDTYPE mdType = null;
        try{                     
            mdType = MdSec.MDTYPE.fromValue(getNamespaceMap().get(namespace));                              
        }catch(IllegalArgumentException e){
            log.error(e.getMessage());
            mdType = MdSec.MDTYPE.OTHER;                        
        }
        MdSec.MdWrap mdWrap = new MdSec.MdWrap(mdType);                                                            
        if(mdType == MdSec.MDTYPE.OTHER){
            mdWrap.setOTHERMDTYPE(
                namespace != null && !namespace.isEmpty() ? namespace : doc.getDocumentElement().getLocalName()
            );
        } 
        mdWrap.setMIMETYPE("text/xml");        
        mdWrap.getXmlData().add(doc.getDocumentElement());                
        return mdWrap;
    }
    public static String getSchemaPath(Document doc) {
        String namespace = doc.getDocumentElement().getNamespaceURI();
        String schemaPath = null;
        if(getXsdMap().containsKey(namespace)){                        
            
            HashMap<String,Object>nsEntry = (HashMap<String,Object>)getXsdMap().get(namespace);
            String versionable = (String) (nsEntry.containsKey("versionable") ? nsEntry.get("versionable") : "false");
            HashMap<String,String>versions = (HashMap<String,String>) nsEntry.get("versions");
            
            if(versionable.equals("false")){
                if(versions.containsKey("default")){
                    schemaPath = versions.get("default");
                }
                
            }else if(nsEntry.containsKey("versionKey")){
                String versionKey = (String) nsEntry.get("versionKey");
                String version = doc.getDocumentElement().getAttribute(versionKey);
                if(versions.containsKey(version)){
                    schemaPath = versions.get(version);
                }else if(versions.containsKey("default")){
                    schemaPath = versions.get("default");
                }
            }
        }
        return schemaPath;
    }
    public static String getXsltPath(Element element,String transformToNS){
        String namespace = element.getNamespaceURI();
        String xsltPath = null;
        if(
            getCrosswalk().containsKey(namespace) &&
            getCrosswalk().get(namespace).containsKey(transformToNS)
        ){                        
            
            HashMap<String,Object>nsEntry = (HashMap<String,Object>)getCrosswalk().get(namespace).get(transformToNS);
            String versionable = (String) (nsEntry.containsKey("versionable") ? nsEntry.get("versionable") : "false");
            HashMap<String,String>versions = (HashMap<String,String>) nsEntry.get("versions");
            
            if(versionable.equals("false")){
                if(versions.containsKey("default")){
                    xsltPath = versions.get("default");
                }
                
            }else if(nsEntry.containsKey("versionKey")){
                String versionKey = (String) nsEntry.get("versionKey");
                String version = element.getAttribute(versionKey);
                if(versions.containsKey(version)){
                    xsltPath = versions.get(version);
                }else if(versions.containsKey("default")){
                    xsltPath = versions.get("default");
                }
            }
        }
        return xsltPath;
    }
    public static MdSec.MdWrap createMdWrap(File file) throws ParserConfigurationException, SAXException, IOException, IllegalNamespaceException, NoNamespaceException, MalformedURLException, TransformerException, Exception{           
        //Valideer xml, en geef W3C-document terug
        MdSec.MdWrap mdWrap = createMdWrap(XML.XMLToDocument(file));
        mdWrap.setLabel(file.getName());
        return mdWrap;
    }        
    public static void findDCMdSec(Mets mets,ArrayList<MdSec>dcMdSecs,ArrayList<MdSec>dcCandidateMdSecs){
        for(int i = 0;i< mets.getDmdSec().size();i++){            
            MdSec mdSec = mets.getDmdSec().get(i);            
            MdSec.MdWrap mdWrap = mdSec.getMdWrap();
            if(mdWrap == null){
                continue;
            }            
            Element element = mdWrap.getXmlData().get(0);           
            
            if(element == null || element.getOwnerDocument() == null){
                continue;
            }            
            
            String ns = element.getNamespaceURI();                        
            Document doc = element.getOwnerDocument();            
            ns = (ns != null) ? ns : doc.getDocumentElement().getNamespaceURI();            
            if(ns == null){
                continue;
            }                        
            //dc gevonden
            if(ns.compareTo(NAMESPACE_DC) == 0 || ns.compareTo(NAMESPACE_OAI_DC) == 0){                            
                dcMdSecs.add(mdSec);                            
            }            
            //crosswalk naar dc gevonden
            else if(
                getCrosswalk().containsKey(ns) &&                     
                (getCrosswalk().get(ns).containsKey(NAMESPACE_DC) || getCrosswalk().get(ns).containsKey(NAMESPACE_OAI_DC))
            ){
                dcCandidateMdSecs.add(mdSec);                                  
            } 
        }
    }
    public static void findDC(Mets mets,ArrayList<Element>dcElements,ArrayList<Element>dcCandidates){        
        ArrayList<MdSec>dcMdSecs = new ArrayList<MdSec>();
        ArrayList<MdSec>dcCandidateMdSecs = new ArrayList<MdSec>();        
        findDCMdSec(mets,dcMdSecs,dcCandidateMdSecs);        
        for(MdSec dcMdSec:dcMdSecs){
            dcElements.add(dcMdSec.getMdWrap().getXmlData().get(0));
        }        
        for(MdSec dcCandidateMdSec:dcCandidateMdSecs){
            dcCandidates.add(dcCandidateMdSec.getMdWrap().getXmlData().get(0));
        }
    }
    
    //genereert Dublin Core Document indien geen dergelijke records aanwezig zijn én er een crosswalk bestaat
    public static Document generateDCDoc(Mets mets) throws Exception{
        ArrayList<Element>dcElements = new ArrayList<Element>();
        ArrayList<Element>dcCandidates = new ArrayList<Element>();
        findDC(mets,dcElements,dcCandidates);        
        return generateDCDoc(dcElements,dcCandidates);        
    }
    public static Document generateDCDoc(ArrayList<Element>dcElements,ArrayList<Element>dcCandidates) throws Exception{
        Document dcDoc = null;
        
        if(dcElements.size() <= 0 && dcCandidates.size() > 0){
            Element sourceElement = dcCandidates.get(0);
            String xsltPath = MetsUtils.getXsltPath(sourceElement,MetsUtils.NAMESPACE_DC);
            xsltPath = xsltPath != null ? xsltPath : MetsUtils.getXsltPath(sourceElement,MetsUtils.NAMESPACE_OAI_DC);                
            if(xsltPath == null){
                throw new Exception("no crosswalk found");
            }                
            URL xsltURL = Context.getResource(xsltPath);                
            Document xsltDoc = XML.XMLToDocument(xsltURL);                
            dcDoc = XSLT.transform(sourceElement,xsltDoc);
        }
        return dcDoc;
    }
    public static byte [] DCToBagInfo(Document doc) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException{
        ByteArrayOutputStream baginfoOut = new ByteArrayOutputStream();                
        URL xsltURL = Context.getResource(
            getBaginfoMap().get(
                doc.getDocumentElement().getNamespaceURI()
            )
        );                
        Document xsltDoc = XML.XMLToDocument(xsltURL);                
        XSLT.transform(doc,xsltDoc,baginfoOut);                
        return baginfoOut.toByteArray();
    } 

    public static ArrayList<VelocityTemplate> getBaginfoTemplates() {
        if(baginfoTemplates == null){
            baginfoTemplates = new ArrayList<VelocityTemplate>();            
            HashMap<String,HashMap<String,String>>config = (HashMap<String,HashMap<String,String>>) Beans.getBean("baginfoTemplates");            
            for(Map.Entry<String,HashMap<String,String>> entry:config.entrySet()){
                HashMap<String,String> value = entry.getValue();
                baginfoTemplates.add(new VelocityTemplate(value.get("name"),value.get("path"),value.get("xsd")));
            }
        }
        return baginfoTemplates;
    }    
    public static Document fixNamespace(Document doc)throws NoNamespaceException, TransformerConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException, SAXException, IOException, DtdNoFixFoundException, DocumentCreationFailedException, TransformerException{        
        String name = doc.getDocumentElement().getLocalName();
        log.debug("root element: "+name);
        //baseer je op naam root element
        if(!getDtdTransformations().containsKey(name)){
            log.debug("no dtd found! for "+name);
            throw new DtdNoFixFoundException(name);
        }        
        log.debug("dtd found!");
        URL xsltResource = Context.getResource(getDtdTransformations().get(name));
        log.debug("xsltResource: "+xsltResource);
        Document xsltDoc = XML.XMLToDocument(xsltResource);
        if(xsltDoc == null){
            throw new DocumentCreationFailedException(); 
        }        
        return XSLT.transform(doc,xsltDoc);        
    }

    public static HashMap<String, String> getDtdTransformations() {
        if(dtdTransformations == null){
            dtdTransformations = (HashMap<String, String>) Beans.getBean("dtdTransformations");
        }
        return dtdTransformations;
    }    
    
}
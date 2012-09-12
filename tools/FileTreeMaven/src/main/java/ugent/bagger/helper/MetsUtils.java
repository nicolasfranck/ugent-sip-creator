/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;


import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.MdRefException;
import ugent.bagger.exceptions.NoNamespaceException;
import com.anearalone.mets.AmdSec;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsReader;
import com.anearalone.mets.MetsWriter;
import com.anearalone.mets.StructMap;
import com.anearalone.mets.StructMap.Div;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.validation.Schema;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class MetsUtils {
    
    private static HashMap<String,String> xsdMap = null;
    private static HashMap<String,String> xsltMap = null;
    private static HashMap<String,String> namespaceMap = null;
    private static ArrayList<String>forbiddenNamespaces = null;
    
    private static Log logger = LogFactory.getLog(MetsUtils.class);

    public static HashMap<String, String> getXsdMap() {
        if(xsdMap == null){
            xsdMap = (HashMap<String,String>) Beans.getBean("xsdMap");
        }        
        return xsdMap;
    }
    public static void setXsdMap(HashMap<String, String> xsdMap) {
        MetsUtils.xsdMap = xsdMap;
    }
    public static HashMap<String, String> getXsltMap() {
        if(xsltMap == null){
            xsltMap = (HashMap<String,String>) Beans.getBean("xsltMap");
        }
        return xsltMap;
    }
    public static void setXsltMap(HashMap<String, String> xsltMap) {
        MetsUtils.xsltMap = xsltMap;
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
    public static void writeMets(Mets mets,File file) throws FileNotFoundException, TransformerException, DatatypeConfigurationException, ParserConfigurationException{
        writeMets(mets,new FileOutputStream(file));
    }
    public static void writeMets(Mets mets,OutputStream out) throws ParserConfigurationException, DatatypeConfigurationException, TransformerException{
        new MetsWriter().writeToOutputStream(mets,out);
    }
    public static Mets documentToMets(Document doc){
        Mets mets = new Mets();        
        mets.unmarshal(doc.getDocumentElement());
        return mets;
    }
    public static StructMap toStructMap(Node node){
        StructMap struct = new StructMap();        
        Div div = toDiv(node);
        struct.setDiv(div);                
        return struct;
    }
    public static Div toDiv(Node node){
        Div div = new Div();
        div.setLabel(node.getObject().toString());
        for(Node n:node.getChildren()){
            div.getDiv().add(toDiv(n));
        }
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
    public static void main(String [] args){
        try{
            Mets mets = new Mets();
            ArrayList<Path>paths = Node.structureToList(new File("/tmp/a.txt"));
            Node node = new Node(".");
            for(Path path:paths){
                node.addPath(path);
                logger.debug("adding path "+path);                
            }
            for(Node n:node.getChildren()){
                mets.getStructMap().add(toStructMap(n));
            }
            MetsWriter mw = new MetsWriter();
            mw.writeToOutputStream(mets,new FileOutputStream(new File("/tmp/output.txt")));
        }catch(Exception e){
            logger.debug(e.getMessage());            
        }
    }
    
    public static MdSec createMdSec(File file) throws IOException, SAXException, ParserConfigurationException, IllegalNamespaceException, NoNamespaceException{        
        MdSec mdSec = new MdSec(file.getName());                
        mdSec.setMdWrap(createMdWrap(file));                
        mdSec.setGROUPID(mdSec.getMdWrap().getMDTYPE().toString()); 
        return mdSec;
    }
    public static MdSec createMdSec(Document doc) throws NoNamespaceException, IllegalNamespaceException, MalformedURLException, SAXException, IOException{
        MdSec mdSec = new MdSec(UUID.randomUUID().toString());        
        mdSec.setGROUPID(UUID.randomUUID().toString()); 
        mdSec.setMdWrap(createMdWrap(doc));
        return mdSec;
    }
    public static MdSec.MdWrap createMdWrap(Document doc) throws NoNamespaceException, IllegalNamespaceException, MalformedURLException, SAXException, IOException{
        String namespace = doc.getDocumentElement().getNamespaceURI();      
        //elke xml moet namespace bevatten (geen oude DOCTYPE!)
        if(namespace == null || namespace.isEmpty()){
            throw new NoNamespaceException("no namespace could be found");
        } 
        //sommige xml mag niet in mdWrap: vermijd METS binnen METS!
        if(getForbiddenNamespaces().contains(namespace)){
            throw new IllegalNamespaceException("namespace "+namespace+" is forbidden in mdWrap",namespace);
        }
        //indien XSD bekend, dan validatie hierop       
        if(getXsdMap().containsKey(namespace)){
            logger.debug("validating against "+(String)getXsdMap().get(namespace));            
            URL schemaURL = Context.getResource((String)getXsdMap().get(namespace));                    
            logger.debug("creating schema");            
            logger.debug("url of schema: "+schemaURL);
            Schema schema = ugent.bagger.helper.XML.createSchema(schemaURL);            
            logger.debug("creating schema done!");
            ugent.bagger.helper.XML.validate(doc,schema);            
        } 
        logger.debug("validation successfull");
        MdSec.MDTYPE mdType = null;
        try{                     
            mdType = MdSec.MDTYPE.fromValue(getNamespaceMap().get(namespace));                              
        }catch(IllegalArgumentException e){
            mdType = MdSec.MDTYPE.OTHER;                        
        }
        MdSec.MdWrap mdWrap = new MdSec.MdWrap(mdType);                                                            
        if(mdType == MdSec.MDTYPE.OTHER){
            mdWrap.setOTHERMDTYPE(namespace);
        } 
        mdWrap.setMIMETYPE("text/xml");
        mdWrap.getXmlData().add(doc.getDocumentElement());                
        return mdWrap;
    }
    public static MdSec.MdWrap createMdWrap(File file) throws ParserConfigurationException, SAXException, IOException, IllegalNamespaceException, NoNamespaceException{           
        //Valideer xml, en geef W3C-document terug
        return createMdWrap(ugent.bagger.helper.XML.XMLToDocument(file));
    }
}
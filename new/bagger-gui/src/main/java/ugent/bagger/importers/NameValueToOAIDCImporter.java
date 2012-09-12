/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.importers;

import gov.loc.repository.bagit.utilities.namevalue.NameValueReader.NameValue;
import gov.loc.repository.bagit.utilities.namevalue.impl.NameValueReaderImpl;
import java.io.*;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ugent.bagger.helper.XML;

/**
 *
 * @author nicolas
 */
public class NameValueToOAIDCImporter implements Importer{
    private static Log logger = LogFactory.getLog(NameValueToDCImporter.class);
    private static final String [] DCKeys = {
     "title","creator","subject","description","publisher","contributor","date","type","format","identifier","source","language","relation","coverage","rights"
    };
    public static final String namespaceDC = "http://purl.org/dc/elements/1.1/";
    public static final String namespaceOAI_DC = "http://www.openarchives.org/OAI/2.0/oai_dc/";
    private static boolean hasKey(String lookupKey){
        for(String key:DCKeys){           
            if(key.compareTo(lookupKey) == 0){
                return true;
            }
        }        
        return false;
    }
    
    @Override
    public Document performImport(File file) {
        Document doc = null;      
        try {                       
            doc = performImport(new FileInputStream(file));
        }catch (FileNotFoundException ex) {
            Logger.getLogger(NameValueToOAIDCImporter.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return doc;
    }
    @Override
    public Document performImport(URL url) {
        Document doc = null;
        try {
            doc = performImport(url.openStream());
        } catch (IOException ex) {
            logger.error(ex);   
        }
        return doc;
    }

    @Override
    public Document performImport(InputStream is) {
        Document doc = null;              
        try {           
            DOMImplementation domImpl = XML.getDocumentBuilder().getDOMImplementation();            
            doc = domImpl.createDocument(namespaceOAI_DC,"oai_dc:dc", null);                               
            Element root = doc.getDocumentElement(); 
            
            NameValueReaderImpl reader = new NameValueReaderImpl("UTF-8",is,"bagInfoTxt");
            while(reader.hasNext()){
                NameValue pair = reader.next();   
                String lowerKey = pair.getKey().toLowerCase();
                if(!(lowerKey.startsWith("dc-"))){
                    continue;
                }
                String key = lowerKey.substring(3);                                       
                if(!hasKey(key)){
                    continue;
                }                 
                Element el = doc.createElementNS(namespaceDC,key);                
                el.appendChild(doc.createTextNode(pair.getValue()));
                root.appendChild(el);                
            }
            
        }catch (ParserConfigurationException ex) {
            logger.error(ex);
        }        
        return doc;
    }
    public static void main(String [] args){
        try{
            Document doc = new NameValueToOAIDCImporter().performImport(new File("/tmp/bag-info.txt"));
            XML.DocumentToXML(doc,System.out,true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

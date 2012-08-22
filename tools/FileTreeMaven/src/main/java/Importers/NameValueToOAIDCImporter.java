/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Importers;

import gov.loc.repository.bagit.utilities.namevalue.NameValueReader.NameValue;
import gov.loc.repository.bagit.utilities.namevalue.impl.NameValueReaderImpl;
import helper.XML;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author nicolas
 */
public class NameValueToOAIDCImporter implements Importer{
    private static final String [] DCKeys = {
     "title","creator","subject","description","publisher","contributor","date","type","format","identifier","source","language","relation","coverage","rights"
    };
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
            DOMImplementation domImpl = XML.getDocumentBuilder().getDOMImplementation();            
            doc = domImpl.createDocument("http://www.openarchives.org/OAI/2.0/oai_dc/","oai_dc:dc", null);                               
            Element root = doc.getDocumentElement();                                 
            root.setAttribute("xmlns:dc","http://purl.org/dc/elements/1.1/");                                    
            NameValueReaderImpl reader = new NameValueReaderImpl("UTF-8",new FileInputStream(file),"bagInfoTxt");
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
                Element el = doc.createElement("dc:"+key);            
                el.appendChild(doc.createTextNode(pair.getValue()));
                root.appendChild(el);                
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NameValueToOAIDCImporter.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ParserConfigurationException ex) {
            Logger.getLogger(NameValueToOAIDCImporter.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return doc;
    }
    public static void main(String [] args){
        try{
            XML.DocumentToXML(new NameValueToOAIDCImporter().performImport(new File("/tmp/bag-info.txt")),System.out,true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

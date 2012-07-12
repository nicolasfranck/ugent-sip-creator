/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import helper.XML;
import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author nicolas
 */
public class XMLInfo {
    public static void main(String []args){
        try{
            Document doc = XML.XMLToDocument(new File("/home/nicolas/mets.xml"));            
            System.out.println(doc.getDocumentElement().getNamespaceURI());
            System.out.println(doc.getDocumentElement().getLocalName());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

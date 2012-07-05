/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import helper.XML;
import helper.XSLT;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;

/**
 *
 * @author nicolas
 */
public class XSLTExample3 {
    public static void main(String []args) throws Exception{
        if(args.length < 2)return;

        // link.txt is a symbolic link!
        File file = new File("/home/nicolas/link.txt");
        // absolute path: /home/nicolas/link.txt
        System.out.println("absolute path:"+file.getAbsolutePath());
        // canonical path: /home/nicolas/dwhelper
        System.out.println("canonical path:"+file.getCanonicalPath());
        
        DocumentBuilder db = XML.getDocumentBuilder();
        Document sourceD = XML.XMLToDocument(new File(args[0]));
        Document xsltD = XML.XMLToDocument(new File(args[1]));
        Document outD = XML.createDocument();

        try{
            XSLT.transform(sourceD, xsltD, outD);
            XML.DocumentToXML(outD,System.out,true);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import helper.Context;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class Resource {
    public static void main(String [] args) throws SAXException, MalformedURLException, IOException{
        URL url = Resource.class.getClassLoader().getResource("file:.");
        System.out.println("url: "+url);        
        InputStream in = Context.getResourceAsStream("metadata/xsd/ead.xsd");
        System.out.println("in: "+in);        
        byte [] buffer = new byte[1024];
        int bytesRead = 0;
        while((bytesRead = in.read(buffer)) >  0){
            System.out.write(buffer,0,bytesRead);
        }
    }
}

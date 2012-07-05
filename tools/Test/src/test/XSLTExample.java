/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;


import java.io.File;
import java.net.MalformedURLException;
import org.xml.sax.SAXException;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author nicolas
 */
public class XSLTExample {
    public static void main(String [] args)throws MalformedURLException,SAXException, TransformerConfigurationException, TransformerException{
        if(args.length < 2)return;

        String inputFile = args[0];
        String xsltFile = args[1];       

        Source xmlSource = new StreamSource(new File(inputFile));
        Source xsltSource = new StreamSource(new File(xsltFile));
        Result result = new StreamResult(System.out);

        TransformerFactory transFact = TransformerFactory.newInstance();
        Transformer trans = transFact.newTransformer(xsltSource);
        trans.transform(xmlSource,result);

    }
}

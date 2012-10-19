package ugent.bagger.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author nicolas
 */
public class XSLT {
    private static TransformerFactory tf;

    public static TransformerFactory getTransformerFactory() {
        if(tf == null){
            tf = TransformerFactory.newInstance();
        }
        return tf;
    }
    public static void transform(Element sourceElement,Document xsltDoc,OutputStream out) throws TransformerConfigurationException, TransformerException{
        transform(new DOMSource(sourceElement),new DOMSource(xsltDoc),new StreamResult(out));
    }
    
    public static void transform(URL sourceURL,URL xsltURL,OutputStream out) throws IOException, TransformerConfigurationException, TransformerException{
        transform(sourceURL.openStream(),xsltURL.openStream(),out);       
    }    
    public static void transform(Document sourceD,Document xsltD,OutputStream out) throws TransformerConfigurationException, TransformerException{        
        transform(new DOMSource(sourceD),new DOMSource(xsltD),new StreamResult(out));        
    }
    /*
     * warning: do not reuse your inputstream or readers afterwards, for the parsers close them
     * this will give a 'Bad File Descriptor'
     */
    public static void transform(InputStream sourceIS,InputStream xsltIS,OutputStream out) throws IOException, TransformerConfigurationException, TransformerException{                 
        transform(new StreamSource(sourceIS),new StreamSource(xsltIS),new StreamResult(out));        
    }
    public static void transform(Document sourceD,InputStream xsltIS,Document outD) throws TransformerConfigurationException, TransformerException{                       
        transform(new DOMSource(sourceD),new StreamSource(xsltIS),new DOMResult(outD));           
    }
    public static void transform(Document sourceD,Document xsltD,Document outD) throws TransformerConfigurationException, TransformerException{        
        transform(new DOMSource(sourceD),new DOMSource(xsltD),new DOMResult(outD));        
    }
    public static Document transform(Document sourceD,Document xsltD) throws ParserConfigurationException, TransformerConfigurationException, TransformerException{
        Document outDoc = XML.createDocument();
        transform(sourceD,xsltD,outDoc);
        return outDoc;
    }    
    public static Document transform(Element sourceE,Document xsltD) throws ParserConfigurationException, TransformerConfigurationException, TransformerException{
        Document outDoc = XML.createDocument();
        transform(new DOMSource(sourceE),new DOMSource(xsltD),new DOMResult(outDoc));
        return outDoc;
    }
   
    public static void transform(Source source,Source xslt,Result result) throws TransformerConfigurationException, TransformerException{
        Transformer trans = getTransformerFactory().newTransformer(xslt);
        trans.transform(source,result);
    }
    public static void main(String [] args){
        /*
        try{             
            Document inputDoc = XML.XMLToDocument(new File("/tmp/input.xml"));
            Document xsltDoc = XML.XMLToDocument(new File("/tmp/transform.xslt"));
            Document outDoc = transform(inputDoc, xsltDoc);
            
            XML.DocumentToXML(outDoc,System.out, true);
            Schema schema = XML.createSchema(new URL("http://www.openarchives.org/OAI/2.0/oai_dc.xsd"));
            System.out.println("schema created");
            XML.validate(
                outDoc,
                schema
            );
            System.out.println("validation successfull");
        }catch(Exception e){
            e.printStackTrace();
        }*/
        try{
            URL input = new URL("file:///home/nicolas/sandburgdc.xml");
            URL xslt = new URL("file:///home/nicolas/dc2baginfo.xsl");
            FileOutputStream out = new FileOutputStream(new File("/home/nicolas/bag-info.txt"));
            transform(input,xslt,out);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

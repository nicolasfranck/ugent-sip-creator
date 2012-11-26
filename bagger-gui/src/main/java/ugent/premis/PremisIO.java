package ugent.premis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ugent.bagger.helper.XML;

/**
 *
 * @author nicolas
 */
public class PremisIO {
    public static Premis read(File file) throws ParserConfigurationException, SAXException, IOException, ParseException{
        return read(new FileInputStream(file));
    }
    public static Premis read(InputStream in) throws ParserConfigurationException, SAXException, IOException, ParseException{
        Document doc = XML.XMLToDocument(in);
        Premis premis = new Premis();
        premis.unmarshal(doc.getDocumentElement());
        return premis;
    }
    public static void write(Premis premis,OutputStream out,boolean pretty) throws ParserConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException{        
        XML.DocumentToXML(toDocument(premis),out,pretty);
    }    
    public static Document toDocument(Premis premis) throws ParserConfigurationException{
        Document doc = XML.createDocument();
        Element e = doc.createElementNS(NS.PREMIS.ns(),"premis:premis");        
        premis.marshal(e,doc);       
        doc.appendChild(e);
        return doc;
    }    
    public static Premis toPremis(Element element) throws ParseException{
        Premis premis = new Premis();
        premis.unmarshal(element);
        return premis;
    }
}

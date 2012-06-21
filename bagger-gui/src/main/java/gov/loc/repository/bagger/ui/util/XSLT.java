package gov.loc.repository.bagger.ui.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author nicolas
 */
public class XSLT {
    private TransformerFactory tf;

    public TransformerFactory getTransformerFactory() {
        if(tf == null){
            tf = TransformerFactory.newInstance();
        }
        return tf;
    }
    public void transform(URL sourceURL,URL xsltURL,OutputStream out) throws IOException, TransformerConfigurationException, TransformerException{
        transform(
                sourceURL.openStream(),
                xsltURL.openStream(),
                out
        );       
    }
    public void transform(InputStream sourceIS,InputStream xsltIS,OutputStream out) throws IOException, TransformerConfigurationException, TransformerException{
        Source xmlSource = new StreamSource(sourceIS);
        Source xsltSource = new StreamSource(xsltIS);
        Result result = new StreamResult(out);        
        Transformer trans = getTransformerFactory().newTransformer(xsltSource);
        trans.transform(xmlSource,result);
    }

}

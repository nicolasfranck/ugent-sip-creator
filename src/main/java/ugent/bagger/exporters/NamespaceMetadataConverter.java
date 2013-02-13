package ugent.bagger.exporters;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.XML;
import ugent.bagger.helper.XSLT;

/**
 *
 * @author nicolas
 */
public class NamespaceMetadataConverter implements MetadataConverter{
    protected Map<String,String>config;    
    @Override
    public Document convert(Document doc) throws Exception{
        Document outDoc = null;
        String namespace = doc.getDocumentElement().getNamespaceURI();
       
        if(getConfig().containsKey(namespace)){            
            URL xsltURL = Context.getResource(getConfig().get(namespace));       
            Document xsltDoc = XML.XMLToDocument(xsltURL);
            outDoc = XSLT.transform(doc,xsltDoc);            
        }        
        return outDoc;
    }
    public Map<String,String> getConfig() {
        if(config == null){
            config = new HashMap<String, String>();
        }
        return config;
    }
    public void setConfig(Map config){
        this.config = config;
    }
}

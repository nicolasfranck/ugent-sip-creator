package ugent.premis;

import java.text.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author nicolas
 */
public class PremisRights implements ElementInterface {    
    private String xmlID;
    
    public String getXmlID() {
        return xmlID;
    }
    public void setXmlID(String xmlID) {
        this.xmlID = xmlID;
    }

    @Override
    public void unmarshal(Element e) throws ParseException {
        
    }

    @Override
    public void marshal(Element e, Document d) {
        
    }
}

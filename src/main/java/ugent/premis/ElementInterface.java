package ugent.premis;

import java.text.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public interface ElementInterface {    
    public void unmarshal(Element e) throws ParseException;
    public void marshal(Element e, Document d);
}

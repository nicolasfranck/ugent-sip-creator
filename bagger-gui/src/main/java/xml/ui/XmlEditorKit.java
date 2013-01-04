package xml.ui;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

public class XmlEditorKit extends StyledEditorKit {
 
    static final long serialVersionUID = 2969169649596107757L;
    ViewFactory xmlViewFactory;
 
    public XmlEditorKit() {        
    }
     
    @Override
    public ViewFactory getViewFactory() {
        if(xmlViewFactory == null){
            xmlViewFactory = new XmlViewFactory();
        }
        return xmlViewFactory;
    }
 
    @Override
    public String getContentType() {
        return "text/xml";
    }
}
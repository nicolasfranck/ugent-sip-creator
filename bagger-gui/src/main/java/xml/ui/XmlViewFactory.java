package xml.ui;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class XmlViewFactory extends Object implements ViewFactory {
    @Override
    public View create(Element element) {
        return new XmlView(element);
    } 
}
/*
 * see: 
 */
package xml.ui;

import javax.swing.JEditorPane;

public class XmlTextPane extends JEditorPane { 
    private static final long serialVersionUID = 6270183148379328084L; 
    public XmlTextPane() {         
        // Set editor kit
        setEditorKitForContentType("text/xml",new XmlEditorKit());
        setContentType("text/xml; charset=utf-8");
        
    }     
}
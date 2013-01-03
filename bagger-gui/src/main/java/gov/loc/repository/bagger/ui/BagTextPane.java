package gov.loc.repository.bagger.ui;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class BagTextPane extends JTextPane {
    private static final long serialVersionUID = -505900021814525136L;
    private static final Log log = LogFactory.getLog(BagTextPane.class);
    private StyledDocument document;
    private String message = "";
    private Color textBackground = new Color(240, 240, 240);

    public BagTextPane(String message) {
    	super();        
        setMessage(message);
        setAutoscrolls(true);
        setEditable(false);
        setBackground(textBackground);
    }
    
    public void setMessage(String message) {
    	this.message = message;
    	buildDocument();
    	setStyledDocument(document);
    }
    
    public String getMessage() {
    	return message;
    }
    
    private void buildDocument() {
        StyleContext context = new StyleContext();
        document = new DefaultStyledDocument(context);

        Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
        StyleConstants.setFontSize(style, 14);
        StyleConstants.setSpaceAbove(style, 4);
        StyleConstants.setSpaceBelow(style, 4);
        // Insert content
        try {
            document.insertString(document.getLength(),message, style);
        } catch (BadLocationException badLocationException) {
            log.error(badLocationException.getMessage());
        }

        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setBold(attributes, true);
        StyleConstants.setItalic(attributes, true);

        // Third style for icon/component
        Style labelStyle = context.getStyle(StyleContext.DEFAULT_STYLE);

        Icon icon = new ImageIcon("Computer.gif");
        JLabel label = new JLabel(icon);
        StyleConstants.setComponent(labelStyle, label);
    }   
}
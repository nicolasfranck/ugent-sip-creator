package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Date;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/*
 * Nicolas Franck
 * 
 * code based on gov.loc.repository.bagger.ui.ConsolePane
 */

public final class ConsolePane2 extends JPanel {
    private static final int MAX_CONSOLE_MESSAGE_LENGTH = 50000;
    private static final long serialVersionUID = -4290352509246639528L;    
    public static final String CONSOLE_PANE = "consolePane";    
    private Color textBackground = new Color(240, 240, 240);   
    private JTextArea serializedArea;    

    public ConsolePane2(String messages) {       
        setLayout(new BorderLayout());  
        add(createConsoleArea());
        addConsoleMessages(messages);
    }   
    
    public JTextArea getSerializedArea() {
        if(serializedArea == null){
            serializedArea = new JTextArea();
            serializedArea.setToolTipText(ApplicationContextUtil.getMessage("consolepane.msg.help"));
            serializedArea.setEditable(false);
            serializedArea.setLineWrap(true);
            serializedArea.setBackground(textBackground);
            serializedArea.setWrapStyleWord(true);
            serializedArea.setAutoscrolls(true);
            serializedArea.setBorder(BorderFactory.createLineBorder(Color.black));
        }
        return serializedArea;
    }
    public void setSerializedArea(JTextArea serializedArea) {
        this.serializedArea = serializedArea;
    }
    private JScrollPane createConsoleArea() {        
        return new JScrollPane(getSerializedArea());        
    }        
    public void addConsoleMessages(String message) {
    	if (message != null && message.trim().length() != 0) {
            Document consoleMessageDoc = getSerializedArea().getDocument();
            String date = new Date().toString();
            getSerializedArea().append("\n[" + date + "]: " + message);

            if(consoleMessageDoc.getLength() > MAX_CONSOLE_MESSAGE_LENGTH){
                try {
                    consoleMessageDoc.remove(0, consoleMessageDoc.getLength() - MAX_CONSOLE_MESSAGE_LENGTH);
                }catch(BadLocationException e){
                    throw new RuntimeException(e);
                }
            }
            getSerializedArea().setAutoscrolls(true);
            getSerializedArea().setCaretPosition(consoleMessageDoc.getLength());
    	}
    }     
    public void clearConsoleMessages() {
    	getSerializedArea().setText("");
    }
}
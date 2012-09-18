package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.model.BagStatus;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagger.ui.util.LayoutUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ConsolePane extends JPanel {
    private static final int MAX_CONSOLE_MESSAGE_LENGTH = 50000;
    private static final long serialVersionUID = -4290352509246639528L;
    private static final Log log = LogFactory.getLog(ConsolePane.class);
    public static final String CONSOLE_PANE = "consolePane";
    private Dimension maxDimension = new Dimension(400, 300);
    private Dimension preferredDimension = new Dimension(400, 150);
    private Color textBackground = new Color(240, 240, 240);
    private Border emptyBorder = new EmptyBorder(10, 10, 10, 10);
    private JTextArea serializedArea;

    public ConsolePane(String messages) {
        super();
        setLayout(new GridBagLayout());
        createFormControl();
        addConsoleMessages(messages);
    }
    public JComponent createFormControl() {
    	setMaximumSize(maxDimension);    	
    	GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
    	add(createStatusPannel(), gbc);    	
    	gbc = LayoutUtil.buildGridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        add(createConsoleArea(), gbc);
        setBorder(emptyBorder);
        setPreferredSize(preferredDimension);
    	return this;
    }
    
    private JPanel createStatusPannel() {
    	JPanel statusPanel = new JPanel();
    	statusPanel.setLayout(new GridBagLayout());
        int row = 0;
    	int col = 0;
    	BagStatus bagStatus = BagStatus.getInstance();
    	
    	// Complete Status
    	JLabel completeLabel = new JLabel(ApplicationContextUtil.getMessage("compositePane.message.isComplete") + " ");
        completeLabel.setToolTipText(ApplicationContextUtil.getMessage("consolepane.iscomplete.help"));
        GridBagConstraints gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        statusPanel.add(completeLabel, gbc);
        
        JLabel completeStatus = new StatusImageLabel(bagStatus.getCompletenessStatus());
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        statusPanel.add(completeStatus, gbc);

        // Validation Status
        JLabel validationLabel = new JLabel(ApplicationContextUtil.getMessage("compositePane.message.isValid")  + " ");
        validationLabel.setToolTipText(ApplicationContextUtil.getMessage("consolepane.isvalid.help"));
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        statusPanel.add(validationLabel, gbc);
        
        JLabel validationStatus = new StatusImageLabel(bagStatus.getValidationStatus());
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        statusPanel.add(validationStatus, gbc);

        // Profile Compliance Status
        JLabel profileComplianceLabel = new JLabel(ApplicationContextUtil.getMessage("compositePane.message.isMetadata")  + " ");
        profileComplianceLabel.setToolTipText(ApplicationContextUtil.getMessage("consolepane.ismetadata.help"));
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        statusPanel.add(profileComplianceLabel, gbc);
        
        JLabel profileComplianceStatus = new StatusImageLabel(bagStatus.getProfileComplianceStatus());
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        statusPanel.add(profileComplianceStatus, gbc);

        return statusPanel;
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
    @Override
    public boolean requestFocusInWindow(){ 
        return super.requestFocusInWindow();
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
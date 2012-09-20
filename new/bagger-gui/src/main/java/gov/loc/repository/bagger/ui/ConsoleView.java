package gov.loc.repository.bagger.ui;

import javax.swing.JComponent;
import org.springframework.richclient.application.support.AbstractView;

public class ConsoleView extends AbstractView {
    public static ConsoleView instance;
    private ConsolePane consolePane;

    public ConsoleView() {
        instance = this;
    }
    @Override
    protected JComponent createControl() {        
        return getConsolePane();
    }
    public ConsolePane getConsolePane() {
        if(consolePane == null){
            consolePane =  new ConsolePane(getInitialConsoleMsg());
        }
        return consolePane;
    }
    public void setConsolePane(ConsolePane consolePane) {
        this.consolePane = consolePane;
    }   
    public static ConsoleView getInstance() {
        return instance;
    }	
    public void addConsoleMessages(String messages) {
        consolePane.addConsoleMessages(messages);
    }	
    public void clearConsoleMessages() {
        consolePane.clearConsoleMessages();
    }	
    private String getInitialConsoleMsg() {
    	StringBuilder buffer = new StringBuilder();
    	buffer.append(getMessage("consolepane.msg.help"));
    	buffer.append("\n\n");
    	buffer.append(getMessage("consolepane.status.help"));
    	buffer.append("\n\n");
    	return buffer.toString();
    }
}
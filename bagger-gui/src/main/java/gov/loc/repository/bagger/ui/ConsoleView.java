package gov.loc.repository.bagger.ui;

import java.util.ArrayList;
import javax.swing.JComponent;
import org.springframework.util.Assert;
import ugent.bagger.views.DefaultView;

public class ConsoleView extends DefaultView {
    public static ConsoleView instance;    
    private ConsolePane consolePane;
    public static ArrayList<String>delayedMessages= new ArrayList<String>();

    public ConsoleView(){    
        Assert.isNull(instance);
        instance = this;
    }
    @Override
    protected JComponent createControl() {                
        return getConsolePane();
    }   
    
    @Override
    public void componentOpened(){
        super.componentOpened();
        if(delayedMessages.size() > 0){
            for(String message:delayedMessages){
                getConsolePane().addConsoleMessages(message);
            }
            delayedMessages.clear();
        }            
    }
    public ConsolePane getConsolePane() {
        if(consolePane == null){
            consolePane =  new ConsolePane(getInitialConsoleMsg());
        }
        return consolePane;
    }       
    public static ConsoleView getInstance() {
        return instance;
    }	
    public void addConsoleMessages(String messages) {
        getConsolePane().addConsoleMessages(messages);
    }	
    public void clearConsoleMessages() {
        getConsolePane().clearConsoleMessages();
    }	
    private String getInitialConsoleMsg() {
    	StringBuilder buffer = new StringBuilder();
    	buffer.append(getMessage("consolepane.msg.help")).append("\n");        
    	return buffer.toString();
    }
}
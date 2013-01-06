package gov.loc.repository.bagger.ui.util;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import java.awt.Image;
import java.util.Locale;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServices;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.image.ImageSource;


public class ApplicationContextUtil {	    
  
    public static String getMessage(String propertyName) {                        
        return Application.instance().getApplicationContext().getMessage(
            propertyName, null, propertyName, Locale.getDefault()
        );
    }
    public static Image getImage(String imageName) {
        ImageSource source = (ImageSource) getService(ImageSource.class);
        return source.getImage(imageName);
    }
    public static BagView getBagView() {
        return BagView.getInstance();
    }    
    public static DefaultBag getCurrentBag() {
        return getBagView().getBag();
    }
    /*
    public static ConsoleView getConsoleView(){        
        return ConsoleView.getInstance();        
    }*/
    private static ApplicationServices getApplicationServices() {
        return ApplicationServicesLocator.services();
    }  
    /*
    public static void addConsoleMessageByProperty(String messagePropertyName) {     
        if(getConsoleView() == null){            
            ConsoleView.delayedMessages.add(getMessage(messagePropertyName));            
        }else{                        
            getConsoleView().addConsoleMessages(getMessage(messagePropertyName));                        
        }        
    }
    public static void addConsoleMessage(String message) {        
        if(getConsoleView() == null){            
            ConsoleView.delayedMessages.add(message);            
        }else{                        
            getConsoleView().addConsoleMessages(message);                       
        }        
    }*/
    @SuppressWarnings("unchecked")
    private static Object getService(Class serviceType) {
        return getApplicationServices().getService(serviceType);
    }
}
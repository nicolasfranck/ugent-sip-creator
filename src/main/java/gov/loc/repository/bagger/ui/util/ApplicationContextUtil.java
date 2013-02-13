package gov.loc.repository.bagger.ui.util;

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
    static ApplicationServices getApplicationServices() {
        return ApplicationServicesLocator.services();
    }      
    @SuppressWarnings("unchecked")
    static Object getService(Class serviceType) {
        return getApplicationServices().getService(serviceType);
    }
}
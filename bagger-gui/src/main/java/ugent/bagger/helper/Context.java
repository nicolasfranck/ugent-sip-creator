package ugent.bagger.helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.Application;

public class Context{
    static final Log log = LogFactory.getLog(Context.class);

    public static String getMessage(String propertyName){  
        return getMessage(propertyName,null);        
    }
    public static String getMessage(String propertyName,Object [] objects){
        return getMessage(propertyName,objects,null);        
    }
    public static String getMessage(String propertyName,Object [] objects,String defaultMessage){
        return getMessage(propertyName,objects,defaultMessage,Locale.getDefault());        
    }
    public static String getMessage(String propertyName,Object [] objects,String defaultMessage,Locale locale){
        return Application.instance().getApplicationContext().getMessage(
            propertyName,objects,defaultMessage,locale
        );
    }
    public static URL getResource(String str){
        URL url;
        try{
            url = new URL(str);
        }catch(MalformedURLException e){
            url = Context.class.getClassLoader().getResource(str);
        }        
        return url;
    }
    public static InputStream getResourceAsStream(String str){
        InputStream in = null;        
        try{
            URL url = new URL(str);            
            in = url.openStream();
        }catch(MalformedURLException e){
            log.error(e.getMessage());
        }catch(IOException e){
            log.error(e.getMessage()); 
        }        
        if(in == null){
            in = Context.class.getClassLoader().getResourceAsStream(str);
        }        
        return in;
    }
}

package ugent.bagger.helper;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import org.springframework.richclient.application.Application;

public class Context{

    public static String getMessage(String propertyName){                
        return Application.instance().getApplicationContext().getMessage(
            propertyName, null,Locale.getDefault()
        );
    }
    public static String getMessage(String propertyName,Object [] objects){
        return Application.instance().getApplicationContext().getMessage(
            propertyName,objects,Locale.getDefault()
        );
    }
    public static String getMessage(String propertyName,Object [] objects,String defaultMessage){
        return Application.instance().getApplicationContext().getMessage(
            propertyName,objects,defaultMessage, Locale.getDefault()
        );
    }
    public static URL getResource(String str){
        URL url = null;
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
        }catch(MalformedURLException e){e.printStackTrace();}
         catch(IOException e){e.printStackTrace();}
        
        if(in == null){
            in = Context.class.getClassLoader().getResourceAsStream(str);
        }        
        return in;
    }
}

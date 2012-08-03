package helper;


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
		
	
}

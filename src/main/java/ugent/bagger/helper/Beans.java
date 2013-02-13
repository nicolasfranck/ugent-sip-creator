package ugent.bagger.helper;

import org.springframework.richclient.application.Application;

/**
 *
 * @author nicolas
 */
public class Beans {
    public static Object getBean(String id){        
        return Application.instance().getApplicationContext().getBean(id);        
    }
    public static boolean containsBean(String id){
        return Application.instance().getApplicationContext().containsBean(id);        
    }
}

package ugent.bagger.params;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 * 
 * This is meant for swing-components that use the toString method 
 * for labelling the component, while preserving the original object intact
 */
public class ContextObject {
    static final Log log = LogFactory.getLog(ContextObject.class);
    Object object;
    String key;
    public ContextObject(Object object,String key){
        this.object = object;
        this.key = key;
    }
    @Override
    public String toString(){
        String text = object.toString();
        try{
            text = Context.getMessage(key);
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return text;
    }
    public Object getObject() {
        return object;
    }
    public String getKey() {
        return key;
    }    
}

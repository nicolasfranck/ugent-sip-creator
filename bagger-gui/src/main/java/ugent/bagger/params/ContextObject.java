package ugent.bagger.params;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 * 
 * This is meant for swing-components that use the toString method 
 * for labelling the component, while preserving the original object intact
 */
public class ContextObject {
    private Object object;
    private String key;
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
            e.printStackTrace();
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

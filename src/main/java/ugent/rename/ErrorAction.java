package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum ErrorAction {    
    skip("skip"),   
    undoAll("undoAll"),
    abort("abort");    
    String s;
    ErrorAction(String s){
        this.s = s;
    }
    @Override
    public String toString(){
        String translated = null;
        try{
            translated = Context.getMessage("ErrorAction."+s);
        }catch(Exception e){}        
        return translated != null ? translated : s;        
    }
}

package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum ErrorAction {
    //same as 'skip' 
    //ignore("ignore"),
    skip("skip"),   
    undoAll("undoAll"),
    abort("abort");
    //dangerous: can lead to infinite loop
    //,retry("retry");
    private String s;
    private ErrorAction(String s){
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

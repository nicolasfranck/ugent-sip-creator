package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum StartPosRelative {
    BEGIN("begin"),END("end");
    String c;
    StartPosRelative(String c){
        this.c = c;
    }
    @Override
    public String toString(){        
        String translated = null;
        try{
            translated = Context.getMessage("StartPosRelative."+c);
        }catch(Exception e){}                
        return translated != null ? translated : c;        
    }
}
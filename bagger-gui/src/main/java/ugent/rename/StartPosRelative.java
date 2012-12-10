package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum StartPosRelative {
    BEGIN("begin"),END("end"),BEFORE_EXTENSION("before_extension");
    private String c;
    private StartPosRelative(String c){
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
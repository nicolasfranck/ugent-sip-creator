package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum StartPosType {
    ABSOLUTE("ABSOLUTE"),RELATIVE("RELATIVE");
    private String c;
    private StartPosType(String c){
        this.c = c;
    }
    @Override
    public String toString(){    
        String translated = null;
        try{
            translated = Context.getMessage("StartPosType."+c);
        }catch(Exception e){}        
        return translated != null ? translated : c;
    }
}
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
        String translated = Context.getMessage("StartPosType."+c);
        return translated != null ? translated : c;
    }
}
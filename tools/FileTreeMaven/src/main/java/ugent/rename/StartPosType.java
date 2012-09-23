package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum StartPosType {
    ABSOLUTE("absolute"),RELATIVE("relative");
    private String c;
    private StartPosType(String c){
        this.c = c;
    }
    @Override
    public String toString(){
        String s = Context.getMessage(c);
        if(s == null){
            s = c;
        }
        return s;
    }
}
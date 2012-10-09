package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum ErrorAction {
    ignore("ignore"),
    skip("skip"),   
    undoAll("undoAll"),
    abort("abort"),
    retry("retry");
    private String s;
    private ErrorAction(String s){
        this.s = s;
    }
    @Override
    public String toString(){
        String translated = Context.getMessage(this.s);
        if(translated != null){
            return translated;
        }
        return this.s;
    }
}

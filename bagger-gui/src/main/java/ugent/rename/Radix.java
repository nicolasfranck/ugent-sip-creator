package ugent.rename;

import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public enum Radix {
    DECIMAL("DECIMAL"),ALPHABETHICAL("ALPHABETHICAL")/*,HEXADECIMAL("HEXADECIMAL")*/;
    private String c;
    private Radix(String c){
        this.c = c;
    }
    @Override
    public String toString(){
        String translated = null;
        try{
            translated = Context.getMessage("Radix."+c);
        }catch(Exception e){}        
        return translated != null ? translated : c;        
    }
}
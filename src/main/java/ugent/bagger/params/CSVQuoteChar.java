package ugent.bagger.params;

import ugent.bagger.helper.Context;

/**
 *
 * @author njfranck
 */
public enum CSVQuoteChar {
    DOUBLE_QUOTE("\""),SINGLE_QUOTE("\'");
    String c;
    CSVQuoteChar(String c){        
        this.c = c;
    }
    @Override
    public String toString(){        
        String translated = null;        
        try{
            translated = Context.getMessage("CSVQuoteChar."+name());
        }catch(Exception e){}
        return translated != null ? translated : c;
    }
    public char getChar(){
        return c.charAt(0);
    }    
}

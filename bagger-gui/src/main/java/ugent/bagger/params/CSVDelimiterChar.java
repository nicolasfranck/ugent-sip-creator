package ugent.bagger.params;

import ugent.bagger.helper.Context;

/**
 *
 * @author njfranck
 */
public enum CSVDelimiterChar {
    TAB("\t"),SPACE(" "),SEMICOLON(";"),COMMA(",");
    String c;
    CSVDelimiterChar(String c){        
        this.c = c;
    }
    @Override
    public String toString(){        
        String translated = null;
        try{
            translated = Context.getMessage("CSVDelimiterChar."+name());
        }catch(Exception e){}
        return translated != null ? translated : c;
    }
    public char getChar(){
        return c.charAt(0);
    }
}

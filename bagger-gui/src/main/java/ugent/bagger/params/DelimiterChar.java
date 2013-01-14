package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public enum DelimiterChar {
    COMMA(','),SEMICOLON(';');    
    char c;
    DelimiterChar(char c){
        this.c = c;
    }
}

package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public enum DelimiterChar {
    COMMA(','),SEMICOLON(';');
    
    private char c;
    private DelimiterChar(char c){
        this.c = c;
    }
}

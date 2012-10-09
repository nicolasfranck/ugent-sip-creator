package ugent.rename;

/**
 *
 * @author nicolas
 */
public enum PaddingChar {
    NULL('0'),SPACE(' ');
    public char c;
    private PaddingChar(char c){
        this.c = c;
    }   
    @Override
    public String toString(){
        String s = "";
        switch(c){
            case '0':
                s = "0";
                break;
            case ' ':
                s = "";
                break;
        }
        return s;
    }
}
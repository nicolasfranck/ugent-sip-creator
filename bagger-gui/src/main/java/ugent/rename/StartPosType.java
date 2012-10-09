package ugent.rename;

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
        return c;
    }
}
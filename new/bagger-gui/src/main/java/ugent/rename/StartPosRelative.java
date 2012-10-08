package ugent.rename;

/**
 *
 * @author nicolas
 */
public enum StartPosRelative {
    BEGIN("begin"),END("end"),BEFORE_EXTENSION("before_extension");
    private String c;
    private StartPosRelative(String c){
        this.c = c;
    }
    @Override
    public String toString(){        
        return c;
    }
}
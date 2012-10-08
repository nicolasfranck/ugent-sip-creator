package ugent.rename;

/**
 *
 * @author nicolas
 */
public enum Radix {
    DECIMAL("decimaal"),ALPHABETHICAL("alfabetisch"),HEXADECIMAL("hexadecimaal");
    private String c;
    private Radix(String c){
        this.c = c;
    }
    @Override
    public String toString(){
        return c;
    }
}
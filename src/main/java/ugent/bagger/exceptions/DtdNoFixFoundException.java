package ugent.bagger.exceptions;

/**
 *
 * @author nicolas
 */
public class DtdNoFixFoundException extends Exception{
    String root;
    public DtdNoFixFoundException(String root){
        this.root = root;
    }    
    public String getRoot() {
        return root;
    }
}
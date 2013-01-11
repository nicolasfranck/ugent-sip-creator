package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagErrorInvalidVersion implements BagError{   
    @Override
    public String getPath() {
        return "bagit.txt";
    }      
}
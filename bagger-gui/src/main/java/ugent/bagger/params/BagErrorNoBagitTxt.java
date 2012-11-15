package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagErrorNoBagitTxt implements BagError{   
    @Override
    public String getPath() {
        return "bagit.txt";
    }      
}
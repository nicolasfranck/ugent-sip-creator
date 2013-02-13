package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagErrorNoPayloadManifests implements BagError{   
    @Override
    public String getPath(){
        return "manifest-md5.txt";
    }      
}
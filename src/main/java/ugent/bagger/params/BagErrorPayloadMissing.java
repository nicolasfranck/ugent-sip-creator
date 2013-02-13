package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagErrorPayloadMissing implements BagError{
    String path;
    public BagErrorPayloadMissing(String path){
        this.path = path;
    }
    @Override
    public String getPath() {
        return path;
    }    
}

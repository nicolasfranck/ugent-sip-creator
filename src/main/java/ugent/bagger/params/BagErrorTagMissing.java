package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagErrorTagMissing implements BagError{
    String path;
    public BagErrorTagMissing(String path){
        this.path = path;
    }
    @Override
    public String getPath() {
        return path;
    }    
}
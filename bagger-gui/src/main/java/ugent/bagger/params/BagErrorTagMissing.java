package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagErrorTagMissing implements BagError{
    private String path;
    public BagErrorTagMissing(String path){
        this.path = path;
    }
    @Override
    public String getPath() {
        return path;
    }    
}
package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagErrorFileNotInManifest implements BagError{
    String path;
    public BagErrorFileNotInManifest(String path){
        this.path = path;
    }
    @Override
    public String getPath() {
        return path;
    }    
}
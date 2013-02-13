package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagErrorFileNotAllowedInBagDir implements BagError{
    String path;    
    public BagErrorFileNotAllowedInBagDir(String path){
        this.path = path;        
    }
    @Override
    public String getPath() {
        return path;
    }
      
}
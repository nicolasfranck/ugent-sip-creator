package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagErrorPayloadNotInDataDirectory implements BagError{
    String path;
    String directory;
    public BagErrorPayloadNotInDataDirectory(String path,String directory){
        this.path = path;
        this.directory = directory;
    }
    @Override
    public String getPath() {
        return path;
    }
    public String getDirectory() {
        return directory;
    }    
}

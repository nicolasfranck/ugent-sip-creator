package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagErrorFixityFailureTag implements BagErrorFixityFailure{
    String path;
    String fixity;    
    
    public BagErrorFixityFailureTag(String path,String fixity){
        this.path = path;
        this.fixity = fixity;           
    }
   @Override
    public String getFixity() {
        return fixity;
    }   
    @Override
    public String getPath() {
        return path;
    }  
}
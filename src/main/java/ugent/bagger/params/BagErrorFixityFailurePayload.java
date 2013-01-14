package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagErrorFixityFailurePayload implements BagErrorFixityFailure{
    String path;
    String fixity;    
    
    public BagErrorFixityFailurePayload(String path,String fixity){
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
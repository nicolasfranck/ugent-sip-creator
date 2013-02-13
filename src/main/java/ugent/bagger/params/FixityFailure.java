package ugent.bagger.params;

/**
 *
 * @author njfranck
 */
public class FixityFailure {
    String path;
    String fixity;

    public FixityFailure(String path,String fixity){
        this.path = path;
        this.fixity = fixity;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getFixity() {
        return fixity;
    }
    public void setFixity(String fixity) {
        this.fixity = fixity;
    }    
}
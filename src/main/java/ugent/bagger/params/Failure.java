package ugent.bagger.params;

/**
 *
 * @author njfranck
 */
public class Failure {
    String path;
    
    public Failure(String path){
        this.path = path;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }    
}

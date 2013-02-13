package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class VelocityTemplate {
    String name;
    String path;
    String xsd;
    public VelocityTemplate(String name,String path,String xsd){
        this.name = name;
        this.path = path;
        this.xsd = xsd;
    }
    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }
    public String getXsd() {
        return xsd;
    }
    public void setXsd(String xsd) {
        this.xsd = xsd;
    }        
    @Override
    public String toString(){
        return name;
    }
}
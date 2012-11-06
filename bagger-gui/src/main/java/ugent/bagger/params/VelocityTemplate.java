package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class VelocityTemplate {
    private String name;
    private String path;
    private String xsd;
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
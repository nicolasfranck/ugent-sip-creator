package helper;
public class Path {
    private Object [] components = new Object [] {};
    public Path(){}
    public Path(Object [] objects){
        setComponents(objects);
    }
    public Object[] getComponents() {
        return components;
    }
    public void setComponents(Object[] components) {
        this.components = components;
    } 
    @Override
    public String toString(){
        return ArrayUtils.join(components,"/");
    }
}

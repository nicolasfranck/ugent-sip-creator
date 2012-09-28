package ugent.bagger.helper;

/**
 *
 * @author nicolas
 */
public class TNode {
    private Object object;
    private String name;
    public TNode(Object object,String name){
        this.object = object;
        this.name = name;
    }
    public Object getObject() {
        return object;
    }
    public void setObject(Object object) {
        this.object = object;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString(){
        return name;
    }
}
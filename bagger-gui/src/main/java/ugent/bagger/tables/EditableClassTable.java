package ugent.bagger.tables;

import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class EditableClassTable<T> extends ClassTable<T> {
    public EditableClassTable(final ArrayList<T>data,String [] cols,String id){
        super(data,cols,id);                         
    }
    public void deleteSelected(){
        for(T t:getSelections()){
            delete(t);
        }
    }
    public void delete(T t){
        getData().remove(t);
    }
}

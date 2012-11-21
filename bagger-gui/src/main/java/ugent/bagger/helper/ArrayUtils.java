package ugent.bagger.helper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author nicolas
 */
public class ArrayUtils {
    public static String join(Object [] objects,String delimiter){
        StringBuilder result = new StringBuilder();
        
        for(int i = 0;i < objects.length;i++){
            if(i < objects.length - 1){
                result.append(objects[i]).append(delimiter);                
            }else{
                result.append(objects[i]);                
            }
        }
        
        return result.toString();
    }
    public static<T> ArrayDiff diff(ArrayList<T>a,ArrayList<T>b){
        ArrayDiff diff = new ArrayDiff();
        //added
        for(T o:b){
            if(!a.contains(o)){
                diff.getAdded().add(o);
            }
        }
        //deleted
        for(T o:a){
            if(!b.contains(o)){
                diff.getDeleted().add(o);
            }
        }
        return diff;
    }
    public static class ArrayDiff<T> {
        ArrayList<T>added;
        ArrayList<T>deleted;

        public ArrayList<T> getAdded() {
            if(added == null){
                added = new ArrayList<T>();
            }
            return added;
        }        
        public ArrayList<T> getDeleted() {
            if(deleted == null){
                deleted = new ArrayList<T>();
            }
            return deleted;
        }               
    }
    public static void main(String [] main){
        String [] a = {"a","b","c","e"};
        String [] b = {"a","b","c","d"};
        ArrayList<Object>lista = new ArrayList<Object>(Arrays.asList(a));
        ArrayList<Object>listb = new ArrayList<Object>(Arrays.asList(b));        
        
        ArrayDiff diff = diff(lista,listb);
        System.out.println("added: "+diff.getAdded().size());
        System.out.println("deleted: "+diff.getDeleted().size());
    }
}

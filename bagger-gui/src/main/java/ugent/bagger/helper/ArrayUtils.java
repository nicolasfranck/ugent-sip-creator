package ugent.bagger.helper;

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
}

package ugent.bagger.helper;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author nicolas
 */
public class CSV2 {
    
    public static void main(String [] args){
        try{
            CSVUtils.readCSV(new File("/home/nicolas/adressenall.csv"),new IteratorListener(){
                @Override
                public void execute(Object o) {
                    Map<String,String>map = (Map<String,String>)o;
                    Set<String>keys = map.keySet();
                    for(String key:keys){
                        System.out.println(key+" : "+map.get(key));
                    }
                    System.out.println();
                }
            }); 
                      
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

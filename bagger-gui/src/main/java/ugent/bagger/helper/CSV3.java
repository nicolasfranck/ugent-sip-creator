package ugent.bagger.helper;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author nicolas
 */
public class CSV3 {
    
    public static void main(String [] args){
        try{
            Properties prop = new Properties();
            prop.load(new FileReader(new File("/home/nicolas/velocity.properties")));

            /*  first, get and initialize an engine  */
            
            final VelocityEngine ve = new VelocityEngine();        
            Set<Map.Entry<Object,Object>> keys = prop.entrySet();
            for(Map.Entry entry:keys){
                ve.setProperty((String)entry.getKey(),(String)entry.getValue());
            }
            ve.init();
            
            final String template = "$ID $NAAM $DEPT";
           
            CSVUtils.readCSV(new File("/home/nicolas/adressenall.csv"),new IteratorListener(){
                @Override
                public void execute(Object o) {
                    Map<String,String>map = (Map<String,String>)o;
                    final VelocityContext context = new VelocityContext(map);
                    
                    StringWriter writer = new StringWriter();
                    try {
                        
                        boolean ok = ve.evaluate(context,writer,"LOG",template);                        
                        System.out.println(writer.toString());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }); 
                      
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

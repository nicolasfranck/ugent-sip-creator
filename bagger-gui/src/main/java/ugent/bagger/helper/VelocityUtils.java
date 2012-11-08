package ugent.bagger.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author nicolas
 */
public class VelocityUtils {
    private static VelocityEngine velocityEngine;
    private static HashMap<String,String>velocityMap;
    private static Log log = LogFactory.getLog(VelocityUtils.class);

    public static HashMap<String, String> getVelocityMap() {
        if(velocityMap == null){
            velocityMap = (HashMap<String,String>) Beans.getBean("velocity");
        }
        return velocityMap;
    }
    public static VelocityEngine getVelocityEngine() throws IOException {
        if(velocityEngine == null){
            velocityEngine = new VelocityEngine();            
            String propertiesFileName = getVelocityMap().get("properties");
            Properties props = new Properties();
            props.load(Context.getResourceAsStream(propertiesFileName));
            for(Entry entry:props.entrySet()){
                velocityEngine.setProperty((String)entry.getKey(),entry.getValue());
            }
        }
        return velocityEngine;
    } 
    
}

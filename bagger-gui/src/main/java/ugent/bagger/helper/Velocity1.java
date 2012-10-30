/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *
 * @author nicolas
 */
public class Velocity1 {
    public static void main(String [] args) throws Exception{
        Properties prop = new Properties();
        prop.load(new FileReader(new File("/home/nicolas/velocity.properties")));
        
        /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();        
        Set<Entry<Object,Object>> keys = prop.entrySet();
        
        for(Entry entry:keys){
            ve.setProperty((String)entry.getKey(),(String)entry.getValue());
        }
        
        ve.init();
        /*  next, get the Template  */                
        Template t = ve.getTemplate("/home/nicolas/helloworld.vm");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("name", "World");
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        /* show the World */
        System.out.println( writer.toString() );  
    }
}

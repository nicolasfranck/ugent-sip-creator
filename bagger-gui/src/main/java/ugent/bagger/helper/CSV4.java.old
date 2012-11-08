/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

/**
 *
 * @author nicolas
 */
public class CSV4 {
    public static void main(String [] args){
        try{
            final Template template = VelocityUtils.getVelocityEngine().getTemplate("/home/nicolas/adressen.vm");
            
            CSVUtils.readCSV(new File("/home/nicolas/adressenall.csv"),new IteratorListener(){
                @Override
                public void execute(Object o) {
                    Map<String,String>map = (Map<String,String>)o;
                    final VelocityContext context = new VelocityContext(map);
                    
                    StringWriter writer = new StringWriter();
                    try {
                        template.merge(context,writer);                        
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

package gov.loc.repository.bagger.ui.handlers;

import java.util.HashMap;
import ugent.bagger.exporters.Exporter;
import ugent.bagger.helper.Beans;

/**
 *
 * @author nicolas
 */
public class ExportUtils {
    static HashMap<String,HashMap<String,Object>>exporters;
    
    public static HashMap<String,HashMap<String,Object>>getExporters(){
        if(exporters == null){
            exporters = (HashMap<String,HashMap<String,Object>>) Beans.getBean("exporters");
        }
        return exporters;
    }
    public static String [] getExporterNames(){                
        return getExporters().keySet().toArray(new String [] {});
    }
    public static Exporter getExporterByName(String name) throws InstantiationException, ClassNotFoundException, IllegalAccessException{
        Exporter exporter = null;
        
        if(getExporters().containsKey(name)){
            HashMap<String,Object>config = getExporters().get(name);
            if(config.containsKey("class")){
                exporter = (Exporter) Class.forName(config.get("class").toString()).newInstance();
            }
        }
                
        return exporter;
    }
}

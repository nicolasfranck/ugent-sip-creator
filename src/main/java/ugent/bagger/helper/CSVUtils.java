package ugent.bagger.helper;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Document;
import ugent.bagger.params.VelocityTemplate;

/**
 *
 * @author nicolas
 */
public class CSVUtils {
    public static List<Map<String,String>>readCSV(Reader reader,CsvPreference csvPreference) throws IOException{
        final ArrayList<Map<String,String>>list = new ArrayList<Map<String,String>>();
        readCSV(reader,csvPreference,new IteratorListener(){
            @Override
            public void execute(Object o) {
                list.add((Map<String,String>)o);
            }        
        },-1);
        return list;
    }
    public static void readCSV(File file,IteratorListener listener) throws IOException{
        readCSV(file,CsvPreference.STANDARD_PREFERENCE,listener);
    }
    public static void readCSV(File file,CsvPreference csvPreference,IteratorListener listener) throws IOException{
        readCSV(new BufferedReader(new FileReader(file)),CsvPreference.STANDARD_PREFERENCE,listener,-1);
    }
    public static void readCSV(Reader reader,IteratorListener listener) throws IOException{
        readCSV(reader,CsvPreference.STANDARD_PREFERENCE,listener,-1);       
    }
    public static void readCSV(Reader reader,CsvPreference csvPreference,IteratorListener listener,int num) throws IOException{
        ICsvMapReader mapReader = new CsvMapReader(reader,csvPreference);
        final String [] cols = mapReader.getHeader(true);   
        
        int i = 0;
        Map<String,String>map;        
        while((map = mapReader.read(cols)) != null){
            if(num > 0 && i > num){
                break;
            }
            i++;
            listener.execute(map);            
        }        
    }
    public static CsvPreference createCSVPreference(char quote,char del,String eol,boolean surroundingSpacesNeedQuotes){                
        return new CsvPreference.Builder(quote,del,eol).surroundingSpacesNeedQuotes(true).build();
    }
    public static Document templateToDocument(VelocityTemplate vt,HashMap<String,String>record) throws IOException, ResourceNotFoundException, ParseErrorException, Exception{
        VelocityEngine ve = VelocityUtils.getVelocityEngine();
        Template template = ve.getTemplate(vt.getPath());            
        HashMap<String,Object>r = new HashMap<String,Object>();

        r.put("record",record);
        VelocityContext vcontext = new VelocityContext(r);
        StringWriter writer = new StringWriter();
        template.merge(vcontext,writer);                        

        String output = writer.toString();

        //zet xml om naar w3c.document
        return XML.XMLToDocument(new StringReader(output));
    }
    public static HashMap<String,String>createDefaultMap(HashMap<String,ArrayList<String>> bagInfo){
        HashMap<String,String>map = new HashMap<String, String>();        
        HashMap<String,HashMap<String,String>>csvDefaultMapConfig = (HashMap<String,HashMap<String,String>>)Beans.getBean("csvDefaultMapConfig");                
        if(csvDefaultMapConfig != null){
            Set<String>keys = csvDefaultMapConfig.keySet();
            for(String key:keys){
                HashMap<String,String>keyConfig = csvDefaultMapConfig.get(key);
                String type = keyConfig.get("type");
                String value = keyConfig.get("value");
                if(type != null && !type.isEmpty()){
                    if(type.equals("bag-info") && bagInfo.containsKey(key)){
                        ArrayList<String>values = bagInfo.get(key);
                        value = values.size() > 0 ? values.get(0):"";
                        map.put(key,value);
                    }else if(type.equals("custom")){
                        map.put(key,value);
                    }
                }
            }
        }        
        return map;
    }
}
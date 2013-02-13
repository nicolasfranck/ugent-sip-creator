/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;
import ugent.bagger.helper.CSVUtils;

/**
 *
 * @author njfranck
 */
public class CSVTest {
    public static void main(String...args) throws FileNotFoundException, IOException{
        
        File file = new File("/home/njfranck/test.csv");
        CsvPreference csvPreference = CSVUtils.createCSVPreference(
            '\"',
            ',',
            "\n",
            true
        ); 
        
        //PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File("/tmp/output.txt")),"UTF8"));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out,"UTF8"));
                            
        ICsvMapReader mapReader = new CsvMapReader(new InputStreamReader(new FileInputStream(file),"UTF8"),csvPreference);                       
        
        final String [] cols = mapReader.getHeader(true); 

        HashMap<String,String>map;
        while((map = (HashMap<String,String>) mapReader.read(cols)) != null){                                
            Iterator<String>iterator = map.keySet().iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                out.println("key: "+key);
                out.println("value: \""+map.get(key)+"\"");
            }                        
        }
    }
}

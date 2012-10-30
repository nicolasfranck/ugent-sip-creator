package ugent.bagger.helper;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

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
            if(i > num){
                break;
            }
            listener.execute(map);            
        }        
    }
    public static CsvPreference createCSVPreference(char quote,char del,String eol,boolean surroundingSpacesNeedQuotes){                
        return new CsvPreference.Builder(quote,del,eol).surroundingSpacesNeedQuotes(true).build();
    }
}
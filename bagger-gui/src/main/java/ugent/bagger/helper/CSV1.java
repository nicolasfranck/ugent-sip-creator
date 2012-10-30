package ugent.bagger.helper;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.reader.internal.DefaultCSVEntryParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;

/**
 *
 * @author nicolas
 */
public class CSV1 {
    public static void main(String [] args){
        try{
            Reader reader = new BufferedReader(new FileReader("/home/nicolas/adressenall.csv"));
            CSVStrategy strategy = new CSVStrategy(',','"','#',true,true);
            CSVReader<String []> csvReader = new CSVReaderBuilder<String []>(reader).strategy(strategy).entryParser(new DefaultCSVEntryParser()).build();
            Iterator<String []> iterator = csvReader.iterator();
            while(iterator.hasNext()){
                System.out.println(ArrayUtils.join(iterator.next()," $$ "));
            }            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

package ugent.bagger.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author nicolas
 */
public class CSV2 {
    public class Person {
        private String name;
        private String firstName;
        private String number;
        
        public Person(){}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
        
    }
    public static void main(String [] args){
        try{
            Reader reader = new BufferedReader(new FileReader("/home/nicolas/entries.csv"));
            
            ICsvBeanReader beanReader = new CsvBeanReader(reader,CsvPreference.STANDARD_PREFERENCE);
            final String [] cols = beanReader.getHeader(true);            
            for(String col:cols){
                System.out.println("col: '"+col+"'");
            }
            
            Person x;
            while((x = beanReader.read(Person.class,cols)) != null){
                System.out.println("firstName: "+x.getFirstName());
                System.out.println("name: "+x.getName());
                System.out.println("number: "+x.getNumber());
                System.out.println();
            }
                      
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

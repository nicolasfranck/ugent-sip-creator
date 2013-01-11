package ugent.rename;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author njfranck
 */
public class FileSorter {
    static Comparator fileNameSorter =  new Comparator<File>(){
        @Override
        public int compare(File f1,File f2){                        
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    };
    static Comparator fileDateModifiedSorter =  new Comparator<File>(){
        @Override
        public int compare(File f1,File f2){                        
            return (int)(f1.lastModified() - f2.lastModified());            
        }
    };
    
    public static void sort(ArrayList<File>files,PreSort preSort){
        if(preSort == PreSort.FILE_NAME){
            Collections.sort(files,fileNameSorter);
        }else if(preSort == PreSort.FILE_DATE_MODIFIED){
            Collections.sort(files,fileDateModifiedSorter);            
        }
    }
}

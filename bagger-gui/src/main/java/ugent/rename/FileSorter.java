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
    static Comparator fileNameSorterAsc =  new Comparator<File>(){
        @Override
        public int compare(File f1,File f2){                        
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    };
    static Comparator fileNameSorterDesc =  new Comparator<File>(){
        @Override
        public int compare(File f1,File f2){
            return -(fileNameSorterDesc.compare(f1,f2));            
        }
    };
    static Comparator fileDateModifiedSorterAsc =  new Comparator<File>(){
        @Override
        public int compare(File f1,File f2){  
            long f1Date = f1.lastModified();
            long f2Date = f2.lastModified();
            if(f1Date > f2Date){
                return 1;
            }else if(f1Date < f2Date){
                return -1;
            }
            return 0;                       
        }
    };
    static Comparator fileDateModifiedSorterDesc =  new Comparator<File>(){
        @Override
        public int compare(File f1,File f2){  
            return -(fileDateModifiedSorterAsc.compare(f1,f2));                      
        }
    };
    
    public static void sort(ArrayList<File>files,PreSort preSort){
        if(preSort == PreSort.FILE_NAME_ASC){
            Collections.sort(files,fileNameSorterAsc);
        }else if(preSort == PreSort.FILE_NAME_DESC){
            Collections.sort(files,fileNameSorterDesc);            
        }else if(preSort == PreSort.FILE_DATE_MODIFIED_ASC){
            Collections.sort(files,fileDateModifiedSorterAsc);            
        }else if(preSort == PreSort.FILE_DATE_MODIFIED_DESC){
            Collections.sort(files,fileDateModifiedSorterDesc);            
        }
    }
}

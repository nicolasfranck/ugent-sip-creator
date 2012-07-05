/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import test.*;
import java.io.File;
import java.util.ArrayList;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import java.util.Collection;

/**
 *
 * @author nicolas
 */
public class FileUtils {
    
    public static ArrayList<File> listFiles(String fn){
        return listFiles(new File(fn));
    }
    public static ArrayList<File> listFiles(File f){
        final ArrayList<File> files = new ArrayList<File>();
        listFiles(
            f,new IteratorListener(){
                public void execute(Object o){                   
                    files.add((File)o);
                }
            }
        );
        return files;
    }
    public static void listFiles(String filename,IteratorListener l){
        listFiles(new File(filename),l);
    }
    public static void listFiles(File f,IteratorListener l){
        //must be directory that can be read
        if(!f.isDirectory() || f.listFiles() == null)return;
        for(File sb:f.listFiles()){
            if(sb.isDirectory()){
                listFiles(sb,l);
            }else if(sb.isFile()){
                l.execute((Object)sb);
            }
        }        
    }

}

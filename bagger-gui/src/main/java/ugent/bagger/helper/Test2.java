/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class Test2 {
    public static void removeEmptyDirectories(File dir){
        System.out.println("removeEmptyDirectories('"+dir+"')");
        if(dir.isDirectory()){
            for(File file:dir.listFiles()){                
                if(file.isDirectory()){
                    removeEmptyDirectories(file);
                    File [] list = file.listFiles();
                    if(list == null || list.length == 0){
                        System.out.println("removing "+file);
                        file.delete();
                    }                    
                }
            }
        }
    }
    public static void main(String...args){
        removeEmptyDirectories(new File("/home/nicolas/xml"));
    }
}

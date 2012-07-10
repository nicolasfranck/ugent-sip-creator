/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.File;
import java.util.ArrayList;
import javax.activation.MimetypesFileTypeMap;

/**
 *
 * @author nicolas
 */
public class Mimes {
    public static void main(String [] args){       
        MimetypesFileTypeMap map = new MimetypesFileTypeMap();
        for(String path:args){
            ArrayList<File> list = FileUtils.listFiles(path);
            
            for(File file:list){
                String mimeType = map.getContentType(file);
                System.out.println(
                    "'"+file.getAbsolutePath()+"' '"+mimeType+"'"
                );
            }
            
        }
    }
}

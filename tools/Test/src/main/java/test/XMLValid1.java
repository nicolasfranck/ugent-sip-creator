/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import helper.XML;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class XMLValid1 {
    public static void main(String []args) throws MalformedURLException{
        if(args.length < 2)System.exit(1);
        File schemaFile = new File(args[0]);
        URL schemaURL = new URL("file://"+schemaFile.getAbsolutePath());
        
        for(int i = 1;i < args.length;i++){
            String filePath = args[i];            
            ArrayList<File> files = FileUtils.listFiles(new File(filePath));
            for(File file:files){
                URL url = new URL("file://"+file.getAbsolutePath());
                
                try{            
                    System.out.print("validating '"+url.toString()+"' against schema '"+schemaFile.getAbsolutePath()+"'.. ");
                    XML.validate(url,schemaURL);
                    XML.validate(url,schemaURL);                    
                    System.out.println("successfull");                                        
                }catch(Exception e){
                    System.out.println("failed");                    
                }
            }
            
        }
        
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

/**
 *
 * @author nicolas
 */
import eu.medsea.mimeutil.MimeType;
import java.io.File;
import eu.medsea.mimeutil.MimeUtil;
import java.util.Collection;
import java.util.Iterator;

public class Mimes3 {
    public static void main(String [] args){        
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        //System.out.println("length args:"+args.length);
        for(String filename:args){
            System.out.println(filename);
            FileUtils.listFiles(filename,new IteratorListener(){
                public void execute(Object o){
                    File file = (File)o;
                    try{
                        System.out.print(file.getAbsolutePath()+":");
                        Collection mimeTypes = MimeUtil.getMimeTypes(file);
                        Iterator<MimeType> it = mimeTypes.iterator();
                        while(it.hasNext()){
                            MimeType mime = it.next();
                            System.out.println(mime.getMediaType()+"/"+mime.getSubType());
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }            
            });            
       }
    }
}
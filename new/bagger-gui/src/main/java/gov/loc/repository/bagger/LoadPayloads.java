/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.loc.repository.bagger;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFile;
import java.io.File;
import java.io.InputStream;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

/**
 *
 * @author nicolas
 */
public class LoadPayloads {
    public static void main(String [] args){
        BagFactory bagFactory = new BagFactory();
        Bag bag = bagFactory.createBag();  
        bag.setFile(new File("/home/nicolas/newbaggie.zip"));
        //bag.loadFromPayloadFiles();
        System.out.println("getFile: "+bag.getFile());       
       
        for(BagFile file:bag.getPayload()){
            System.out.println("bagFile: "+file.getFilepath());
        }        
        for(BagFile file:bag.getTags()){
            System.out.println("tagFile: "+file.getFilepath());
        }
        try{
            FileSystemManager fsManager = VFS.getManager();
            FileObject fileObject = fsManager.resolveFile("zip:/home/nicolas/Desktop/bagits/archive-ugent-be-0D38FA8E-1BFC-11E0-A53B-D862A2B3687C.zip!/archive-ugent-be-0D38FA8E-1BFC-11E0-A53B-D862A2B3687C/mets.xml");
            InputStream in = fileObject.getContent().getInputStream();
            if(in != null){
                byte [] buffer = new byte[1024];
                int bytesRead = 0;                
                while((bytesRead = in.read(buffer)) > 0){
                    System.out.write(buffer,0,bytesRead);
                }
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

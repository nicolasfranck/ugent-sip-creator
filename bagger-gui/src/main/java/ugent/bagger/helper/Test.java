/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.writer.impl.FileSystemWriter;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author nicolas
 */
public class Test {
    public static void main(String...args){
        BagFactory bf = new BagFactory();        
        Bag bag = bf.createBag(new File("/home/nicolas/baggie3"));
        Manifest payloadManifest = bag.getPayloadManifest(Manifest.Algorithm.MD5);
        Manifest tagManifest = bag.getTagManifest(Manifest.Algorithm.MD5);
        
        Iterator<BagFile>itPayloads = bag.getPayload().iterator();
        while(itPayloads.hasNext()){
            BagFile bagFile = itPayloads.next();
            System.out.println("bagFile: "+bagFile.getFilepath()+" exists: "+bagFile.exists());
            if(!bagFile.exists()){
                itPayloads.remove();
                payloadManifest.remove(bagFile.getFilepath());
            }
        }
        //bag.removeBagFile("data/InfoInputPane.java");
        /**/
        
        FileSystemWriter bagWriter = new FileSystemWriter(bf);
       
        bag = bagWriter.write(bag,new File("/home/nicolas/baggie3"));
    }
}

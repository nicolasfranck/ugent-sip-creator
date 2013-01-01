/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.transformer.impl.DefaultCompleter;
import gov.loc.repository.bagit.writer.impl.FileSystemWriter;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author nicolas
 */
public class Test2 {
    public static void main(String...args){
        /*BagFactory bf = new BagFactory();        
        Bag bag = bf.createBag(new File("/home/nicolas/baggie3"));
        
        DefaultCompleter completer = new DefaultCompleter(new BagFactory());
        completer.setClearExistingPayloadManifests(true);
        completer.setClearExistingTagManifests(true);
        completer.setPayloadManifestAlgorithm(Manifest.Algorithm.MD5);
        completer.setTagManifestAlgorithm(Manifest.Algorithm.MD5);
        bag = completer.complete(bag);*/
        File file = new File("/home/nicolas/Bagger-LC");
        while(file != null){
            System.out.println(file);
            file = file.getParentFile();
        }
    }
}

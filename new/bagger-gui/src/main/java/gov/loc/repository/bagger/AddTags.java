package gov.loc.repository.bagger;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.ProgressListener;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.FileSystemWriter;
import gov.loc.repository.bagit.writer.impl.TarBz2Writer;
import gov.loc.repository.bagit.writer.impl.TarGzWriter;
import gov.loc.repository.bagit.writer.impl.TarWriter;
import gov.loc.repository.bagit.writer.impl.ZipWriter;
import java.io.File;

/**
 *
 * @author nicolas
 */
public class AddTags {
    public static void writeBag(BagFactory bagFactory,Bag bag,File file){
        Writer writer;        
        if(file.getName().endsWith(".zip")){
            writer = new ZipWriter(bagFactory);
        }else if(file.getName().endsWith(".tar")){
            writer = new TarWriter(bagFactory);
        }else if(file.getName().endsWith(".tar.gz")){
            writer = new TarGzWriter(bagFactory);                
        }else if(file.getName().endsWith(".tar.bz2")){
            writer = new TarBz2Writer(bagFactory);
        }else{
            writer = new FileSystemWriter(bagFactory);
        }     
        writer.addProgressListener(new ProgressListener() {
            @Override
            public void reportProgress(String activity, Object o, Long count, Long total) {
                System.out.println(activity+": "+count+"/"+total);
            }
        });            
        writer.write(bag,file);
    }
    public static void main(String [] args){
        File file = new File("/tmp/newbaggie.zip");
        BagFactory bagFactory = new BagFactory();
        Bag bag = bagFactory.createBag();  
        bag.setFile(file);
        
        String [] fileNames = new String [] {
            "/home/nicolas/bhsl-pap","/home/nicolas/mets-api"
        };
        for(String fileName:fileNames){
            bag.addFileToPayload(new File(fileName));
        }
        
        bag = bag.makeComplete();
        
        
        
        long start = System.nanoTime();
        writeBag(bagFactory,bag,file);
        long end = System.nanoTime();
        System.out.println("time to create bag:"+(end - start));
        
        bag.addFileAsTag(new File("/home/nicolas/mets.xml"));
        
        
        //bag = bag.makeComplete();
        
        start = System.nanoTime();
        writeBag(bagFactory,bag,file);
        end = System.nanoTime();
        System.out.println("time to recreate bag:"+(end - start));
    }
}

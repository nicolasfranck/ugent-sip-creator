package ugent.bagger.helper;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.ProgressListener;
import gov.loc.repository.bagit.impl.FileBagFile;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.FileSystemWriter;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class BagitTest {
    public static void main(String [] args){
        BagFactory bf = new BagFactory();
        Bag bag = bf.createBag();        
        bag.addFilesToPayload(FUtils.listFiles(new File("/home/nicolas/java")));
        bag = bag.makeComplete();
        Writer bw = new FileSystemWriter(bf);
        bw.addProgressListener(new ProgressListener() {
            @Override
            public void reportProgress(String activity, Object item, Long count, Long total) {
                if(count == null || total == null){
                    return;
                }
                System.out.println(count+"/"+total);
            }
        });
        bw.write(bag,new File("/home/nicolas/javabag"));
        for(BagFile bagFile:bag.getPayload()){
            if(!(bagFile instanceof FileBagFile)){
                continue;
            }
            FileBagFile f = (FileBagFile)bagFile;
           
        }
    }
}

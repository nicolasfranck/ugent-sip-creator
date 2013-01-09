/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.FileSystemWriter;
import java.io.File;

/**
 *
 * @author njfranck
 */
public class Test3 {
    public static void main(String...args){
        BagFactory bf = new BagFactory();
        Bag bag = bf.createBag();
        
        bag.addFileToPayload(new File("/home/njfranck/test/a.txt"));
        bag.addFileToPayload(new File("/home/njfranck/test/A.txt"));
        
        bag = bag.makeComplete();
        
        Writer bw = new FileSystemWriter(bf);
        
        bw.write(bag,new File("/home/njfranck/test/bag-a"));
    }
}

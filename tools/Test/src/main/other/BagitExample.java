/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.ProgressListener;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.FileSystemWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author nicolas
 */
public class BagitExample {
    public static List<File> listFiles(File dir){
        ArrayList<File> files = new ArrayList<File>();
        if(dir == null)return files;
        else if(dir.isFile()){
            files.add(dir);
            return files;
        }
        for(File file:dir.listFiles()){
            if(file.isDirectory()){
                files.addAll(listFiles(file));
            }else{
                files.add(file);
            }
        }
        return files;
    }
    public static void usage(){
        System.err.println("usage: java Test.BagitExample <dest> <source-1> <source-2> .. <source-n>");
        System.exit(1);
    }
    public static void main(String []args){
        if(args.length < 2)usage();

        try{
            BagFactory bagFactory = new BagFactory();
            Bag bag = bagFactory.createBag();
            for(int i = 1;i < args.length;i++){
                System.out.println("adding "+args[i]);
                bag.addFilesToPayload(listFiles(new File(args[i])));
            }            
            bag = bag.makeComplete();
            Writer writer = new FileSystemWriter(bagFactory);
            writer.addProgressListener(new ProgressListener() {
                public void reportProgress(String activity, Object o, Long count, Long total) {
                    System.out.println(activity+": "+count+"/"+total);
                }
            });
            writer.write(bag,new File(args[0]));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

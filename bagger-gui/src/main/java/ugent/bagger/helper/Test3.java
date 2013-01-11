/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;

import java.io.File;
import org.apache.commons.vfs2.FileObject;

/**
 *
 * @author njfranck
 */
public class Test3 {
    public static void main(String...args)throws Exception{
        /*BagFactory bf = new BagFactory();
        Bag bag = bf.createBag();
        
        bag.addFileToPayload(new File("/home/njfranck/test/a.txt"));
        bag.addFileToPayload(new File("/home/njfranck/test/A.txt"));
        
        bag = bag.makeComplete();
        
        Writer bw = new FileSystemWriter(bf);
        
        bw.write(bag,new File("/home/njfranck/test/bag-a"));
        Pattern pattern = Pattern.compile("(.*)");
        System.out.println("pattern: "+pattern);
        String text = "hello/\\";
        Matcher matcher = pattern.matcher(text);        
        
        String newText = matcher.replaceAll("\\U$1");
        System.out.println(text+" => "+newText);*/
        
        File file = new File("/home/njfranck/test/dit heeft spaties?!.txt");
        //FileObject fobject = FUtils.resolveFile("file:///"+file.toURI().toString());
        FileObject fobject = FUtils.resolveFile("file:///"+file.toString());
        System.out.println("fobject: "+fobject);
    }
}

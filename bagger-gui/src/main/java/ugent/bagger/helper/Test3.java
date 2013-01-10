/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author njfranck
 */
public class Test3 {
    public static void main(String...args){
        /*BagFactory bf = new BagFactory();
        Bag bag = bf.createBag();
        
        bag.addFileToPayload(new File("/home/njfranck/test/a.txt"));
        bag.addFileToPayload(new File("/home/njfranck/test/A.txt"));
        
        bag = bag.makeComplete();
        
        Writer bw = new FileSystemWriter(bf);
        
        bw.write(bag,new File("/home/njfranck/test/bag-a"));*/
        Pattern pattern = Pattern.compile("(.*)");
        System.out.println("pattern: "+pattern);
        String text = "hello/\\";
        Matcher matcher = pattern.matcher(text);        
        
        String newText = matcher.replaceAll("\\U$1");
        System.out.println(text+" => "+newText);
    }
}

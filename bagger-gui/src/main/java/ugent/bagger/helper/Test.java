package ugent.bagger.helper;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;


/**
 *
 * @author nicolas
 */
public class Test {
    public static void main(String...args){
        
        try  {
           FileSystem fs = FileSystems.getDefault();
           Path path = fs.getPath("/home/njfranck/examples.desktop");
           BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);           
           
           System.out.println("creationTime: "+ attributes.creationTime());
           System.out.println("lastAccessTime: "+ attributes.lastAccessTime());
           System.out.println("lastModifiedTime: "+ attributes.lastModifiedTime());
        }catch (IOException e) { 
             e.printStackTrace();
        }
    }
}

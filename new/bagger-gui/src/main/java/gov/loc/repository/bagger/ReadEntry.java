package gov.loc.repository.bagger;

import java.io.InputStream;
import ugent.bagger.helper.FUtils;

/**
 *
 * @author nicolas
 */
public class ReadEntry {
    public static void main(String [] args){
        
        try{
            
            String entryString = FUtils.getEntryStringFor("/home/nicolas/baggie.tar.bz2","baggie/mets.xml");
            System.out.println("entryString: "+entryString);
            InputStream in = FUtils.getInputStreamFor(entryString);
            System.out.println("inputStream: "+in);
            int bytesRead = 0;
            byte [] buf = new byte[1024];
            while((bytesRead = in.read(buf)) > 0){
                System.out.write(buf,0,bytesRead);
            }
            in.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

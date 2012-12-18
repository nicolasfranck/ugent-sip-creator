package ugent.bagger.exporters;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.Deflater;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.XML;

/**
 *
 * @author nicolas
 */
public class DSpaceSipTest {
    public static void main(String [] args){
        try{
            DSpaceSIPMets sip = new DSpaceSIPMets(true,Deflater.BEST_SPEED);                        
            
            sip.addDescriptiveMD("DC",XML.XMLToDocument(new File("/home/nicolas/dc.xml"),false).getDocumentElement());
            
            ArrayList<File>files = FUtils.listFiles(new File("/home/nicolas/java"));
            for(File file:files){
                System.out.print("adding "+file+" to sip..");
                sip.addBitstream(file,file.getName(),"ORIGINAL",false);                
                System.out.println("done");
            }
            sip.write(new File("/tmp/mysip.zip"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
}

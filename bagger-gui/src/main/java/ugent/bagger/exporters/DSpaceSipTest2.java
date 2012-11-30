package ugent.bagger.exporters;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.Mets;
import java.io.File;
import java.util.zip.Deflater;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.XML;

/**
 *
 * @author nicolas
 */
public class DSpaceSipTest2 {
    public static void main(String [] args){
        try{
            File metsXMLFile = new File("/home/nicolas/baggie/mets.xml");
            File metsDir = metsXMLFile.getParentFile();
            Mets mets = MetsUtils.readMets(metsXMLFile);
            
            DSpaceSIPMets sip = new DSpaceSIPMets(true,Deflater.BEST_SPEED);                        
            
            sip.addDescriptiveMD("DC",XML.XMLToDocument(new File("/home/nicolas/dc.xml"),false).getDocumentElement());            
            
            for(FileSec.FileGrp fileGroup:mets.getFileSec().getFileGrp()){
                System.out.println("fileGroup USE "+fileGroup.getUse());
                if(!fileGroup.getUse().equals("payloads")){
                    continue;
                }
                for(FileSec.FileGrp.File metsFile:fileGroup.getFile()){
                    File payloadFile = new File(metsDir,metsFile.getFLocat().get(0).getXlinkHREF());
                    System.out.println("adding payload "+payloadFile);
                    DSpaceSIPMets.PackageFile packageFile = new DSpaceSIPMets.PackageFile(payloadFile);
                    packageFile.setMetsFile(metsFile);
                    sip.addPackageFile(packageFile,"ORIGINAL",false);
                }
            }
            
            sip.write(new File("/tmp/mysip.zip"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
}

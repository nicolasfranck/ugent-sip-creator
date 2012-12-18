package ugent.bagger.exporters;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import java.io.File;
import java.util.zip.Deflater;
import ugent.bagger.helper.MetsUtils;

/**
 *
 * @author nicolas
 */
public class DSpaceSipTest2 {
    public static void main(String [] args){
        try{
            File metsXMLFile = new File("/home/nicolas/bags/baggie/mets.xml");
            File metsDir = metsXMLFile.getParentFile();
            Mets mets = MetsUtils.readMets(metsXMLFile);
            
            DSpaceSIPMets sip = new DSpaceSIPMets(true,Deflater.BEST_SPEED);                        
            
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
            for(MdSec mdSec:mets.getDmdSec()){
                sip.addDescriptiveMD(mdSec.getMdWrap().getMDTYPE().toString(),mdSec.getMdWrap().getXmlData().get(0));
            }
            
            sip.write(new File("/tmp/mysip.zip"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
}

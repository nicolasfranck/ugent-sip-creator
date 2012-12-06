package ugent.bagger.exporters;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.MdSec.MdWrap;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import java.io.File;
import java.util.ArrayList;
import org.w3c.dom.Element;

/**
 *
 * @author nicolas
 */
public class ExportDSpaceMetsArchive implements Exporter{
    @Override
    public ArrayList<MdSec> getConversionCandidates(Mets mets) {
        ArrayList<MdSec>conversionCandidates = new ArrayList<MdSec>();
        
        return conversionCandidates;                
    }
    @Override
    public void export(MetsBag metsBag, Mets mets) {
        ArrayList<MdSec>conversionCandidates = getConversionCandidates(mets);
        
        DSpaceSIPMets dspace = new DSpaceSIPMets();
        File dir = metsBag.getRootDir();
        
        for(MdSec mdSec:conversionCandidates){
           MdWrap mdWrap = mdSec.getMdWrap();
           String type = mdWrap.getMIMETYPE();
           Element element = mdWrap.getXmlData().get(0);
           dspace.addDescriptiveMD(type,element);           
        }
        for(FileSec.FileGrp fileGroup:mets.getFileSec().getFileGrp()){
                System.out.println("fileGroup USE "+fileGroup.getUse());
                if(!fileGroup.getUse().equals("payloads")){
                    continue;
                }
                for(FileSec.FileGrp.File metsFile:fileGroup.getFile()){
                    File payloadFile = new File(dir,metsFile.getFLocat().get(0).getXlinkHREF());
                    System.out.println("adding payload "+payloadFile);
                    DSpaceSIPMets.PackageFile packageFile = new DSpaceSIPMets.PackageFile(payloadFile);
                    packageFile.setMetsFile(metsFile);
                    dspace.addPackageFile(packageFile,"ORIGINAL",false);
                }
            }
    }
}

package ugent.bagger.exporters;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.MdSec.MdWrap;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.BagitMetsValidationException;
import ugent.bagger.helper.MetsUtils;

/**
 *
 * @author nicolas
 */
public class ExporterDSpaceMetsArchive extends Exporter{       
    @Override
    public void export(File dir,OutputStream out) 
            throws                 
                BagitMetsValidationException,
                IOException, 
                UnsupportedEncodingException, 
                DatatypeConfigurationException,
                ParserConfigurationException,
                TransformerException,
                SAXException,
                FileNotFoundException,
                ParseException
    {           
        
        //validate directory
        validate(dir);
        
        //create Mets
        Mets mets = MetsUtils.readMets(new File(dir,"mets.xml"));
        
        //retrieve metadata
        ArrayList<MdSec>metadata = getMetadata(mets);
        
        //create export
        DSpaceSIPMets exporter = new DSpaceSIPMets();        
        
        for(MdSec mdSec:metadata){
           MdWrap mdWrap = mdSec.getMdWrap();
           String type = mdWrap.getMIMETYPE();
           Element element = mdWrap.getXmlData().get(0);
           exporter.addDescriptiveMD(type,element);           
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
                exporter.addPackageFile(packageFile,"ORIGINAL",false);
            }
        }
        exporter.write(out);
    }

    @Override
    public ArrayList<MdSec> getMetadata(Mets mets) {
        ArrayList<MdSec>metadata = new ArrayList<MdSec>();
        
        metadata.addAll(mets.getDmdSec());        
        
        return metadata;
    }
}

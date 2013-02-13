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
import org.apache.commons.vfs2.FileObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.BagitMetsValidationException;
import ugent.bagger.helper.FUtils;

/**
 *
 * @author nicolas
 */
public class ExporterDSpaceMetsArchive2 extends Exporter{       
    protected MetadataConverter metadataConverter;
    public static String [] noRootElement = {"dc"};
    
    @Override
    public void export(MetsBag metsBag,Mets mets,OutputStream out) 
            throws                 
                BagitMetsValidationException,
                IOException, 
                UnsupportedEncodingException, 
                DatatypeConfigurationException,
                ParserConfigurationException,
                TransformerException,
                SAXException,
                FileNotFoundException,
                ParseException,
                Exception
    {           
        File file = metsBag.getFile();        
        
        String name = file.getName();
        if(!file.isDirectory()){
            int pos = name.lastIndexOf('.');
            name = pos >= 0 ? name.substring(0,pos) : name;            
        }
        
        
        //validate metsBag and mets
        validate(metsBag,mets);        
        
        //retrieve metadata
        ArrayList<MdSec>metadata = getMetadata(mets);        
        
        //create export
        DSpaceSIPMets2 exporter = new DSpaceSIPMets2();                
        
        for(MdSec mdSec:metadata){
           MdWrap mdWrap = mdSec.getMdWrap();
           String type = mdWrap.getMDTYPE() == MdSec.MDTYPE.OTHER ? mdWrap.getOTHERMDTYPE() : mdWrap.getMDTYPE().toString();
           Element element = mdWrap.getXmlData().get(0);
           
           boolean rootForbidden = false;
           
           for(String rootName:noRootElement){
               System.out.println(rootName+" == "+element.getLocalName());
               if(rootName.toLowerCase().equals(element.getLocalName().toLowerCase())){
                   rootForbidden = true;
                   break;
               }
           }
           
           if(rootForbidden){
               NodeList nodes = element.getChildNodes();
               ArrayList<Element>elements = new ArrayList<Element>();
                              
               for(int i = 0;i<nodes.getLength();i++){
                   Node node = nodes.item(i);
                   if(node.getNodeType() == Node.ELEMENT_NODE){
                       elements.add((Element)node);
                   }
                   
               }
               exporter.addDescriptiveMD(type,elements.toArray(new Element []{}));
           }else{
               exporter.addDescriptiveMD(type,element);           
           }
           
        }
        
        for(FileSec.FileGrp fileGroup:mets.getFileSec().getFileGrp()){            
            if(!fileGroup.getUse().equals("payloads")){
                continue;
            }
            for(FileSec.FileGrp.File metsFile:fileGroup.getFile()){   
                String relativePath = metsFile.getFLocat().get(0).getXlinkHREF();
                String entryString;
                String longName;
                String shortName = relativePath;
                if(!file.isDirectory()){
                    entryString = FUtils.getEntryStringFor(file.getAbsolutePath(),name+File.separator+relativePath);
                    longName = entryString;     
                }else{
                    entryString = FUtils.getEntryStringFor(file.getAbsolutePath(),relativePath);
                    longName = new File(file,relativePath).getAbsolutePath();                    
                }                                
                FileObject fobject = FUtils.resolveFile(entryString);                
                DSpaceSIPMets2.PackageFile packageFile = new DSpaceSIPMets2.PackageFileObject(
                    fobject,shortName,longName
                );
                packageFile.setMetsFile(metsFile);
                exporter.addPackageFile(packageFile,"ORIGINAL",false);                
            }
        }
        exporter.write(out);
    }

    @Override
    public ArrayList<MdSec> getMetadata(Mets mets) {
        return (ArrayList<MdSec>) mets.getDmdSec();        
    }

    @Override
    public MetadataConverter getMetadataConverter() {
        return metadataConverter;
    }

    @Override
    public void setMetadataConverter(MetadataConverter converter) {
        this.metadataConverter = converter;
    }

    @Override
    public String getExtension() {
        return "zip";
    }
}

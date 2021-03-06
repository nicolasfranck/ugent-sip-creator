package ugent.bagger.exporters;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.MdSec;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.BagitMetsValidationException;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.XML;

/**
 *
 * @author nicolas
 */
public class ExporterDSpaceSimpleArchive extends Exporter{       
    protected MetadataConverter metadataConverter;
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
        DSpaceSIPSimpleArchive sip = new DSpaceSIPSimpleArchive();                
        
        //set metadata
        if(getMetadataConverter() != null){
            for(MdSec mdSec:getMetadata(mets)){
                Element element = mdSec.getMdWrap().getXmlData().get(0);
                Document doc = XML.createDocument();
                doc.appendChild(doc.importNode(element,true));
                Document outDoc = getMetadataConverter().convert(doc);
                if(outDoc == null){
                    continue;
                }
                String schema = outDoc.getDocumentElement().getAttribute("schema");
                
                if(schema == null || schema.isEmpty()){
                    schema = "dc";
                }
                sip.setMetadata(name,schema,outDoc);
            }
        }
        
        
        //set files
        for(FileSec.FileGrp fileGroup:mets.getFileSec().getFileGrp()){            
            if(!fileGroup.getUse().equals("payloads")){
                continue;
            }
            
            for(FileSec.FileGrp.File metsFile:fileGroup.getFile()){   
                String relativePath = metsFile.getFLocat().get(0).getXlinkHREF();
                String entryString;
                String longName;                
                if(!file.isDirectory()){
                    entryString = FUtils.getEntryStringFor(file.getAbsolutePath(),name+File.separator+relativePath);
                }else{
                    entryString = FUtils.getEntryStringFor(file.getAbsolutePath(),relativePath);                    
                }   
                longName = relativePath;                                    
                FileObject fobject = FUtils.resolveFile(entryString);                        
                DSpaceSIPSimpleArchive.PackageFile packageFile = new DSpaceSIPSimpleArchive.PackageFileObject(
                    fobject
                );
                sip.setFile(name,longName,fobject);                
            }
        }
        sip.write(out);
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

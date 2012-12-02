package ugent.bagger.bagitmets.validation;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.FileSec;
import com.anearalone.mets.FileSec.FileGrp.File.FLocat;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.NS;
import com.anearalone.mets.SharedEnums.CHECKSUMTYPE;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagit.utilities.SimpleResult;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import gov.loc.repository.bagit.verify.impl.ParallelManifestChecksumVerifier;
import gov.loc.repository.bagit.verify.impl.ValidVerifierImpl;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.validation.Schema;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ugent.bagger.exceptions.BagitMetsValidationException;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.PremisUtils;
import ugent.bagger.helper.XML;
import ugent.premis.Premis;

/**
 *
 * @author nicolas
 */
public class BagitMetsValidator {    
    public void validate(File file) throws BagitMetsValidationException{
        
        //directory?
        if(!file.isDirectory()){
            throw new BagitMetsValidationException(RESULT.BAGIT_NOT_DIRECTORY,file+" is not a directory");
        }
        //all files readable?
        ArrayList<File>notReadableFiles = FUtils.getNotReadableFiles(file);
        if(!notReadableFiles.isEmpty()){
            throw new BagitMetsValidationException(RESULT.BAGIT_FILES_NOT_READABLE,"not all files in directory "+file+" are readable");
        }
        
        MetsBag metsBag = new MetsBag(file,null);        
        
        //validate bag
        CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
        ParallelManifestChecksumVerifier manifestVerifier = new ParallelManifestChecksumVerifier();
        ValidVerifierImpl validVerifier = new ValidVerifierImpl(completeVerifier, manifestVerifier);                          
        SimpleResult result = metsBag.validateBag(validVerifier);
        if(!result.isSuccess()){
            throw new BagitMetsValidationException(RESULT.BAGIT_NOT_VALID,"directory ");
        }
        
        //mets.xml exists?
        File metsFile = new File(file,"mets.xml");
        if(!metsFile.exists()){
            throw new BagitMetsValidationException(RESULT.METS_MISSING,"mets.xml missing in directory "+file);
        }
            
        //mets.xml readable?
        if(!metsFile.canRead()){
            throw new BagitMetsValidationException(RESULT.METS_NOT_READABLE,"mets.xml is not readable");
        }
        
        Mets mets = null;
        //xml? valid mets-document?
        try{
            Document document = XML.XMLToDocument(file,false);
            String ns = document.getDocumentElement().getNamespaceURI();
            if(!ns.equals(NS.METS)){
                throw new Exception();
            }
            String schemaPath = MetsUtils.getSchemaPath(document);
            URL schemaURL = Context.getResource(schemaPath);            
            Schema schema = XML.createSchema(schemaURL);                        
            XML.validate(document,schema);
            mets = MetsUtils.readMets(file);
        }catch(Exception e){
            throw new BagitMetsValidationException(RESULT.METS_NOT_VALID,"mets.xml is not valid");
        }       
        
        //fileSec missing
        if(mets.getFileSec() == null){
            throw new BagitMetsValidationException(RESULT.METS_FILESEC_MISSING,"fileSec is missing from mets.xml");
        }
        
        //fileGrp with id 'payloads' or 'tags' missing
        FileSec.FileGrp fileGrpPayloads = null;
        FileSec.FileGrp fileGrpTags = null;
        
        for(FileSec.FileGrp fileGroup:mets.getFileSec().getFileGrp()){
            String id = fileGroup.getID();
            if(id != null && id.equals("payloads")){
                fileGrpPayloads = fileGroup;
            }else if(id != null && id.equals("tags")){
                fileGrpTags = fileGroup;
            }
        }
        if(fileGrpPayloads == null){
            throw new BagitMetsValidationException(RESULT.METS_FILEGRP_PAYLOADS_MISSING,"fileGrp with ID 'payloads' is missing from mets.xml");
        }
        if(fileGrpTags == null){
            throw new BagitMetsValidationException(RESULT.METS_FILEGRP_TAGS_MISSING,"fileGrp with ID 'tags' is missing from mets.xml");
        }
        
        if(fileGrpPayloads.getFile().isEmpty()){
            throw new BagitMetsValidationException(RESULT.METS_FILEGRP_PAYLOADS_EMPTY,"fileGrp with ID 'payloads' does not contain element of type 'file'");
        }
        if(fileGrpTags.getFile().isEmpty()){
            throw new BagitMetsValidationException(RESULT.METS_FILEGRP_TAGS_EMPTY,"fileGrp with ID 'tags' does not contain element of type 'file'");
        }
        
        for(FileSec.FileGrp.File f:fileGrpPayloads.getFile()){
            //ID
            String id = f.getID();
            if(id == null || id.isEmpty()){
                throw new BagitMetsValidationException(RESULT.METS_FILE_ID_MISSING,"element 'file' has empty attribute ID");
            }
            //mimetype missing
            String mimetype = f.getMIMETYPE();
            if(mimetype == null || mimetype.isEmpty()){
                throw new BagitMetsValidationException(RESULT.METS_FILE_MIMETYPE_MISSING,"element 'file' with attribute ID '"+id+" does not contain a MIMETYPE");
            }
            //size missing
            long size = f.getSIZE();
            if(size == 0){
                throw new BagitMetsValidationException(RESULT.METS_FILE_SIZE_NOT_VALID,"element 'file' with attribute ID '"+id+" does have valid SIZE");
            }
            //checksumtype missing
            CHECKSUMTYPE checksumType = f.getCHECKSUMTYPE();
            if(checksumType == null){
                throw new BagitMetsValidationException(RESULT.METS_FILE_CHECKSUMTYPE_MISSING,"element 'file' with attribute ID '"+id+" does have a valid CHECKSUMTYPE");
            }
            //checksumtype missing
            String checksum = f.getCHECKSUM();
            if(checksum == null || checksum.isEmpty()){
                throw new BagitMetsValidationException(RESULT.METS_FILE_CHECKSUM_MISSING,"element 'file' with attribute ID '"+id+" does have a CHECKSUM");
            }
            //FLocat
            FLocat flocat = f.getFLocat().isEmpty() ? null:f.getFLocat().get(0);
            if(flocat == null){
                throw new BagitMetsValidationException(RESULT.METS_FLOCAT_MISSING,"element 'file' with attribute ID '"+id+" does have a FLocat");
            }
            String xhref = flocat.getXlinkHREF();
            if(xhref == null || xhref.isEmpty()){
                throw new BagitMetsValidationException(RESULT.METS_FLOCAT_XHREF_MISSING,"FLocat of File with ID '"+id+"' does not have xhref link");
            }
            File pfile = new File(file,flocat.getXlinkHREF());
            if(!pfile.exists()){
                throw new BagitMetsValidationException(RESULT.METS_FLOCAT_XHREF_MISSING,"FLocat of File with ID '"+id+"' does not refer to an existing file");
            }            
        }
        //amdSec 'bagit'
        AmdSec amdSecBagit = null;
        for(AmdSec amdSec:mets.getAmdSec()){
            String id = amdSec.getID();
            if(id != null && id.equals("bagit")){
                amdSecBagit = amdSec;
                break;
            }
        }
        if(amdSecBagit == null){
            throw new BagitMetsValidationException(RESULT.METS_AMDSEC_BAGIT_MISSING,"amdSec with ID 'bagit' is missing from mets.xml");
        }
        MdSec digiprovMDBagit = null;
        for(MdSec mdSec:amdSecBagit.getDigiprovMD()){
            String id = mdSec.getID();
            if(id != null && id.equals("bagit_digiprovMD")){
                digiprovMDBagit = mdSec;
                break;
            }
        }
        if(digiprovMDBagit == null){
            throw new BagitMetsValidationException(RESULT.METS_DIGIPROVMD_BAGIT_MISSING,"digiprovMD with ID 'bagit_digiprovMD'  is missing from mets.xml");
        }
        if(!PremisUtils.isPremisMdSec(digiprovMDBagit)){
            throw new BagitMetsValidationException(RESULT.METS_DIGIPROVMD_BAGIT_PREMIS_MISSING,"digiprovMD with ID 'bagit_digiprovMD' does not have a premis record");
        }
        Premis premis = null;
        try{
            Element e = digiprovMDBagit.getMdWrap().getXmlData().get(0);
            premis = new Premis();
            premis.unmarshal(e);
        }catch(Exception e){
            
        }
    }
}

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
import java.nio.file.Files;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ugent.bagger.bagitmets.DefaultBagItMets;
import ugent.bagger.exceptions.BagFetchForbiddenException;
import ugent.bagger.exceptions.BagNoDataException;
import ugent.bagger.exceptions.BagitMetsValidationException;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.PremisUtils;
import ugent.bagger.helper.XML;
import ugent.premis.Premis;
import ugent.premis.PremisEvent;
import ugent.premis.PremisObject;

/**
 *
 * @author nicolas
 */
public class BagitMetsValidator {    
    static final Log log = LogFactory.getLog(BagitMetsValidator.class);       
    
    public ArrayList<BagitMetsValidationException> validate(File file) throws BagitMetsValidationException{
        ArrayList<BagitMetsValidationException>warnings = new ArrayList<BagitMetsValidationException>();
        
        //directory?
        if(!file.isDirectory()){
            throw new BagitMetsValidationException(RESULT.BAGIT_NOT_DIRECTORY,file+" is not a directory");
        }        
        System.out.println("file "+file+" is directory");       
        
        //all files readable?
        ArrayList<File>notReadableFiles = FUtils.getNotReadableFiles(file);
        if(!notReadableFiles.isEmpty()){
            throw new BagitMetsValidationException(RESULT.BAGIT_FILES_NOT_READABLE,"not all files in directory "+file+" are readable");
        }
        System.out.println("all files are readable");
        MetsBag metsBag = null;
        try{
            metsBag = new MetsBag(file,null);    
        }catch(BagFetchForbiddenException e){
            throw new BagitMetsValidationException(RESULT.BAGIT_FETCH_FORBIDDEN,e.getMessage());
        }catch(BagNoDataException e){
            throw new BagitMetsValidationException(RESULT.BAGIT_DATA_MISSING,e.getMessage());
        }catch(Exception e){
            throw new BagitMetsValidationException(RESULT.UNKNOWN_EXCEPTION,e.getMessage());
        }
        
        //mets.xml exists?
        File metsFile = new File(file,"mets.xml");
        if(!metsFile.exists()){
            throw new BagitMetsValidationException(RESULT.METS_MISSING,"mets.xml missing in directory "+file);
        }
        
        System.out.println(file+" contains a mets.xml");
            
        //mets.xml readable?
        if(!Files.isReadable(metsFile.toPath())){
            throw new BagitMetsValidationException(RESULT.METS_NOT_READABLE,"mets.xml is not readable");
        }
        
        System.out.println(file+" has readable mets.xml");
        
        Mets mets = null;
        //xml? valid mets-document?
        try{            
            Document document = XML.XMLToDocument(metsFile,false);
            System.out.println("xml document created");
            String ns = document.getDocumentElement().getNamespaceURI();            
            System.out.println("ns: "+ns+" <=> "+NS.METS.ns());
            if(!ns.equals(NS.METS.ns())){
                throw new Exception();
            }            
            
            /*
            String schemaPath = MetsUtils.getSchemaPath(document);           
            System.out.println("schemaPath: "+schemaPath);
            URL schemaURL = Context.getResource(schemaPath);            
            System.out.println("schemaURL: "+schemaURL);
            Schema schema = XML.createSchema(schemaURL);                        
            System.out.println("schema created");
            XML.validate(document,schema);
            System.out.println("validated against schema");*/
            
            mets = new Mets();
            mets.unmarshal(document.getDocumentElement());
            
        }catch(Exception e){
            throw new BagitMetsValidationException(RESULT.METS_NOT_VALID,"mets.xml is not valid: "+e.getMessage());
        }       
        
        System.out.println(file+" has valid mets.xml");        
        
        warnings.addAll(validate(metsBag,mets));
        
        return warnings;
    }
    public static void main(String [] args){
        ArrayList<File>files = new ArrayList<File>();
        for(File file:new File("/home/nicolas/bags").listFiles()){
            files.add(file);
        }
        BagitMetsValidator validator = new BagitMetsValidator();
        for(File file:files){
            try{
                ArrayList<BagitMetsValidationException>warnings = validator.validate(file);
                System.out.println("warning: ");
                for(BagitMetsValidationException warning:warnings){
                    System.out.println(warning);
                }
            }catch(BagitMetsValidationException e){
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    public ArrayList<BagitMetsValidationException> validate(MetsBag metsBag,Mets mets) throws BagitMetsValidationException{
        ArrayList<BagitMetsValidationException>warnings = new ArrayList<BagitMetsValidationException>();
        
        Assert.notNull(metsBag);
        Assert.notNull(mets);
        
        File file = metsBag.getFile();       
        
        //validate bag
        CompleteVerifierImpl completeVerifier = new CompleteVerifierImpl();
        ParallelManifestChecksumVerifier manifestVerifier = new ParallelManifestChecksumVerifier();
        ValidVerifierImpl validVerifier = new ValidVerifierImpl(completeVerifier, manifestVerifier);                          
        SimpleResult result = metsBag.validateBag(validVerifier);
        if(!result.isSuccess()){
            throw new BagitMetsValidationException(RESULT.BAGIT_NOT_VALID,"directory ");
        }
        System.out.println(file+" is a valid bagit");        
        
        String name = file.getName();
        if(!file.isDirectory()){
            int pos = name.lastIndexOf('.');
            name = pos >= 0 ? name.substring(0,pos) : name;
            System.out.println("name of subdirectory: "+name);
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
            
            String entry;
            if(!file.isDirectory()){
                entry = FUtils.getEntryStringFor(file.getAbsolutePath(),name+File.separator+xhref);
            }else{
                entry = FUtils.getEntryStringFor(file.getAbsolutePath(),xhref);
            }            
            
            System.out.println("checking entry '"+entry+"' in flocat");
            FileObject fobject = null;
            boolean exists = false;
            try{
                fobject = FUtils.resolveFile(entry);
                exists = fobject.exists();
            }catch(Exception e){
                log.error(e.getMessage());
                e.printStackTrace();
            }            
            if(fobject == null || !exists){
                throw new BagitMetsValidationException(RESULT.METS_FLOCAT_XHREF_MISSING,"FLocat of File with ID '"+id+"' does not refer to an existing file");
            }            
        }
        //amdSec 'bagit'
        AmdSec amdSecBagit = PremisUtils.getAmdSecBagit((ArrayList<AmdSec>)mets.getAmdSec());        
        if(amdSecBagit == null){
            throw new BagitMetsValidationException(RESULT.METS_AMDSEC_BAGIT_MISSING,"amdSec with ID 'bagit' is missing from mets.xml");
        }
        //digiprovMD 'bagit_digiprovMD'
        MdSec digiprovMDBagit = PremisUtils.getPremisMdSec((ArrayList<MdSec>)amdSecBagit.getDigiprovMD());        
        if(digiprovMDBagit == null){
            throw new BagitMetsValidationException(RESULT.METS_DIGIPROVMD_BAGIT_MISSING,"digiprovMD with ID 'bagit_digiprovMD'  is missing from mets.xml");
        }
        if(!PremisUtils.isPremisMdSec(digiprovMDBagit)){
            throw new BagitMetsValidationException(RESULT.METS_DIGIPROVMD_BAGIT_PREMIS_MISSING,"digiprovMD with ID 'bagit_digiprovMD' does not have a premis record");
        }
        //premis
        Premis premis = null;
        try{
            Element e = digiprovMDBagit.getMdWrap().getXmlData().get(0);
            premis = new Premis();
            premis.unmarshal(e);
        }catch(Exception e){
            throw new BagitMetsValidationException(RESULT.METS_DIGIPROVMD_BAGIT_PREMIS_NOT_VALID,"premis document in digiprovMD contains errors: "+e.getMessage());
        }
        //object with xmlID 'bagit' and type 'representation'
        PremisObject premisObjectBagit = null;
        for(PremisObject object:premis.getObject()){
            String xmlID = object.getXmlID();
            PremisObject.PremisObjectType type = object.getType();
            
            if(
                xmlID != null && xmlID.equals("bagit") && 
                object.getType() != null && object.getType() == PremisObject.PremisObjectType.representation
            ){
                premisObjectBagit = object;
                break;
            }
        }
        if(premisObjectBagit == null){
            throw new BagitMetsValidationException(RESULT.METS_DIGIPROVMD_BAGIT_PREMIS_OBJECT_BAGIT_MISSING,"premis document in digiprovMD does not have object with xmlID 'bagit' and type 'representation'");
        }
        //event of type 'bagit' (warning)
        ArrayList<PremisEvent>events = new ArrayList<PremisEvent>();
        for(PremisEvent event:premis.getEvent()){
            String type = event.getEventType();
            if(type != null && type.equals("bagit")){
                events.add(event);
            }
        }
        if(events.isEmpty()){
            warnings.add(new BagitMetsValidationException(RESULT.METS_DIGIPROVMD_BAGIT_PREMIS_EVENT_BAGIT_MISSING,"premis document in digiprovMD does not have events of type 'bagit' (warning)"));
        }
        //dmdSec available
        if(mets.getDmdSec().isEmpty()){
            warnings.add(new BagitMetsValidationException(RESULT.METS_DMDSEC_MISSING,"mets.xml does not contain dmdSec (warning)"));
        }
        
        return warnings;
    }
}

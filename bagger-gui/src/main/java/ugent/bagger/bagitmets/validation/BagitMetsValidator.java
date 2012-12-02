package ugent.bagger.bagitmets.validation;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.NS;
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
import ugent.bagger.exceptions.BagitMetsValidationException;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.XML;

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
            
        }
    }
}

package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class BagitMetsValidationResult {
    public enum TYPE {
        //bagit is not complete
        BAGIT_NOT_COMPLETE,
        //bagit is not valid
        BAGIT_NOT_VALID,
        //invalid xml or does not conform to Mets schema
        METS_NOT_VALID,
        //fileGrp with id 'payloads' is missing
        METS_FILEGRP_PAYLOADS_MISSING,        
        //fileGrp with id 'payloads' is empty
        METS_FILEGRP_PAYLOADS_EMPTY,        
        //fileGrp with id 'tags' is missing
        METS_FILEGRP_TAGS_MISSING,        
        //fileGrp with id 'tags' is empty
        METS_FILEGRP_TAGS_EMPTY,        
        //File does not contain attribute MIMETYPE
        METS_FILE_MIMETYPE_MISSING,
        //File does not contain attribute SIZE
        METS_FILE_SIZE_MISSING,
        //attribute SIZE of File is not a number
        METS_FILE_SIZE_NOT_VALID,
        //File does not contain attribute 'CHECKSUMTYPE'
        METS_FILE_CHECKSUMTYPE_MISSING,
        //File does not contain attribute 'CHECKSUM'
        METS_FILE_CHECKSUM_MISSING,
        //File does not have a FLocat
        METS_FLOCAT_MISSING,
        //FLocat does not contain attribite xlink:href
        METS_FLOCAT_XHREF_MISSING,                
        //attribute xlink:href of FLocat does not refer to an existing file
        METS_FLOCAT_XHREF_NOT_A_FILE,        
    }
}

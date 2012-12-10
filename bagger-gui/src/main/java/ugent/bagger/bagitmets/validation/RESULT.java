/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.bagitmets.validation;

/**
 *
 * @author nicolas
 */
public enum RESULT {     
    //unknown exception
    UNKNOWN_EXCEPTION("UNKNOWN_EXCEPTION",true),
    //only directories supported!
    BAGIT_NOT_DIRECTORY("BAGIT_NOT_DIRECTORY",true),
    //directory not fully readable
    BAGIT_FILES_NOT_READABLE("BAGIT_FILES_NOT_READABLE",true),
    //fetch.txt forbidden
    BAGIT_FETCH_FORBIDDEN("BAGIT_FETCH_FORBIDDEN",true),
    //no data directory
    BAGIT_DATA_MISSING("BAGIT_DATA_MISSING",true),
    //bagit not valid
    BAGIT_NOT_VALID("BAGIT_NOT_VALID",true),
    //mets.xml not found
    METS_MISSING("METS_NOT_VALID",true),
    //mets.xml not readable
    METS_NOT_READABLE("METS_NOT_READABLE",true),
    //invalid xml or does not conform to Mets schema
    METS_NOT_VALID("METS_NOT_VALID",true),
    //fileSec missing
    METS_FILESEC_MISSING("METS_FILESEC_MISSING",true),     
    //fileGrp with id 'payloads' is missing
    METS_FILEGRP_PAYLOADS_MISSING("METS_FILEGRP_PAYLOADS_MISSING",true),        
    //fileGrp with id 'payloads' is empty
    METS_FILEGRP_PAYLOADS_EMPTY("METS_FILEGRP_PAYLOADS_EMPTY",true),        
    //fileGrp with id 'tags' is missing
    METS_FILEGRP_TAGS_MISSING("METS_FILEGRP_TAGS_MISSING",true),        
    //fileGrp with id 'tags' is empty
    METS_FILEGRP_TAGS_EMPTY("METS_FILEGRP_TAGS_EMPTY",true),        
    //File does not contain attribute ID
    METS_FILE_ID_MISSING("METS_FILE_ID_MISSING",true),
    //File does not contain attribute MIMETYPE
    METS_FILE_MIMETYPE_MISSING("METS_FILE_MIMETYPE_MISSING",true),
    //File does not contain attribute SIZE
    METS_FILE_SIZE_MISSING("METS_FILE_SIZE_MISSING",true),
    //attribute SIZE of File is not a number
    METS_FILE_SIZE_NOT_VALID("METS_FILE_SIZE_NOT_VALID",true),
    //File does not contain attribute 'CHECKSUMTYPE'
    METS_FILE_CHECKSUMTYPE_MISSING("METS_FILE_CHECKSUMTYPE_MISSING",true),
    //File does not contain attribute 'CHECKSUM'
    METS_FILE_CHECKSUM_MISSING("METS_FILE_CHECKSUM_MISSING",true),
    //File does not have a FLocat
    METS_FLOCAT_MISSING("METS_FLOCAT_MISSING",true),
    //FLocat does not contain attribite xlink:href
    METS_FLOCAT_XHREF_MISSING("METS_FLOCAT_XHREF_MISSING",true),                
    //attribute xlink:href of FLocat does not refer to an existing file
    METS_FLOCAT_XHREF_NOT_A_FILE("METS_FLOCAT_XHREF_NOT_A_FILE",true),
    //amdSec with id 'bagit' missing
    METS_AMDSEC_BAGIT_MISSING("METS_AMDSEC_BAGIT_MISSING",true),
    //digiprovMD with id 'bagit' missing
    METS_DIGIPROVMD_BAGIT_MISSING("METS_DIGIPROVMD_BAGIT_MISSING",true),
    //digiprovMD does not contain premis record
    METS_DIGIPROVMD_BAGIT_PREMIS_MISSING("METS_DIGIPROVMD_BAGIT_PREMIS_MISSING",true),
    //digiprovMD does not contain a valid premis record (according to premis schema)
    METS_DIGIPROVMD_BAGIT_PREMIS_NOT_VALID("METS_DIGIPROVMD_BAGIT_PREMIS_NOT_VALID",true),
    //premis record does not have object 'representation' with xmlID 'bagit'
    METS_DIGIPROVMD_BAGIT_PREMIS_OBJECT_BAGIT_MISSING("METS_DIGIPROVMD_BAGIT_PREMIS_OBJECT_BAGIT_MISSING",true),
    //object 'representation' with xmlID 'bagit' contains errors
    METS_DIGIPROVMD_BAGIT_PREMIS_OBJECT_BAGIT_NOT_VALID("METS_DIGIPROVMD_BAGIT_PREMIS_OBJECT_BAGIT_NOT_VALID",true),
    //warning: premis record does not have events of type 'bagit'
    METS_DIGIPROVMD_BAGIT_PREMIS_EVENT_BAGIT_MISSING("METS_DIGIPROVMD_BAGIT_PREMIS_EVENT_BAGIT_MISSING",false),
    //warning: events of type 'bagit' contain error
    METS_DIGIPROVMD_BAGIT_PREMIS_EVENT_BAGIT_NOT_VALID("METS_DIGIPROVMD_BAGIT_PREMIS_EVENT_BAGIT_NOT_VALID",false),
    //warning: no dmdSec
    METS_DMDSEC_MISSING("METS_DMDSEC_MISSING",false);
    
    private String type;
    private boolean error;
    private RESULT(String type,boolean error){
        this.type = type;
        this.error = error;
    }
    public String getType() {
        return type;
    }
    public boolean isError() {
        return error;
    }    
}

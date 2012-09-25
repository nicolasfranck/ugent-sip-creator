package gov.loc.repository.bagger.bag.impl;

import com.anearalone.mets.Mets;
import com.anearalone.mets.SharedEnums;
import com.anearalone.mets.SharedEnums.CHECKSUMTYPE;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.Manifest;

/**
 *
 * @author nicolas
 */
public abstract class MetsCreator {
    public static SharedEnums.CHECKSUMTYPE resolveChecksumType(String algorithm){
        if(algorithm.equalsIgnoreCase(Manifest.Algorithm.MD5.bagItAlgorithm)) {
            return CHECKSUMTYPE.MD_5;
        }else if(algorithm.equalsIgnoreCase(Manifest.Algorithm.SHA1.bagItAlgorithm)) {
            return CHECKSUMTYPE.SHA_1;
        }else if(algorithm.equalsIgnoreCase(Manifest.Algorithm.SHA256.bagItAlgorithm)) {
            return CHECKSUMTYPE.SHA_256;
        }else if(algorithm.equalsIgnoreCase(Manifest.Algorithm.SHA512.bagItAlgorithm)) {
            return CHECKSUMTYPE.SHA_512;
        }else{
            return CHECKSUMTYPE.MD_5;
        }
    }
    public abstract Mets create(Bag bag);
}

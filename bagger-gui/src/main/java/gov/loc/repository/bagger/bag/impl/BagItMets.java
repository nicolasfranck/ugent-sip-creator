package gov.loc.repository.bagger.bag.impl;

import com.anearalone.mets.Mets;
import com.anearalone.mets.SharedEnums;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.Manifest;

/**
 *
 * @author nicolas
 */
public abstract class BagItMets {
    public static SharedEnums.CHECKSUMTYPE resolveChecksumType(String algorithm){
        if(algorithm.equalsIgnoreCase(Manifest.Algorithm.MD5.bagItAlgorithm)) {
            return SharedEnums.CHECKSUMTYPE.MD_5;
        }else if(algorithm.equalsIgnoreCase(Manifest.Algorithm.SHA1.bagItAlgorithm)) {
            return SharedEnums.CHECKSUMTYPE.SHA_1;
        }else if(algorithm.equalsIgnoreCase(Manifest.Algorithm.SHA256.bagItAlgorithm)) {
            return SharedEnums.CHECKSUMTYPE.SHA_256;
        }else if(algorithm.equalsIgnoreCase(Manifest.Algorithm.SHA512.bagItAlgorithm)) {
            return SharedEnums.CHECKSUMTYPE.SHA_512;
        }else{
            return SharedEnums.CHECKSUMTYPE.MD_5;
        }
    }
    //open een mets-document die als tagfile is opgeslagen in een bagit
    public abstract Mets onOpenBag(MetsBag bag);
    //na creatie van payload-manifest moet het mets-document aangepast worden
    //nadien creëert MetsBag de manifest voor de tagfiles
    protected abstract Mets onSaveBag(MetsBag bag,Mets mets);        
}

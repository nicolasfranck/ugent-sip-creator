/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.loc.repository.bagger.bag.impl;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.SharedEnums;
import com.anearalone.mets.StructMap;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import java.util.ArrayList;

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
    public abstract Mets onOpenBag(Bag bag);
    //na creatie van payload-manifest moet het mets-document aangepast worden
    //nadien creëert MetsBag de manifest voor de tagfiles
    protected abstract Mets onSaveBag(Bag bag,Mets mets);    
    protected abstract FileSec createFileSecPayloads(ArrayList<BagFile>payloads);
    protected abstract FileSec createFileSecTagfiles(ArrayList<BagFile>tagfiles);
    protected abstract StructMap createStructMapPayloads(ArrayList<BagFile>payloads);
    protected abstract StructMap createStructMapTagfiles(ArrayList<BagFile>tagfiles);
}

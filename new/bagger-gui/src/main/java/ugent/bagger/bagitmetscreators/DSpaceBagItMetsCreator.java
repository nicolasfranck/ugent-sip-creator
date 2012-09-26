package ugent.bagger.bagitmetscreators;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.LocatorElement;
import com.anearalone.mets.Mets;
import com.anearalone.mets.SharedEnums;
import com.anearalone.mets.StructMap;
import gov.loc.repository.bagger.bag.impl.BagItMetsCreator;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.tree.DefaultMutableTreeNode;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;

/**
 *
 * @author nicolas
 */
public class DSpaceBagItMetsCreator extends BagItMetsCreator{
    private Mets mets;
    public DSpaceBagItMetsCreator(Mets mets){
        assert(mets != null);
        this.mets = mets;
    }
    @Override
    public Mets create(Bag bag) {

        BagView bagView = BagView.getInstance();
        DefaultBag defaultBag = bagView.getBag();

        Manifest.Algorithm tagManifestAlg = DefaultBag.resolveAlgorithm(defaultBag.getTagManifestAlgorithm());            
        SharedEnums.CHECKSUMTYPE tagManifestChecksumType = resolveChecksumType(defaultBag.getTagManifestAlgorithm());                                           

        Manifest.Algorithm payloadManifestAlg = DefaultBag.resolveAlgorithm(defaultBag.getPayloadManifestAlgorithm());            
        SharedEnums.CHECKSUMTYPE payloadManifestChecksumType = resolveChecksumType(defaultBag.getPayloadManifestAlgorithm());             

        Mets mets = null;
        try{       

            Manifest payloadManifest = bag.getPayloadManifest(payloadManifestAlg);
            Manifest tagfileManifest = bag.getTagManifest(tagManifestAlg);
            mets = bagView.getInfoInputPane().getBagInfoInputPane().getMets();   

            //fileSec
            FileSec fileSec = mets.getFileSec();
            if(fileSec == null){
                fileSec = new FileSec();
                mets.setFileSec(fileSec);
            }                       
            List<FileSec.FileGrp> fileGroups = fileSec.getFileGrp();            
            fileGroups.clear();
            FileSec.FileGrp fileGroup = new FileSec.FileGrp();            
            fileGroup.setID("CONTENT");
            fileGroup.setUse("CONTENT");
            List<FileSec.FileGrp.File>files = fileGroup.getFile();            

            Pattern ncname_forbidden = Pattern.compile("[^a-zA-Z0-9_-]");                
            
            for(BagFile bagFile:bag.getPayload()){

                //xsd:ID moet NCName zijn                    
                String fileId = ncname_forbidden.matcher(bagFile.getFilepath()).replaceAll("-");                    
                //String fileId = bagFile.getFilepath().replaceAll("/","-");
                FileSec.FileGrp.File metsFile = new FileSec.FileGrp.File(fileId);                                                            
                metsFile.setSIZE(bagFile.getSize());                    

                String mimeType;                
                File payloadFile = new File(bagView.getBag().getRootDir(),bagFile.getFilepath());                

                if(payloadFile.isFile()){                    
                    mimeType = FUtils.getMimeType(payloadFile);
                }else{                                            
                    //indien bag geen directory is, maar een tar of zip
                    mimeType = FUtils.getMimeType(bagFile.newInputStream());
                }

                metsFile.setMIMETYPE(mimeType);                                                                    
                String checksumFile = payloadManifest.get(bagFile.getFilepath());         

                metsFile.setCHECKSUM(checksumFile);
                metsFile.setCHECKSUMTYPE(payloadManifestChecksumType);                                                     

                FileSec.FileGrp.File.FLocat flocat = new FileSec.FileGrp.File.FLocat();
                flocat.setLOCTYPE(LocatorElement.LOCTYPE.URL);
                flocat.setXlinkHREF(bagFile.getFilepath());                 
                metsFile.getFLocat().add(flocat);

                files.add(metsFile);                                      
            }
            fileGroups.add(fileGroup);

            //structMap
            DefaultMutableTreeNode rootNodePayloads = bagView.getBagPayloadTree().getParentNode();              
            DefaultMutableTreeNode rootNodeTagFiles = bagView.getBagTagFileTree().getParentNode();
            StructMap structMapPayloads = MetsUtils.toStructMap(rootNodePayloads);
            structMapPayloads.setType("BAGIT_PAYLOAD_TREE");
            StructMap structMapTagFiles = MetsUtils.toStructMap(rootNodeTagFiles);
            structMapTagFiles.setType("BAGIT_TAGFILE_TREE");

            mets.getStructMap().clear();
            mets.getStructMap().add(structMapPayloads);
            mets.getStructMap().add(structMapTagFiles);                    

        }catch(Exception e){
            e.printStackTrace();            
        }            
        return mets;
    }   
}

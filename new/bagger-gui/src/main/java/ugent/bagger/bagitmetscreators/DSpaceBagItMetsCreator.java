package ugent.bagger.bagitmetscreators;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.LocatorElement;
import com.anearalone.mets.Mets;
import com.anearalone.mets.SharedEnums;
import com.anearalone.mets.StructMap;
import com.anearalone.mets.StructMap.Div;
import com.anearalone.mets.StructMap.Div.Fptr;
import gov.loc.repository.bagger.bag.impl.BagItMetsCreator;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.swing.tree.DefaultMutableTreeNode;
import ugent.bagger.helper.DefaultMetsCallback;
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
        final HashMap<String,String> fileIdMap = new HashMap<String,String>();
        
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
            
            //group payloads
            FileSec.FileGrp payloadGroup = new FileSec.FileGrp();            
            payloadGroup.setID("PAYLOADS");
            payloadGroup.setUse("CONTENT");
            List<FileSec.FileGrp.File>payloadFiles = payloadGroup.getFile();            

            for(BagFile bagFile:bag.getPayload()){

                //xsd:ID moet NCName zijn                    
                String fileId = UUID.randomUUID().toString();                        
                
                fileIdMap.put(bagFile.getFilepath(),fileId);                
                
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

                payloadFiles.add(metsFile);                                      
            }
            fileGroups.add(payloadGroup);
            
            //group tagfiles
            FileSec.FileGrp tagFileGroup = new FileSec.FileGrp();            
            tagFileGroup.setID("TAGFILES");
            tagFileGroup.setUse("TAGS");
            List<FileSec.FileGrp.File>tagFiles = tagFileGroup.getFile();
            
            for(BagFile bagFile:bag.getTags()){
                
                if(bagFile.getFilepath().equals("mets.xml")){
                    //je kan niet verwijzen naar jezelf
                    continue;
                }
                                   
                String fileId = UUID.randomUUID().toString();                        
                
                fileIdMap.put(bagFile.getFilepath(),fileId);
                
                System.out.println("mapping "+bagFile.getFilepath()+" => "+fileId);
                
                FileSec.FileGrp.File metsFile = new FileSec.FileGrp.File(fileId);                                                            
                metsFile.setSIZE(bagFile.getSize());                    

                String mimeType;                
                File tagFile = new File(bagView.getBag().getRootDir(),bagFile.getFilepath());                

                if(tagFile.isFile()){                    
                    mimeType = FUtils.getMimeType(tagFile);
                }else{                                            
                    //indien bag geen directory is, maar een tar of zip
                    mimeType = FUtils.getMimeType(bagFile.newInputStream());
                }

                metsFile.setMIMETYPE(mimeType);                                                                    
                String checksumFile = tagfileManifest.get(bagFile.getFilepath());         

                metsFile.setCHECKSUM(checksumFile);
                metsFile.setCHECKSUMTYPE(tagManifestChecksumType);                                                     

                FileSec.FileGrp.File.FLocat flocat = new FileSec.FileGrp.File.FLocat();
                flocat.setLOCTYPE(LocatorElement.LOCTYPE.URL);
                flocat.setXlinkHREF(bagFile.getFilepath());                 
                metsFile.getFLocat().add(flocat);

                tagFiles.add(metsFile);  
            }
            
            fileGroups.add(tagFileGroup);
            

            //structMap
            DefaultMutableTreeNode rootNodePayloads = bagView.getBagPayloadTree().getParentNode();              
            DefaultMutableTreeNode rootNodeTagFiles = bagView.getBagTagFileTree().getParentNode();            
            //StructMap structMapPayloads = MetsUtils.toStructMap(rootNodePayloads);
            
            StructMap structMapPayloads = MetsUtils.toStructMap(rootNodePayloads,new DefaultMetsCallback(){
                @Override
                public void onCreateDiv(Div div){
                    for(int i = 0;i<div.getFptr().size();i++){
                        Fptr filePointer = div.getFptr().get(i);
                        System.out.println("looking for key "+filePointer.getFILEID());
                        String fileId = filePointer.getFILEID();     
                        if(fileIdMap.containsKey(fileId)){
                            System.out.println("found: "+fileIdMap.get(fileId));
                            filePointer.setFILEID(fileIdMap.get(fileId));                        
                        }                        
                    }
                }
            });
            
            structMapPayloads.setType("BAGIT_PAYLOAD_TREE");
            //StructMap structMapTagFiles = MetsUtils.toStructMap(rootNodeTagFiles);            
            
            StructMap structMapTagFiles = MetsUtils.toStructMap(rootNodeTagFiles,new DefaultMetsCallback(){
                @Override
                public void onCreateDiv(Div div){
                    int indexMetsXML = -1;
                    for(int i = 0;i<div.getFptr().size();i++){
                        Fptr filePointer = div.getFptr().get(i);
                        String fileId = filePointer.getFILEID();
                        if(fileId.compareTo("mets.xml") == 0){
                            indexMetsXML = i;
                        }else if(fileIdMap.containsKey(fileId)){
                            filePointer.setFILEID(fileIdMap.get(fileId));
                        }
                    }
                    if(indexMetsXML >= 0){
                        div.getFptr().remove(indexMetsXML);
                    }
                }
            });
            
            structMapTagFiles.setType("BAGIT_TAGFILE_TREE");
            
            //reset FILEID
            //resetDiv(structMapPayloads.getDiv(),fileIdMap);
            //resetDiv(structMapTagFiles.getDiv(),fileIdMap);
            
            

            mets.getStructMap().clear();
            mets.getStructMap().add(structMapPayloads);
            mets.getStructMap().add(structMapTagFiles);                    

        }catch(Exception e){
            e.printStackTrace();            
        }            
        return mets;
    }   
    public static void resetDiv(Div div,HashMap<String,String>fileIdMap){
        int indexMetsXML = -1;
        for(int i = 0;i<div.getFptr().size();i++){
            Fptr filePointer = div.getFptr().get(i);
            String fileId = filePointer.getFILEID();
            if(fileId.compareTo("mets.xml") == 0){
                indexMetsXML = i;
            }else if(fileIdMap.containsKey(fileId)){
                filePointer.setFILEID(fileIdMap.get(fileId));
            }
        }
        if(indexMetsXML >= 0){
            div.getFptr().remove(indexMetsXML);
        }
        for(Div d:div.getDiv()){
            resetDiv(d,fileIdMap);
        }
    }
}
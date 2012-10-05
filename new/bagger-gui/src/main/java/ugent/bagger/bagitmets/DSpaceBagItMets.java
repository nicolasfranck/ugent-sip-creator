package ugent.bagger.bagitmets;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.LocatorElement;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.SharedEnums;
import com.anearalone.mets.StructMap;
import com.anearalone.mets.StructMap.Div;
import com.anearalone.mets.StructMap.Div.Fptr;
import gov.loc.repository.bagger.bag.impl.BagItMets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.DateUtils;
import ugent.bagger.helper.DefaultMetsCallback;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;

/**
 *
 * @author nicolas
 */
public class DSpaceBagItMets extends BagItMets{
    private static final Log log = LogFactory.getLog(DSpaceBagItMets.class);
    @Override
    public Mets onOpenBag(Bag bag) {
        String pathMets;            
        Mets mets = null;  
        BagView bagView = BagView.getInstance();
        
        if(bagView.getBag().getSerialMode() != DefaultBag.NO_MODE){            
            pathMets = FUtils.getEntryStringFor(bag.getFile().getAbsolutePath(),bagView.getBag().getName()+"/mets.xml");
        }else{
            pathMets = FUtils.getEntryStringFor(bag.getFile().getAbsolutePath(),"mets.xml");            
        }        
        try{
            mets = MetsUtils.readMets(FUtils.getInputStreamFor(pathMets));            
        }catch(Exception e){      
            e.printStackTrace();            
            log.debug(e.getMessage());            
        }
        if(mets == null){            
            mets = new Mets();
        }
        return mets;
    }

    @Override
    public Mets onSaveBag(Bag bag, Mets mets) {
        BagView bagView = BagView.getInstance();
        DefaultBag defaultBag = bagView.getBag();

        Manifest.Algorithm tagManifestAlg = DefaultBag.resolveAlgorithm(defaultBag.getTagManifestAlgorithm());            
        SharedEnums.CHECKSUMTYPE tagManifestChecksumType = resolveChecksumType(defaultBag.getTagManifestAlgorithm());                                           

        Manifest.Algorithm payloadManifestAlg = DefaultBag.resolveAlgorithm(defaultBag.getPayloadManifestAlgorithm());            
        SharedEnums.CHECKSUMTYPE payloadManifestChecksumType = resolveChecksumType(defaultBag.getPayloadManifestAlgorithm());             
        
        final HashMap<String,String> fileIdMap = new HashMap<String,String>();
        
        try{       

            Manifest payloadManifest = bag.getPayloadManifest(payloadManifestAlg);
            Manifest tagfileManifest = bag.getTagManifest(tagManifestAlg);            

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
            payloadGroup.setID("Payloads");
            payloadGroup.setUse("Content");
            List<FileSec.FileGrp.File>payloadFiles = payloadGroup.getFile();            

            for(BagFile bagFile:bag.getPayload()){

                //xsd:ID moet NCName zijn                    
                String fileId = UUID.randomUUID().toString();                        
                
                //houd mapping filepath <=> fileid bij
                fileIdMap.put(bagFile.getFilepath(),fileId);                                
                
                //SIZE
                FileSec.FileGrp.File metsFile = new FileSec.FileGrp.File(fileId);                                                            
                metsFile.setSIZE(bagFile.getSize());                    

                //MIMETYPE
                String mimeType;                
                File rootDir = bagView.getBag().getRootDir();
                File payloadFile = new File(rootDir,bagFile.getFilepath());                

                if(payloadFile.isFile()){                    
                    mimeType = FUtils.getMimeType(payloadFile);
                }else{                                            
                    //indien bag geen directory is, maar een tar of zip
                    mimeType = FUtils.getMimeType(bagFile.newInputStream());
                }

                metsFile.setMIMETYPE(mimeType);                                                                    
                String checksumFile = payloadManifest.get(bagFile.getFilepath());                         
                
                //CHECKSUM en CHECKSUMTYPE
                metsFile.setCHECKSUM(checksumFile);
                metsFile.setCHECKSUMTYPE(payloadManifestChecksumType);                                                                            
                
                //CREATED => als map: dateCreated van het bestand, anders van de zip/tar
                //dateCreated niet op alle systemen ondersteund, dus we nemen lastModified
                try{
                    
                    if(payloadFile.isFile()){                    
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(payloadFile.lastModified())));                    
                    }else if(rootDir.exists()){                        
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(rootDir.lastModified())));;                        
                    }else{
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender());;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                //FLocat
                FileSec.FileGrp.File.FLocat flocat = new FileSec.FileGrp.File.FLocat();
                flocat.setLOCTYPE(LocatorElement.LOCTYPE.URL);
                flocat.setXlinkHREF(bagFile.getFilepath());                 
                flocat.setXlinkTitle(bagFile.getFilepath().replaceFirst("data/",""));
                
                metsFile.getFLocat().add(flocat);
                
                payloadFiles.add(metsFile);                                      
            }
            fileGroups.add(payloadGroup);
            
            //group tagfiles
            FileSec.FileGrp tagFileGroup = new FileSec.FileGrp();            
            tagFileGroup.setID("Tagfiles");
            tagFileGroup.setUse("Tags");
            List<FileSec.FileGrp.File>tagFiles = tagFileGroup.getFile();
            
            for(BagFile bagFile:bag.getTags()){
                
                if(bagFile.getFilepath().equals("mets.xml")){
                    //je kan niet verwijzen naar jezelf
                    continue;
                }else if(bagFile.getFilepath().startsWith("tagmanifest-")){
                    //je kan geen checksum bijhouden van iets dat straks zal wijzigen
                }
                                   
                String fileId = UUID.randomUUID().toString();                        
                
                fileIdMap.put(bagFile.getFilepath(),fileId);                
                
                //SIZE
                FileSec.FileGrp.File metsFile = new FileSec.FileGrp.File(fileId);                                                            
                metsFile.setSIZE(bagFile.getSize());                    

                //MIMETYPE
                String mimeType; 
                File rootDir = bagView.getBag().getRootDir();
                File tagFile = new File(rootDir,bagFile.getFilepath());                

                if(tagFile.isFile()){                    
                    mimeType = FUtils.getMimeType(tagFile);
                }else{                                            
                    //indien bag geen directory is, maar een tar of zip
                    mimeType = FUtils.getMimeType(bagFile.newInputStream());
                }

                metsFile.setMIMETYPE(mimeType);                                                                    
                String checksumFile = tagfileManifest.get(bagFile.getFilepath());         

                //CHECKSUM en CHECKSUMTYPE
                metsFile.setCHECKSUM(checksumFile);
                metsFile.setCHECKSUMTYPE(tagManifestChecksumType);                                                     
                
                //CREATED => als map: dateCreated van het bestand, anders van de zip/tar
                //dateCreated niet op alle systemen ondersteund, dus we nemen lastModified
                try{
                    if(tagFile.isFile()){
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(tagFile.lastModified())));
                    }else if(rootDir.exists()){
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(rootDir.lastModified())));;
                    }else{                     
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender());;                    
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                FileSec.FileGrp.File.FLocat flocat = new FileSec.FileGrp.File.FLocat();
                flocat.setLOCTYPE(LocatorElement.LOCTYPE.URL);
                flocat.setXlinkHREF(bagFile.getFilepath());                 
                metsFile.getFLocat().add(flocat);

                tagFiles.add(metsFile);  
            }
            
            fileGroups.add(tagFileGroup);            

            //structMaps
            DefaultMutableTreeNode rootNodePayloads = bagView.getBagPayloadTree().getParentNode();              
            DefaultMutableTreeNode rootNodeTagFiles = bagView.getBagTagFileTree().getParentNode();            
            
            //structmap payloads
            StructMap structMapPayloads = MetsUtils.toStructMap(rootNodePayloads,new DefaultMetsCallback(){
                @Override
                public void onCreateDiv(Div div,DefaultMutableTreeNode node){                   
                    for(int i = 0;i<div.getFptr().size();i++){                        
                        Fptr filePointer = div.getFptr().get(i);                                                                                           
                        String fileId = fileIdMap.get(filePointer.getFILEID());                        
                        if(fileId != null){
                            filePointer.setFILEID(fileId);
                        }                        
                    }                   
                }
            });            
            structMapPayloads.setType("BAGIT_PAYLOAD_TREE");             
            
            //structmap tagfiles            
            StructMap structMapTagFiles = MetsUtils.toStructMap(rootNodeTagFiles,new DefaultMetsCallback(){
                @Override
                public void onCreateDiv(Div div,DefaultMutableTreeNode node){
                    int indexMetsXML = -1;
                    int indexTagmanifest = -1;
                    for(int i = 0;i<div.getFptr().size();i++){                        
                        Fptr filePointer = div.getFptr().get(i);                                               
                        String fileId = filePointer.getFILEID().replaceFirst("^data\\/","");                                                     
                       
                        if(fileId.compareTo("mets.xml") == 0){
                            indexMetsXML = i;
                        }else if(fileId.startsWith("tagmanifest-")){
                            indexTagmanifest = i;
                        }else if(fileIdMap.containsKey(fileId)){                            
                            filePointer.setFILEID(fileIdMap.get(fileId));
                        }
                    }
                    if(indexMetsXML >= 0){
                        div.getFptr().remove(indexMetsXML);
                    }                    
                    if(indexTagmanifest >= 0){
                        div.getFptr().remove(indexTagmanifest);
                    }
                   
                }
            });            
            structMapTagFiles.setType("BAGIT_TAGFILE_TREE");            
        
            mets.getStructMap().clear();
            mets.getStructMap().add(structMapPayloads);
            mets.getStructMap().add(structMapTagFiles);
            
            //structMap payloads moet verwijzingen bevatten naar metadata voor het volledige dspace item            
            List<String>dmdList = structMapPayloads.getDiv().getDMDID();
            for(MdSec mdSec:mets.getDmdSec()){
                structMapPayloads.getDiv().getDMDID().add(mdSec.getID());
            }

        }catch(Exception e){
            e.printStackTrace();            
        }            
        return mets;
    }
}
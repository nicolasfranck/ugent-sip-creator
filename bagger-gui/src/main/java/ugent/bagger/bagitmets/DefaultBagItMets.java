package ugent.bagger.bagitmets;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.FileSec;
import com.anearalone.mets.LocatorElement;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsHdr;
import com.anearalone.mets.MetsHdr.Agent;
import com.anearalone.mets.MetsHdr.Agent.ROLE;
import com.anearalone.mets.SharedEnums;
import com.anearalone.mets.StructMap;
import com.anearalone.mets.StructMap.Div;
import com.anearalone.mets.StructMap.Div.Fptr;
import gov.loc.repository.bagger.bag.impl.BagItMets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class DefaultBagItMets extends BagItMets{
    
    private static final Log log = LogFactory.getLog(DefaultBagItMets.class);
       
    
    @Override
    public Mets onOpenBag(MetsBag metsBag) {
        Bag bag = metsBag.getBag();
        
        String pathMets;            
        Mets mets = null;          
        
        if(metsBag.getSerialMode() != MetsBag.NO_MODE){            
            pathMets = FUtils.getEntryStringFor(bag.getFile().getAbsolutePath(),metsBag.getName()+"/mets.xml");
        }else{
            pathMets = FUtils.getEntryStringFor(bag.getFile().getAbsolutePath(),"mets.xml");            
        }        
        try{
            mets = MetsUtils.readMets(FUtils.getInputStreamFor(pathMets));            
        }catch(Exception e){                              
            log.debug(e.getMessage());            
        }
        if(mets == null){            
            mets = new Mets();
        }        
        
        return mets;
    }
    private static Comparator defaultBagFileSorter =  new Comparator<BagFile>(){
        @Override
        public int compare(BagFile f1, BagFile f2){
            return f1.getFilepath().compareToIgnoreCase(f2.getFilepath());            
        }
    };

    @Override
    public Mets onSaveBag(MetsBag metsBag,Mets mets) {                
        
        Bag bag = metsBag.getBag();        
        
        //metsHdr
        MetsHdr header = mets.getMetsHdr();
        if(header == null){
            header = new MetsHdr();
            mets.setMetsHdr(header);            
            try{
                header.setCREATEDATE(DateUtils.DateToGregorianCalender());                           
            }catch(Exception e){
                log.debug(e.getMessage());                
            }
        }
        try{
            header.setLASTMODDATE(DateUtils.DateToGregorianCalender());            
        }catch(Exception e){
            log.debug(e.getMessage());            
        } 
        header.setID(MetsUtils.createID());        
        
        //agents
        Agent agent = new MetsHdr.Agent(ROLE.DISSEMINATOR,"SIP Creator UGENT 1.0");        
        header.getAgent().clear();
        header.getAgent().add(agent);        
        
        //manifest informatie
        Manifest.Algorithm tagManifestAlg = DefaultBag.resolveAlgorithm(metsBag.getTagManifestAlgorithm());                    
        SharedEnums.CHECKSUMTYPE tagManifestChecksumType = resolveChecksumType(metsBag.getTagManifestAlgorithm());     
        
        System.out.println("metsBag.payloadManifestAlgorithm: "+metsBag.getPayloadManifestAlgorithm());
        Manifest.Algorithm payloadManifestAlg = DefaultBag.resolveAlgorithm(metsBag.getPayloadManifestAlgorithm());            
        System.out.println("payloadManifestAlg: "+payloadManifestAlg);
        SharedEnums.CHECKSUMTYPE payloadManifestChecksumType = resolveChecksumType(metsBag.getPayloadManifestAlgorithm()); 
        
        Manifest payloadManifest = bag.getPayloadManifest(payloadManifestAlg);
        System.out.println("payloadManifest: "+payloadManifest);
        Manifest tagfileManifest = bag.getTagManifest(tagManifestAlg);                                
        System.out.println("tagfileManifest: "+tagfileManifest);
        
        //files
        final HashMap<String,String> fileIdMap = new HashMap<String,String>();
        
        try{                     
            
            Collection<BagFile>payloads = bag.getPayload();            
            Collection<BagFile>tags = bag.getTags();            
            
            System.out.println("payloads: "+payloads.size());
            System.out.println("tags: "+tags.size());

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
            payloadGroup.setID("payloads");
            payloadGroup.setUse("payloads");
            List<FileSec.FileGrp.File>payloadFiles = payloadGroup.getFile();            
        
            for(BagFile bagFile:payloads){

                System.out.println("payload "+bagFile.getFilepath());
                
                //xsd:ID moet NCName zijn                    
                String fileId = MetsUtils.createID();                        
                
                System.out.println("payload test 1");
                
                //houd mapping filepath <=> fileid bij
                fileIdMap.put(bagFile.getFilepath(),fileId);                                
                
                System.out.println("payload test 2");
                
                //SIZE
                FileSec.FileGrp.File metsFile = new FileSec.FileGrp.File(fileId);                                                                            
                metsFile.setSIZE(bagFile.getSize()); 
                
                System.out.println("payload test 3");
        
                //MIMETYPE
                String mimeType;                
                File rootDir = metsBag.getRootDir();
                File payloadFile = new File(rootDir,bagFile.getFilepath());                

                if(payloadFile.isFile()){                    
                    mimeType = FUtils.getMimeType(payloadFile);
                }else{                                            
                    //indien bag geen directory is, maar een tar of zip
                    mimeType = FUtils.getMimeType(bagFile.newInputStream());
                }
                
                System.out.println("payload test 4");

                metsFile.setMIMETYPE(mimeType);                                                                    
                
                String checksumFile = payloadManifest.get(bagFile.getFilepath());                                         
                
                //CHECKSUM en CHECKSUMTYPE
                metsFile.setCHECKSUM(checksumFile);
                metsFile.setCHECKSUMTYPE(payloadManifestChecksumType);                                                                                                          
                
                System.out.println("payload test 5");
                
                //CREATED => als map: dateCreated van het bestand, anders van de zip/tar
                //dateCreated niet op alle systemen ondersteund, dus we nemen lastModified
                try{                                          
                    if(payloadFile.isFile()){                                              
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(payloadFile.lastModified())));                    
                    }else if(rootDir != null && rootDir.exists()){                                                    
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(rootDir.lastModified())));                  
                    }else{                            
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender());
                    }                       
                }catch(Exception e){                    
                    log.debug(e.getMessage());                                        
                }    
                
                System.out.println("payload test 6");

                //FLocat
                FileSec.FileGrp.File.FLocat flocat = new FileSec.FileGrp.File.FLocat();
                flocat.setLOCTYPE(LocatorElement.LOCTYPE.URL);
                flocat.setXlinkHREF(bagFile.getFilepath());                 
                flocat.setXlinkTitle(bagFile.getFilepath());
                
                metsFile.getFLocat().add(flocat);
                
                System.out.println("payload test 7");
                
                payloadFiles.add(metsFile);                                      
            }
            fileGroups.add(payloadGroup);        
            
            
            System.out.println("common test 1");
            
            //group tagfiles
            FileSec.FileGrp tagFileGroup = new FileSec.FileGrp();            
            tagFileGroup.setID("tags");
            tagFileGroup.setUse("tags");
            List<FileSec.FileGrp.File>tagFiles = tagFileGroup.getFile();
            
            for(BagFile bagFile:tags){
                System.out.println("tag "+bagFile.getFilepath());
                
                String filePath = bagFile.getFilepath();
                
                if(filePath.equals("mets.xml")){
                    //je kan niet verwijzen naar jezelf
                    continue;
                }else if(filePath.startsWith("tagmanifest-")){
                    //je kan geen checksum bijhouden van iets dat straks zal wijzigen
                    continue;
                }
                                   
                String fileId = MetsUtils.createID();                        
                
                System.out.println("tag test 1");
                
                fileIdMap.put(filePath,fileId);                
                
                System.out.println("tag test 2");
                
                
                //SIZE
                FileSec.FileGrp.File metsFile = new FileSec.FileGrp.File(fileId);                                                            
                metsFile.setSIZE(bagFile.getSize());                    
                
                System.out.println("tag test 3");

                //MIMETYPE
                String mimeType; 
                File rootDir = metsBag.getRootDir();
                File tagFile = new File(rootDir,filePath);                
                
                System.out.println("tag test 4");

                if(tagFile.isFile()){                    
                    mimeType = FUtils.getMimeType(tagFile);
                }else{                                            
                    //indien bag geen directory is, maar een tar of zip
                    mimeType = FUtils.getMimeType(bagFile.newInputStream());
                }

                metsFile.setMIMETYPE(mimeType);                                                                    
                String checksumFile = tagfileManifest.get(filePath);         
                
                System.out.println("tag test 5");

                //CHECKSUM en CHECKSUMTYPE
                metsFile.setCHECKSUM(checksumFile);
                metsFile.setCHECKSUMTYPE(tagManifestChecksumType);                                                     
                
                //CREATED => als map: dateCreated van het bestand, anders van de zip/tar
                //dateCreated niet op alle systemen ondersteund, dus we nemen lastModified
                try{                                   
                    if(tagFile.isFile()){
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(tagFile.lastModified())));
                    }else if(rootDir != null && rootDir.exists()){
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(rootDir.lastModified())));
                    }else{                     
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender());
                    }                    
                }catch(Exception e){    
                    e.printStackTrace();
                    log.debug(e.getMessage());                    
                }
                
                System.out.println("tag test 6");
                
                FileSec.FileGrp.File.FLocat flocat = new FileSec.FileGrp.File.FLocat();                
                flocat.setLOCTYPE(LocatorElement.LOCTYPE.URL);
                flocat.setXlinkHREF(filePath);                 
                metsFile.getFLocat().add(flocat);                

                tagFiles.add(metsFile);  
            }
            
            fileGroups.add(tagFileGroup);            
            
            System.out.println("common test 2");
            
            //make node trees            
            DefaultMutableTreeNode rootNodePayloads = null;              
            DefaultMutableTreeNode rootNodeTagFiles = null;            
            
            //node tree voor payloads
            String [] payloadPaths = new String [payloads.size()];
            int i = 0;
            for(BagFile payload:payloads){            
                payloadPaths[i++] = payload.getFilepath();
            }            
            List<DefaultMutableTreeNode> listNodes = FUtils.listToStructure(payloadPaths);            
            
            if(!listNodes.isEmpty()){
                rootNodePayloads = listNodes.get(0);  
            }
            
            //node tree voor tags            
            String [] tagPaths = new String [tags.size()];
            i = 0;
            for(BagFile tag:tags){            
                tagPaths[i++] = tag.getFilepath();
            }            
            listNodes = FUtils.listToStructure(tagPaths);                        
            rootNodeTagFiles = new DefaultMutableTreeNode(".");
            for(DefaultMutableTreeNode n:listNodes){
                rootNodeTagFiles.add(n);
            }            

            //structMaps            
            
            //structmap payloads
            StructMap structMapPayloads;
            
            if(rootNodePayloads != null){
                structMapPayloads = MetsUtils.toStructMap(rootNodePayloads,new DefaultMetsCallback(){
                    @Override
                    public void onCreateDiv(Div div,DefaultMutableTreeNode node){                   
                        for(int i = 0;i<div.getFptr().size();i++){                        
                            Fptr filePointer = div.getFptr().get(i);                                                               
                            String fid = filePointer.getFILEID().replaceAll("\\\\","/");                                                                            
                            String fileId = fileIdMap.get(fid);                                                
                            if(fileId != null){                            
                                filePointer.setFILEID(fileId);
                            }           
                        }                   
                    }
                });
            }else{
                structMapPayloads = new StructMap();
            }            
                        
            structMapPayloads.setType("bagit_payload_tree");               
            
            //structmap tagfiles            
            StructMap structMapTagFiles;
            
            if(rootNodeTagFiles != null){
                structMapTagFiles = MetsUtils.toStructMap(rootNodeTagFiles,new DefaultMetsCallback(){
                    @Override
                    public void onCreateDiv(Div div,DefaultMutableTreeNode node){
                        int indexMetsXML = -1;
                        int indexTagmanifest = -1;
                        for(int i = 0;i<div.getFptr().size();i++){                        
                            Fptr filePointer = div.getFptr().get(i);                               
                            String fileId = filePointer.getFILEID().replaceFirst("^\\.\\/","").replaceFirst("^\\.\\\\","");                                                     

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
            }else{
                structMapTagFiles = new StructMap();
            }         
            
            structMapTagFiles.setType("bagit_tag_tree");            
        
            mets.getStructMap().clear();
            mets.getStructMap().add(structMapPayloads);
            mets.getStructMap().add(structMapTagFiles);
            
            //structMap payloads moet verwijzingen bevatten naar metadata voor het volledige dspace item                        
            for(MdSec mdSec:mets.getDmdSec()){
                structMapPayloads.getDiv().getDMDID().add(mdSec.getID());
            }            
            for(AmdSec amdSec:mets.getAmdSec()){
                structMapPayloads.getDiv().getDMDID().add(amdSec.getID());
            }            
            
            //Bag-Id
            ArrayList<String>listBagIds = metsBag.getInfo().getFieldMap().get("Bag-Id");
            if(!listBagIds.isEmpty()){
                mets.setOBJID(listBagIds.get(0));
            }   
         

        }catch(Exception e){
            e.printStackTrace();
            log.debug(e.getMessage());                  
        }        
        
        try{
            PremisBagitMetsAnalyser analyser = new PremisBagitMetsAnalyser();
            analyser.analyse(metsBag,mets);
        }catch(Exception e){
            e.printStackTrace();
            log.debug(e.getMessage());             
        }
        
        return mets;
    }
}
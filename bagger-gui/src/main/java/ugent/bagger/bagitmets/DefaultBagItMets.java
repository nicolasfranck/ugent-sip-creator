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
import javax.xml.datatype.XMLGregorianCalendar;
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
    
    static final Log log = LogFactory.getLog(DefaultBagItMets.class);       
    
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
            log.error(e.getMessage());            
        }
        if(mets == null){            
            mets = new Mets();
        }        
        
        return mets;
    }
    static Comparator defaultBagFileSorter =  new Comparator<BagFile>(){
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
                log.error(e.getMessage());                
            }
        }
        try{
            header.setLASTMODDATE(DateUtils.DateToGregorianCalender());            
        }catch(Exception e){
            log.error(e.getMessage());            
        } 
        header.setID(MetsUtils.createID());        
        
        //agents
        Agent agent = new MetsHdr.Agent(ROLE.DISSEMINATOR,"SIP Creator UGENT 1.0");        
        header.getAgent().clear();
        header.getAgent().add(agent);        
        
        //manifest informatie
        Manifest.Algorithm tagManifestAlg = DefaultBag.resolveAlgorithm(metsBag.getTagManifestAlgorithm());                    
        SharedEnums.CHECKSUMTYPE tagManifestChecksumType = resolveChecksumType(metsBag.getTagManifestAlgorithm());             
        
        Manifest.Algorithm payloadManifestAlg = DefaultBag.resolveAlgorithm(metsBag.getPayloadManifestAlgorithm());    
        SharedEnums.CHECKSUMTYPE payloadManifestChecksumType = resolveChecksumType(metsBag.getPayloadManifestAlgorithm()); 
        
        Manifest payloadManifest = bag.getPayloadManifest(payloadManifestAlg);        
        Manifest tagfileManifest = bag.getTagManifest(tagManifestAlg);                                        
        
        //files
        int num = 0;
        final HashMap<String,String> fileIdMap = new HashMap<String,String>();
        
        try{                     
            
            Collection<BagFile>payloads = bag.getPayload();            
            Collection<BagFile>tags = bag.getTags();     
            HashMap<String,XMLGregorianCalendar>oldDatesCreated = new HashMap<String, XMLGregorianCalendar>();
            
            //fileSec
            FileSec fileSec = mets.getFileSec();
            if(fileSec == null){
                fileSec = new FileSec();
                mets.setFileSec(fileSec);
            }   
            
            //sla oude datums van creatie op
            for(FileSec.FileGrp fileGroup:fileSec.getFileGrp()){
                if(!fileGroup.getID().equals("payloads")){
                    continue;
                }
                List<FileSec.FileGrp.File>pFiles = fileGroup.getFile();
                for(FileSec.FileGrp.File pFile:pFiles){
                    if(
                        pFile.getCREATED() != null &&
                        !pFile.getFLocat().isEmpty()
                    ){
                        System.out.println(
                            "storing "+
                            pFile.getFLocat().get(0).getXlinkHREF() +
                            " => " +
                            pFile.getCREATED()
                        );
                        oldDatesCreated.put(
                            pFile.getFLocat().get(0).getXlinkHREF(),
                            pFile.getCREATED()
                        );
                    }
                }
            }
            
            List<FileSec.FileGrp> fileGroups = fileSec.getFileGrp();            
            fileGroups.clear();            
            
            //group payloads
            FileSec.FileGrp payloadGroup = new FileSec.FileGrp();            
            payloadGroup.setID("payloads");
            payloadGroup.setUse("payloads");
            List<FileSec.FileGrp.File>payloadFiles = payloadGroup.getFile();            
        
            for(BagFile bagFile:payloads){
                
                File rootDir = metsBag.getFile();
                //payloadFile == naar data-map gekopiëerd bestand
                //newFile == oude locatie
                File payloadFile = new File(rootDir,bagFile.getFilepath());
                File newFile = metsBag.getNewEntries().get(bagFile.getFilepath());
                
                System.out.println("newFile: "+newFile);
                System.out.println("payloadFile: "+payloadFile);
                
                //xsd:ID moet NCName zijn                    
                //String fileId = MetsUtils.createID();                        
                String fileId = "DS."+(num++);
                
                
                //houd mapping filepath <=> fileid bij
                fileIdMap.put(bagFile.getFilepath(),fileId);                                
                
                //SIZE
                FileSec.FileGrp.File metsFile = new FileSec.FileGrp.File(fileId);  
                if(newFile != null){
                    metsFile.setSIZE(newFile.length()); 
                }else{
                    metsFile.setSIZE(bagFile.getSize());
                }
                
                
                //MIMETYPE
                String mimeType;                
                                
                if(newFile != null){
                    mimeType = FUtils.getMimeType(newFile);
                }else{
                    if(payloadFile.isFile()){                    
                        mimeType = FUtils.getMimeType(payloadFile);
                    }else{                                            
                        //indien bag geen directory is, maar een tar of zip
                        mimeType = FUtils.getMimeType(bagFile.newInputStream());
                    }
                }
                metsFile.setMIMETYPE(mimeType);                                                                    
                                
                //CHECKSUM en CHECKSUMTYPE
                String checksumFile = payloadManifest.get(bagFile.getFilepath());                                         
                metsFile.setCHECKSUM(checksumFile);
                metsFile.setCHECKSUMTYPE(payloadManifestChecksumType);                                                                                                          
                
                //CREATED => als map: dateCreated van het bestand, anders van de zip/tar
                //dateCreated niet op alle systemen ondersteund, dus we nemen lastModified
                try{
                    
                    //nieuwe bestanden (apart bij te houden, want payloadFile is het gekopiëerde bestand!)
                    if(newFile != null){
                        metsFile.setCREATED(
                            DateUtils.DateToGregorianCalender(
                                new Date(newFile.lastModified())
                            )
                        );
                    }
                    //oude bestanden in bestaande bag
                    else if(oldDatesCreated.containsKey(bagFile.getFilepath())){
                        metsFile.setCREATED(oldDatesCreated.get(bagFile.getFilepath()));
                    }else if(payloadFile.isFile()){                            
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(payloadFile.lastModified())));                    
                    }else if(rootDir != null && rootDir.exists()){                                                    
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(rootDir.lastModified())));                  
                    }else{                            
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender());
                    }
                    
                                           
                }catch(Exception e){                    
                    log.error(e.getMessage());                                        
                }    

                //FLocat
                FileSec.FileGrp.File.FLocat flocat = new FileSec.FileGrp.File.FLocat();
                flocat.setLOCTYPE(LocatorElement.LOCTYPE.URL);
                flocat.setXlinkHREF(bagFile.getFilepath());                 
                flocat.setXlinkTitle(bagFile.getFilepath());
                
                metsFile.getFLocat().add(flocat);
                
                payloadFiles.add(metsFile);                                      
            }
            fileGroups.add(payloadGroup);        
            
            //group tagfiles
            FileSec.FileGrp tagFileGroup = new FileSec.FileGrp();            
            tagFileGroup.setID("tags");
            tagFileGroup.setUse("tags");
            List<FileSec.FileGrp.File>tagFiles = tagFileGroup.getFile();
            
            for(BagFile bagFile:tags){
                
                String filePath = bagFile.getFilepath();
                File rootDir = metsBag.getFile();
                File tagFile = new File(rootDir,filePath);
                File newFile = metsBag.getNewEntries().get(bagFile.getFilepath());
                
                if(filePath.equals("mets.xml")){
                    //je kan niet verwijzen naar jezelf
                    continue;
                }else if(filePath.startsWith("tagmanifest-")){
                    //je kan geen checksum bijhouden van iets dat straks zal wijzigen
                    continue;
                }
                                   
                //String fileId = MetsUtils.createID();                        
                String fileId = "DS."+(num++);
                
                fileIdMap.put(filePath,fileId);                
                
                //SIZE
                FileSec.FileGrp.File metsFile = new FileSec.FileGrp.File(fileId); 
                if(newFile != null){
                    metsFile.setSIZE(newFile.length());
                }else{
                    metsFile.setSIZE(bagFile.getSize());                    
                }
                
                
                //MIMETYPE
                String mimeType; 
                                
                
                if(newFile != null){
                    mimeType = FUtils.getMimeType(newFile);
                }else{
                    if(tagFile.isFile()){                    
                        mimeType = FUtils.getMimeType(tagFile);
                    }else{                                            
                        //indien bag geen directory is, maar een tar of zip
                        mimeType = FUtils.getMimeType(bagFile.newInputStream());
                    }
                }                

                metsFile.setMIMETYPE(mimeType);                                                                    
                String checksumFile = tagfileManifest.get(filePath);         
                
                //CHECKSUM en CHECKSUMTYPE
                metsFile.setCHECKSUM(checksumFile);
                metsFile.setCHECKSUMTYPE(tagManifestChecksumType);                                               
                
                //CREATED => als map: dateCreated van het bestand, anders van de zip/tar
                //dateCreated niet op alle systemen ondersteund, dus we nemen lastModified
                try{  
                    if(newFile != null){
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(newFile.lastModified())));
                    }else if(tagFile.isFile()){
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(tagFile.lastModified())));
                    }else if(rootDir != null && rootDir.exists()){
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(rootDir.lastModified())));
                    }else{                     
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender());
                    }                    
                }catch(Exception e){                       
                    log.error(e.getMessage());                    
                }
                                
                FileSec.FileGrp.File.FLocat flocat = new FileSec.FileGrp.File.FLocat();                
                flocat.setLOCTYPE(LocatorElement.LOCTYPE.URL);
                flocat.setXlinkHREF(filePath);                 
                metsFile.getFLocat().add(flocat);                

                tagFiles.add(metsFile);  
            }
            
            fileGroups.add(tagFileGroup);            
            
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
            if(listBagIds != null && !listBagIds.isEmpty()){
                mets.setOBJID(listBagIds.get(0));
            }   
         

        }catch(Exception e){            
            log.error(e.getMessage());                  
        }        
        
        try{
            PremisBagitMetsAnalyser analyser = new PremisBagitMetsAnalyser();
            analyser.analyse(metsBag,mets);
        }catch(Exception e){            
            log.error(e.getMessage());             
        }
        
        return mets;
    }
}
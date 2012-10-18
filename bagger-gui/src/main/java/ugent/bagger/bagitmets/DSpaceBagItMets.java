package ugent.bagger.bagitmets;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.LocatorElement;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.MdSec.MdWrap;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.DateUtils;
import ugent.bagger.helper.DefaultMetsCallback;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.XML;
import ugent.bagger.helper.XSLT;

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
    private static Comparator defaultBagFileSorter =  new Comparator<BagFile>(){
        @Override
        public int compare(BagFile f1, BagFile f2){
            return f1.getFilepath().compareToIgnoreCase(f2.getFilepath());            
        }
    };

    @Override
    public Mets onSaveBag(Bag bag,Mets mets) {
        BagView bagView = BagView.getInstance();
        DefaultBag defaultBag = bagView.getBag();
        
        //metadata: controleer of er een Dublin Core record aan toegevoegd is
        int dcFound = -1;
        for(int i = 0;i< mets.getDmdSec().size();i++){
            MdSec mdSec = mets.getDmdSec().get(i);
            MdWrap mdWrap = mdSec.getMdWrap();
            if(mdWrap == null){
                continue;
            }
            Element element = mdWrap.getXmlData().get(0);
            if(element == null || element.getOwnerDocument() == null){
                continue;
            }
            Document doc = element.getOwnerDocument();
            if(
                doc.getNamespaceURI().compareTo("http://purl.org/dc/elements/1.1/") == 0 ||
                doc.getNamespaceURI().compareTo("http://www.openarchives.org/OAI/2.0/oai_dc/") == 0    
            ){
                dcFound = i;
                break;
            }
        }
        //niet gevonden? Ga op zoek naar crosswalk voor 1ste element die er een heeft => TODO!
        if(mets.getDmdSec().size() >  0 && dcFound < 0){
            HashMap<String, HashMap<String, String>> crosswalk = MetsUtils.getCrosswalk();
            String xsltPath = null;
            for(HashMap<String, String> entry:crosswalk.values()){
                if(xsltPath != null){
                    break;
                }
                Set<String>keys = entry.keySet();
                for(String key:keys){
                    if(
                        key.compareTo("http://purl.org/dc/elements/1.1/") == 0 ||
                        key.compareTo("http://www.openarchives.org/OAI/2.0/oai_dc/") == 0
                    ){
                        xsltPath = entry.get(key);                        
                        break;
                    }
                }                
            }
            if(xsltPath != null){
                try{
                    URL xsltURL = Context.getResource(xsltPath);
                    Document xsltDoc = XML.XMLToDocument(xsltURL);
                    
                }catch(Exception e){
                    e.printStackTrace();
                    log.error(e);
                }                
            }
        }

        //files
        Manifest.Algorithm tagManifestAlg = DefaultBag.resolveAlgorithm(defaultBag.getTagManifestAlgorithm());            
        SharedEnums.CHECKSUMTYPE tagManifestChecksumType = resolveChecksumType(defaultBag.getTagManifestAlgorithm());                                           

        Manifest.Algorithm payloadManifestAlg = DefaultBag.resolveAlgorithm(defaultBag.getPayloadManifestAlgorithm());            
        SharedEnums.CHECKSUMTYPE payloadManifestChecksumType = resolveChecksumType(defaultBag.getPayloadManifestAlgorithm());             
        
        final HashMap<String,String> fileIdMap = new HashMap<String,String>();
        
        try{       

            Manifest payloadManifest = bag.getPayloadManifest(payloadManifestAlg);
            Manifest tagfileManifest = bag.getTagManifest(tagManifestAlg);                     
            
            Collection<BagFile>payloads = bag.getPayload();            
            Collection<BagFile>tags = bag.getTags();            

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

            for(BagFile bagFile:payloads){

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
                    if(bagView.getMetsFileDateCreated() == MetsFileDateCreated.CURRENT_DATE){                        
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender());
                    }else{                        
                        if(payloadFile.isFile()){                                              
                            metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(payloadFile.lastModified())));                    
                        }else if(rootDir != null && rootDir.exists()){                                                    
                            metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(rootDir.lastModified())));                  
                        }else{                            
                            metsFile.setCREATED(DateUtils.DateToGregorianCalender());
                        }    
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    log.debug(e.getMessage());                    
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
            tagFileGroup.setID("Tagfiles");
            tagFileGroup.setUse("Tags");
            List<FileSec.FileGrp.File>tagFiles = tagFileGroup.getFile();
            
            for(BagFile bagFile:tags){
                
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
                    if(bagView.getMetsFileDateCreated() == MetsFileDateCreated.CURRENT_DATE){                        
                        metsFile.setCREATED(DateUtils.DateToGregorianCalender());
                    }else{                        
                        if(tagFile.isFile()){
                            metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(tagFile.lastModified())));
                        }else if(rootDir != null && rootDir.exists()){
                            metsFile.setCREATED(DateUtils.DateToGregorianCalender(new Date(rootDir.lastModified())));
                        }else{                     
                            metsFile.setCREATED(DateUtils.DateToGregorianCalender());
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    log.debug(e.getMessage());                    
                }
                
                
                FileSec.FileGrp.File.FLocat flocat = new FileSec.FileGrp.File.FLocat();
                flocat.setLOCTYPE(LocatorElement.LOCTYPE.URL);
                flocat.setXlinkHREF(bagFile.getFilepath());                 
                metsFile.getFLocat().add(flocat);

                tagFiles.add(metsFile);  
            }
            
            fileGroups.add(tagFileGroup);            
            
            //make node trees            
            DefaultMutableTreeNode rootNodePayloads;              
            DefaultMutableTreeNode rootNodeTagFiles;            
            
            String [] payloadPaths = new String [payloads.size()];
            int i = 0;
            for(BagFile payload:payloads){            
                payloadPaths[i++] = payload.getFilepath();
            }            
            List<DefaultMutableTreeNode> listNodes = FUtils.listToStructure(payloadPaths);            
            rootNodePayloads = listNodes.get(0);            
            
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
            StructMap structMapPayloads = MetsUtils.toStructMap(rootNodePayloads,new DefaultMetsCallback(){
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
            structMapPayloads.setType("BAGIT_PAYLOAD_TREE");             
            
            //structmap tagfiles            
            StructMap structMapTagFiles = MetsUtils.toStructMap(rootNodeTagFiles,new DefaultMetsCallback(){
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
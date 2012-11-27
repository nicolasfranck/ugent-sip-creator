package ugent.bagger.bagitmets;

import com.anearalone.mets.AmdSec;
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
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.ArrayUtils;
import ugent.bagger.helper.ArrayUtils.ArrayDiff;
import ugent.bagger.helper.DateUtils;
import ugent.bagger.helper.DefaultMetsCallback;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.PremisUtils;
import ugent.premis.Premis;
import ugent.premis.PremisEvent;
import ugent.premis.PremisIO;
import ugent.premis.PremisObject;
import ugent.premis.PremisObject.PremisObjectType;

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
        BagView bagView = BagView.getInstance();
        
        if(bagView.getBag().getSerialMode() != DefaultBag.NO_MODE){            
            pathMets = FUtils.getEntryStringFor(bag.getFile().getAbsolutePath(),bagView.getBag().getName()+"/mets.xml");
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
        
        BagView bagView = BagView.getInstance();
        Bag bag = metsBag.getBag();        
        
        //manifest informatie
        Manifest.Algorithm tagManifestAlg = DefaultBag.resolveAlgorithm(metsBag.getTagManifestAlgorithm());            
        SharedEnums.CHECKSUMTYPE tagManifestChecksumType = resolveChecksumType(metsBag.getTagManifestAlgorithm());         
        Manifest.Algorithm payloadManifestAlg = DefaultBag.resolveAlgorithm(metsBag.getPayloadManifestAlgorithm());            
        SharedEnums.CHECKSUMTYPE payloadManifestChecksumType = resolveChecksumType(metsBag.getPayloadManifestAlgorithm()); 
        
        Manifest payloadManifest = bag.getPayloadManifest(payloadManifestAlg);
        Manifest tagfileManifest = bag.getTagManifest(tagManifestAlg);                                
        
        System.out.println("test 1");
        //premis        
        Premis premis = metsBag.getPremis();  
        
        System.out.println("premis: "+premis);
        System.out.println("premis.object.size: "+premis.getObject().size());
                
        //object van type 'bitstream' behoren toe aan bagit
        Iterator<PremisObject>it = premis.getObject().iterator();
        while(it.hasNext()){
            PremisObject object = it.next();
            System.out.println("object: "+object);
            if(object.getType() == PremisObjectType.bitstream){
                System.out.println("deleting object "+object);
                it.remove();                
                System.out.println("object deleted!");
            }
        }   
        
        System.out.println("test 2");
        
        PremisObject pobject = new PremisObject(PremisObject.PremisObjectType.bitstream);
        pobject.setVersion("2.0");        
        pobject.setXmlID("bagit");
        
        PremisObject.PremisObjectIdentifier id = new PremisObject.PremisObjectIdentifier();
        id.setObjectIdentifierType("name");
        id.setObjectIdentifierValue(metsBag.getName());
        pobject.getObjectIdentifier().add(id);
        
        PremisObject.PremisObjectCharacteristics chars = new PremisObject.PremisObjectCharacteristics();
        chars.setCompositionLevel(0);        
        chars.setSize(0);
        
        PremisObject.PremisFormat format = new PremisObject.PremisFormat();
        format.setFormatNote("bagit");;
        
        chars.getFormat().add(format);
        pobject.getObjectCharacteristics().add(chars);
        premis.getObject().add(pobject);   
        
        System.out.println("test 3");
        
        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDate = dformat.format(new Date());        
        String dateId = formattedDate.replaceAll("[^a-zA-Z0-9]+","-");
        
        //eerste keer (indien oude bagit voor het eerst hier wordt ingeladen, dan is er nog geen eventlog)
        //en kan er bijgevolg geen verschil berekent worden
        ArrayList<PremisEvent>bagitEvents = new ArrayList<PremisEvent>();
        for(PremisEvent event:premis.getEvent()){                        
            if(event.getEventType() != null && event.getEventType().equals("bagit")){
                bagitEvents.add(event);
            }
        }
        
        System.out.println("test 4");
        
        if(bagitEvents.isEmpty()){
            System.out.println("test 'is empty'");
            
            PremisEvent ev = new PremisEvent();            
            PremisEvent.PremisEventIdentifier evid = new PremisEvent.PremisEventIdentifier();
            evid.setEventIdentifierType("dateTime");
            evid.setEventIdentifierValue(dateId);
            ev.setEventIdentifier(evid);
            ev.setXmlID(dateId);
            ev.setVersion("2.0");
            ev.setEventType("bagit");
            
            ev.setEventDateTime(formattedDate);            
            ev.setEventDetail("files added to bagit");
            
            for(String file:metsBag.getPayloadPaths()){
                PremisEvent.PremisLinkingObjectIdentifier oid = new PremisEvent.PremisLinkingObjectIdentifier();
                oid.setLinkingObjectIdentifierType("URL");
                oid.setLinkingObjectIdentifierValue(file);
                ev.getLinkingObjectIdentifier().add(oid);
            }
            for(String file:metsBag.getPayloadPaths()){
                PremisEvent.PremisLinkingObjectIdentifier oid = new PremisEvent.PremisLinkingObjectIdentifier();
                oid.setLinkingObjectIdentifierType("URL");
                oid.setLinkingObjectIdentifierValue(file);
                ev.getLinkingObjectIdentifier().add(oid);
            }
            premis.getEvent().add(ev);
        }
        //er is wel een eventLog
        else{
            
            System.out.println("test 'is not empty'");
            
            //verschil oud en nieuw berekenen
            ArrayList<String>oldFileList = metsBag.getOldFileList();
            ArrayList<String>newFileList = (ArrayList<String>) metsBag.getPayloadPaths();
            newFileList.addAll(metsBag.getTagFilePaths());

            ArrayDiff<String>diff = ArrayUtils.diff(oldFileList,newFileList);
            
            if(!diff.getAdded().isEmpty()){
                PremisEvent ev = new PremisEvent();
                PremisEvent.PremisEventIdentifier evid = new PremisEvent.PremisEventIdentifier();
                evid.setEventIdentifierType("dateTime");
                evid.setEventIdentifierValue(dateId);
                ev.setEventIdentifier(evid);
                ev.setXmlID(dateId);
                ev.setVersion("2.0");
                ev.setEventType("bagit");
                ev.setEventDateTime(formattedDate);            
                ev.setEventDetail("files added to bagit");
                for(String file:diff.getAdded()){
                    PremisEvent.PremisLinkingObjectIdentifier oid = new PremisEvent.PremisLinkingObjectIdentifier();
                    oid.setLinkingObjectIdentifierType("URL");
                    oid.setLinkingObjectIdentifierValue(file);
                    ev.getLinkingObjectIdentifier().add(oid);
                }            
                premis.getEvent().add(ev);
            }
            if(!diff.getDeleted().isEmpty()){
                PremisEvent ev = new PremisEvent();
                PremisEvent.PremisEventIdentifier evid = new PremisEvent.PremisEventIdentifier();
                evid.setEventIdentifierType("dateTime");
                evid.setEventIdentifierValue(dateId);
                ev.setEventIdentifier(evid);
                ev.setXmlID(dateId);
                ev.setVersion("2.0");
                ev.setEventType("bagit");
                ev.setEventDateTime(formattedDate);            
                ev.setEventDetail("files deleted from bagit");
                for(String file:diff.getDeleted()){
                    PremisEvent.PremisLinkingObjectIdentifier oid = new PremisEvent.PremisLinkingObjectIdentifier();
                    oid.setLinkingObjectIdentifierType("URL");
                    oid.setLinkingObjectIdentifierValue(file);
                    ev.getLinkingObjectIdentifier().add(oid);
                }            
                premis.getEvent().add(ev);
            }
        }
        
        System.out.println("test 5");
        
        try{
            AmdSec amdSec = PremisUtils.getAmdSecBagit((ArrayList<AmdSec>)mets.getAmdSec());
            if(amdSec == null){
                amdSec = new AmdSec();
                amdSec.setID("bagit");
                mets.getAmdSec().add(amdSec);
            }
            for(MdSec d:amdSec.getDigiprovMD()){
                
            }
            amdSec.getDigiprovMD().clear();
            MdSec mdSec = new MdSec("bagit_premis");
            MdWrap mdWrap = new MdWrap(MdSec.MDTYPE.PREMIS);
            mdWrap.setMDTYPEVERSION("2.0");
            mdWrap.getXmlData().add(PremisIO.toDocument(premis).getDocumentElement());
            mdSec.setMdWrap(mdWrap);
            amdSec.getDigiprovMD().add(mdSec);                     
        
            //test
            PremisIO.write(premis,new FileOutputStream(new File("/tmp/premis.xml")),true);
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //files
        final HashMap<String,String> fileIdMap = new HashMap<String,String>();
        
        try{                     
            
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
            payloadGroup.setID("payloads");
            payloadGroup.setUse("payloads");
            List<FileSec.FileGrp.File>payloadFiles = payloadGroup.getFile();            

            for(BagFile bagFile:payloads){

                //xsd:ID moet NCName zijn                    
                String fileId = MetsUtils.createID();                        
                
                //houd mapping filepath <=> fileid bij
                fileIdMap.put(bagFile.getFilepath(),fileId);                                
                
                //SIZE
                FileSec.FileGrp.File metsFile = new FileSec.FileGrp.File(fileId);                                                                            
                metsFile.setSIZE(bagFile.getSize()); 
                
                

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
            tagFileGroup.setID("tags");
            tagFileGroup.setUse("tags");
            List<FileSec.FileGrp.File>tagFiles = tagFileGroup.getFile();
            
            for(BagFile bagFile:tags){
                
                if(bagFile.getFilepath().equals("mets.xml")){
                    //je kan niet verwijzen naar jezelf
                    continue;
                }else if(bagFile.getFilepath().startsWith("tagmanifest-")){
                    //je kan geen checksum bijhouden van iets dat straks zal wijzigen
                }
                                   
                String fileId = MetsUtils.createID();                        
                
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
            
                       
            structMapTagFiles.setType("bagit_tagfile_tree");            
        
            mets.getStructMap().clear();
            mets.getStructMap().add(structMapPayloads);
            mets.getStructMap().add(structMapTagFiles);
            
            //structMap payloads moet verwijzingen bevatten naar metadata voor het volledige dspace item            
            List<String>dmdList = structMapPayloads.getDiv().getDMDID();
            for(MdSec mdSec:mets.getDmdSec()){
                structMapPayloads.getDiv().getDMDID().add(mdSec.getID());
            }
            
            //Bag-Id
            ArrayList<String>listBagIds = metsBag.getInfo().getFieldMap().get("Bag-Id");
            if(!listBagIds.isEmpty()){
                mets.setOBJID(listBagIds.get(0));
            }            

        }catch(Exception e){
            e.printStackTrace();            
        }
        
        
        return mets;
    }
}
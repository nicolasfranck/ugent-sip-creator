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
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.utilities.namevalue.NameValueReader;
import gov.loc.repository.bagit.utilities.namevalue.impl.NameValueReaderImpl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
    public static final String NAMESPACE_DC = "http://purl.org/dc/elements/1.1/";
    public static final String NAMESPACE_OAI_DC = "http://www.openarchives.org/OAI/2.0/oai_dc/";
    public static final String XSLT_DC2BAGINFO = "http://www.openarchives.org/OAI/2.0/oai_dc/";
    
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
        MetsBag defaultBag = bagView.getBag();        
        
        Manifest.Algorithm tagManifestAlg = DefaultBag.resolveAlgorithm(defaultBag.getTagManifestAlgorithm());            
        SharedEnums.CHECKSUMTYPE tagManifestChecksumType = resolveChecksumType(defaultBag.getTagManifestAlgorithm());         
        Manifest.Algorithm payloadManifestAlg = DefaultBag.resolveAlgorithm(defaultBag.getPayloadManifestAlgorithm());            
        SharedEnums.CHECKSUMTYPE payloadManifestChecksumType = resolveChecksumType(defaultBag.getPayloadManifestAlgorithm()); 
        
        Manifest payloadManifest = bag.getPayloadManifest(payloadManifestAlg);
        Manifest tagfileManifest = bag.getTagManifest(tagManifestAlg);
        
        
        //metadata: controleer of er een Dublin Core record aan toegevoegd is        
        HashMap<String, HashMap<String, String>> crosswalk = MetsUtils.getCrosswalk();
        
        //System.out.println("iterating dmdSec");
        Document dcDoc = null;
        
        
        ArrayList<Element>dcElements = new ArrayList<Element>();
        ArrayList<Element>dcCandidates = new ArrayList<Element>();
        
        for(int i = 0;i< mets.getDmdSec().size();i++){
            //System.out.println("mdSec "+i);
            MdSec mdSec = mets.getDmdSec().get(i);
            
            MdWrap mdWrap = mdSec.getMdWrap();
            if(mdWrap == null){
                continue;
            }
            //System.out.println("mdSec "+i+", mdWrap found");
            
            Element element = mdWrap.getXmlData().get(0);           
            
            if(element == null || element.getOwnerDocument() == null){
                continue;
            }            
            //System.out.println("mdSec "+i+", element found");
            
            String ns = element.getNamespaceURI();            
            //System.out.println("mdSec "+i+", ns: "+ns);
            Document doc = element.getOwnerDocument();            
            ns = (ns != null) ? ns : doc.getDocumentElement().getNamespaceURI();            
            if(ns == null){
                continue;
            }            
            //System.out.println("mdSec "+i+", ns not empty");
            //dc gevonden
            if(ns.compareTo(NAMESPACE_DC) == 0 || ns.compareTo(NAMESPACE_OAI_DC) == 0){            
                System.out.println("mdSec "+i+", dcFound!");
                dcElements.add(element);                            
            }            
            //crosswalk naar dc gevonden
            else if(crosswalk.containsKey(ns)){
                //System.out.println("mdSec "+i+", looking for crosswalk");
                String xsltPath = null;
                if(crosswalk.get(ns).containsKey(NAMESPACE_DC)){
                    xsltPath = crosswalk.get(ns).get(NAMESPACE_DC);
                }else if(crosswalk.get(ns).containsKey(NAMESPACE_OAI_DC)){
                    xsltPath = crosswalk.get(ns).get(NAMESPACE_OAI_DC);
                }                
                if(xsltPath != null){
                    dcCandidates.add(element);                  
                }
            } 
        }
        //System.out.println("num dcElements: "+dcElements.size()+", num dcCandidates: "+dcCandidates.size());
        //voeg DC toe indien nodig
        if(dcElements.size() <= 0 && dcCandidates.size() > 0){
            
            Element element = dcCandidates.get(0);
            String ns = element.getNamespaceURI();
            ns = (ns != null) ? ns : element.getOwnerDocument().getDocumentElement().getNamespaceURI();
            String xsltPath = null;
            try{
                if(crosswalk.get(ns).containsKey(NAMESPACE_DC)){
                    xsltPath = crosswalk.get(ns).get(NAMESPACE_DC);
                }else if(crosswalk.get(ns).containsKey(NAMESPACE_OAI_DC)){
                    xsltPath = crosswalk.get(ns).get(NAMESPACE_OAI_DC);
                }
                URL xsltURL = Context.getResource(xsltPath);
                //System.out.println("xsltURL: "+xsltURL);
                Document xsltDoc = XML.XMLToDocument(xsltURL);
                //System.out.println("xsltDoc created");
                //System.out.println("Doc output:");
                XML.ElementToXML(element,System.out);
                Document outDoc = XSLT.transform(element,xsltDoc);
                //System.out.println("print to outputstream");
                XML.DocumentToXML(outDoc,System.out,true);
                mets.getDmdSec().add(MetsUtils.createMdSec(outDoc));
                dcDoc = outDoc;                                        
            }catch(Exception e){
                e.printStackTrace();
                log.error(e);
            }
        }else if(dcElements.size() > 0){
            try{
                Document doc = XML.createDocument();
                Node node = doc.importNode(dcElements.get(0),true);
                doc.appendChild(node);
                dcDoc = doc;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        //schrijf naar bag-info.txt
        if(dcDoc != null){
            try{                
                ByteArrayOutputStream baginfoOut = new ByteArrayOutputStream();                
                URL xsltURL = Context.getResource(
                    MetsUtils.getBaginfoMap().get(
                        dcDoc.getDocumentElement().getNamespaceURI()
                    )
                );                
                Document xsltDoc = XML.XMLToDocument(xsltURL);                
                XSLT.transform(dcDoc,xsltDoc,baginfoOut);                
                ByteArrayInputStream baginfoIn = new ByteArrayInputStream(baginfoOut.toByteArray());
                NameValueReaderImpl reader = new NameValueReaderImpl(
                    "UTF-8",baginfoIn,"bagInfoTxt"
                );                
                while(reader.hasNext()){
                    NameValueReader.NameValue pair = reader.next();                  
                    bag.getBagInfoTxt().put(pair);                 
                } 
                Manifest tagManifest = defaultBag.getBag().getTagManifest(tagManifestAlg);
                //for(String key:tagManifest.keySet()){
                //    System.out.println("key: "+key+", value: "+tagManifest.get(key));
                //}
            }catch(Exception e){
                log.error(e);
                e.printStackTrace();
            }
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
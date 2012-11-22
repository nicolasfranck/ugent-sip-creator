package gov.loc.repository.bagger.bag.impl;

import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsWriter;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.Manifest.Algorithm;
import gov.loc.repository.bagit.utilities.MessageDigestHelper;
import gov.loc.repository.bagit.writer.Writer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import ugent.premis.Premis;

/**
 *
 * @author nicolas
 */
public final class MetsBag extends DefaultBag{
    BagItMets bagItMets;      
    Premis eventLog;
    ArrayList<String>oldFileList;

    public Premis getEventLog() {
        return eventLog;
    }
    public void setEventLog(Premis eventLog) {
        this.eventLog = eventLog;
    }    
    public ArrayList<String> getOldFileList() {
        if(oldFileList == null){
            oldFileList = new ArrayList<String>();
        }        
        return oldFileList;
    }
    public MetsBag() {          
        super();            
        initOldFileList();
    }
    public MetsBag(File rootDir, String version) {
        super(rootDir,version);        
        initOldFileList();
    }    

    public BagItMets getBagItMets() {                
        return bagItMets;
    }

    public void setBagItMets(BagItMets bagItMets) {
        this.bagItMets = bagItMets;
    }
    public void initOldFileList(){        
        oldFileList = (ArrayList<String>) getPayloadPaths();
        oldFileList.addAll(getTagFilePaths());
    }
     
    @Override
    public boolean write(Writer bw){        
        prepareBilBagInfoIfDirty();
        generateManifestFiles();
        
        HashMap<String,ArrayList<String>>map = getInfo().getFieldMap();
        for(String key:map.keySet()){
            for(String value:map.get(key)){
                System.out.println("key: "+key+", value: "+value);
            }
        }
        
        Bag bag = getBag();        
        
        Mets mets = getBagItMets().onSaveBag(
            this,
            BagView.getInstance().getInfoFormsPane().getInfoInputPane().getMets()
        );
        
        if(mets == null){
            mets = new Mets();
        }
        File tmpdir = new File(System.getProperty("java.io.tmpdir"));
        File metsFile = new File(tmpdir,"mets.xml");
        metsFile.deleteOnExit();
        
        try{
            MetsWriter metsWriter = new MetsWriter();
            metsWriter.writeToFile(mets,metsFile);
            
            try{
                removeBagFile("mets.xml");  
            }catch(Exception e){}
            addTagFile(metsFile);
            isBuildPayloadManifest(false);
            isBuildTagManifest(true);
            generateManifestFiles();
            isBuildPayloadManifest(true);
          
        }catch(Exception e){             
            log.debug(e);            
        }        
        return writeBag(bw);
    }
    public void createPreBag(File data, String version,String [] ignoreFiles) {
        super.createPreBag(data,version);
        //pre bag steeds een map, dus we kunnen prutsen naar believen
        Bag bag = getBag();  
        
        //remove ignored files
        if(ignoreFiles != null && ignoreFiles.length > 0){
            Algorithm pa = resolveAlgorithm(getPayloadManifestAlgorithm());
            Manifest payloadManifest = bag.getPayloadManifest(pa);
            for(String ignoreFile:ignoreFiles){
                log.debug("looking for ignoreFile: "+ignoreFile);
                String path = "data/"+ignoreFile;
                if(payloadManifest.containsKey(path)){
                    log.debug("removing ignoreFile "+ignoreFile+" from payload manifest");
                    payloadManifest.remove(path);
                    log.debug("removing ignoreFile "+ignoreFile+" from system");
                    File payloadFile = new File(bag.getFile(),path);
                    if(payloadFile.exists()){
                        payloadFile.delete();
                    }
                    log.debug("removing ignoreFile "+ignoreFile+" from payload list");
                    bag.removeBagFile(path);                    
                }
            }
            //write out payload manifest
            try{
                File payloadManifestFile = new File(bag.getFile(),"manifest-"+pa.bagItAlgorithm+".txt"); 
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(payloadManifestFile)));
                for (Entry<String,String>entry:payloadManifest.entrySet()){
                    //key: path, value: checksum
                    writer.println(entry.getValue()+"  "+entry.getKey());
                }
                writer.close();
            }catch(Exception e){
                log.debug(e.getMessage());                
            }            
        }
        
        Mets mets = getBagItMets().onSaveBag(
            this,
            BagView.getInstance().getInfoFormsPane().getInfoInputPane().getMets()
        );
        
        if(mets == null){
            mets = new Mets();
        }
        
        File metsFile = new File(bag.getFile(),"mets.xml");
        
        try{
            MetsWriter metsWriter = new MetsWriter();
            metsWriter.writeToFile(mets,metsFile);            
            
            //manually add checksum
            Algorithm ta = resolveAlgorithm(getTagManifestAlgorithm());
            
            
            Manifest tagManifest = bag.getTagManifest(ta);
            String checksumMets = MessageDigestHelper.generateFixity(metsFile,ta);            
            
            File tagmanifestFile = new File(bag.getFile(),"tagmanifest-"+ta.bagItAlgorithm+".txt");                       
            
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(tagmanifestFile,true)));
            writer.println(checksumMets+"  mets.xml");            
            writer.close();
            
        }catch(Exception e){              
            log.debug(e);            
        } 
    }
    @Override
    public void createPreBag(File data, String version) {        
        createPreBag(data,version,new String [] {});
    }
    public void createPreBagAddKeepFilesToEmptyFolders(File data,String version,String [] ignoreFiles){
        super.createPreBagAddKeepFilesToEmptyFolders(data,version);
        
        //pre bag steeds een map, dus we kunnen prutsen naar believen
        Bag bag = getBag();        
        
        //remove ignored files
        if(ignoreFiles != null && ignoreFiles.length > 0){
            Algorithm pa = resolveAlgorithm(getPayloadManifestAlgorithm());   
            Manifest payloadManifest = bag.getPayloadManifest(pa);
            for(String ignoreFile:ignoreFiles){
                log.debug("looking for ignoreFile: "+ignoreFile);
                String path = "data/"+ignoreFile;
                if(payloadManifest.containsKey(path)){
                    log.debug("removing ignoreFile "+ignoreFile+" from payload manifest");
                    payloadManifest.remove(path);
                    log.debug("removing ignoreFile "+ignoreFile+" from system");
                    File payloadFile = new File(bag.getFile(),path);
                    if(payloadFile.exists()){
                        payloadFile.delete();
                    }
                    log.debug("removing ignoreFile "+ignoreFile+" from payload list");
                    bag.removeBagFile(path);                    
                }
            }
            //write out payload manifest
            try{
                File payloadManifestFile = new File(bag.getFile(),"manifest-"+pa.bagItAlgorithm+".txt"); 
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(payloadManifestFile)));
                for (Entry<String,String>entry:payloadManifest.entrySet()){
                    //key: path, value: checksum
                    writer.println(entry.getValue()+"  "+entry.getKey());
                }
                writer.close();
            }catch(Exception e){
                log.debug(e.getMessage());                
            }            
        }
        
        Mets mets = getBagItMets().onSaveBag(
            this,
            BagView.getInstance().getInfoFormsPane().getInfoInputPane().getMets()
        );
        
        if(mets == null){
            mets = new Mets();
        }
        
        File metsFile = new File(bag.getFile(),"mets.xml");
        
        try{
            MetsWriter metsWriter = new MetsWriter();
            metsWriter.writeToFile(mets,metsFile);            
            
            //manually add checksum
            Algorithm ta = resolveAlgorithm(getTagManifestAlgorithm());            
            Manifest tagManifest = bag.getTagManifest(ta);
            String checksumMets = MessageDigestHelper.generateFixity(metsFile,ta);                        
            File tagmanifestFile = new File(bag.getFile(),"tagmanifest-"+ta.bagItAlgorithm+".txt");                                   
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(tagmanifestFile,true)));
            writer.println(checksumMets+"  mets.xml");            
            writer.close();
            
        }catch(Exception e){   
            log.debug(e.getMessage());                    
        } 
    }
    
    @Override
    public void createPreBagAddKeepFilesToEmptyFolders(File data,String version){
        createPreBagAddKeepFilesToEmptyFolders(data,version,new String [] {});
    }    
}
package gov.loc.repository.bagger.bag.impl;

import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsWriter;
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
import org.apache.commons.vfs2.FileSystemException;
import ugent.bagger.exceptions.BagFetchForbiddenException;
import ugent.bagger.exceptions.BagNoDataException;
import ugent.premis.Premis;

/**
 *
 * @author nicolas
 */
public final class MetsBag extends DefaultBag{
    BagItMets bagItMets;          
    Premis premis;
    ArrayList<String>oldFileList;        
    HashMap<String,File>newEntries;
    Mets mets;

    public HashMap<String, File> getNewEntries() {
        if(newEntries == null){
            newEntries = new HashMap<String, File>();                   
        }
        return newEntries;
    }

    //log of all files added to the payload (good to track the file path of the new files,
    //because these cannot be derived from BagFile
       
    public Mets getMets() {
        if(mets == null){
            mets = new Mets();
        }
        return mets;
    }
    public void setMets(Mets mets) {
        this.mets = mets;
    }   
    public Premis getPremis() {
        if(premis == null){
            premis = new Premis();
        }
        return premis;
    }
    public void setPremis(Premis premis) {
        this.premis = premis;
    }
    public ArrayList<String> getOldFileList() {
        if(oldFileList == null){
            oldFileList = new ArrayList<String>();
        }        
        return oldFileList;
    }    
    protected void init(){
        initOldFileList();       
    }
    public MetsBag() throws BagFetchForbiddenException, FileSystemException, BagNoDataException {          
        super();            
        init();
    }
    public MetsBag(File rootDir, String version) throws BagFetchForbiddenException, FileSystemException, BagNoDataException {
        super(rootDir,version);        
        init();
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
        //herschrijf bag-info.txt!
        getInfo().prepareBilBagInfo(getBag().getBagInfoTxt());
        //bereken checksums
        generateManifestFiles();
        
        setMets(getBagItMets().onSaveBag(this,getMets()));
        
        File tmpdir = new File(System.getProperty("java.io.tmpdir"));
        File metsFile = new File(tmpdir,"mets.xml");
        metsFile.deleteOnExit();
        
        try{
            MetsWriter metsWriter = new MetsWriter();
            metsWriter.writeToFile(getMets(),metsFile);
            
            try{
                removeBagFile("mets.xml");  
            }catch(Exception e){
                log.error(e.getMessage());
            }
            addTagFile(metsFile);
            isBuildPayloadManifest(false);
            isBuildTagManifest(true);
            generateManifestFiles();
            isBuildPayloadManifest(true);
            
             
          
        }catch(Exception e){             
            log.error(e.getMessage());          
        }        
        boolean writeOk = writeBag(bw);
        
        if(!isAddKeepFilesToEmptyFolders && getFile().isDirectory()){
            //check directories that are empty
            removeEmptyDirectories(new File(getFile(),"data"));           
        }
        
        return writeOk;
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
                log.error(e.getMessage());                
            }            
        }
        
        setMets(getBagItMets().onSaveBag(this,getMets()));        
        
        File metsFile = new File(bag.getFile(),"mets.xml");
        
        try{
            MetsWriter metsWriter = new MetsWriter();
            metsWriter.writeToFile(getMets(),metsFile);            
            
            //manually add checksum
            Algorithm ta = resolveAlgorithm(getTagManifestAlgorithm());            
            
            Manifest tagManifest = bag.getTagManifest(ta);
            String checksumMets = MessageDigestHelper.generateFixity(metsFile,ta);            
            
            File tagmanifestFile = new File(bag.getFile(),"tagmanifest-"+ta.bagItAlgorithm+".txt");                       
            
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(tagmanifestFile,true)));
            writer.println(checksumMets+"  mets.xml");            
            writer.close();
            
        }catch(Exception e){              
            log.error(e.getMessage());          
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
                log.error(e.getMessage());                
            }            
        }
        
        setMets(getBagItMets().onSaveBag(this,getMets()));        
        
        File metsFile = new File(bag.getFile(),"mets.xml");
        
        try{
            MetsWriter metsWriter = new MetsWriter();
            metsWriter.writeToFile(getMets(),metsFile);            
            
            //manually add checksum
            Algorithm ta = resolveAlgorithm(getTagManifestAlgorithm());            
            Manifest tagManifest = bag.getTagManifest(ta);
            String checksumMets = MessageDigestHelper.generateFixity(metsFile,ta);                        
            File tagmanifestFile = new File(bag.getFile(),"tagmanifest-"+ta.bagItAlgorithm+".txt");                                   
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(tagmanifestFile,true)));
            writer.println(checksumMets+"  mets.xml");            
            writer.close();
            
        }catch(Exception e){   
            log.error(e.getMessage());                    
        } 
    }
    
    @Override
    public void createPreBagAddKeepFilesToEmptyFolders(File data,String version){
        createPreBagAddKeepFilesToEmptyFolders(data,version,new String [] {});
    }    
    /*
    @Override
    public void addFileToPayload(File payloadFile){
        super.addFileToPayload(payloadFile);
        
        if(payloadFile.isDirectory()){
            File rootDir = payloadFile.getParentFile();
            ArrayList<File>descendants = FUtils.listFiles(payloadFile);
            for(File descendant:descendants){
                String entry = descendant.getAbsolutePath().replace(rootDir.getAbsolutePath()+File.separatorChar,"");
                entry = "data/"+entry.replace('\\','/');
                getNewEntries().put(entry,descendant);                        
                System.out.println("adding "+entry+" => "+descendant.getAbsolutePath());
            }
        }else{
            getNewEntries().put(payloadFile.getName(),payloadFile);                    
            System.out.println("adding "+payloadFile.getName()+" => "+payloadFile.getAbsolutePath());
        }
        System.out.println(getNewEntries());
    }
    @Override
    public void removeBagFile(String fileName){
        super.removeBagFile(fileName);
        getNewEntries().remove(fileName);
        System.out.println(getNewEntries());
        
    }    
    @Override
    public void removePayloadDirectory(String fileName){
        super.removePayloadDirectory(fileName);
        Iterator<Entry<String,File>>iterator = getNewEntries().entrySet().iterator();
        
        while(iterator.hasNext()){
            Entry<String,File>entry = iterator.next();
            if(entry.getKey().startsWith(fileName)){
                iterator.remove();                
                System.out.println("removing "+entry.getKey()+" from "+fileName);
            }
        }
        System.out.println(getNewEntries());
    }*/
}
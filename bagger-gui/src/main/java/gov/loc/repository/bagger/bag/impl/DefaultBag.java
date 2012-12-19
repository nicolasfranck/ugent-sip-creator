package gov.loc.repository.bagger.bag.impl;

import gov.loc.repository.bagger.bag.BagInfoField;
import gov.loc.repository.bagger.bag.BaggerFetch;
import gov.loc.repository.bagger.model.BagStatus;
import gov.loc.repository.bagger.model.Status;
import gov.loc.repository.bagit.*;
import gov.loc.repository.bagit.BagFactory.Version;
import gov.loc.repository.bagit.FetchTxt.FilenameSizeUrl;
import gov.loc.repository.bagit.Manifest.Algorithm;
import gov.loc.repository.bagit.transformer.HolePuncher;
import gov.loc.repository.bagit.transformer.impl.DefaultCompleter;
import gov.loc.repository.bagit.transformer.impl.HolePuncherImpl;
import gov.loc.repository.bagit.utilities.SimpleResult;
import gov.loc.repository.bagit.verify.impl.CompleteVerifierImpl;
import gov.loc.repository.bagit.verify.impl.ValidVerifierImpl;
import gov.loc.repository.bagit.writer.Writer;
import java.io.File;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import ugent.bagger.exceptions.BagFetchForbiddenException;
import ugent.bagger.exceptions.BagNoDataException;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.FUtils;

public class DefaultBag {
    protected static final Log log = LogFactory.getLog(DefaultBag.class);
    public static final long KB = 1024;
    public static final long MB = 1048576;
    public static final long GB = 1073741824;
    public static final long MAX_SIZE = 104857600; // 100 MB
    public static final short NO_MODE = 0;
    public static final short ZIP_MODE = 1;
    public static final short TAR_MODE = 2;
    public static final short TAR_GZ_MODE = 3;
    public static final short TAR_BZ2_MODE = 4;
    public static final String NO_LABEL = "none";
    public static final String ZIP_LABEL = "zip";
    public static final String TAR_LABEL = "tar";
    public static final String GZ_LABEL = "gz";
    public static final String TAR_GZ_LABEL = "tar.gz";
    public static final String TAR_BZ2_LABEL = "tar.bz2";

    // Bag option flags
    protected boolean isHoley = false;
    protected boolean isSerial = true;
    protected boolean isAddKeepFilesToEmptyFolders = false;

    // bag building (saving) options
    protected boolean isBuildTagManifest = true;
    protected boolean isBuildPayloadManifest = true;
    protected String tagManifestAlgorithm;
    protected String payloadManifestAlgorithm;
    protected short serialMode = NO_MODE;

    // Bag state flags
    protected boolean isValidateOnSave = false;
    protected boolean isSerialized = false;
    protected boolean dirty = false;

    protected File rootDir = null;
    protected String name = "bag_";
    protected long size;
    protected long totalSize = 0;

    protected Bag bilBag;
    protected DefaultBagInfo bagInfo = null;   
    protected BaggerFetch fetch;    
    
    protected String versionString = null;
    protected File bagFile = null;


    public DefaultBag() throws BagFetchForbiddenException, FileSystemException, BagNoDataException {            
        this(null,Version.V0_96.versionString);
    }

    public DefaultBag(File rootDir, String version) throws BagFetchForbiddenException, FileSystemException, BagNoDataException {
        this.versionString = version;
        init(rootDir);
    }

    private void init(File rootDir) throws BagFetchForbiddenException, FileSystemException, BagNoDataException{
        boolean newBag = rootDir == null;
        resetStatus();
        this.rootDir = rootDir;
        
        if(!newBag){
            if(rootDir.isDirectory()){
                //check fetch.txt
                File fetchFile = new File(rootDir,"fetch.txt");
                if(fetchFile.exists()){
                    throw new BagFetchForbiddenException(rootDir);
                }
                //check data directory
                File dataDir = new File(rootDir,"data");
                if(!(dataDir.exists() && dataDir.isDirectory())){
                    throw new BagNoDataException(rootDir);
                }
            }else{
                String basename = rootDir.getName();
                String path = rootDir.getAbsolutePath();
                int index = basename.lastIndexOf('.');
                String n = index >= 0 ? basename.substring(0,index) : basename;
                n = n.toLowerCase().endsWith(".tar") ? n.substring(0,n.length() - ".tar".length()) :n;

                //check fetch.txt            
                String entry = FUtils.getEntryStringFor(path,n+"/fetch.txt");
                FileObject fobject = FUtils.resolveFile(entry);
                if(fobject.exists()){
                    throw new BagFetchForbiddenException(rootDir);
                }
                //check data directory
                entry = FUtils.getEntryStringFor(path,n+"/data");
                fobject = FUtils.resolveFile(entry);
                if(!fobject.exists()){
                    throw new BagNoDataException(rootDir);
                }
            }       
        }

        log.debug("DefaultBag.init file: "+rootDir+", version: "+versionString);
        
        BagFactory bagFactory = new BagFactory();
        if (!newBag) {
            bilBag = bagFactory.createBag(this.rootDir);
            versionString = bilBag.getVersion().versionString;
        } else if (versionString != null) {            
            Version version = Version.valueOfString(versionString);            
            bilBag = bagFactory.createBag(version);
        } else {
            bilBag = bagFactory.createBag();
        }
        initializeBilBag();

        bagInfo = new DefaultBagInfo(bilBag);                       

        FetchTxt fetchTxt = bilBag.getFetchTxt();
        if (fetchTxt != null && !fetchTxt.isEmpty()) {
            String url = getBaseUrl(fetchTxt);
            if (url != null && !url.isEmpty()) {
                isHoley(true);
                BaggerFetch f = getFetch();
                f.setBaseURL(url);
                this.fetch = f;
            } else {
                isHoley(false);
            }
        }

        /*
         * BUG: getPayloadManifestAlgorithm() levert hierdoor altijd MD5 op, ook al heb je enkel manifest-sha512.txt
         */
                
        //this.payloadManifestAlgorithm = Manifest.Algorithm.MD5.bagItAlgorithm;        
        //this.tagManifestAlgorithm = Manifest.Algorithm.MD5.bagItAlgorithm;               
        
        tagManifestAlgorithm = 
                !bilBag.getTagManifests().isEmpty() ? 
                    bilBag.getTagManifests().get(0).getAlgorithm().bagItAlgorithm : 
                    Manifest.Algorithm.MD5.bagItAlgorithm;
        payloadManifestAlgorithm = 
                !bilBag.getPayloadManifests().isEmpty() ? 
                    bilBag.getPayloadManifests().get(0).getAlgorithm().bagItAlgorithm : 
                    Manifest.Algorithm.MD5.bagItAlgorithm;
        
        this.bagInfo.updateBagInfoFieldMapFromBilBag(bilBag.getBagInfoTxt());
    }

    protected void initializeBilBag() {
        BagInfoTxt bagInfoTxt = bilBag.getBagInfoTxt();
        if (bagInfoTxt == null) {
            bagInfoTxt = bilBag.getBagPartFactory().createBagInfoTxt();
            /* */
            Set<String> keys = bagInfoTxt.keySet();
            for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
                String key = (String) iter.next();
                bagInfoTxt.remove(key);
            }
            /* */
            bilBag.putBagFile(bagInfoTxt);
        }
        BagItTxt bagIt = bilBag.getBagItTxt();
        if (bagIt == null) {
            bagIt = bilBag.getBagPartFactory().createBagItTxt();
            bilBag.putBagFile(bagIt);
        }
    }

    public void createPreBag(File data, String version) {
        BagFactory bagFactory = new BagFactory();
        PreBag preBag = bagFactory.createPreBag(data);
        if (version == null) {
            log.debug("no version supplied, so using latest");
            bilBag = preBag.makeBagInPlace(BagFactory.LATEST, false);            
        } else {
            log.debug("version supplied: "+Version.valueOfString(version));
            bilBag = preBag.makeBagInPlace(Version.valueOfString(version),false);            
        }
    }

    /*
    * Makes BIL API call to create Bag in place and 
    * adding .keep files in empty Pay load folders 
    */
    public void createPreBagAddKeepFilesToEmptyFolders(File data, String version) {
        BagFactory bagFactory = new BagFactory();
        PreBag preBag = bagFactory.createPreBag(data);
        if(version == null){
            bilBag = preBag.makeBagInPlace(BagFactory.LATEST, false, true);            
        }else{
            bilBag = preBag.makeBagInPlace(Version.valueOfString(version),false, true);            
        }
    }	

    public File getBagFile() {
        return bagFile;
    }

    private void setBagFile(File fname) {
        this.bagFile = fname;
    }

    public String getDataDirectory() {
        return bilBag.getBagConstants().getDataDirectory();
    }

    protected void resetStatus() {
        isComplete(Status.UNKNOWN);
        isValid(Status.UNKNOWN);        
    }

    public void setVersion(String versionString) {
        this.versionString = versionString;
    }

    public String getVersion() {
        return this.versionString;
    }

    public void setName(String name) {
        String[] list = name.split("\\.");
        if (list != null && list.length > 0) {
            name = list[0];
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    // This directory contains either the bag directory or serialized bag file
    public void setRootDir(File rootDir) {
        this.rootDir = rootDir;
    }

    public File getRootDir() {
        return rootDir;
    }


    public void isHoley(boolean isHoley) {
        this.isHoley = isHoley;
    }

    public boolean isHoley() {
        return isHoley;
    }

    public void isSerial(boolean isSerial) {
        this.isSerial = isSerial;
    }

    public boolean isSerial() {
        return isSerial;
    }

    public void setSerialMode(short serialMode) {
        this.serialMode = serialMode;
    }

    public short getSerialMode() {
        return serialMode;
    }
   
    public void isBuildTagManifest(boolean isBuildTagManifest) {
        this.isBuildTagManifest = isBuildTagManifest;
    }

    public boolean isBuildTagManifest() {
        return isBuildTagManifest;
    }

    public void isBuildPayloadManifest(boolean isBuildPayloadManifest) {
        this.isBuildPayloadManifest = isBuildPayloadManifest;
    }

    public boolean isBuildPayloadManifest() {
        return isBuildPayloadManifest;
    }

    public void setTagManifestAlgorithm(String tagManifestAlgorithm) {
        this.tagManifestAlgorithm = tagManifestAlgorithm;
    }

    public String getTagManifestAlgorithm() {
        return tagManifestAlgorithm;
    }

    public void setPayloadManifestAlgorithm(String payloadManifestAlgorithm) {
        this.payloadManifestAlgorithm = payloadManifestAlgorithm;
    }

    public String getPayloadManifestAlgorithm() {
        return payloadManifestAlgorithm;
    }

    /*
    *  Setter Method 
    *  for the passed value associated with the ".keep Files in Empty Folder(s):" Check Box 
    */	
    public void isAddKeepFilesToEmptyFolders(boolean isAddKeepFilesToEmptyFolders) {
        this.isAddKeepFilesToEmptyFolders = isAddKeepFilesToEmptyFolders;
    }

    /*
    *  Getter Method 
    *  for the value return value associated with the "Add .keep Files To Empty Folder" Check Box 
    */
    public boolean isAddKeepFilesToEmptyFolders() {
        return isAddKeepFilesToEmptyFolders;
    }	

    public void isValidateOnSave(boolean isValidateOnSave) {
        this.isValidateOnSave = isValidateOnSave;
    }

    public boolean isValidateOnSave() {
        return isValidateOnSave;
    }

    private void isComplete(Status status) {
        BagStatus.getInstance().getCompletenessStatus().setStatus(status);
    }

    private void isValid(Status status) {
        BagStatus.getInstance().getValidationStatus().setStatus(status);
    }
   
    public void isSerialized(boolean isSerialized) {
        this.isSerialized = isSerialized;
    }

    public boolean isSerialized() {
        return isSerialized;
    }

    public void updateBagInfo(Map<String,ArrayList<String>> map) {
        changeToDirty();        
        bagInfo.update(map);
    }	

    public DefaultBagInfo getInfo() {
        return bagInfo;
    }

    public String getBagInfoContent() {
        return bagInfo != null ? bagInfo.toString():"";        
    }

    public String getBaseUrl(FetchTxt fetchTxt) {
        String httpToken = "http:";
        String delimToken = "bagit";
        String baseUrl = "";
        try {
            if(fetchTxt != null){
                if(!fetchTxt.isEmpty()){
                    FilenameSizeUrl fsu = fetchTxt.get(0);
                    if (fsu != null) {
                        String url = fsu.getUrl();
                        baseUrl = url;
                        String[] list = url.split(delimToken);
                        for (int i = 0; i < list.length; i++) {
                            String s = list[i];
                            if (s.trim().startsWith(httpToken)) {
                                baseUrl = s;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return baseUrl;
    }

    public void setFetch(BaggerFetch fetch) {
        this.fetch = fetch;
    }

    public BaggerFetch getFetch() {
        if (fetch == null){
            fetch = new BaggerFetch();
        }
        return fetch;
    }

    public List<String> getFetchPayload() {
        List<String> list = new ArrayList<String>();
        FetchTxt fetchTxt = bilBag.getFetchTxt();
        if(fetchTxt != null){
            for (int i = 0; i < fetchTxt.size(); i++) {
                FilenameSizeUrl f = fetchTxt.get(i);                
                log.debug("DefaultBag.getFetchPayload: " + f.toString());
                list.add(f.getFilename());
            }
        }
        return list;        
    }

    public String getDataContent() {
        totalSize = 0;
        StringBuilder dcontent = new StringBuilder();
        dcontent.append(getDataDirectory()).append("/");
        dcontent.append('\n');
        Collection<BagFile> files = bilBag.getPayload();
        if (files != null) {
            for (Iterator<BagFile> it = files.iterator(); it.hasNext();) {
                try {
                    BagFile bf = it.next();
                    if (bf != null) {
                        totalSize += bf.getSize();
                        dcontent.append(bf.getFilepath());
                        dcontent.append('\n');
                    }
                }catch(Exception e) {
                    log.error("DefaultBag.getDataContent: " + e.getMessage());
                }
            }
        }
        setSize(totalSize);
        return dcontent.toString();
    }
    public long getDataSize() {
        return totalSize;
    }
    public int getDataNumber() {
        return bilBag.getPayload().size();
    }
    
    public List<String> getPayloadPaths() {
        ArrayList<String> pathList = new ArrayList<String>();
        Collection<BagFile> payload = bilBag.getPayload();
        
        if(payload != null) {
            for (Iterator<BagFile> it = payload.iterator(); it.hasNext();) {                
                pathList.add(it.next().getFilepath());
            }
        }
        return pathList;
    }
    public List<String> getTagFilePaths() {
        ArrayList<String> pathList = new ArrayList<String>();
        Collection<BagFile>tags = bilBag.getTags();
        
        if(tags != null) {
            for (Iterator<BagFile> it = tags.iterator(); it.hasNext();) {                
                pathList.add(it.next().getFilepath());
            }
        }
        return pathList;
    }

    public String addTagFile(File file) {
        changeToDirty();
        isComplete(Status.UNKNOWN);

        String message = null;
        if(file != null) {
            try {
                bilBag.addFileAsTag(file);
            }catch (Exception e){                
                message = Context.getMessage("defaultBag.addTagFile.Exception",new Object [] {file,e.getMessage()});
            }
        }
        return message;
    }

    public boolean write(Writer bw) {
        prepareBilBagInfoIfDirty();
       
        generateManifestFiles();

        if(isHoley){
            if (getFetch().getBaseURL() != null) {
                BagInfoTxt bagInfoTxt = bilBag.getBagInfoTxt();

                List<Manifest> manifests = bilBag.getPayloadManifests();
                List<Manifest> tags = bilBag.getTagManifests();

                HolePuncher puncher = new HolePuncherImpl(new BagFactory());
                bilBag = puncher.makeHoley(bilBag,this.getFetch().getBaseURL(), true, true, false);
                // makeHoley deletes baginfo so put back
                bilBag.putBagFile(bagInfoTxt);
                if(manifests != null){
                    for (int i = 0; i < manifests.size(); i++) {
                        bilBag.putBagFile(manifests.get(i));
                    }
                }
                if(tags != null){
                    for (int i = 0; i < tags.size(); i++) {
                        bilBag.putBagFile(tags.get(i));
                    }
                }
            }
        }

        return writeBag(bw);
    }    
    
    public SimpleResult completeBag(CompleteVerifierImpl completeVerifier) {
        prepareBilBagInfoIfDirty();
        
        SimpleResult result = completeVerifier.verify(bilBag);

        if(completeVerifier.isCancelled()) {
            isComplete(Status.UNKNOWN);
            return result;
        } 

        isComplete(result.isSuccess()? Status.PASS : Status.FAILURE);
        
        return result;
    }

    public SimpleResult validateBag(ValidVerifierImpl validVerifier) {
        prepareBilBagInfoIfDirty();
       
        SimpleResult result = validVerifier.verify(bilBag);        

        if(validVerifier.isCancelled()) {
            isValid(Status.UNKNOWN);        
            return result;
        } 
        
        isValid(result.isSuccess()? Status.PASS : Status.FAILURE);
        if(result.isSuccess()){
            isComplete(Status.PASS);
        }        
        return result;
    }

    protected String fileStripSuffix(String filename) {
        return new StringTokenizer(filename,".").nextToken();        
    }

    protected boolean writeBag(Writer bw) {
      
        File bagFile = null;        
        String bagName = fileStripSuffix(getRootDir().getName());
        File parentDir = getRootDir().getParentFile();
        
        log.debug("DefaultBag.writeBag parentDir: " + parentDir + ", bagName: "+bagName);

        setName(bagName);
        if(serialMode == NO_MODE) {
            bagFile = new File(parentDir,getName());
        } else if (serialMode == ZIP_MODE) {
            
            int i = bagName.lastIndexOf('.');
            if (i > 0 && i < bagName.length() - 1) {
                String sub = bagName.substring(i + 1);
                if (!sub.equalsIgnoreCase(ZIP_LABEL)) {
                    bagName += "." + ZIP_LABEL;
                }
            } else {
                bagName += "." + ZIP_LABEL;
            }
            bagFile = new File(parentDir,bagName);
           
        } else if (serialMode == TAR_MODE) {
          
            int i = bagName.lastIndexOf('.');
            if (i > 0 && i < bagName.length() - 1) {
                if (!bagName.substring(i + 1).toLowerCase().equals(TAR_LABEL)) {
                    bagName += "." + TAR_LABEL;
                }
            } else {
                bagName += "." + TAR_LABEL;
            }
            bagFile = new File(parentDir, bagName);
            
        } else if (serialMode == TAR_GZ_MODE) {
           
            int i = bagName.lastIndexOf('.');
            if (i > 0 && i < bagName.length() - 1) {
                if (!bagName.substring(i + 1).toLowerCase().equals(TAR_GZ_LABEL)) {
                    bagName += "." + TAR_GZ_LABEL;
                }
            } else {
                bagName += "." + TAR_GZ_LABEL;
            }
            bagFile = new File(parentDir, bagName);
            
        } else if (serialMode == TAR_BZ2_MODE) {
            
            int i = bagName.lastIndexOf('.');
            if (i > 0 && i < bagName.length() - 1) {
                if (!bagName.substring(i + 1).toLowerCase().equals(TAR_BZ2_LABEL)) {
                    bagName += "." + TAR_BZ2_LABEL;
                }
            } else {
                bagName += "." + TAR_BZ2_LABEL;
            }
            bagFile = new File(parentDir, bagName);
           
        }
        setBagFile(bagFile);

        log.info("Bag-Info to write: " + bilBag.getBagInfoTxt());

        isSerialized(false);

        Bag newBag = bw.write(bilBag,bagFile);
        if(newBag != null) {
            bilBag = newBag;
            // write successful
            isSerialized(true);
        }
        
        return newBag != null;
    }    
   
    public static Algorithm resolveAlgorithm(String algorithm){        
        
        if(algorithm.equalsIgnoreCase(Manifest.Algorithm.MD5.bagItAlgorithm)) {
            return Algorithm.MD5;
        }else if(algorithm.equalsIgnoreCase(Manifest.Algorithm.SHA1.bagItAlgorithm)) {
            return Algorithm.SHA1;
        }else if(algorithm.equalsIgnoreCase(Manifest.Algorithm.SHA256.bagItAlgorithm)) {
            return Algorithm.SHA256;
        }else if(algorithm.equalsIgnoreCase(Manifest.Algorithm.SHA512.bagItAlgorithm)) {
            return Algorithm.SHA512;
        }else{
            return Algorithm.MD5;
        }                
    }
   

    public void generateManifestFiles() {
        DefaultCompleter completer = new DefaultCompleter(new BagFactory());                    
        if (isBuildPayloadManifest) {  
            log.debug("generating payload manifest");
            completer.setPayloadManifestAlgorithm(resolveAlgorithm(payloadManifestAlgorithm));            
        }
        if (isBuildTagManifest) {
            log.debug("generating tag manifest");            
            completer.setClearExistingTagManifests(true);
            completer.setGenerateTagManifest(true);
            completer.setTagManifestAlgorithm(resolveAlgorithm(tagManifestAlgorithm));           
        }
        if (bilBag.getBagInfoTxt() != null) {
            completer.setGenerateBagInfoTxt(true);
        }
        bilBag = completer.complete(bilBag);
    }

    public void clear() {       
        bagInfo.clearFields();		
    }

    public void addField(BagInfoField field) {
        changeToDirty();       
        bagInfo.addField(field);
    }

    public void removeBagInfoField(String key) {
        changeToDirty();        
        bagInfo.removeField(key);
    }

    public void addFileToPayload(File file) {
        changeToDirty();
        isComplete(Status.UNKNOWN);       
        bilBag.addFileToPayload(file);
    }

    public Collection<BagFile> getTags() {
        return bilBag.getTags();
    }

    public void removeBagFile(String fileName) {
        changeToDirty();
        isComplete(Status.UNKNOWN);		
        bilBag.removeBagFile(fileName);
    }
    public void removePayloadDirectory(String fileName) {
        changeToDirty();
        isComplete(Status.UNKNOWN);
        log.debug("DefaultBag::removePayloadDirectory('"+fileName+"')");        
        bilBag.removePayloadDirectory(fileName);
    }
    public Collection<BagFile> getPayload() {
        return bilBag.getPayload();
    }
    public FetchTxt getFetchTxt() {
        return bilBag.getFetchTxt();
    }
    protected void changeToDirty() {
        this.dirty = true;
        isValid(Status.UNKNOWN);
    }
    protected void prepareBilBagInfoIfDirty() {
        if(dirty){
            bagInfo.prepareBilBagInfo(bilBag.getBagInfoTxt());
        }
    }   
    public Bag getBag(){
        return bilBag;                    
    }        
}
package ugent.bagger.params;

import java.io.File;
import java.util.ArrayList;
import ugent.bagger.bagitmets.MetsFileDateCreated;

/**
 *
 * @author nicolas
 */
public class CreateBagsParams {
    private ArrayList<File>directories;
    private String version;
    //private String profile = "<no profile>";
    private boolean keepEmptyDirectories = false;
    private String metadataPaths = "";
    private String [] metadata;
    private boolean keepMetadata = true;
    private boolean addDC = false;
    private boolean writeToBagInfo = false;
    private boolean bagInPlace = true;
    private ArrayList<File> outputDir;
    private MetsFileDateCreated metsFileDateCreated;

    public String[] getMetadata() {
        if(metadata == null){
            metadata = new String [] {};
        }
        return metadata;
    }
    public void setMetadata(String[] metadata) {
        this.metadata = metadata;
    }
    public boolean isKeepEmptyDirectories() {
        return keepEmptyDirectories;
    }
    public void setKeepEmptyDirectories(boolean keepEmptyDirectories) {
        this.keepEmptyDirectories = keepEmptyDirectories;
    }
    public String getMetadataPaths() {
        return metadataPaths;
    }
    public void setMetadataPaths(String metadataPaths) {
        this.metadataPaths = metadataPaths;
    }
    public boolean isKeepMetadata() {
        return keepMetadata;
    }
    public void setKeepMetadata(boolean keepMetadata) {
        this.keepMetadata = keepMetadata;
    }
    public boolean isAddDC() {
        return addDC;
    }
    public void setAddDC(boolean addDC) {
        this.addDC = addDC;
    }
    public boolean isWriteToBagInfo() {
        return writeToBagInfo;
    }
    public void setWriteToBagInfo(boolean writeToBagInfo) {
        this.writeToBagInfo = writeToBagInfo;
    }
    public ArrayList<File> getOutputDir() {
        if(outputDir == null){
            outputDir = new ArrayList<File>();
        }
        return outputDir;
    }
    public void setOutputDir(ArrayList<File> outputDir) {
        this.outputDir.clear();
        this.outputDir.add(outputDir.get(0));        
    }    

    public ArrayList<File> getDirectories() {
        if(directories == null){
            directories = new ArrayList<File>();
        }
        return directories;
    }
    public void setDirectories(ArrayList<File> directories) {
        this.directories = directories;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    /*
    public String getProfile() {
        return profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }*/
    public boolean isBagInPlace() {
        return bagInPlace;
    }
    public void setBagInPlace(boolean bagInPlace) {
        this.bagInPlace = bagInPlace;
    }
    public MetsFileDateCreated getMetsFileDateCreated() {
        if(metsFileDateCreated == null){
            metsFileDateCreated = MetsFileDateCreated.CURRENT_DATE;
        }
        return metsFileDateCreated;
    }
    public void setMetsFileDateCreated(MetsFileDateCreated metsFileDateCreated) {
        this.metsFileDateCreated = metsFileDateCreated;
    }    
}
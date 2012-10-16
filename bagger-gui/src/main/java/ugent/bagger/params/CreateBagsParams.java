package ugent.bagger.params;

import gov.loc.repository.bagit.BagFactory;
import java.io.File;
import java.util.ArrayList;
import ugent.bagger.bagitmets.MetsFileDateCreated;

/**
 *
 * @author nicolas
 */
public class CreateBagsParams {
    private ArrayList<File>directories;
    private String bagVersion;
    private String profile;
    private boolean bagInPlace = true;
    private ArrayList<File> outputDir;

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
    private MetsFileDateCreated metsFileDateCreated;

    public ArrayList<File> getDirectories() {
        if(directories == null){
            directories = new ArrayList<File>();
        }
        return directories;
    }
    public void setDirectories(ArrayList<File> directories) {
        this.directories = directories;
    }

    public String getBagVersion() {
        if(bagVersion == null){
            bagVersion = BagFactory.Version.V0_96.versionString;
        }
        return bagVersion;
    }

    public void setBagVersion(String bagVersion) {
        this.bagVersion = bagVersion;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

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

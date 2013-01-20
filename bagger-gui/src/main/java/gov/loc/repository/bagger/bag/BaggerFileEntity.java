package gov.loc.repository.bagger.bag;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import gov.loc.repository.bagit.utilities.FilenameHelper;
import java.io.File;
import java.text.MessageFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Bagger needs to know where the file came from so that it can be retrieved: rootSrc,
 * and also where the file belongs within the bag: bagSrc.
 * Once the logical location of bagSrc becomes a physical copy isInBag=true
 * If the file is unselected from the BagTree, it is marked to be removed from the bag
 * by setting isIncluded=false.
 * 
 * In order to create a bag, all data files to be included are copied to the bag data dir.
 * If they already exist they are written over, or deleted if marked as isIncluded=false.
 *
 * If the file comes from a pre-existing bag, then the rootSrc and bagSrc will be the same,
 * and isInBag=true, otherwise it comes from somewhere else and needs to be placed in the
 * current bag.
 * 
 * @author Jon Steinbach
 */
public class BaggerFileEntity {
    static final Log log = LogFactory.getLog(BaggerFileEntity.class);
    File rootParent;				// c:\\user\my documents\
    File rootSrc;					// c:\\user\my documents\datadir\dir1\file1
    File bagSrc;					// c:\\user\my documents\bag\data\datadir\dir1\file
    String normalizedName;			// datadir\dir1\file1
    boolean isInBag = false;
    boolean isIncluded = true;

    public BaggerFileEntity(){
    }
    public BaggerFileEntity(File rootSrc) {
        this.rootSrc = rootSrc;
    }
    public BaggerFileEntity(File rootParent, File rootSrc, File bagParent) {
        this.rootParent = rootParent;
        this.rootSrc = rootSrc;
        File bagDataDir = new File(bagParent, AbstractBagConstants.DATA_DIRECTORY);
        this.normalizedName = removeBasePath(rootParent.getAbsolutePath(), rootSrc.getAbsolutePath());
        this.bagSrc = new File(bagDataDir, normalizedName);
        if (this.rootSrc.getAbsolutePath().equalsIgnoreCase(this.bagSrc.getAbsolutePath())) {
            isInBag = true;
        }
    }
    public BaggerFileEntity(File rootParent, File rootSrc, File bagParent, File bagSrc) {
        this.rootParent = rootParent;
        this.rootSrc = rootSrc;
        this.bagSrc = bagSrc;
        if (this.rootSrc.getAbsolutePath().equalsIgnoreCase(this.bagSrc.getAbsolutePath())) {
            isInBag = true;
        }
    }
    @Override
    public String toString() {
        return getNormalizedName();
    }
    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }
    public String getNormalizedName() {
        return normalizedName;
    }

    public void setRootParent(File rootParent) {
        this.rootParent = rootParent;
    }
    public File getRootParent() {
        return rootParent;
    }
    public void setRootSrc(File rootSrc) {
        this.rootSrc = rootSrc;
    }
    public File getRootSrc() {
        return rootSrc;
    }
    public void setBagSrc(File bagDir, File src) {
        this.bagSrc = new File(bagDir,src.getPath());       
    }
    public void setBagSrc(File bagSrc) {
        this.bagSrc = bagSrc;
    }
    public File getBagSrc() {
        return bagSrc;
    }
    public void setIsInBag(boolean isInBag) {
        this.isInBag = isInBag;
    }
    public boolean getIsInBag() {
        return isInBag;
    }
    public void setIsIncluded(boolean isIncluded) {
        this.isIncluded = isIncluded;
    }
    public boolean getIsIncluded() {
        return isIncluded;
    }
    public boolean copyRootToBag() {
        boolean success = false;
        // TODO perform the copy
        isInBag = true;
        return success;
    }
    public static String removeBasePath(String basePath, String filename) throws RuntimeException {
        if (filename == null) {
            throw new RuntimeException("Cannot remove basePath from null");
        }		
        String normBasePath = normalize(basePath);
        String normFilename = normalize(filename);
        String filenameWithoutBasePath = null;
        if(basePath == null){
            filenameWithoutBasePath = normFilename;
        }else{
            if(! normFilename.startsWith(normBasePath)){
                throw new RuntimeException(MessageFormat.format("Cannot remove basePath {0} from {1}", basePath, filename));
            }
            if(normBasePath.equals(normFilename)){
                filenameWithoutBasePath = "";
            }else{
                int delta;
                if(normBasePath.endsWith("/") || normBasePath.endsWith("\\")){
                    delta = 0;
                }else{
                    delta = 1;
                }
                filenameWithoutBasePath = normFilename.substring(normBasePath.length() + delta);                
            }
        }
        log.debug(MessageFormat.format("Removing {0} from {1} resulted in {2}", basePath, filename, filenameWithoutBasePath));
        return filenameWithoutBasePath;
    }
    public static String normalize(String filename){
        return FilenameHelper.normalizePathSeparators(filename);
    }
}
package ugent.bagger.params;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class ValidateManifestResult {
    private File file;
    private String checksumFound;
    private String checksumComputed;   
    
    public ValidateManifestResult(File file,String checksumFound,String checksumComputed){
        this.file = file;
        this.checksumFound = checksumFound;
        this.checksumComputed = checksumComputed;
    }

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public String getChecksumFound() {
        return checksumFound;
    }
    public void setChecksumFound(String checksumFound) {
        this.checksumFound = checksumFound;
    }
    public String getChecksumComputed() {
        return checksumComputed;
    }
    public void setChecksumComputed(String checksumComputed) {
        this.checksumComputed = checksumComputed;
    }
    public boolean isSuccess() {
        return checksumFound.compareTo(checksumComputed) == 0;
    }
}
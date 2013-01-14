package ugent.bagger.params;

import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.Manifest.Algorithm;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class ValidateManifestParams {
    ArrayList<File> files;
    Manifest.Algorithm algorithm;

    public ArrayList<File> getFiles() {
        if(files == null){
            files = new ArrayList<File>();
        }
        return files;
    }
    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }    
    public Algorithm getAlgorithm() {
        if(algorithm == null){
            algorithm = Manifest.Algorithm.MD5;
        }
        return algorithm;
    }
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }    
}
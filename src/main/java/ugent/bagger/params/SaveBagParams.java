package ugent.bagger.params;

import gov.loc.repository.bagit.Manifest.Algorithm;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class SaveBagParams {
    ArrayList<File>outputFile;
    BagMode bagMode = BagMode.NO_MODE;
    Algorithm algorithm = Algorithm.MD5;

    public ArrayList<File> getOutputFile() {
        if(outputFile == null){
            outputFile = new ArrayList<File>();
        }
        return outputFile;
    }

    public void setOutputFile(ArrayList<File> outputFile) {
        this.outputFile = outputFile;
    }

    public BagMode getBagMode() {
        return bagMode;
    }

    public void setBagMode(BagMode bagMode) {
        this.bagMode = bagMode;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    
}

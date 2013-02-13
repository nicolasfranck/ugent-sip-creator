package ugent.bagger.params;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class BagValidateParams {    
    boolean valid;
    ArrayList<File>files = new ArrayList<File>();
    
    public boolean isValid() {
        return valid;
    }
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    public ArrayList<File> getFiles() {
        return files;
    }
    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }    
}
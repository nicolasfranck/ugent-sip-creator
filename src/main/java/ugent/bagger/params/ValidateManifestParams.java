package ugent.bagger.params;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class ValidateManifestParams {
    ArrayList<File> files;    

    public ArrayList<File> getFiles() {
        if(files == null){
            files = new ArrayList<File>();
        }
        return files;
    }
    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }        
}
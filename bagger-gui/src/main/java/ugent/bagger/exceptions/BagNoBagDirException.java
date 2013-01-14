package ugent.bagger.exceptions;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class BagNoBagDirException extends Exception{
    File file;
    public BagNoBagDirException(File file){
        this.file = file;
    }    
    public File getFile() {
        return file;
    }    
}

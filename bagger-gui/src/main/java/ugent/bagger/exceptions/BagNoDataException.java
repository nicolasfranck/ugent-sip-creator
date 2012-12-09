package ugent.bagger.exceptions;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class BagNoDataException extends Exception{
    private File file;
    public BagNoDataException(File file){
        this.file = file;
    }    
    public File getFile() {
        return file;
    }    
}

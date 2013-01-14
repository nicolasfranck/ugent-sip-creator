package ugent.bagger.exceptions;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class BagFetchForbiddenException extends Exception{
    File file;
    public BagFetchForbiddenException(File file){
        this.file = file;
    }    
    public File getFile() {
        return file;
    }    
}

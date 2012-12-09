package ugent.bagger.exceptions;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class BagFetchForbiddenException extends Exception{
    private File file;
    public BagFetchForbiddenException(File file){
        this.file = file;
    }    
    public File getFile() {
        return file;
    }    
}

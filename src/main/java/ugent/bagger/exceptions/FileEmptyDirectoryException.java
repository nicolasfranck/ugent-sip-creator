package ugent.bagger.exceptions;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class FileEmptyDirectoryException extends Exception{
    File file;
    public FileEmptyDirectoryException(File file){
        this.file = file;
    }    
    public File getFile() {
        return file;
    }    
}

package ugent.bagger.exceptions;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class FileNotReadableException extends Exception{
    File file;
    public FileNotReadableException(File file){
        this.file = file;
    }    
    public File getFile() {
        return file;
    }    
}

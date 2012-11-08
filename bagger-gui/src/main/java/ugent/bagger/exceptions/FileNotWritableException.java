package ugent.bagger.exceptions;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class FileNotWritableException extends Exception{
    private File file;
    public FileNotWritableException(File file){
        this.file = file;
    }    
    public File getFile() {
        return file;
    }    
}

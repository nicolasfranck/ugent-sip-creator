package ugent.bagger.exceptions;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class FileNameNotPortableException extends Exception{
    File file;
    public FileNameNotPortableException(File file){
        this.file = file;
    }    
    public File getFile() {
        return file;
    }    
}

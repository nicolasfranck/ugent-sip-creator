package ugent.bagger.exceptions;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class FileNotRegularException extends Exception{
    private File file;
    public FileNotRegularException(File file){
        this.file = file;
    }    
    public File getFile() {
        return file;
    }    
}

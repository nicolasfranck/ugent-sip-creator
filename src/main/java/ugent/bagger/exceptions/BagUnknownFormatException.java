package ugent.bagger.exceptions;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class BagUnknownFormatException extends Exception{
    File file;
    public BagUnknownFormatException(File file){
        this.file = file;
    }    
    public File getFile() {
        return file;
    }    
}

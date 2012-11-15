package ugent.bagger.exceptions;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class BagUnknownFormatException extends Exception{
    private File file;
    public BagUnknownFormatException(File file){
        this.file = file;
    }    
    public File getFile() {
        return file;
    }    
}

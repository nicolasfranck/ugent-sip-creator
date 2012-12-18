package gov.loc.repository.bagger.bag.impl;

/**
 *
 * @author nicolas
 */
public class FilePathTooLongException extends Exception{
    String path;
    public FilePathTooLongException(){
        super();
    }
    public FilePathTooLongException(String path,String message){
        super(message);
        this.path = path;
    }

    public String getPath() {
        return path;
    }   
    
}

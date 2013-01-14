package ugent.bagger.filters;

import java.io.File;
import java.nio.file.Files;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author nicolas
 */
public final class FileExtensionFilter extends FileFilter{
    
    String description;  
    static final Log log = LogFactory.getLog(FileExtensionFilter.class);
    String [] extensions;
    boolean ignoreCase = false;           

    public boolean isIgnoreCase() {
        return ignoreCase;
    }
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }   
    public String [] getExtensions() {
        return extensions;
    }
    public void setExtensions(String [] extensions) {
        this.extensions = extensions;
    }
    boolean extensionValid(String filename){
        int posDot = filename.lastIndexOf(".");
        if(posDot < 0) {
            return false;
        }
        String ext = filename.substring(posDot+1);
        for(String extension:extensions){
            if(ext.compareTo(extension) == 0){
                return true;
            }
        }
        return false;
    }
    public FileExtensionFilter(String [] extensions,String description){
        setExtensions(extensions);
        setDescription(description);
    }
    public FileExtensionFilter(String [] extensions,String description,boolean ignoreCase){
        setExtensions(extensions);
        setDescription(ignoreCase ? description.toLowerCase():description);
        setIgnoreCase(ignoreCase);
    }
    @Override
    public boolean accept(File file) {        
        String name = file.getName();
        if(isIgnoreCase()) {
            name = name.toLowerCase();
        }                       
        return Files.isReadable(file.toPath()) && (
            file.isDirectory() ||
            extensionValid(name)            
        );
    }   
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String getDescription() {
        return description;
    }
}
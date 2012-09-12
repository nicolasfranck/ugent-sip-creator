/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.apache.log4j.Logger;

/**
 *
 * @author nicolas
 */
public final class FileExtensionFilter extends FileFilter{
    
    private String description;  
    private static Logger logger = Logger.getLogger(FileExtensionFilter.class);
    private String [] extensions;
    private boolean ignoreCase = false;           

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
    private boolean extensionValid(String filename){
        int posDot = filename.lastIndexOf(".");
        if(posDot < 0)return false;
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
        logger.debug("filtering file: "+file.getAbsolutePath());
        String name = file.getName();
        if(isIgnoreCase()) {
            name = name.toLowerCase();
        }           
            
        return file.canRead() && (
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


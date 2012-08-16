/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.apache.log4j.Logger;

/**
 *
 * @author nicolas
 */
public final class ExtensionFilter extends FileFilter{
    
    private String description;  
    private static Logger logger = Logger.getLogger(ExtensionFilter.class);
    private String extension;
    private boolean ignoreCase = false;           

    public boolean isIgnoreCase() {
        return ignoreCase;
    }
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }   
    public String getExtension() {
        return extension;
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public ExtensionFilter(String extension,String description){
        setExtension(extension);
        setDescription(description);
    }
    public ExtensionFilter(String extension,String description,boolean ignoreCase){
        setExtension(extension);
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
            name.endsWith("."+getExtension())
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


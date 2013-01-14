/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
public final class DirectoryFilter extends FileFilter{
    
    String description;  
    static final Log log = LogFactory.getLog(DirectoryFilter.class);
    
    public DirectoryFilter(String description){      
        setDescription(description);
    }  
    @Override
    public boolean accept(File file) {
        return file.isDirectory() && Files.isReadable(file.toPath());
    }   
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String getDescription() {
        return description;
    }
}
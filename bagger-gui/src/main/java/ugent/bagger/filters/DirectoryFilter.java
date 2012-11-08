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
public final class DirectoryFilter extends FileFilter{
    
    private String description;  
    private static Logger log = Logger.getLogger(DirectoryFilter.class);
    
    public DirectoryFilter(String description){      
        setDescription(description);
    }  
    @Override
    public boolean accept(File file) {
        return file.isDirectory() && file.canRead();
    }   
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String getDescription() {
        return description;
    }
}


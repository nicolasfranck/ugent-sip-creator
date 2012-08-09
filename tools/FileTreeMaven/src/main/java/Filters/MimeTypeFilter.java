/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Filters;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import java.io.File;
import java.util.Collection;
import javax.swing.filechooser.FileFilter;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 *
 * @author nicolas
 */
public class MimeTypeFilter extends FileFilter{

    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    private String description;
    private MimeType mimeType;
    private static Logger logger = Logger.getLogger(MimeTypeFilter.class);

    public MimeType getMimeType(){
        return mimeType;
    }
    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
    }    
    public MimeTypeFilter(MimeType mimeType,String description){
        setMimeType(mimeType);
        setDescription(description);
    }
    @Override
    public boolean accept(File file) {
        logger.debug("filtering file: "+file.getAbsolutePath());
        return file.canRead() && (
            file.isDirectory() ||
            isMimeTypeCorrect(file)
        );
    }
    private boolean isMimeTypeCorrect(File file){
        logger.debug("isMimeTypeCorrect of file '"+file.getAbsolutePath()+"'");
        logger.debug("check against mimeType '"+mimeType+"'");
        Collection mimes = MimeUtil.getMimeTypes(file);
        logger.debug("num mimeTypes found:"+mimes.size());
        if(!mimes.isEmpty()){
            Iterator it = mimes.iterator();
            while(it.hasNext()){
                MimeType mimeTypeFound = (MimeType) it.next();
                logger.debug("file has mimeType '"+mimeTypeFound+"'");

                if(
                    mimeTypeFound.toString().compareTo(mimeType.toString()) == 0
                ){
                    logger.debug("found!");
                    return true;
                }
            }
        }
        return false;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String getDescription() {
        return description;
    }
}


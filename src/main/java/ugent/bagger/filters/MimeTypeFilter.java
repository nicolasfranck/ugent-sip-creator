package ugent.bagger.filters;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author nicolas
 */
public class MimeTypeFilter extends FileFilter{

    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    String description;
    MimeType mimeType;
    static final Log log = LogFactory.getLog(MimeTypeFilter.class);

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
        log.debug("filtering file: "+file.getAbsolutePath());
        return file.canRead() && (
            file.isDirectory() ||
            isMimeTypeCorrect(file)
        );
    }
    boolean isMimeTypeCorrect(File file){
        log.debug("isMimeTypeCorrect of file '"+file.getAbsolutePath()+"'");
        log.debug("check against mimeType '"+mimeType+"'");
        Collection mimes = MimeUtil.getMimeTypes(file);
        log.debug("num mimeTypes found:"+mimes.size());
        if(!mimes.isEmpty()){
            Iterator it = mimes.iterator();
            while(it.hasNext()){
                MimeType mimeTypeFound = (MimeType) it.next();
                log.debug("file has mimeType '"+mimeTypeFound+"'");

                if(
                    mimeTypeFound.toString().compareTo(mimeType.toString()) == 0
                ){
                    log.debug("found!");
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


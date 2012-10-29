package ugent.bagger.params;

import eu.medsea.mimeutil.MimeUtil;

/**
 *
 * @author nicolas
 */
public abstract class AbstractFile {
    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }    
    public abstract String getName();
    public abstract long getSize();
    public abstract Object getFile();
    public abstract long lastModified();
    public abstract boolean isReadable();
    public abstract boolean isWritable();
    public abstract boolean isDirectory();
    public abstract boolean isFile();
    public abstract String getMimeType();
    @Override
    public String toString(){
        return getName();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import java.io.File;
import java.util.Collection;
/**
 *
 * @author nicolas
 */
public class FileSource extends File{
    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }
        
    private long lastModified = -1;
    private Collection<MimeType> mimeTypes;

    public FileSource(String path){
        super(path);        
    }
    public FileSource(File file){
        super(file.getAbsolutePath());        
    }
    public FileSource(String path,boolean setMimeType){
        super(path);        
        if(setMimeType)getMimeTypes();
    }
    public FileSource(File file,boolean setMimeType){
        super(file.getAbsolutePath());        
        if(setMimeType)getMimeTypes();
    }
    public Collection<MimeType> getMimeTypes() {
        if(
                mimeTypes == null ||
                lastModified < this.lastModified()
        ){
            this.setMimeTypes(MimeUtil.getMimeTypes(this));
        }
        return mimeTypes;
    }
    private void setMimeTypes(Collection<MimeType> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }   
    @Override
    public String toString(){
        return this.getName();
    }
}

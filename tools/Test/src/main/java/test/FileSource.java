/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import java.io.File;
import java.util.Collection;
/**
 *
 * @author nicolas
 */
public class FileSource extends File{

    private File target;
    //aanvankelijk gelijk aan 'this', maar naarmate het hernoemd wordt niet meer
    private File copy;
    private long lastModified = -1;
    private Collection<MimeType> mimeTypes;

    public FileSource(String path){
        super(path);
        setCopy(this);
    }
    public FileSource(File file){
        super(file.getAbsolutePath());
        setCopy(this);
    }
    public FileSource(String path,boolean setMimeType){
        super(path);
        setCopy(this);
        if(setMimeType)getMimeTypes();
    }
    public FileSource(File file,boolean setMimeType){
        super(file.getAbsolutePath());
        setCopy(this);
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
    public File getTarget() {
        return target;
    }
    public void setTarget(File target) {
        this.target = target;
    }

    public File getCopy() {
        return copy;
    }
    public void setCopy(File copy) {
        this.copy = new File(copy.getAbsolutePath());
    }
    @Override
    public String toString(){
        return this.copy.getName();
    }
}

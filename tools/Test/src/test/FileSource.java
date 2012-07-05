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
    private String label = null;
    private long lastModified = -1;
    private Collection<MimeType> mimeTypes;

    public FileSource(String path){
        super(path);
        setLabel(this.getName());
    }
    public FileSource(File file){
        super(file.getAbsolutePath());
        setLabel(this.getName());
    }
    public FileSource(String path,boolean setMimeType){
        super(path);
        setLabel(this.getName());
        if(setMimeType)getMimeTypes();
    }
    public FileSource(File file,boolean setMimeType){
        super(file.getAbsolutePath());
        setLabel(this.getName());
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
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    @Override
    public String toString(){
        return this.label;
    }
}

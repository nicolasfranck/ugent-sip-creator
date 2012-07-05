/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
import eu.medsea.mimeutil.MimeUtil;
import java.io.File;
import java.util.Collection;
/**
 *
 * @author nicolas
 */
public class FileInfo {
    
    private String label = null;
    private File source = null;
    private long sourceLastModified = -1;
    private File target = null;
    private Collection mimeTypes = null;
    
    public FileInfo(File source){
        setSource(source);
        setLabel(source.getName());
    }
    public FileInfo(File source,boolean checkMimeType){
        setSource(source,checkMimeType);
        setLabel(source.getName());
    }  
    public FileInfo(FileInfo fi){
        this.source = fi.getSource();
        this.target = fi.getTarget();
        this.label = fi.getLabel();
        this.mimeTypes = fi.getMimeTypes();
    }
    public Collection getMimeTypes(){
        if(
                mimeTypes == null ||
                sourceLastModified < this.source.lastModified()
        ){
            this.setMimeTypes(MimeUtil.getMimeTypes(source));
        }
        return mimeTypes;
    }
    private void setMimeTypes(Collection mimeTypes){
        this.mimeTypes = mimeTypes;
    }
    public File getSource() {
        return source;
    }
    public void setSource(File source) {
        setSource(source,false);
    }
    public void setSource(File source,boolean checkMimeType){
        this.source = source;        
        if(checkMimeType)
            this.setMimeTypes(MimeUtil.getMimeTypes(source));
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

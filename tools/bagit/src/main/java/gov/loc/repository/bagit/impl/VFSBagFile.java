package gov.loc.repository.bagit.impl;

import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.utilities.VFSHelper;
import java.io.InputStream;
import org.apache.commons.vfs2.FileObject;

public final class VFSBagFile implements BagFile {
    private String filepath;
    private String fileURI = null;

    public VFSBagFile(String name, FileObject fileObject){
        setFilePath(name);
        setFileObject(fileObject);       
    }

    public VFSBagFile(String name, String fileURI) {
        setFilePath(name);
        setFileURI(fileURI);        
    }

    public String getFileURI() {
        return fileURI;
    }

    public void setFileURI(String fileURI) {
        this.fileURI = fileURI;
    }

    public FileObject getFileObject() {
        return VFSHelper.getFileObject(getFileURI());
    }
    public void setFileObject(FileObject fileObject){
        setFileURI(fileObject.getName().getURI());        
    }

    @Override
    public InputStream newInputStream() {
        try{
            return getFileObject().getContent().getInputStream();
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getFilepath() {
        return filepath;
    }
    @Override
    public void setFilePath(String name) {
        this.filepath = name;
    }

    @Override
    public boolean exists(){
        try {
            if(getFileObject() != null && getFileObject().exists()){
                return true;
            }
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    @Override
    public long getSize() {
        try {
            return getFileObject().getContent().getSize();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

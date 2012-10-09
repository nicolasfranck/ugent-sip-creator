package gov.loc.repository.bagit.impl;

import gov.loc.repository.bagit.BagFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileBagFile implements BagFile {
    private File file;
    private String filepath;

    public FileBagFile(String filepath, File file) {
        this.filepath = filepath;
        this.file = file;
    }

    @Override
    public InputStream newInputStream() {
        try{
            return new FileInputStream(file);
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
    public void setFilePath(String filepath) {
        this.filepath = filepath;
    }
    public File getFile(){
        return file;
    }
    public void setFile(File file){
        this.file = file;
    }

    @Override
    public boolean exists() {
        return (file != null && file.exists());
        //Nicolas Franck
        /*
        if(file != null && file.exists()) {
                return true;			
        }
        return false;*/
    }

    @Override
    public long getSize() {
        return exists() ? file.length():0L;        
    }        
}
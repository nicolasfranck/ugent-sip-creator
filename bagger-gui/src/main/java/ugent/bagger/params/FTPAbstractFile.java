package ugent.bagger.params;

import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author nicolas
 */
public class FTPAbstractFile extends AbstractFile {
    private FTPFile file;
    public FTPAbstractFile(FTPFile file){
        this.file = file;
    }
    @Override
    public String getName() {
        return file.getName();
    }
    @Override
    public long getSize() {
        return file.getSize();
    }
    @Override
    public Object getFile() {
        return file;
    }
    @Override
    public long lastModified() {
        return file.getTimestamp().getTimeInMillis();
    }
    @Override
    public boolean isReadable() {
        return file.hasPermission(FTPFile.USER_ACCESS,FTPFile.READ_PERMISSION);
    }
    @Override
    public boolean isWritable() {
        return file.hasPermission(FTPFile.USER_ACCESS,FTPFile.WRITE_PERMISSION);
    } 
    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public String getMimeType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
}
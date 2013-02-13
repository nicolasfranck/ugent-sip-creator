package ugent.bagger.params;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import java.io.File;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author nicolas
 */
public class FileAbstractFile extends AbstractFile{
    File file;    
    static DateFormat formatter = DateFormat.getInstance();
    
    public FileAbstractFile(File file){
        this.file = file;
    }
    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public Object getFile() {
        return file;
    }

    @Override
    public long getLastModified() {
        return file.lastModified();
    }
    @Override
    public Date getLastModifiedDate(){                
        return new Date(getLastModified());
        //return formatter.format(new Date(getLastModified()));
    }

    @Override
    public boolean isReadable() {
        return file.canRead();
    }

    @Override
    public boolean isWritable() {
        return file.canWrite();
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
        if(file.isFile()){                    
            Collection mimes = MimeUtil.getMimeTypes(file);
            if(!mimes.isEmpty()){
                Iterator it = mimes.iterator();
                while(it.hasNext()){                          
                    return ((MimeType)it.next()).toString();
                }
            }else{
                return "application/octet-stream";
            }
        }
        return "";        
    }    
}

package gov.loc.repository.bagit.impl;

import gov.loc.repository.bagit.BagFile;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringBagFile implements BagFile {

    private String filepath;
    private byte[] buf = new byte[0];
    private static final String ENC = "utf-8";

    public StringBagFile(String name, byte[] data){
        this.filepath = name;
        this.buf = data;
    }

    public StringBagFile(String name, String str) {		
        this.filepath = name;
        if (str != null) {
            try {
                this.buf = str.getBytes(ENC);
            }
            catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public boolean exists() {        
        return buf.length > 0;
        //Nicolas Franck
        /*
        if(buf.length == 0) {
            return false;
        }
        return true;*/
    }

    @Override
    public String getFilepath() {
        return filepath;
    }
    @Override
    public long getSize() {
        return buf.length;
    }
    @Override
    public InputStream newInputStream() {
        return new ByteArrayInputStream(buf);
    }
    @Override
    public void setFilePath(String name) {
        this.filepath = name;
    }
}
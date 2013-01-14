package ugent.bagger.exporters;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.vfs2.FileObject;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ugent.bagger.helper.XML;

/**
 *
 * @author nicolas
 */
public class DSpaceSIPSimpleArchive {
    // Zip file compression level
    protected int compression = 0;
    //list files
    protected HashMap<String,HashMap<String,PackageFile>>packageFiles = new HashMap<String,HashMap<String,PackageFile>>();
    //list metadata
    protected HashMap<String,HashMap<String,Document>>metadata = new HashMap<String,HashMap<String,Document>>();
    protected static final String CONTENTS = "contents";
    protected static final String EOL = System.getProperty("line.separator");
    protected static final int BUFFER_SIZE = 1024 * 4;    
    
    public void setMetadata(String dir,String type,File file) throws ParserConfigurationException, SAXException, IOException{
        setMetadata(dir,type,XML.XMLToDocument(file));
    }
    public void setMetadata(String dir,String type,Document doc){                        
        if(!metadata.containsKey(dir)){
            metadata.put(dir,new HashMap<String,Document>());
        }
        metadata.get(dir).put(type,doc);
    }    
    public void setFile(String dir,String name,File file){
        setFile(dir,name,new PackageFileSimple(file));
    }
    public void setFile(String dir,String name,FileObject fileObject){
        setFile(dir,name,new PackageFileObject(fileObject));
    }
    public void setFile(String dir,String name,PackageFile packageFile){
        Assert.notNull(name);
        Assert.notNull(packageFile);
        if(name.equals(CONTENTS)){
            return;
        }
        if(!packageFiles.containsKey(dir)){
            packageFiles.put(dir,new HashMap<String,PackageFile>());
        }        
        packageFiles.get(dir).put(name,packageFile);
    }
    protected void writeContents(HashMap<String,PackageFile>files,OutputStream out) throws IOException{
        Iterator<String>keys = files.keySet().iterator();
        while(keys.hasNext()){                                
            out.write((keys.next()+EOL).getBytes());
        }
    }
    protected static void copyStream(final InputStream input, final OutputStream output)throws IOException{        
        final byte[] buffer = new byte[BUFFER_SIZE];        
        int bytesRead = 0;        
        while((bytesRead = input.read(buffer,0,BUFFER_SIZE)) > 0){
            output.write(buffer,0,bytesRead);
        }        
    }

    public void write(OutputStream out) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        ZipOutputStream zip = new ZipOutputStream(out);        

        // NOTE: Never set method to ZipOutputStream.STORED since that mode
        // demands setting size of each entry, we don't know it for manifest.
        // Oddly, this works even when compression is NO_COMPRESSION.
        zip.setLevel(compression);
        zip.setMethod(ZipOutputStream.DEFLATED);
        
        for(Map.Entry<String,HashMap<String,PackageFile>>e:packageFiles.entrySet()){
            String dir = e.getKey();
            HashMap<String,PackageFile>files = e.getValue();
            
            // write 'contents' first.
            ZipEntry contents = new ZipEntry(dir+"/"+CONTENTS);        
            zip.putNextEntry(contents);
            writeContents(files,zip);
            zip.closeEntry();
            
            // copy all files, incl. bitstreams, into zip
            for(Map.Entry<String,PackageFile>e2:files.entrySet()){
                PackageFile packageFile = e2.getValue();        
                ZipEntry ze = new ZipEntry(dir+"/"+e2.getKey());            
                zip.putNextEntry(ze);            
                copyStream(packageFile.newInputStream(),zip);
                zip.closeEntry();            
            }       
            //write metadata (and possible overwrite old entries)
            if(metadata.containsKey(dir)){
                HashMap<String,Document>docs = metadata.get(dir);
                for(Map.Entry<String,Document>e2:docs.entrySet()){
                    String key = e2.getKey();
                    Document doc = e2.getValue();
                    String n;
                    if(key.equals("dc")){
                        n = dir+"/dublin_core.xml";
                    }else{
                        n = dir+"/metadata_"+key+".xml";
                    }
                    ZipEntry ze = new ZipEntry(n);
                    zip.putNextEntry(ze);
                    XML.DocumentToXML(doc,zip);
                    zip.closeEntry();
                }
            }
        }
        
        zip.close();
    }
    
    public static abstract class PackageFile {        
        public abstract InputStream newInputStream() throws IOException;                
    }
    public static class PackageFileSimple extends PackageFile{
        protected File file;        
        public PackageFileSimple(File file){
            this.file = file;            
        }
        @Override
        public InputStream newInputStream() throws IOException{
            return new BufferedInputStream(new FileInputStream(file));            
        }        
    }
    public static class PackageFileObject extends PackageFile {        
        protected FileObject fileObject;
        public PackageFileObject(FileObject fileObject){            
            this.fileObject = fileObject;                                    
        }
        @Override
        public InputStream newInputStream() throws IOException {
            return fileObject.getContent().getInputStream();
        }        
    }
    /*
    public static void main(String [] args){
        File [] directories = new File [] {
            new File("/home/nicolas/pakket"),
            new File("/home/nicolas/mets")
        };
        
        
        DSpaceSIPSimpleArchive sip = new DSpaceSIPSimpleArchive();
        
        try{
            for(File dir:directories){
                ArrayList<File>list = FUtils.listFiles(dir,true);
                for(File file:list){
                    if(file.getAbsolutePath().equals(dir.getAbsolutePath()+"/dublin_core.xml")){
                        System.out.println("setting dublin core");
                        sip.setMetadata(dir.getName(),"dc",file);
                    }else{
                        String relativeName = file.getAbsolutePath().replace(dir.getAbsolutePath()+"/","");
                        System.out.println("relativeName: "+relativeName);
                        sip.setFile(dir.getName(),relativeName,file);                    
                    }
                }
            }
            sip.write(new BufferedOutputStream(new FileOutputStream(new File("/tmp/simple-archive.zip"))));
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
}

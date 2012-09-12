/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package creator;

//BagIt
import com.anearalone.mets.AmdSec;
import com.anearalone.mets.FileSec;
import com.anearalone.mets.FileSec.FileGrp;
import com.anearalone.mets.FileSec.FileGrp.File;
import com.anearalone.mets.FileSec.FileGrp.File.FLocat;
import com.anearalone.mets.LocatorElement.LOCTYPE;
import com.anearalone.mets.LocatorElement.TYPE;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.MdSec.MDTYPE;
import com.anearalone.mets.MdSec.MdWrap;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsWriter;
import com.anearalone.mets.SharedEnums.CHECKSUMTYPE;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.ProgressListener;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.FileSystemWriter;
import gov.loc.repository.bagit.writer.impl.TarBz2Writer;
import gov.loc.repository.bagit.writer.impl.TarGzWriter;
import gov.loc.repository.bagit.writer.impl.TarWriter;
import gov.loc.repository.bagit.writer.impl.ZipWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class BagItMETSCreator {
    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }
    
    private ArrayList<java.io.File>dmdFiles = new ArrayList<java.io.File>();
    private ArrayList<Amd>amds = new ArrayList<Amd>();
    
    private ArrayList<java.io.File>payloads = new ArrayList<java.io.File>();
    private java.io.File file;
    
    private static HashMap<String,MDTYPE>mappingNamespaceType = new HashMap<String,MDTYPE>() {{
        put("http://www.loc.gov/MARC21/slim",MDTYPE.MARC);
        put("http://www.loc.gov/mods/v3",MDTYPE.MODS);
        put("info:lc/xmlns/premis-v2",MDTYPE.PREMIS);
        put("urn:isbn:1-931666-22-9",MDTYPE.EAD);        
        
    }};

    public ArrayList<Amd> getAmds() {
        return amds;
    }

    public void setAmds(ArrayList<Amd> amds) {
        this.amds = amds;
    }

    
    public ArrayList<java.io.File> getDmdFiles() {
        return dmdFiles;
    }
    public void setDmdFiles(ArrayList<java.io.File> dmdFiles) {
        this.dmdFiles = dmdFiles;
    } 
    public ArrayList<java.io.File> getPayloads() {
        return payloads;
    }
    public void setPayloads(ArrayList<java.io.File> payloads) {
        this.payloads = payloads;
    }
    public java.io.File getFile() {
        return file;
    }
    public void setFile(java.io.File file) {
        this.file = file;
    }  
    
    
    private static String getMimeType(java.io.File file){        
        Collection mimes = MimeUtil.getMimeTypes(file);
        if(!mimes.isEmpty()){
            Iterator it = mimes.iterator();            
            while(it.hasNext()){
                return ((MimeType)it.next()).toString();
            }
        }
        return "application/octet-stream";
    }
    public Bag createBag(){
        Bag bag = null;
        
        try{

            //start bag                     
            BagFactory bagFactory = new BagFactory();
            bag = bagFactory.createBag();      
            
            //add payloads     
            for(java.io.File file:getPayloads()){
                bag.addFileToPayload(file);
            }              
            
            
            //add tags (mets)
            java.io.File tempDir = new java.io.File(
                System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString()
            );
            tempDir.mkdirs();            
            java.io.File tempMetsFile = new java.io.File(tempDir.getAbsolutePath()+"/mets.xml");                                

            Mets mets = new Mets();

            //files to mets
            FileSec fileSec = new FileSec();
            mets.setFileSec(fileSec);
            List<FileGrp>fileGrps = fileSec.getFileGrp();
            FileGrp fileGrpMA = new FileGrp();
            fileGrps.add(fileGrpMA);
            fileGrpMA.setUse("MASTER");
            fileGrpMA.setID(UUID.randomUUID().toString());
            List<File>files = fileGrpMA.getFile();

            for(java.io.File sourceFile:getPayloads()){
                ArrayList<java.io.File>list = ugent.bagger.helper.FileUtils.listFiles(sourceFile);                         
                for(java.io.File file:list){                                                  
                    String pathInBag = "data" +
                        file.getAbsolutePath().replaceFirst(
                                sourceFile.getParentFile().getAbsolutePath()
                                ,""
                        );                   
                   
                    File metsFile = new File(MD5(file.getAbsolutePath()));

                    Map<Manifest.Algorithm,String> mapChecksums = bag.getChecksums(pathInBag);

                    metsFile.setSIZE(file.length());                                     
                    metsFile.setMIMETYPE(getMimeType(file));

                    if(mapChecksums.containsKey(Manifest.Algorithm.MD5)){                        
                        metsFile.setCHECKSUMTYPE(CHECKSUMTYPE.MD_5);
                        metsFile.setCHECKSUM(mapChecksums.get(Manifest.Algorithm.MD5));
                    }
                    files.add(metsFile);
                    List<FLocat>flocats = metsFile.getFLocat();
                    FLocat flocat = new FLocat();
                    flocat.setLOCTYPE(LOCTYPE.URL);
                    flocat.setXlinkHREF(pathInBag);
                    flocat.setXlinkType(TYPE.SIMPLE);
                    flocats.add(flocat);                   
                }
            }
            
            //add tags
            
            //add dmdSec
            for(java.io.File dmdFile:dmdFiles){                
                mets.getDmdSec().add(makeMdSec(dmdFile));           
            }  
            //add amdSec
            for(Amd amd:getAmds()){
                AmdSec amdSec = new AmdSec();
                
                for(java.io.File f:amd.getDigiprovMDFile()){                                        
                    amdSec.getDigiprovMD().add(makeMdSec(f));
                }
                for(java.io.File f:amd.getRightsMDFile()){                    
                    amdSec.getDigiprovMD().add(makeMdSec(f));
                }
                for(java.io.File f:amd.getSourceMDFile()){                    
                    amdSec.getSourceMD().add(makeMdSec(f));
                }
                for(java.io.File f:amd.getTechMDFile()){                    
                    amdSec.getTechMD().add(makeMdSec(f));
                }
                
                mets.getAmdSec().add(amdSec);
            }         
            
            //write mets
            MetsWriter mw = new MetsWriter();                    
            mw.writeToFile(mets,tempMetsFile);
            
            bag.addFileAsTag(tempMetsFile);
            
            
            //makeComplete geeft NIEUWE bag terug! (oude: enkel data directory)            
            bag = bag.makeComplete();
            
            //Writer writer = new FileSystemWriter(bagFactory);
            Writer writer;
            
            if(getFile().getName().endsWith(".zip")){
                writer = new ZipWriter(bagFactory);
            }else if(getFile().getName().endsWith(".tar")){
                writer = new TarWriter(bagFactory);
            }else if(getFile().getName().endsWith(".tar.gz")){
                writer = new TarGzWriter(bagFactory);                
            }else if(getFile().getName().endsWith(".tar.bz2")){
                writer = new TarBz2Writer(bagFactory);
            }else{
                writer = new FileSystemWriter(bagFactory);
            }     
            
            writer.addProgressListener(new ProgressListener() {
                @Override
                public void reportProgress(String activity, Object o, Long count, Long total) {
                    System.out.println(activity+": "+count+"/"+total);
                }
            });            
            writer.write(bag,getFile());            
            
            
            
            //delete temp dir
            FileUtils.deleteDirectory(tempDir);            
            
            //stats
            System.out.println("payloads:");
            for(BagFile bagFile:bag.getPayload()){
                System.out.println("\t"+bagFile.getFilepath()+" ("+bagFile.getSize()+")");
            }
            System.out.println("tags:");
            for(BagFile bagFile:bag.getTags()){
                System.out.println("\t"+bagFile.getFilepath()+" ("+bagFile.getSize()+")");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return bag;
    }   
    private static String MD5(String input) {
       try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
              sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
           }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }       
        return null;
    }
    private static MdSec makeMdSec(java.io.File file) throws ParserConfigurationException, SAXException, IOException{
        MdSec mdSec = new MdSec(file.getName());
        mdSec.setMdWrap(makeMdWrap(file));
        return mdSec;
    }
    private static MdWrap makeMdWrap(java.io.File file) throws ParserConfigurationException, SAXException, IOException{
        Document doc = ugent.bagger.helper.XML.XMLToDocument(file);
        Element element = doc.getDocumentElement();
        System.out.println("namespace: "+element.getNamespaceURI());
        MdWrap wrapper = new MdWrap(getMetadataType(element.getNamespaceURI()));                                                    
        wrapper.setMIMETYPE("text/xml");
        wrapper.getXmlData().add(element);
        return wrapper;
    }    
    public static MDTYPE getMetadataType(String namespace){
        if(mappingNamespaceType.containsKey(namespace))return mappingNamespaceType.get(namespace);
        return MDTYPE.OTHER;
    }
    public static void main(String [] args) throws IOException{
        java.io.File [] list = new java.io.File("/home/nicolas/bhsl-pap").listFiles();
        java.io.File saveAsFile = new java.io.File("/tmp/output");        
        
        
        BagItMETSCreator creator = new BagItMETSCreator();
        creator.setFile(saveAsFile);
        creator.setPayloads(new ArrayList<java.io.File>(Arrays.asList(list)));
        creator.setDmdFiles(ugent.bagger.helper.FileUtils.listFiles(new java.io.File("/home/nicolas/ead")));
        
        
        Amd amd = new Amd();
        amd.getDigiprovMDFile().addAll(ugent.bagger.helper.FileUtils.listFiles(new java.io.File("/home/nicolas/ead")));
        amd.getSourceMDFile().addAll(ugent.bagger.helper.FileUtils.listFiles(new java.io.File("/home/nicolas/ead")));
        amd.getTechMDFile().addAll(ugent.bagger.helper.FileUtils.listFiles(new java.io.File("/home/nicolas/ead")));
        amd.getRightsMDFile().addAll(ugent.bagger.helper.FileUtils.listFiles(new java.io.File("/home/nicolas/ead")));        
        
        creator.getAmds().add(amd);
        
        creator.createBag();
    }
   
}

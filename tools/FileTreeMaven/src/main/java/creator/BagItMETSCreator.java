/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package creator;

//BagIt
import com.anearalone.mets.FileSec;
import com.anearalone.mets.FileSec.FileGrp;
import com.anearalone.mets.FileSec.FileGrp.File;
import com.anearalone.mets.FileSec.FileGrp.File.FLocat;
import com.anearalone.mets.LocatorElement.LOCTYPE;
import com.anearalone.mets.LocatorElement.TYPE;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsWriter;
import com.anearalone.mets.SharedEnums.CHECKSUMTYPE;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.ProgressListener;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.FileSystemWriter;
import gov.loc.repository.bagit.writer.impl.ZipWriter;

//METS
import com.anearalone.mets.StructMap.Div.*;

//Standard
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author nicolas
 */
public class BagItMETSCreator {
    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }
    public static String getMimeType(java.io.File file){
        System.out.println("getting mimetype for "+file.getAbsolutePath());
        System.out.println("exists? "+(file.exists() ? "yes":"no"));
        Collection mimes = MimeUtil.getMimeTypes(file);
        if(!mimes.isEmpty()){
            Iterator it = mimes.iterator();
            while(it.hasNext()){
                return ((MimeType)it.next()).toString();
            }
        }
        return "application/octet-stream";
    }
    public static void main(String [] args) throws IOException{
        try{

            BagFactory bagFactory = new BagFactory();
            Bag bag = bagFactory.createBag();

            //add payloads
            bag.addFilesToPayload(helper.FileUtils.listFiles("/home/nicolas/bhsl-pap"));

            bag = bag.makeComplete();

            //add tags
            //java.io.File tempMetsFile = java.io.File.createTempFile(UUID.randomUUID().toString(),UUID.randomUUID().toString());
            //tempMetsFile.deleteOnExit();

            java.io.File tempMetsFile = new java.io.File("/tmp/mets.xml");

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

            for(BagFile payload:bag.getPayload()){
                System.out.println("adding payload to mets file: "+payload.getFilepath());
                File metsFile = new File(payload.getFilepath());

                Map<Manifest.Algorithm,String> mapChecksums = bag.getChecksums(payload.getFilepath());

                metsFile.setSIZE(payload.getSize());
                
                metsFile.setMIMETYPE(getMimeType(
                    new java.io.File(bag.getFile().getAbsolutePath()+"/"+payload.getFilepath())
                ));

                if(mapChecksums.containsKey(Manifest.Algorithm.MD5)){
                    System.out.println("CHECKSUM found!!!!!");
                    metsFile.setCHECKSUMTYPE(CHECKSUMTYPE.MD_5);
                    metsFile.setCHECKSUM(mapChecksums.get(Manifest.Algorithm.MD5));
                }
                
                
                files.add(metsFile);

                List<FLocat>flocats = metsFile.getFLocat();
                FLocat flocat = new FLocat();
                flocat.setLOCTYPE(LOCTYPE.URL);
                flocat.setXlinkHREF(payload.getFilepath());
                flocat.setXlinkType(TYPE.SIMPLE);
                flocats.add(flocat);
            }
            MetsWriter mw = new MetsWriter();           
            mw.writeToFile(mets,tempMetsFile);

            //makeComplete geeft NIEUWE bag terug! (oude: enkel data directory)
            
            Writer writer = new FileSystemWriter(bagFactory);
            writer.addProgressListener(new ProgressListener() {
                @Override
                public void reportProgress(String activity, Object o, Long count, Long total) {
                    System.out.println(activity+": "+count+"/"+total);
                }
            });
            MessageDigest.getInstance("MD5").digest();
            writer.write(bag,new java.io.File("/tmp/newbag"));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static String MD5(String input) {
       try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
              sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
           }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}

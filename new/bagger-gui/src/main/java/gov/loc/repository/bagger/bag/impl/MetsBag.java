package gov.loc.repository.bagger.bag.impl;

import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsWriter;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.Manifest.Algorithm;
import gov.loc.repository.bagit.utilities.MessageDigestHelper;
import gov.loc.repository.bagit.writer.Writer;
import java.io.File;

/**
 *
 * @author nicolas
 */
public class MetsBag extends DefaultBag{
    private BagItMets bagItMets;
    
    public MetsBag() {  
        super();            
    }
    public MetsBag(File rootDir, String version) {
        super(rootDir,version);        
    }    

    public BagItMets getBagItMets() {        
        return bagItMets;
    }

    public void setBagItMets(BagItMets bagItMets) {
        this.bagItMets = bagItMets;
    }
     
    @Override
    public String write(Writer bw){        
        prepareBilBagInfoIfDirty();
        generateManifestFiles();
        
        Bag bag = getBag();        
        
        Mets mets = getBagItMets().onSaveBag(
            bag,
            BagView.getInstance().getInfoInputPane().getInfoInputPane().getMets()
        );
        
        if(mets == null){
            mets = new Mets();
        }
        File tmpdir = new File(System.getProperty("java.io.tmpdir"));
        File metsFile = new File(tmpdir,"mets.xml");
        
        try{
            MetsWriter metsWriter = new MetsWriter();
            metsWriter.writeToFile(mets,metsFile);
            addTagFile(metsFile);
            
            //manually add checksum
            Algorithm ta = resolveAlgorithm(getTagManifestAlgorithm());
            Manifest tagManifest = bag.getTagManifest(ta);
            String checksumMets = MessageDigestHelper.generateFixity(metsFile,ta);
            tagManifest.remove("mets.xml");
            tagManifest.put("mets.xml",checksumMets);
            
        }catch(Exception e){              
            e.printStackTrace();            
        }        
        String messages = writeBag(bw);
        if (bw.isCancelled()) {
            return "Save cancelled.";
        }
        return messages;       
        
    }
}
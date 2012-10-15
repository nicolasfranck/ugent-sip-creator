package gov.loc.repository.bagger.bag.impl;

import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsWriter;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.Manifest.Algorithm;
import gov.loc.repository.bagit.utilities.MessageDigestHelper;
import gov.loc.repository.bagit.writer.Writer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

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
            BagView.getInstance().getInfoFormsPane().getInfoInputPane().getMets()
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
    @Override
    public void createPreBag(File data, String version) {
        super.createPreBag(data,version);
        //pre bag steeds een map, dus we kunnen prutsen naar believen
        Bag bag = getBag();        
        
        Mets mets = getBagItMets().onSaveBag(
            bag,
            new Mets()
        );
        
        File metsFile = new File(bag.getFile(),"mets.xml");
        
        try{
            MetsWriter metsWriter = new MetsWriter();
            metsWriter.writeToFile(mets,metsFile);            
            
            //manually add checksum
            Algorithm ta = resolveAlgorithm(getTagManifestAlgorithm());
            
            Manifest tagManifest = bag.getTagManifest(ta);
            String checksumMets = MessageDigestHelper.generateFixity(metsFile,ta);            
            
            File tagmanifestFile = new File(bag.getFile(),"tagmanifest-"+ta.bagItAlgorithm+".txt");                       
            
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(tagmanifestFile,true)));
            writer.println(checksumMets+"  mets.xml");            
            writer.close();
            
        }catch(Exception e){              
            e.printStackTrace();            
        } 
    }
    @Override
    public void createPreBagAddKeepFilesToEmptyFolders(File data,String version){
        super.createPreBagAddKeepFilesToEmptyFolders(data,version);
        //pre bag steeds een map, dus we kunnen prutsen naar believen
        Bag bag = getBag();        
        
        Mets mets = getBagItMets().onSaveBag(
            bag,
            new Mets()
        );
        
        File metsFile = new File(bag.getFile(),"mets.xml");
        
        try{
            MetsWriter metsWriter = new MetsWriter();
            metsWriter.writeToFile(mets,metsFile);            
            
            //manually add checksum
            Algorithm ta = resolveAlgorithm(getTagManifestAlgorithm());
            
            Manifest tagManifest = bag.getTagManifest(ta);
            String checksumMets = MessageDigestHelper.generateFixity(metsFile,ta);            
            
            File tagmanifestFile = new File(bag.getFile(),"tagmanifest-"+ta.bagItAlgorithm+".txt");                       
            
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(tagmanifestFile,true)));
            writer.println(checksumMets+"  mets.xml");            
            writer.close();
            
        }catch(Exception e){              
            e.printStackTrace();            
        } 
    }
}
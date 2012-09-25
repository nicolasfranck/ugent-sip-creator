package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.FileSec.FileGrp;
import com.anearalone.mets.FileSec.FileGrp.File.FLocat;
import com.anearalone.mets.LocatorElement;
import com.anearalone.mets.Mets;
import com.anearalone.mets.SharedEnums.CHECKSUMTYPE;
import com.anearalone.mets.StructMap;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.Manifest.Algorithm;
import gov.loc.repository.bagit.utilities.MessageDigestHelper;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Handler;
import ugent.bagger.workers.LongTask2;

public class SaveBagHandler2 extends Handler {
    private static final Log log = LogFactory.getLog(SaveBagHandler2.class);
    private static final long serialVersionUID = 1L;    
    private File tmpRootPath;
    private boolean clearAfterSaving = false;
    private String messages;
    
    public SaveBagHandler2() {
        super();        
    }
    //wordt uitgevoerd indien men op de toolbarbutton drukt
    @Override
    public void actionPerformed(ActionEvent e) {
        BagView bagView = BagView.getInstance();
        DefaultBag bag = bagView.getBag();
        bagView.getInfoInputPane().getUpdateBagHandler().updateBag();
        if(bagView.getBagRootPath().exists()) {
            tmpRootPath = bagView.getBagRootPath();
            confirmWriteBag();
        }else {
            saveBag(bagView.getBagRootPath());
        }
    }

    @Override
    public void execute(){        
        SwingUtils.monitor(new SaveBagWorker(),"saving bag","");        
    }

    public void setTmpRootPath(File f) {
        this.tmpRootPath = f;
    }

    public File getTmpRootPath() {
        return this.tmpRootPath;
    }

    public void setClearAfterSaving(boolean b) {
        this.clearAfterSaving = b;
    }

    public boolean getClearAfterSaving() {
        return this.clearAfterSaving;
    }

    public void saveBag(File file) {
    	DefaultBag bag = BagView.getInstance().getBag();
        bag.setRootDir(file);        
        execute();
    }

    public void confirmWriteBag() {
        final BagView bagView = BagView.getInstance();
        ConfirmationDialog dialog = new ConfirmationDialog() {
            boolean isCancel = true;
            @Override
            protected void onConfirm() {
                DefaultBag bag = bagView.getBag();
                if (bag.getSize() > DefaultBag.MAX_SIZE) {
                    confirmAcceptBagSize();
                } else {
                    bagView.setBagRootPath(tmpRootPath);
                    saveBag(bagView.getBagRootPath());
                }
            }
            @Override
            protected void onCancel(){
                super.onCancel();
                if (isCancel) {
                    cancelWriteBag();
                    isCancel = false;
                }
            }
        };
        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(bagView.getPropertyMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(bagView.getPropertyMessage("bag.dialog.message.create"));
        dialog.showDialog();
    }

    private void cancelWriteBag() {
    	clearAfterSaving = false;
    }

    public void confirmAcceptBagSize() {
        final BagView bagView = BagView.getInstance();
        ConfirmationDialog dialog = new ConfirmationDialog() {
            @Override
            protected void onConfirm() {
                bagView.setBagRootPath(tmpRootPath);
                saveBag(bagView.getBagRootPath());
            }
        };
        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(bagView.getPropertyMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(bagView.getPropertyMessage("bag.dialog.message.accept"));
        dialog.showDialog();
    }

    public void saveBagAs() {
        BagView bagView = BagView.getInstance();
    	DefaultBag bag = bagView.getBag();
        File selectFile = new File(File.separator+".");
        JFrame frame = new JFrame();
        JFileChooser fs = new JFileChooser(selectFile);
    	fs.setDialogType(JFileChooser.SAVE_DIALOG);
    	fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	fs.addChoosableFileFilter(bagView.getInfoInputPane().getNoFilter());
    	fs.addChoosableFileFilter(bagView.getInfoInputPane().getZipFilter());
        fs.addChoosableFileFilter(bagView.getInfoInputPane().getTarFilter());
        fs.setDialogTitle("Save Bag As");
    	fs.setCurrentDirectory(bag.getRootDir());
    	if (bag.getName() != null && !bag.getName().equalsIgnoreCase(bagView.getPropertyMessage("bag.label.noname"))) {
            String selectedName = bag.getName();
            if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                selectedName += "."+DefaultBag.ZIP_LABEL;
                fs.setFileFilter(bagView.getInfoInputPane().getZipFilter());
            }
            else if (bag.getSerialMode() == DefaultBag.TAR_MODE) {
                selectedName += "."+DefaultBag.TAR_LABEL;
                fs.setFileFilter(bagView.getInfoInputPane().getTarFilter());
            }
            else {
                fs.setFileFilter(bagView.getInfoInputPane().getNoFilter());
            }
            fs.setSelectedFile(new File(selectedName));
    	}
    	int option = fs.showSaveDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fs.getSelectedFile();
            save(file);
        }
    }

    public void save(File file) {
        BagView bagView = BagView.getInstance();
    	DefaultBag bag = bagView.getBag();
        if (file == null) {
            file = bagView.getBagRootPath();
        }
        bag.setName(file.getName());
        File bagFile = new File(file, bag.getName());
    	if (bagFile.exists()) {
            tmpRootPath = file;
            confirmWriteBag();
    	} else {
            if (bag.getSize() > DefaultBag.MAX_SIZE) {
                tmpRootPath = file;
                confirmAcceptBagSize();
            } else {
                bagView.setBagRootPath(file);
                saveBag(BagView.getInstance().getBagRootPath());
            }
    	}
        String fileName = bagFile.getAbsolutePath();
        bagView.infoInputPane.setBagName(fileName);
        bagView.getControl().invalidate();
    }
    
    private class SaveBagWorker extends LongTask2{
        @Override
        protected Object doInBackground() throws Exception {
            
            BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());                        

            final BagView bagView = BagView.getInstance();
            DefaultBag bag = bagView.getBag();
            
            Writer bagWriter = null;
            
            //Nicolas Franck - begin
            Algorithm tagManifestAlg = Algorithm.MD5;
            CHECKSUMTYPE tagManifestChecksumType = CHECKSUMTYPE.MD_5;
            String tagManifestKey = "md5";
            if(bag.getTagManifestAlgorithm().compareToIgnoreCase(Algorithm.MD5.bagItAlgorithm) == 0){
                tagManifestAlg = Algorithm.MD5;
                tagManifestChecksumType = CHECKSUMTYPE.MD_5;
                tagManifestKey = "md5";
            }else if (bag.getTagManifestAlgorithm().compareToIgnoreCase(Algorithm.SHA1.bagItAlgorithm) == 0){
                tagManifestAlg = Algorithm.SHA1;
                tagManifestChecksumType = CHECKSUMTYPE.SHA_1;
                tagManifestKey = "sha1";
            }else if (bag.getTagManifestAlgorithm().compareToIgnoreCase(Algorithm.SHA256.bagItAlgorithm) == 0){
                tagManifestAlg = Algorithm.SHA256;
                tagManifestChecksumType = CHECKSUMTYPE.SHA_256;
                tagManifestKey = "sha256";
            }else if(bag.getTagManifestAlgorithm().compareToIgnoreCase(Algorithm.SHA512.bagItAlgorithm) == 0){
                tagManifestAlg = Algorithm.SHA512;            
                tagManifestChecksumType = CHECKSUMTYPE.SHA_512;
                tagManifestKey = "sha256";
            }
             
            Algorithm payloadManifestAlg = Algorithm.MD5;
            CHECKSUMTYPE payloadManifestChecksumType = CHECKSUMTYPE.MD_5;
            String payloadManifestKey = "md5";
            if(bag.getTagManifestAlgorithm().compareToIgnoreCase(Algorithm.MD5.bagItAlgorithm) == 0){
                payloadManifestAlg = Algorithm.MD5;
                payloadManifestChecksumType = CHECKSUMTYPE.MD_5;
                payloadManifestKey = "md5";
            }else if (bag.getTagManifestAlgorithm().compareToIgnoreCase(Algorithm.SHA1.bagItAlgorithm) == 0){
                payloadManifestAlg = Algorithm.SHA1;
                payloadManifestChecksumType = CHECKSUMTYPE.SHA_1;
                payloadManifestKey = "sha1";
            }else if (bag.getTagManifestAlgorithm().compareToIgnoreCase(Algorithm.SHA256.bagItAlgorithm) == 0){
                payloadManifestAlg = Algorithm.SHA256;
                payloadManifestChecksumType = CHECKSUMTYPE.SHA_256;
                payloadManifestKey = "sha256";
            }else if(bag.getTagManifestAlgorithm().compareToIgnoreCase(Algorithm.SHA512.bagItAlgorithm) == 0){
                payloadManifestAlg = Algorithm.SHA512;            
                payloadManifestChecksumType = CHECKSUMTYPE.SHA_512;
                payloadManifestKey = "sha512";
            }
            //Nicolas Franck - end

            try {
                BagFactory bagFactory = new BagFactory();
                short mode = bag.getSerialMode();
                if (mode == DefaultBag.NO_MODE) {
                    bagWriter = new FileSystemWriter(bagFactory);
                } else if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                    bagWriter = new ZipWriter(bagFactory);
                } else if (mode == DefaultBag.TAR_MODE) {
                    bagWriter = new TarWriter(bagFactory);
                } else if (mode == DefaultBag.TAR_GZ_MODE) {
                    bagWriter = new TarGzWriter(bagFactory);
                } else if (mode == DefaultBag.TAR_BZ2_MODE) {
                    bagWriter = new TarBz2Writer(bagFactory);
                }                               
                
                bagWriter.addProgressListener(this);                
                messages = bag.write(bagWriter);

                if (messages != null && !messages.trim().isEmpty()) {
                    bagView.showWarningErrorDialog("Warning - bag not saved", "Problem saving bag:\n" + messages);
                } else {
                    bagView.showWarningErrorDialog("Bag saved", "Bag saved successfully.\n" );
                }
                
                //Nicolas Franck - begin
                //write mets after creation bag
                try{              
                    Manifest payloadManifest = bag.getBag().getPayloadManifest(payloadManifestAlg);
                    Manifest tagfileManifest = bag.getBag().getTagManifest(tagManifestAlg);
                    Mets mets = bagView.getInfoInputPane().getBagInfoInputPane().getMets();   

                    //fileSec
                    FileSec fileSec = mets.getFileSec();
                    if(fileSec == null){
                        fileSec = new FileSec();
                        mets.setFileSec(fileSec);
                    }                       
                    List<FileGrp> fileGroups = fileSec.getFileGrp();            
                    fileGroups.clear();
                    FileGrp fileGroup = new FileGrp();            
                    fileGroup.setID("CONTENT");
                    fileGroup.setUse("CONTENT");
                    List<FileSec.FileGrp.File>files = fileGroup.getFile();            

                    for(BagFile bagFile:bag.getPayload()){                   
                        
                        String fileId = bagFile.getFilepath().replace('/','-');
                        
                        FileSec.FileGrp.File metsFile = new FileSec.FileGrp.File(fileId);                                        
                        metsFile.setSIZE(bagFile.getSize());
                        metsFile.setMIMETYPE(FUtils.getMimeType(new File(bagFile.getFilepath())));                                                
                        String checksumFile = payloadManifest.get(bagFile.getFilepath());         
                        metsFile.setCHECKSUM(checksumFile);
                        metsFile.setCHECKSUMTYPE(payloadManifestChecksumType);                                                     
                        
                        FLocat flocat = new FLocat();
                        flocat.setLOCTYPE(LocatorElement.LOCTYPE.URL);
                        flocat.setXlinkHREF(bagFile.getFilepath());                 
                        metsFile.getFLocat().add(flocat);
                        
                        files.add(metsFile);                                      
                    }
                    fileGroups.add(fileGroup);

                    //structMap
                    DefaultMutableTreeNode rootNodePayloads = bagView.getBagPayloadTree().getParentNode();              
                    DefaultMutableTreeNode rootNodeTagFiles = bagView.getBagTagFileTree().getParentNode();
                    StructMap structMapPayloads = MetsUtils.toStructMap(rootNodePayloads);
                    structMapPayloads.setType("BAGIT_PAYLOAD_TREE");
                    StructMap structMapTagFiles = MetsUtils.toStructMap(rootNodeTagFiles);
                    structMapTagFiles.setType("BAGIT_TAGFILE_TREE");

                    mets.getStructMap().clear();
                    mets.getStructMap().add(structMapPayloads);
                    mets.getStructMap().add(structMapTagFiles);                                        
                    
                    //now write mets and updated tagmanifest to bagit
                    String pathMets;
                    String pathTagManifest;
                    String extension = "";
                    String name = bag.getBagFile().getName();
                    int i = name.lastIndexOf('.');
                    String baseName = (i >= 0) ? name.substring(0,i):name;                    
                    extension  = (i > 0 && i < name.length() - 1)? name.substring(i + 1).toLowerCase():extension;                    
                    
                    System.out.println("bagFile: "+bag.getBagFile().getAbsolutePath()+", exists: "+bag.getBagFile().exists());
                    
                    if(bag.getSerialMode() != DefaultBag.NO_MODE){                        
                        pathMets = extension+":"+bag.getBagFile().getAbsolutePath()+"!/"+baseName+"/mets.xml";
                        pathTagManifest = extension+":"+bag.getBagFile().getAbsolutePath()+"!/"+baseName+"/tagmanifest-"+tagManifestKey+".txt";
                    }else{
                        pathMets = "file://"+new File(bag.getBagFile(),"mets.xml").getAbsolutePath();
                        pathTagManifest = "file://"+new File(bag.getBagFile(),"tagmanifest-"+tagManifestKey+".txt").getAbsolutePath();
                    }
                    
                    //write mets to tag file                    
                    MetsUtils.writeMets(mets,FUtils.getOutputStreamFor(pathMets));                       

                    //add mets as tagfile to bagit manually
                    String checksumMetsFile = MessageDigestHelper.generateFixity(FUtils.getInputStreamFor(pathMets),tagManifestAlg);
                    tagfileManifest.remove("mets.xml");
                    tagfileManifest.put("mets.xml",checksumMetsFile);
                    
                    Iterator it = tagfileManifest.keySet().iterator();                    
                    
                    
                    BufferedWriter writer = new BufferedWriter(new PrintWriter(FUtils.getOutputStreamFor(pathTagManifest)));                                                       
                    while(it.hasNext()){  
                        String key = (String)it.next();
                        writer.write(key+"  "+tagfileManifest.get(key) +"\n");
                    }                    
                    

                }catch(Exception e){
                    log.debug(e.getMessage());                
                }
                //Nicolas Franck - end
               
                if (bag.isSerialized()) {
                    if(clearAfterSaving){                       
                        bagView.clearBagHandler.clearExistingBag();
                        setClearAfterSaving(false);
                    }else{
                        if(bag.isValidateOnSave()) {
                            bagView.validateBagHandler.validateBag();
                        }                     
                        File bagFile = bag.getBagFile();
                        log.info("BagView.openExistingBag: " + bagFile);
                        bagView.openBagHandler.openExistingBag(bagFile);
                        bagView.updateSaveBag();
                    }
                } else {
                    ApplicationContextUtil.addConsoleMessage(messages);
                    bagView.updateManifestPane();
                }                
            }catch(Exception e){
                log.debug(e.getMessage());                
            }            
            BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());            
            return null;
        }    
        @Override
        public void cancel(){ 
            BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());            
        }    
    }
}
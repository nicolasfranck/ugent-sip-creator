package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.FileUtils;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.panels.MetsPanel;

public class OpenBagHandler extends AbstractAction {
    private static final Log log = LogFactory.getLog(OpenBagHandler.class);
    private static final long serialVersionUID = 1L;   

    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
    public OpenBagHandler() {
        super();        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());
        openBag();
        BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());
    }

    public void openBag() {
        BagView bagView = BagView.getInstance();
        File selectFile = new File(File.separator+".");        
        JFileChooser fo = new JFileChooser(selectFile);
        fo.setDialogType(JFileChooser.OPEN_DIALOG);
        
        fo.addChoosableFileFilter(bagView.getInfoInputPane().getNoFilter());
        fo.addChoosableFileFilter(bagView.getInfoInputPane().getZipFilter());
        fo.addChoosableFileFilter(bagView.getInfoInputPane().getTarFilter());
        fo.setFileFilter(bagView.getInfoInputPane().getNoFilter());
        fo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if(bagView.getBagRootPath() != null){
            fo.setCurrentDirectory(bagView.getBagRootPath().getParentFile());
        }
        fo.setDialogTitle("Existing Bag Location");
        int option = fo.showOpenDialog(Application.instance().getActiveWindow().getControl());

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fo.getSelectedFile();
            if (file == null) {
                file = bagView.getBagRootPath();
            }
            openExistingBag(file);
        }
    }

    public void openExistingBag(File file) {
        BagView bagView = BagView.getInstance();
        DefaultBag bag = bagView.getBag();
    	bagView.getInfoInputPane().getBagInfoInputPane().enableForms(true);
    	bagView.clearBagHandler.clearExistingBag();

        try{
            bagView.clearBagHandler.newDefaultBag(file);
            System.out.println("file: "+file);
            System.out.println("file afterwards: "+bag.getBagFile());
            ApplicationContextUtil.addConsoleMessage("Opened the bag " + file.getAbsolutePath());
        }catch(Exception ex){
            ApplicationContextUtil.addConsoleMessage("Failed to create bag: " + ex.getMessage());    	    
    	    return;
        }
        
        bagView.getInfoInputPane().setBagVersion(bag.getVersion());
        bagView.getInfoInputPane().setProfile(bag.getProfile().getName());       
        String fileName = file.getAbsolutePath();
        bagView.getInfoInputPane().setBagName(fileName);

    	String name = file.getName();
        int i = name.lastIndexOf('.');
        String baseName = (i >= 0) ? name.substring(0,i):name;
        String extension = "";
        if (i > 0 && i < name.length() - 1) {
            extension = name.substring(i + 1).toLowerCase();
            if (extension.contains("gz")) {
                bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.TAR_GZ_LABEL);                
                bag.setSerialMode(DefaultBag.TAR_GZ_MODE);
                bag.isSerial(true);
            } else if (extension.contains("bz2")) {
                bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.TAR_BZ2_LABEL);                
                bag.setSerialMode(DefaultBag.TAR_BZ2_MODE);
                bag.isSerial(true);
            } else if (extension.contains(DefaultBag.TAR_LABEL)) {
                bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.TAR_LABEL);                
                bag.setSerialMode(DefaultBag.TAR_MODE);
                bag.isSerial(true);
            } else if (extension.contains(DefaultBag.ZIP_LABEL)) {
                System.out.println("dit is een zip!!");
                bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.ZIP_LABEL);                
                bag.setSerialMode(DefaultBag.ZIP_MODE);
                bag.isSerial(true);
            } else {
                System.out.println("dit is een map!!");
                bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.NO_LABEL);                
                bag.setSerialMode(DefaultBag.NO_MODE);
                bag.isSerial(false);
            }
        } else {
            bagView.getInfoInputPane().getSerializeValue().setText(DefaultBag.NO_LABEL);            
            bag.setSerialMode(DefaultBag.NO_MODE);
            bag.isSerial(false);
        }
        bagView.getInfoInputPane().getSerializeValue().invalidate();

        if(bag.isHoley()) {
            bagView.getInfoInputPane().setHoley("true");
        }else{
            bagView.getInfoInputPane().setHoley("false");
        }
        bagView.getInfoInputPane().getHoleyValue().invalidate();
        bagView.updateBaggerRules();
        bagView.setBagRootPath(file);
        
        File rootSrc;
        String path;
    	if(bag.getFetchTxt() != null){
            path = bag.getFetch().getBaseURL();
            rootSrc = new File(file,bag.getFetchTxt().getFilepath());
    	}else{
            path = AbstractBagConstants.DATA_DIRECTORY;
            rootSrc = new File(file,bag.getDataDirectory());
    	}
    	bagView.getBagPayloadTree().populateNodes(bag,path,rootSrc, true);
    	bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());
    	bagView.updateManifestPane();
    	bagView.enableBagSettings(true);
        String msgs = bag.validateMetadata();
        if(msgs != null){
            ApplicationContextUtil.addConsoleMessage(msgs);
        }
        bagView.getInfoInputPane().getBagInfoInputPane().populateForms(true);
        bagView.updateOpenBag();   
        
        //Nicolas Franck: load mets
        String pathMets;
        InputStream in;        
        Mets mets = null;
        
        if(bag.isSerial()){
            pathMets = extension+":"+file.getAbsolutePath()+"!/"+baseName+"/mets.xml";
        }else{
            pathMets = "file://"+new File(file,"mets.xml").getAbsolutePath();
        }        
        try{
            in = FileUtils.getInputStreamFor(pathMets);
            mets = MetsUtils.readMets(in);
        }catch(Exception e){
            log.debug(e.getMessage());
        }
        if(mets == null){
            mets = new Mets();
        }
        
        MetsPanel metsPanel = bagView.getInfoInputPane().getBagInfoInputPane().getMetsPanel();
        metsPanel.setMets(mets);
        metsPanel.reset(mets);        
    }
}
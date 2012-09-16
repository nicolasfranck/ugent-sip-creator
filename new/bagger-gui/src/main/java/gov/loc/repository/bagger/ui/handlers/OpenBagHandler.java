package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.progress.BusyIndicator;

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
        fo.addChoosableFileFilter(bagView.infoInputPane.noFilter);
        fo.addChoosableFileFilter(bagView.infoInputPane.zipFilter);
        fo.addChoosableFileFilter(bagView.infoInputPane.tarFilter);
        fo.setFileFilter(bagView.infoInputPane.noFilter);
        fo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if(bagView.getBagRootPath() != null){
            fo.setCurrentDirectory(bagView.getBagRootPath().getParentFile());
        }
        fo.setDialogTitle("Existing Bag Location");
        int option = fo.showOpenDialog(Application.instance().getActiveWindow().getControl());

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fo.getSelectedFile();
            if (file == null) file = bagView.getBagRootPath();
            openExistingBag(file);
        }
    }

    public void openExistingBag(File file) {
        BagView bagView = BagView.getInstance();
    	bagView.getInfoInputPane().bagInfoInputPane.enableForms(true);
    	bagView.clearBagHandler.clearExistingBag();

        try{
            bagView.clearBagHandler.newDefaultBag(file);
            ApplicationContextUtil.addConsoleMessage("Opened the bag " + file.getAbsolutePath());
        }catch(Exception ex){
            ApplicationContextUtil.addConsoleMessage("Failed to create bag: " + ex.getMessage());    	    
    	    return;
        }
        DefaultBag bag = bagView.getBag();
        bagView.getInfoInputPane().setBagVersion(bag.getVersion());
        bagView.getInfoInputPane().setProfile(bag.getProfile().getName());       
        String fileName = file.getAbsolutePath();
        bagView.getInfoInputPane().setBagName(fileName);

    	String s = file.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            String sub = s.substring(i + 1).toLowerCase();
            if (sub.contains("gz")) {
                bagView.getInfoInputPane().serializeValue.setText(DefaultBag.TAR_GZ_LABEL);                
                bag.setSerialMode(DefaultBag.TAR_GZ_MODE);
                bag.isSerial(true);
            } else if (sub.contains("bz2")) {
                bagView.getInfoInputPane().serializeValue.setText(DefaultBag.TAR_BZ2_LABEL);                
                bag.setSerialMode(DefaultBag.TAR_BZ2_MODE);
                bag.isSerial(true);
            } else if (sub.contains(DefaultBag.TAR_LABEL)) {
                bagView.getInfoInputPane().serializeValue.setText(DefaultBag.TAR_LABEL);                
                bag.setSerialMode(DefaultBag.TAR_MODE);
                bag.isSerial(true);
            } else if (sub.contains(DefaultBag.ZIP_LABEL)) {
                bagView.getInfoInputPane().serializeValue.setText(DefaultBag.ZIP_LABEL);                
                bag.setSerialMode(DefaultBag.ZIP_MODE);
                bag.isSerial(true);
            } else {
                bagView.getInfoInputPane().serializeValue.setText(DefaultBag.NO_LABEL);                
                bag.setSerialMode(DefaultBag.NO_MODE);
                bag.isSerial(false);
            }
        } else {
            bagView.getInfoInputPane().serializeValue.setText(DefaultBag.NO_LABEL);            
            bag.setSerialMode(DefaultBag.NO_MODE);
            bag.isSerial(false);
        }
        bagView.getInfoInputPane().serializeValue.invalidate();

        if (bag.isHoley()) {
            bagView.getInfoInputPane().setHoley("true");
        } else {
            bagView.getInfoInputPane().setHoley("false");
        }
        bagView.getInfoInputPane().holeyValue.invalidate();

        bagView.updateBaggerRules();
        bagView.setBagRootPath(file);
        
        File rootSrc;
        String path;
    	if(bag.getFetchTxt() != null){
            path = bag.getFetch().getBaseURL();
            rootSrc = new File(file,bag.getFetchTxt().getFilepath());
    	}else{
            path = AbstractBagConstants.DATA_DIRECTORY;
            rootSrc = new File(file, bag.getDataDirectory());
    	}
    	bagView.getBagPayloadTree().populateNodes(bag, path, rootSrc, true);
    	bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());
    	bagView.updateManifestPane();
    	bagView.enableBagSettings(true);
        String msgs = bag.validateMetadata();
        if(msgs != null){
            ApplicationContextUtil.addConsoleMessage(msgs);
        }
        bagView.getInfoInputPane().bagInfoInputPane.populateForms(true);
        bagView.updateOpenBag();        
    }
}
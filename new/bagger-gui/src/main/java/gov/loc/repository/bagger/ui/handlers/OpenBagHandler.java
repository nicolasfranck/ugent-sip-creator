package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.BagItMets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.InfoInputPane;
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
import ugent.bagger.bagitmets.DSpaceBagItMets;
import ugent.bagger.helper.SwingUtils;

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
        BusyIndicator.showAt(SwingUtils.getFrame());        
        openBag();        
        BusyIndicator.clearAt(SwingUtils.getFrame());
    }

    public void openBag() {
        BagView bagView = BagView.getInstance();
        File selectFile = new File(File.separator+".");        
        JFileChooser fo = new JFileChooser(selectFile);
        fo.setDialogType(JFileChooser.OPEN_DIALOG);
        
        fo.addChoosableFileFilter(bagView.getInfoFormsPane().getNoFilter());
        fo.addChoosableFileFilter(bagView.getInfoFormsPane().getZipFilter());
        fo.addChoosableFileFilter(bagView.getInfoFormsPane().getTarFilter());
        fo.setFileFilter(bagView.getInfoFormsPane().getNoFilter());
        fo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if(bagView.getBagRootPath() != null){
            fo.setCurrentDirectory(bagView.getBagRootPath().getParentFile());
        }
        fo.setDialogTitle("Existing Bag Location");
        int option = fo.showOpenDialog(SwingUtils.getFrame());

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fo.getSelectedFile();
            if (file == null) {
                file = bagView.getBagRootPath();
            }
            openExistingBag(file);
        }
    }

    public void openExistingBag(File file) {        
        BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());
        
        BagView bagView = BagView.getInstance();        
    	bagView.getInfoFormsPane().getInfoInputPane().enableForms(true);
        
        //opgelet: een nieuw DefaultBag wordt aangemaakt, dus
        //beter geen referentie bijhouden nu naar de oude
    	bagView.clearBagHandler.clearExistingBag();
        
        
        
        try{            
            bagView.clearBagHandler.newDefaultBag(file);                  
            ApplicationContextUtil.addConsoleMessage("Opened the bag " + file.getAbsolutePath());
        }catch(Exception ex){        
            ApplicationContextUtil.addConsoleMessage("Failed to create bag: " + ex.getMessage());    	                
            log.debug(ex.getMessage());                                     
            BusyIndicator.clearAt(SwingUtils.getFrame());            
    	    return;
        }
        
        
        bagView.getInfoFormsPane().setBagVersion(bagView.getBag().getVersion());
        bagView.getInfoFormsPane().setProfile(bagView.getBag().getProfile().getName());       
        String fileName = file.getAbsolutePath();
        bagView.getInfoFormsPane().setBagName(fileName);

    	String name = file.getName();
        int i = name.lastIndexOf('.');
        String baseName = (i >= 0) ? name.substring(0,i):name;
        String extension = "";
        if (i > 0 && i < name.length() - 1) {
            extension = name.substring(i + 1).toLowerCase();
            if (extension.contains("gz")) {
                bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.TAR_GZ_LABEL);                
                bagView.getBag().setSerialMode(DefaultBag.TAR_GZ_MODE);
                bagView.getBag().isSerial(true);
            } else if (extension.contains("bz2")) {
                bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.TAR_BZ2_LABEL);                
                bagView.getBag().setSerialMode(DefaultBag.TAR_BZ2_MODE);
                bagView.getBag().isSerial(true);
            } else if (extension.contains(DefaultBag.TAR_LABEL)) {
                bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.TAR_LABEL);                
                bagView.getBag().setSerialMode(DefaultBag.TAR_MODE);
                bagView.getBag().isSerial(true);
            } else if (extension.contains(DefaultBag.ZIP_LABEL)) {                
                bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.ZIP_LABEL);                
                bagView.getBag().setSerialMode(DefaultBag.ZIP_MODE);
                bagView.getBag().isSerial(true);
            } else {                
                bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.NO_LABEL);                
                bagView.getBag().setSerialMode(DefaultBag.NO_MODE);
                bagView.getBag().isSerial(false);
            }
        } else {
            bagView.getInfoFormsPane().getSerializeValue().setText(DefaultBag.NO_LABEL);            
            bagView.getBag().setSerialMode(DefaultBag.NO_MODE);
            bagView.getBag().isSerial(false);
        }
        bagView.getInfoFormsPane().getSerializeValue().invalidate();
        
        bagView.getInfoFormsPane().setHoley(bagView.getBag().isHoley() ? "true":"false");        
        bagView.getInfoFormsPane().getHoleyValue().invalidate();
        bagView.updateBaggerRules();
        bagView.setBagRootPath(file);
        
        File rootSrc;
        String path;
    	if(bagView.getBag().getFetchTxt() != null){
            path = bagView.getBag().getFetch().getBaseURL();
            rootSrc = new File(file,bagView.getBag().getFetchTxt().getFilepath());
    	}else{
            path = AbstractBagConstants.DATA_DIRECTORY;
            rootSrc = new File(file,bagView.getBag().getDataDirectory());
    	}
        
    	bagView.getBagPayloadTree().populateNodes(bagView.getBag(),path,rootSrc,true);
    	bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());
    	bagView.updateManifestPane();
    	bagView.enableBagSettings(true);
        String msgs = bagView.getBag().validateMetadata();
        if(msgs != null){
            ApplicationContextUtil.addConsoleMessage(msgs);
        }
        bagView.getInfoFormsPane().getInfoInputPane().populateForms(true);
        bagView.getInfoFormsPane().getInfoInputPane().enableForms(true);
        
        bagView.updateOpenBag();   
        
        //Nicolas Franck: load mets
        
        BagItMets bagitMets = new DSpaceBagItMets();
        
        Mets mets = bagitMets.onOpenBag(bagView.getBag().getBag());              
        
        InfoInputPane bagInfoInputPane = bagView.getInfoFormsPane().getInfoInputPane();
        bagInfoInputPane.setMets(mets);
        bagInfoInputPane.getMetsPanel().reset(mets);
        
        BusyIndicator.clearAt(SwingUtils.getFrame());
    }    
}
package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.NewBagsDialog;
import gov.loc.repository.bagger.ui.Progress;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.bagitmets.DefaultBagItMets;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Loggable;

//Nicolas Franck: based on code of CreateBagInPlaceHandler, but for creation of multiple bagits at once

public class CreateBagsHandler extends AbstractAction implements Progress,Loggable {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(CreateBagsHandler.class);   
    
    public CreateBagsHandler() {
        super();           
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        execute();
    }

    @Override
    public void execute() {        
        createBags();        
    }

    public void createBags() {
        BagView bagView = BagView.getInstance();             
        NewBagsDialog dialog = new NewBagsDialog(
            SwingUtils.getFrame(),
            true,
            Context.getMessage("NewBagsDialog.title")
        );
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);        
        dialog.pack();
        dialog.setVisible(true);        
    }
    public void createPreBag(File dataFile, String bagItVersion, final String profileName,String [] ignoreFiles) {
        BagView bagView = BagView.getInstance();        
        MetsBag bag = bagView.getBag();        
        
    	if (((dataFile != null) && (bagItVersion != null)) && (profileName !=null)) {            
            String message = "Creating a new bag in place with data: " + dataFile.getName()+ ", version: " + bagItVersion + ", profile: " + profileName;
            log.info(message);            
        }    
    
    	try {
            bag.setBagItMets(new DefaultBagItMets());            
            bag.setRootDir(dataFile);
            bag.createPreBag(dataFile,bagItVersion,ignoreFiles);                      
    	} catch (Exception e) {
            log.error(e);  
            
            String error = "No file or directory selection was made!\n";
            log(error);
            SwingUtils.ShowError("Error - bagging in place",error);    	   
    	}
    }
    public void createPreBag(File dataFile, String bagItVersion, final String profileName) {
        createPreBag(dataFile, bagItVersion, profileName,new String [] {});            
    }
    
    public void createPreBagAddKeepFilesToEmptyFolders(File dataFile, String bagItVersion, final String profileName,String [] ignoreFiles){
        BagView bagView = BagView.getInstance();        
        MetsBag bag = bagView.getBag();        
        
    	if (((dataFile != null) && (bagItVersion != null)) && (profileName !=null)) {
            log.info("Creating a new bag in place with data: " + dataFile.getName()+ ", version: " + bagItVersion + ", profile: " + profileName);
        }    
    
    	try {
            bag.setBagItMets(new DefaultBagItMets());            
            bag.setRootDir(dataFile);
            bag.createPreBagAddKeepFilesToEmptyFolders(dataFile,bagItVersion,ignoreFiles);                      
    	} catch (Exception e) {
            log.error(e);   
            String error = "No file or directory selection was made!\n";
            log(error);
            SwingUtils.ShowError("Error - bagging in place",error);    	    
    	}
    }
    
	/*
     * Prepares the call to Create Bag in Place and 
     * adding .keep files in Empty Pay load Folder(s) 
    */
    public void createPreBagAddKeepFilesToEmptyFolders(File dataFile, String bagItVersion, final String profileName) {
        createPreBagAddKeepFilesToEmptyFolders(dataFile, bagItVersion, profileName,new String [] {});
    }        

    @Override
    public void log(String message) {
        ApplicationContextUtil.addConsoleMessage(message);
    }
}
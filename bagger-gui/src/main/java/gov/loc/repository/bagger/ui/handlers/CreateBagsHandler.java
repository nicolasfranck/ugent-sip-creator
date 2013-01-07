package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.Progress;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.bagitmets.DefaultBagItMets;
import ugent.bagger.dialogs.CreateBagsDialog;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;

//Nicolas Franck: based on code of CreateBagInPlaceHandler, but for creation of multiple bagits at once
//profile uitgeschakeld

public class CreateBagsHandler extends AbstractAction implements Progress/*,Loggable*/ {
    static final long serialVersionUID = 1L;
    static final Log log = LogFactory.getLog(CreateBagsHandler.class);   
    
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
    
        CreateBagsDialog dialog = new CreateBagsDialog(SwingUtils.getFrame(),true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);  
        dialog.pack();
        dialog.setVisible(true);
    }
    public void createPreBag(File dataFile, String bagItVersion,String [] ignoreFiles) {
        BagView bagView = BagView.getInstance();        
        MetsBag bag = bagView.getBag();        
        
    	if ((dataFile != null) && (bagItVersion != null)) {            
            String message = "Creating a new bag in place with data: " + dataFile.getName()+ ", version: " + bagItVersion;
            log.error(message);            
        }    
    
    	try {
            bag.setBagItMets(new DefaultBagItMets());            
            bag.setFile(dataFile);
            bag.createPreBag(dataFile,bagItVersion,ignoreFiles);                      
    	} catch (Exception e) {
            log.error(e.getMessage()); 
            
            String title = Context.getMessage("DefaultBag.createPreBag.Exception.title");
            String message = Context.getMessage(
                "DefaultBag.createPreBag.Exception.description", 
                new Object [] {dataFile,e.getMessage()}
            );
            log.error(message);
            SwingUtils.ShowError(title,message);    	   
    	}
    }
    public void createPreBag(File dataFile, String bagItVersion) {
        createPreBag(dataFile, bagItVersion,new String [] {});            
    }
    
    public void createPreBagAddKeepFilesToEmptyFolders(File dataFile, String bagItVersion,String [] ignoreFiles){
        BagView bagView = BagView.getInstance();        
        MetsBag bag = bagView.getBag();        
        
    	if ((dataFile != null) && (bagItVersion != null)) {
            log.error("Creating a new bag in place with data: " + dataFile.getName()+ ", version: " + bagItVersion);
        }    
    
    	try {
            bag.setBagItMets(new DefaultBagItMets());            
            bag.setFile(dataFile);
            bag.createPreBagAddKeepFilesToEmptyFolders(dataFile,bagItVersion,ignoreFiles);                      
    	} catch (Exception e) {
            log.error(e.getMessage());   
            String title = Context.getMessage("DefaultBag.createPreBag.Exception.title");
            String message = Context.getMessage(
                "DefaultBag.createPreBag.Exception.description", 
                new Object [] {dataFile,e.getMessage()}
            );
            log.error(message);
            SwingUtils.ShowError(title,message); 	    
    	}
    }
    
    /*
        * Prepares the call to Create Bag in Place and 
        * adding .keep files in Empty Pay load Folder(s) 
    */
    public void createPreBagAddKeepFilesToEmptyFolders(File dataFile, String bagItVersion) {
        createPreBagAddKeepFilesToEmptyFolders(dataFile, bagItVersion,new String [] {});
    }        
    /*
    @Override
    public void log(String message) {
        ApplicationContextUtil.addConsoleMessage(message);
    }*/
}
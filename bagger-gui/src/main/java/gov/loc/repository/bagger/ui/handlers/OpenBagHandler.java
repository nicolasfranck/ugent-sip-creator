package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.BagItMets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.InfoInputPane;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import gov.loc.repository.bagit.utilities.SimpleResult;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.bagitmets.DefaultBagItMets;
import ugent.bagger.exceptions.FileNotReadableException;
import ugent.bagger.exceptions.FileNotWritableException;
import ugent.bagger.helper.FUtils;
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
        SwingUtils.ShowBusy();        
        openBag();        
        SwingUtils.ShowDone();        
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
        SwingUtils.ShowBusy();        
        
        BagView bagView = BagView.getInstance();        
        
        try{ 
            
            try{
                FUtils.checkFile(file,true);
            }catch(FileNotWritableException e){
                SwingUtils.ShowError(null,"Waarschuwing: niet alle bestanden beschrijfbaar!");
                System.out.println("file not writable");
            }
            
            bagView.getInfoFormsPane().getInfoInputPane().enableForms(true);

            //opgelet: een nieuw DefaultBag wordt aangemaakt, dus
            //beter geen referentie bijhouden nu naar de oude
            bagView.clearBagHandler.clearExistingBag();
                       
            bagView.clearBagHandler.newDefaultBag(file);                  
            ApplicationContextUtil.addConsoleMessage("Opened the bag " + file.getAbsolutePath());
            

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
            SimpleResult result = bagView.getBag().validateMetadata();
            if(!result.isSuccess()){
                ApplicationContextUtil.addConsoleMessage(result.toString());
            }
            bagView.getInfoFormsPane().getInfoInputPane().populateForms(true);
            bagView.getInfoFormsPane().getInfoInputPane().enableForms(true);        
            bagView.updateOpenBag();   

            //Nicolas Franck: bag-info velden inladen in form blijkbaar niet automatisch (wel na invullen 1ste nieuwe veld)
            bagView.getInfoFormsPane().updateInfoFormsPane(true);

            //Nicolas Franck: load mets

            BagItMets bagitMets = new DefaultBagItMets();

            Mets mets = bagitMets.onOpenBag(bagView.getBag().getBag());              

            InfoInputPane bagInfoInputPane = bagView.getInfoFormsPane().getInfoInputPane();
            bagInfoInputPane.setMets(mets);
            bagInfoInputPane.getMetsPanel().reset(mets);
            
        }catch(FileNotReadableException e){
            SwingUtils.ShowError(null,"bestand is niet leesbaar!");
            bagView.clearBagHandler.clearExistingBag();            
        }catch(FileNotFoundException e){
            SwingUtils.ShowError(null,"bestand is niet gevonden!");
            bagView.clearBagHandler.clearExistingBag();
        }
        
        SwingUtils.ShowDone();
    }        
}
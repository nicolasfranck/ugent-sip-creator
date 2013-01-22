package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.BagItMets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.InfoFormsPane;
import gov.loc.repository.bagger.ui.InfoInputPane;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.bagitmets.DefaultBagItMets;
import ugent.bagger.exceptions.BagFetchForbiddenException;
import ugent.bagger.exceptions.BagNoBagDirException;
import ugent.bagger.exceptions.BagNoDataException;
import ugent.bagger.exceptions.BagUnknownFormatException;
import ugent.bagger.exceptions.FileNotReadableException;
import ugent.bagger.exceptions.FileNotWritableException;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.PremisUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.premis.Premis;

public class OpenBagHandler extends AbstractAction {
    static final Log log = LogFactory.getLog(OpenBagHandler.class);    

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
        
        JFileChooser fo = new JFileChooser();
        fo.setDialogType(JFileChooser.OPEN_DIALOG);                
        fo.addChoosableFileFilter(null);            
        fo.addChoosableFileFilter(
            new FileExtensionFilter(
                new String [] {"zip"},"zip",true
            )
        );
        fo.addChoosableFileFilter(
            new FileExtensionFilter(
                new String [] {"tar"},"tar",true
            )
        );
        fo.addChoosableFileFilter(
            new FileExtensionFilter(
                new String [] {"tar.gz"},"tar.gz",true
            )
        );
        fo.addChoosableFileFilter(
            new FileExtensionFilter(
                new String [] {"tar.bz2"},"tar.bz2",true
            )
        );
        fo.setFileFilter(null);
        fo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);        
        fo.setDialogTitle(Context.getMessage("openBagHandler.fileChooser.dialogTitle"));        
        
        File [] files = SwingUtils.chooseFiles(fo,SwingUtils.getFrame());
        
        if(files.length > 0){
            openExistingBag(files[0]);
        }        
    }

    public void openExistingBag(File file) { 
        SwingUtils.ShowBusy();                
        
        SwingUtils.getStatusBar().setMessage(
            Context.getMessage("StatusBar.openBag.message",new Object []{file.getAbsolutePath()})
        );
        
        BagView bagView = BagView.getInstance();
        InfoFormsPane infoFormsPane = bagView.getInfoFormsPane();
        InfoInputPane infoInputPane = infoFormsPane.getInfoInputPane();                
        
        try{
            infoInputPane.enableForms(false);      
            
            //leesbaar? schrijfbaar?
            FUtils.checkFile(file,true); 

            //opgelet: een nieuw DefaultBag wordt aangemaakt, dus
            //beter geen referentie bijhouden nu naar de oude
            bagView.clearBagHandler.clearExistingBag();      
            
            //formaat correct?
            bagView.clearBagHandler.newDefaultBag(file); 
            log.error(Context.getMessage("clearBagHandler.bagOpened.label",new Object [] {
                file
            }));                
            
            MetsBag metsBag = bagView.getBag();  
            
            infoInputPane.enableForms(true);
            infoFormsPane.setBagVersion(bagView.getBag().getVersion());
                        
            String fileName = file.getAbsolutePath();
            infoFormsPane.setBagName(fileName);

            String name = file.getName();
            int i = name.lastIndexOf('.');
            String baseName = (i >= 0) ? name.substring(0,i):name;
            String extension = "";
            
            if (i > 0 && i < name.length() - 1) {
                extension = name.substring(i + 1).toLowerCase();
            
                if (extension.contains("gz")) {
                    infoFormsPane.getSerializeValue().setText(DefaultBag.TAR_GZ_LABEL);                
                    metsBag.setSerialMode(DefaultBag.TAR_GZ_MODE);
                    metsBag.isSerial(true);
                } else if (extension.contains("bz2")) {
                    infoFormsPane.getSerializeValue().setText(DefaultBag.TAR_BZ2_LABEL);                
                    metsBag.setSerialMode(DefaultBag.TAR_BZ2_MODE);
                    metsBag.isSerial(true);
                } else if (extension.contains(DefaultBag.TAR_LABEL)) {
                    infoFormsPane.getSerializeValue().setText(DefaultBag.TAR_LABEL);                
                    metsBag.setSerialMode(DefaultBag.TAR_MODE);
                    metsBag.isSerial(true);
                } else if (extension.contains(DefaultBag.ZIP_LABEL)) {                
                    infoFormsPane.getSerializeValue().setText(DefaultBag.ZIP_LABEL);                
                    metsBag.setSerialMode(DefaultBag.ZIP_MODE);
                    metsBag.isSerial(true);
                } else {                                    
                    infoFormsPane.getSerializeValue().setText(DefaultBag.NO_LABEL);                
                    metsBag.setSerialMode(DefaultBag.NO_MODE);
                    metsBag.isSerial(false);
                }
            } else {                
                infoFormsPane.getSerializeValue().setText(Context.getMessage("DefaultBag.mode.no_label"));            
                metsBag.setSerialMode(DefaultBag.NO_MODE);
                metsBag.isSerial(false);
            }
            infoFormsPane.getSerializeValue().invalidate();

            infoFormsPane.setHoley(bagView.getBag().isHoley() ? "true":"false");        
            infoFormsPane.getHoleyValue().invalidate();            
            
            metsBag.setFile(file);      

            File rootSrc;
            String path;
            if(metsBag.getFetchTxt() != null){
                path = metsBag.getFetch().getBaseURL();
                rootSrc = new File(file,metsBag.getFetchTxt().getFilepath());
            }else{
                path = AbstractBagConstants.DATA_DIRECTORY;
                rootSrc = new File(file,metsBag.getDataDirectory());
            }

            bagView.getBagPayloadTree().populateNodes(bagView.getBag(),path,rootSrc,true);
            bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());            
            bagView.enableBagSettings(true);            
         
            infoInputPane.populateForms();
            infoInputPane.enableForms(true);        
            bagView.updateOpenBag();   

            //Nicolas Franck: bag-info velden inladen in form blijkbaar niet automatisch (wel na invullen 1ste nieuwe veld)            
            infoInputPane.getBagInfoForm().setFieldMap(
                metsBag.getInfo().getFieldMap()
            );
            infoInputPane.getBagInfoForm().resetFields();

            //Nicolas Franck: load mets and set premis
            BagItMets bagitMets = new DefaultBagItMets();
            Mets mets = bagitMets.onOpenBag(metsBag);                                 
           
            Premis premis = null;
            try{
                premis = PremisUtils.setPremis(mets);
            }catch(Exception e){
                log.error(e.getMessage());             
            }
            
            metsBag.setPremis(premis);    
            metsBag.setMets(mets);            
            infoInputPane.resetMets(mets);       
            
        }catch(FileNotWritableException e){
            log.error(e.getMessage());
            SwingUtils.ShowError(
                Context.getMessage("clearBagHandler.FileNotWritableException.title"),
                Context.getMessage("clearBagHandler.FileNotWritableException.description",new Object [] {file})
            );
        }catch(FileNotReadableException e){
            log.error(e.getMessage());
            SwingUtils.ShowError(
                Context.getMessage("clearBagHandler.FileNotReadableException.title"),
                Context.getMessage("clearBagHandler.FileNotReadableException.description",new Object [] {file})
            );                     
        }catch(FileNotFoundException e){
           log.error(e.getMessage());
           SwingUtils.ShowError(
                Context.getMessage("clearBagHandler.FileNotFoundException.title"),
                Context.getMessage("clearBagHandler.FileNotFoundException.description",new Object [] {file})
            );            
        }catch(BagUnknownFormatException e){
            log.error(e.getMessage());
            SwingUtils.ShowError(
                Context.getMessage("clearBagHandler.BagUnknownFormatException.title"),
                Context.getMessage("clearBagHandler.BagUnknownFormatException.description",new Object [] {file})
            );
        }catch(BagNoBagDirException ex){
            log.error(ex.getMessage());
            /*
             * geserialiseerde bag bevat geen hoofdmap met dezelfde naam
             */
            String basename = file.getName();
            int index = basename.lastIndexOf('.');
            String n = index >= 0 ? basename.substring(0,index) : basename;
            SwingUtils.ShowError(
                Context.getMessage("clearBagHandler.BagNoBagDirException.title"),
                Context.getMessage("clearBagHandler.BagNoBagDirException.description",new Object [] {n,file})
            );
        }catch(BagFetchForbiddenException e){
            log.error(e.getMessage());
            SwingUtils.ShowError(
                Context.getMessage("clearBagHandler.BagFetchForbiddenException.title"),
                Context.getMessage("clearBagHandler.BagFetchForbiddenException.description",new Object [] {file})
            );
        }catch(BagNoDataException e){
            log.error(e.getMessage());
            SwingUtils.ShowError(
                Context.getMessage("clearBagHandler.BagNoDataException.title"),
                Context.getMessage("clearBagHandler.BagNoDataException.description",new Object [] {file})
            );
        }
        
        SwingUtils.getStatusBar().setMessage("");
        SwingUtils.ShowDone();
    }           
}
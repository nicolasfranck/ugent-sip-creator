package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.BagItMets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import ugent.bagger.bagitmets.DefaultBagItMets;
import ugent.bagger.exceptions.BagNoBagDirException;
import ugent.bagger.exceptions.BagUnknownFormatException;
import ugent.bagger.exceptions.FileNotReadableException;
import ugent.bagger.exceptions.FileNotWritableException;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.premis.Premis;

public class OpenBagHandler extends AbstractAction {
    private static final Log log = LogFactory.getLog(OpenBagHandler.class);
    private static final long serialVersionUID = 1L;   

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
        fo.setDialogTitle(Context.getMessage("openBagHandler.fileChooser.dialogTitle"));
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
            
            //leesbaar? schrijfbaar?
            FUtils.checkFile(file,true);            
            
            bagView.getInfoFormsPane().getInfoInputPane().enableForms(true);

            //opgelet: een nieuw DefaultBag wordt aangemaakt, dus
            //beter geen referentie bijhouden nu naar de oude           
            
            bagView.clearBagHandler.clearExistingBag();      
            
            //formaat correct?
            bagView.clearBagHandler.newDefaultBag(file);                  
            ApplicationContextUtil.addConsoleMessage(
                Context.getMessage("clearBagHandler.bagOpened.label",new Object [] {
                    file
                })
            );            

            bagView.getInfoFormsPane().setBagVersion(bagView.getBag().getVersion());
                        
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
         
            bagView.getInfoFormsPane().getInfoInputPane().populateForms(true);
            bagView.getInfoFormsPane().getInfoInputPane().enableForms(true);        
            bagView.updateOpenBag();   

            //Nicolas Franck: bag-info velden inladen in form blijkbaar niet automatisch (wel na invullen 1ste nieuwe veld)            
            bagView.getInfoFormsPane().getInfoInputPane().getBagInfoForm().setFieldMap(
                bagView.getBag().getInfo().getFieldMap()
            );
            bagView.getInfoFormsPane().getInfoInputPane().getBagInfoForm().resetFields();

            //Nicolas Franck: load mets

            BagItMets bagitMets = new DefaultBagItMets();

            Mets mets = bagitMets.onOpenBag(bagView.getBag());
            
            bagView.getInfoFormsPane().getInfoInputPane().resetMets(mets);           
            
            
            //Nicolas Franck: set eventLog
            for(AmdSec amdSec:mets.getAmdSec()){
                if(amdSec.getID() != null && amdSec.getID().equals("BAGIT_EVENT_LOG")){
                    if(!amdSec.getDigiprovMD().isEmpty()){
                        setEventLog(bagView.getBag(),mets,amdSec,amdSec.getDigiprovMD().get(0));
                    }else if(!amdSec.getTechMD().isEmpty()){
                        setEventLog(bagView.getBag(),mets,amdSec,amdSec.getTechMD().get(0));
                    }
                    break;
                }
            }
            
        }catch(FileNotWritableException e){
            SwingUtils.ShowError(
                Context.getMessage("clearBagHandler.FileNotWritableException.title"),
                Context.getMessage("clearBagHandler.FileNotWritableException.description",new Object [] {file})
            );
        }catch(FileNotReadableException e){
            SwingUtils.ShowError(
                Context.getMessage("clearBagHandler.FileNotReadableException.title"),
                Context.getMessage("clearBagHandler.FileNotReadableException.description",new Object [] {file})
            );                     
        }catch(FileNotFoundException e){
           SwingUtils.ShowError(
                Context.getMessage("clearBagHandler.FileNotFoundException.title"),
                Context.getMessage("clearBagHandler.FileNotFoundException.description",new Object [] {file})
            );            
        }catch(BagUnknownFormatException e){
            SwingUtils.ShowError(
                Context.getMessage("clearBagHandler.BagUnknownFormatException.title"),
                Context.getMessage("clearBagHandler.BagUnknownFormatException.description",new Object [] {file})
            );
        }catch(BagNoBagDirException ex){
            log.debug(ex.getMessage());
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
        }
        
        SwingUtils.ShowDone();
    }        
    protected void setEventLog(MetsBag metsBag,Mets mets,AmdSec amdSec,MdSec mdSec){
        try{
            if(
                mdSec != null && mdSec.getMdWrap() != null && 
                mdSec.getMdWrap().getXmlData() != null && !mdSec.getMdWrap().getXmlData().isEmpty()
            ){                
                Element e = mdSec.getMdWrap().getXmlData().get(0);
                Premis premis = new Premis();
                premis.unmarshal(e);
                
                metsBag.setEventLog(premis);               
                //verwijder uit zichtbare lijst
                mets.getAmdSec().remove(amdSec);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
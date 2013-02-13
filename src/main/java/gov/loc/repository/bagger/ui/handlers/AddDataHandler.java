package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.Progress;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.exceptions.FileNameNotPortableException;
import ugent.bagger.exceptions.FileNotReadableException;
import ugent.bagger.exceptions.FileNotWritableException;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.SwingUtils;

public class AddDataHandler extends AbstractAction implements Progress /*,Loggable*/ {
    static final Log log = LogFactory.getLog(AddDataHandler.class);
  
    public AddDataHandler() {
        super();        
    }
    @Override
    public void execute() {
        addData();
    }
    @Override
    public void actionPerformed(ActionEvent e) {                
        execute();                        
    }    
    public void addData(){
        
        final BagView bagView = BagView.getInstance();        
        
        File [] files = SwingUtils.chooseFiles(
            Context.getMessage("bag.message.addfiles"), 
            null, 
            JFileChooser.FILES_AND_DIRECTORIES, 
            true
        );         
            
        SwingUtils.ShowBusy();                        
        
        addBagData(files);        
        bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());
        bagView.updateAddData();
        
        SwingUtils.ShowDone();
        
    }    
    public void addBagData(File[] files) {                
        log.error(Context.getMessage("addDataHandler.addBagDataList.start",new Object []{files.length}));
    	if(files != null){
            for (int i=0; i < files.length; i++) {                
                addBagData(files[i],!(i < files.length-1));                
            }
    	}        
        log.error(Context.getMessage("addDataHandler.addBagDataList.end",new Object []{files.length}));
    }
    protected void addBagDataReport(File file,boolean isSuccess){
        if(isSuccess){
            log.error(Context.getMessage("addDataHandler.addBagData.success",new Object []{file}));
        }else{
            log.error(Context.getMessage("addDataHandler.addBagData.error",new Object []{file}));
        }
    }
    public void addBagData(File file, boolean lastFileFlag) {
        
        final BagView bagView = BagView.getInstance();  
        final MetsBag metsBag = bagView.getBag();
        
        
        log.error(Context.getMessage("addDataHandler.addBagData.start",new Object []{file}));
        
        try{
            File bagFile = metsBag.getFile();            
            
            try{               
                //leesbaar?
                FUtils.checkFile(file,true);                
            }catch(FileNotWritableException e){
                log.debug(e.getMessage());
                //doe niets
            }
            
            try{                
                //bestandsnamen cross platform overdraagbaar?                
                FUtils.checkSafeFiles(file);
            }catch(FileNameNotPortableException e){
                                
                //één of meerdere bestandsnamen zijn niet portabel                                
                
                String error = null;
                if(file.isDirectory()){
                    error = Context.getMessage(
                        "addDataHandler.FileNameNotPortableException.directory.message",
                        new Object [] {file,e.getFile()}
                    );                    
                }else{
                    error = Context.getMessage(
                        "addDataHandler.FileNameNotPortableException.file.message",
                        new Object [] {file}
                    );                    
                }
                
                log.error(error);
                SwingUtils.ShowError(null,error);  
                
                addBagDataReport(file,false);
                
                return;
            }
            
            //lege map verboden
            if(file.isDirectory()){
                File [] children = file.listFiles();
                if(children == null || children.length == 0){
                    String error = Context.getMessage(
                        "addDataHandler.emptyDirectory.warning",
                        new Object [] {file}
                    );
                    SwingUtils.ShowError(null,error);
                    log.error(error);
                    
                    addBagDataReport(file,false);
                    
                    return;
                }
            }
            
            //bestanden maken deel uit van de data-map
            /*
             * ook de bag mag niet aan zichzelf worden toegevoegd
             * reden: vb. 'baggie' toevoegen aan bag 'baggie'
             *  1. creatie virtueel path baggie onder payloads
             *  2. berekening checksums, i.e. van alles onder 'baggie'
             *  3. schrijven bag:
             *      3.1. manifests,mets.xml en andere tags worden geschreven 
             *      3.2. kopiëren van payloads naar data-map.
             * 
             *      => geen probleem voor alles onder baggie/data,
             *      maar nog vóór het schrijven worden de tags en 
             *      de manifests die je wil toevoegen gewijzigd (3.1).
             *      je berekent dus checksums, wijzigt dan die 
             *      bestanden en dan voeg je ze toe..
             *      die tags en manifests zullen dus negatief valideren.
             */
            if(
                bagFile != null && (
                    (bagFile.isDirectory() && FUtils.isDescendant(bagFile,file)) ||
                    (bagFile.getAbsoluteFile().getAbsolutePath().equals(file.getAbsoluteFile().getAbsolutePath()))
                )
            ){
                String error = Context.getMessage(
                        "addDataHandler.fileInBag.warning",
                        new Object [] {file}
                    );
                SwingUtils.ShowError(null,error);
                log.error(error);
                
                addBagDataReport(file,false);
                
                return;
            }            
            
            bagView.getBag().addFileToPayload(file);
            boolean alreadyExists = bagView.getBagPayloadTree().addNodes(file, false);
            if(alreadyExists) {
                String title = Context.getMessage("addDataHandler.fileExists.title");
                
                String message = Context.getMessage(
                    "addDataHandler.fileExists.message",
                    new Object [] {file.getName()}
                );                        
                log.error(message);
                SwingUtils.ShowError(title,message);
                
                addBagDataReport(file,false);
            }else{
                addBagDataReport(file,true);
            }
        }catch(FileNotReadableException e){ 
            String title = Context.getMessage("addDataHandler.FileNotReadableException.title");
            String message = Context.getMessage(
                "addDataHandler.FileNotReadableException.message",
                new Object [] {e.getFile()}
            ); 
            log.error(message);
            addBagDataReport(file,false);
            SwingUtils.ShowError(title,message);            
        }catch(FileNotFoundException e){ 
            String title = Context.getMessage("addDataHandler.FileNotFoundException.title");
            String message = Context.getMessage(
                "addDataHandler.FileNotFoundException.message",
                new Object [] {file}
            ); 
            log.error(message);
            addBagDataReport(file,false);
            SwingUtils.ShowError(title,message);            
        }catch (Exception e){
            String title = Context.getMessage("addDataHandler.unknowException.title");
            String message = Context.getMessage(
                "addDataHandler.fileExists.message",
                new Object [] {e.getMessage()}
            ); 
            log.error(message);
            addBagDataReport(file,false);
            SwingUtils.ShowError(title,message);            
        }    	
    }    
}
package gov.loc.repository.bagger.ui.handlers;

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
    static final long serialVersionUID = 1L;    

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
        
        String message = Context.getMessage("bag.message.filesadded");        
        addBagData(files);
        log.error(message);        
        for(File file:files){
            log.error("\t"+file.getAbsolutePath());
        }
        bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());
        bagView.updateAddData();

        //Nicolas Franck: geen validate of complete nuttig
        //=> zie updateAddData() in BagView
        /*
        bagView.validateExecutor.setEnabled(false);
        bagView.validateBagHandler.setEnabled(false);
        bagView.completeExecutor.setEnabled(false);
        bagView.completeBagHandler.setEnabled(false);*/
        
        SwingUtils.ShowDone();
        
    }    
    public void addBagData(File[] files) {                
    	if(files != null){
            for (int i=0; i < files.length; i++) {
                log.error("addBagData[" + i + "] " + files[i].getName());                
                addBagData(files[i],!(i < files.length-1));                
            }
    	}        
    }
    public void addBagData(File file, boolean lastFileFlag) {
        
        final BagView bagView = BagView.getInstance();    	
        try{
            File bagFile = bagView.getBag().getFile();            
            
            try{
                //leesbaar?
                FUtils.checkFile(file,true);
                //bestandsnamen cross platform overdraagbaar?
                FUtils.checkSafeFiles(file);
            }catch(FileNotWritableException e){
                log.error(e.getMessage());
                //doe niets
            }catch(FileNameNotPortableException e){
                //één of meerdere bestandsnamen zijn niet portabel
                log.error(e.getMessage());                
                
                if(file.isDirectory()){
                    SwingUtils.ShowError(
                        null,
                        Context.getMessage(
                            "addDataHandler.FileNameNotPortableException.directory.message",
                            new Object [] {file,e.getFile()}
                        )
                    );
                }else{
                    SwingUtils.ShowError(
                        null,
                        Context.getMessage(
                            "addDataHandler.FileNameNotPortableException.file.message",
                            new Object [] {file}
                        )
                    );
                }
                
                return;
            }
            
            //lege map verboden
            if(file.isDirectory()){
                File [] children = file.listFiles();
                if(children == null || children.length == 0){
                    SwingUtils.ShowError(
                        null,
                        Context.getMessage(
                            "addDataHandler.emptyDirectory.warning",
                            new Object [] {file}
                        )
                    );
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
                SwingUtils.ShowError(
                    null,
                    Context.getMessage(
                        "addDataHandler.fileInBag.warning",
                        new Object [] {file}
                    )
                );
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
            }
        }catch(FileNotReadableException e){          
            String title = Context.getMessage("addDataHandler.FileNotReadableException.title");
            String message = Context.getMessage(
                "addDataHandler.FileNotReadableException.message",
                new Object [] {e.getFile()}
            ); 
            log.error(message);
            SwingUtils.ShowError(title,message);            
        }catch(FileNotFoundException e){            
            String title = Context.getMessage("addDataHandler.FileNotFoundException.title");
            String message = Context.getMessage(
                "addDataHandler.FileNotFoundException.message",
                new Object [] {file}
            ); 
            log.error(message);
            SwingUtils.ShowError(title,message);            
        }catch (Exception e){            
            
            log.error("BagView.addBagData: " + e);
            
            String title = Context.getMessage("addDataHandler.unknowException.title");
            String message = Context.getMessage(
                "addDataHandler.fileExists.message",
                new Object [] {e.getMessage()}
            ); 
            log.error(message);
            SwingUtils.ShowError(title,message);            
        }    	
    }    
}
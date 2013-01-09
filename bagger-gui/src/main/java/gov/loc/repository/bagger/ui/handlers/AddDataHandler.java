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
        bagView.validateExecutor.setEnabled(false);
        bagView.validateBagHandler.setEnabled(false);
        bagView.completeExecutor.setEnabled(false);
        bagView.completeBagHandler.setEnabled(false);
        
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
                FUtils.checkFile(file,true);
            }catch(FileNotWritableException e){
                //doe niets
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
            if(bagFile != null && bagFile.isDirectory() && FUtils.isDescendant(bagFile,file)){
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
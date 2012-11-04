package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.Progress;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Loggable;

public class AddDataHandler extends AbstractAction implements Progress,Loggable {
    private static final Log log = LogFactory.getLog(AddDataHandler.class);
    private static final long serialVersionUID = 1L;    

    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
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
    @Override
    public void log(String message){
        ApplicationContextUtil.addConsoleMessage(message);
    }
    public void addData(){
        
        
        
        final BagView bagView = BagView.getInstance();
        /*
         * Nicolas Franck: default directory
         * homedir default, en indien opgegeven lastDir? 
        */
        String dir = System.getProperty("java.bagger.filechooser.lastdirectory");
        if(dir == null){           
            dir = System.getProperty("user.home");           
        }

        //File selectFile = new File(File.separator+".");
        
        //Nicolas Franck: een frame die niet gebruikt wordt heeft geen effect!
        //JFrame frame = new JFrame();
        //JFileChooser fc = new JFileChooser(selectFile);
        JFileChooser fc = new JFileChooser(dir);

        fc.setDialogType(JFileChooser.OPEN_DIALOG);
    	fc.setMultiSelectionEnabled(true);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
        /*
         * Nicolas Franck
         */
        fc.setDialogTitle(ApplicationContextUtil.getMessage("bag.message.addfiles"));
        //fc.setDialogTitle("Add File or Directory");
    	int option = fc.showOpenDialog(Application.instance().getActiveWindow().getControl());

        if (option == JFileChooser.APPROVE_OPTION) {
            
            BusyIndicator.showAt(SwingUtils.getFrame());
            
            File[] files = fc.getSelectedFiles();
            String message = ApplicationContextUtil.getMessage("bag.message.filesadded");
            if (files != null && files.length >0) {
                addBagData(files);
                log(message + " " + getFileNames(files));
            } else {
            	File file = fc.getSelectedFile();
            	addBagData(file, true);
            	log(message + " " + file.getAbsolutePath());
            }
            bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());
            bagView.updateAddData();
            
            //Nicolas Franck: geen validate of complete nuttig
            bagView.validateExecutor.setEnabled(false);
            bagView.validateBagHandler.setEnabled(false);
            bagView.completeExecutor.setEnabled(false);
            bagView.completeBagHandler.setEnabled(false);
            
            BusyIndicator.clearAt(SwingUtils.getFrame());
            
            
        }
        /*
         * Nicolas Franck
         */
        String lastDir = fc.getCurrentDirectory().getAbsolutePath();
        System.setProperty("java.bagger.filechooser.lastdirectory",lastDir);        
        
    }
    private String getFileNames(File[] files) {
        
    	StringBuilder stringBuff = new StringBuilder();
    	int totalFileCount = files.length;
    	int displayCount = 20;
    	if (totalFileCount < 20) {
            displayCount = totalFileCount;
    	}
    	for (int i = 0; i < displayCount; i++) {
            if (i != 0) {
                stringBuff.append("\n");
            }
            stringBuff.append(files[i].getAbsolutePath());
    	}
    	if(totalFileCount > displayCount){
            stringBuff.append("\n").append(totalFileCount - displayCount).append(" more...");
    	}
        return stringBuff.toString();
    }
    public void addBagData(File[] files) {        
        
    	if(files != null){
            for (int i=0; i < files.length; i++) {
                log.info("addBagData[" + i + "] " + files[i].getName());
                
                addBagData(files[i],!(i < files.length-1));
                //Nicolas Franck
                /*
                if (i < files.length-1) {
                    addBagData(files[i], false);
                }
                else {
                    addBagData(files[i], true);
                }*/
            }
    	}
    }
    public void addBagData(File file, boolean lastFileFlag) {
        
        final BagView bagView = BagView.getInstance();    	
        try{
            bagView.getBag().addFileToPayload(file);
            boolean alreadyExists = bagView.getBagPayloadTree().addNodes(file, false);
            if(alreadyExists) {
                String title = Context.getMessage("addDataHandler.fileExists.title");
                String message = Context.getMessage(
                    "addDataHandler.fileExists.message",
                    new Object [] {file.getName()}
                );                        
                log(message);
                SwingUtils.ShowError(title,message);
            }
        }catch (Exception e){
            log.error("BagView.addBagData: " + e);
            String title = Context.getMessage("addDataHandler.unknowException.title");
            String message = Context.getMessage(
                "addDataHandler.fileExists.message",
                new Object [] {e.getMessage()}
            ); 
            log(message);
            SwingUtils.ShowError(title,message);            
        }    	
    }    
}
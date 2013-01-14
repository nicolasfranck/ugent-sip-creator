package gov.loc.repository.bagger.ui.handlers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;

public class OpenLogFileExecutor extends AbstractActionCommandExecutor {    
    static final Log log = LogFactory.getLog(OpenLogFileExecutor.class);

    public OpenLogFileExecutor() {
        super();
        setEnabled(true);            
    }
    @Override
    public void execute(){                
        File logFile = new File(System.getProperty("java.io.tmpdir"),"bagger-ugent/bagger-ugent.log");
        try{
            
            if(Desktop.isDesktopSupported()){
                Desktop.getDesktop().open(logFile);
            }else{
                SwingUtils.ShowMessage(
                    null,
                    Context.getMessage(
                        "OpenLogFileExecutor.DesktopNotSupported",
                        new Object [] {logFile}
                    )
                );
            }            
            SwingUtils.ShowDone();
        }catch(IOException e){
            log.error(e.getMessage());
            SwingUtils.ShowError(
                null,
                Context.getMessage("OpenLogFileExecutor.IOException",new Object [] {
                    logFile,e.getMessage()
                })
            );
        }        
    }
}
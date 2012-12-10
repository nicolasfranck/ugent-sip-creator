package gov.loc.repository.bagger.ui.handlers;

import java.awt.Dialog;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.exporters.Exporter;
import ugent.bagger.exporters.ExporterDSpaceMetsArchive;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.wizards.ExportWizardDialog;

public class ExportExecutor extends AbstractActionCommandExecutor {
 
    public ExportExecutor() {
        super();        
    }
   
    @Override
    public void execute() {        
        SwingUtils.ShowBusy();    
        
        ExportWizardDialog dialog = new ExportWizardDialog("ExportWizardDialog");        
        dialog.showDialog();
        
        /*
        ArrayList<File>files = new ArrayList<File>();
        int i = 0;
        for(File file:new File("/home/nicolas/bags").listFiles()){
            System.out.println("writing "+file+" to sip!");
            try{
                Exporter exporter = new ExporterDSpaceMetsArchive();
                exporter.export(
                    file,
                    new FileOutputStream(new File("/tmp/dspace-mets-"+i+".zip"))
                );
            }catch(Exception e){
                e.printStackTrace();
            }   
            i++;
        }        
        SwingUtils.ShowDone();*/
    }
}
package gov.loc.repository.bagger.ui.handlers;

import java.io.File;
import java.util.ArrayList;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import ugent.bagger.bagitmets.validation.BagitMetsValidator;
import ugent.bagger.exceptions.BagitMetsValidationException;
import ugent.bagger.helper.SwingUtils;

public class ExportExecutor extends AbstractActionCommandExecutor {
 
    public ExportExecutor() {
        super();        
    }
   
    @Override
    public void execute() {        
       SwingUtils.ShowBusy();
       ArrayList<File>files = new ArrayList<File>();
        for(File file:new File("/home/nicolas/bags").listFiles()){
            files.add(file);
        }
        BagitMetsValidator validator = new BagitMetsValidator();
        for(File file:files){
            try{
                ArrayList<BagitMetsValidationException>warnings = validator.validate(file);
                System.out.println("warnings.size = "+warnings.size());
                System.out.println("warnings: ");
                for(BagitMetsValidationException warning:warnings){
                    System.out.println(warning);
                }
            }catch(BagitMetsValidationException e){
                e.printStackTrace();
            }
        }
        SwingUtils.ShowDone();
    }
}
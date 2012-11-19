package ugent.bagger.workers;

import com.anearalone.mets.MdSec;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;

/**
 *
 * @author nicolas
 */
public class TaskAddMdSecFromFile extends DefaultWorker {    
    private File [] files;
    public TaskAddMdSecFromFile(File [] files){
        super();
        this.files = files;
    }
    @Override
    @SuppressWarnings("empty-statement")
    protected Void doInBackground() throws Exception {
        if(files == null){
            return null;
        }
        
        int succeeded = 0;
        int numErrors = 0;

        for(int i = 0;i<files.length;i++){                                                        
            File file = files[i];
            try{
                MdSec mdSec = MetsUtils.createMdSec(file);                        
                send(mdSec);                    
                succeeded++;
            }catch(IOException e){                  
                numErrors++;
                log(Context.getMessage("mdSecTable.addMdSec.IOException",new Object []{
                    file,e.getMessage()
                }));                    
            }catch(SAXException e){                  
                numErrors++;
                log(Context.getMessage("mdSecTable.addMdSec.SAXException",new Object []{
                    file,e.getMessage()
                })+"\n");                                                    
            }catch(ParserConfigurationException e){                  
                numErrors++;
                log(Context.getMessage("mdSecTable.addMdSec.ParserConfigurationException",new Object []{
                    e.getMessage()
                })+"\n");                    
            }catch(IllegalNamespaceException e){                
                numErrors++;
                log(Context.getMessage("mdSecTable.addMdSec.IllegalNamespaceException",new Object []{
                    file,e.getNamespace()
                })+"\n");
            }
            catch(NoNamespaceException e){                          
                numErrors++;                
                log(Context.getMessage("mdSecTable.addMdSec.noNamespaceException",new Object []{
                    file
                })+"\n");                                
            }                            
            int percent = (int)Math.floor( ((i+1) / ((float)files.length))*100);                                                                        
            setProgress(percent);                
        }             
        
        //report
        String report = Context.getMessage("report.message",new Integer []{
            succeeded,numErrors
        });
        String reportLog = Context.getMessage("report.log");

        SwingUtils.ShowMessage(null,report+"\n"+reportLog);
        
        if(succeeded > 0){
            success(true);
        }
        return null;
    }    
}
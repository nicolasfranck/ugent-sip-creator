package ugent.bagger.workers;

import com.anearalone.mets.MdSec;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.DtdNoFixFoundException;
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
    static final Log log = LogFactory.getLog(TaskAddMdSecFromFile.class);
    File [] files;
    int succeeded = 0;
    int numErrors = 0;
    
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

        for(int i = 0;i<files.length;i++){                                                        
            File file = files[i];
            
            String error = null;
            
            try{
                MdSec mdSec = MetsUtils.createMdSec(file);                        
                String namespace = mdSec.getMdWrap().getXmlData().get(0).getNamespaceURI();
                if(namespace == null || namespace.isEmpty()){
                    boolean proceed = SwingUtils.confirm(
                        null,
                        Context.getMessage("TaskAddMdSecFromFile.warnings.namespacemissing",new Object [] {
                            file.getAbsolutePath()                        
                        })
                    );                    
                    if(!proceed){                        
                        numErrors++;
                        continue;
                    }
                }
                send(mdSec);                    
                succeeded++;
            }catch(IOException e){                                  
                error = Context.getMessage("mdSecTable.addMdSec.IOException",new Object []{
                    file,e.getMessage()
                });                
            }catch(SAXException e){                                  
                error = Context.getMessage("mdSecTable.addMdSec.SAXException",new Object []{
                    file,e.getMessage()
                });                
            }catch(ParserConfigurationException e){                                  
                error = Context.getMessage("mdSecTable.addMdSec.ParserConfigurationException",new Object []{
                    e.getMessage()
                });
            }catch(IllegalNamespaceException e){                                
                error = Context.getMessage("mdSecTable.addMdSec.IllegalNamespaceException",new Object []{
                    file,e.getNamespace()
                });
            }
            catch(NoNamespaceException e){                                                 
                error = Context.getMessage("mdSecTable.addMdSec.NoNamespaceException",new Object []{
                    file
                });
            }catch(DtdNoFixFoundException e){                                    
                error = Context.getMessage("mdSecTable.addMdSec.DtdNoFixFoundException",new Object []{
                    file
                });
            }catch(Exception e){                                        
                error = Context.getMessage("mdSecTable.addMdSec.Exception",new Object []{
                    file
                });
            }
            
            if(error != null){
                numErrors++;
                SwingUtils.ShowError(null,error);
                log.error(error);
            }
            
            int percent = (int)Math.floor( ((i+1) / ((float)files.length))*100);                                                                        
            if(!isDone()){
                setProgress(percent); 
            }            
        }    
        
        if(succeeded > 0){
            success(true);
        }
        return null;
    }    
    @Override
    public void done(){
        super.done();
        //report
        String report = Context.getMessage("report.message",new Integer []{
            files.length,succeeded,numErrors
        });
        String reportLog = Context.getMessage("report.log");
        SwingUtils.ShowMessage(null,report+"\n"+reportLog);
    }
}
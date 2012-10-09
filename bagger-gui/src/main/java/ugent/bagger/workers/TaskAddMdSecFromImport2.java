package ugent.bagger.workers;

import com.anearalone.mets.MdSec;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.importers.ImportType;
import ugent.bagger.importers.Importer;
import ugent.bagger.importers.ImporterFactory;

/**
 *
 * @author nicolas
 */
public class TaskAddMdSecFromImport2 extends DefaultWorker {    
    private File [] files;
    private ImportType importType;
    public TaskAddMdSecFromImport2(File [] files,ImportType importType){
        this.files = files;
        this.importType = importType;
    }
    @Override
    protected Void doInBackground() throws Exception {      
        int succeeded = 0;

        for(int i = 0;i<files.length;i++){
            File file = files[i];               
            try{
                Importer importer = ImporterFactory.createImporter(file,importType);
                if(importer == null){
                    System.out.println("no importer found for "+file);
                    continue;
                }
                System.out.println("importing from file "+file);
                Document doc = importer.performImport(file);
                if(doc == null){
                    System.out.println("doc creation failed "+file);
                    continue;
                }

                MdSec mdSec = MetsUtils.createMdSec(doc);                        
                
                send(mdSec);
                
                succeeded++;
            }catch(IOException e){                                      
                log(Context.getMessage("mdSecTable.addMdSec.IOException",new Object []{
                    file,e.getMessage()
                })+"\n");                
            }catch(SAXException e){                                                
                log(ugent.bagger.helper.Context.getMessage("mdSecTable.addMdSec.SAXException",new Object []{
                    file,e.getMessage()
                })+"\n");                                                
            }catch(IllegalNamespaceException e){                
                log(ugent.bagger.helper.Context.getMessage("mdSecTable.addMdSec.IllegalNamespaceException",new Object []{
                    file,e.getNamespace()
                })+"\n");                
            }
            catch(NoNamespaceException e){                                               
                log(Context.getMessage("mdSecTable.addMdSec.noNamespaceException",new Object []{
                    file
                })+"\n");                    
            }                        
            int percent = (int)Math.floor( ((i+1) / ((float)files.length))*100);                                                                        
            setProgress(percent); 
        }
        if(succeeded > 0){
            success(true);
        }
        return null;
    }
}
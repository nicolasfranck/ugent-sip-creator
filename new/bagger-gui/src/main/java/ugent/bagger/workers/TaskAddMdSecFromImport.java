/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.workers;

import com.anearalone.mets.MdSec;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.importers.Importer;
import ugent.bagger.importers.ImporterFactory;

/**
 *
 * @author nicolas
 */
public class TaskAddMdSecFromImport extends DefaultWorker {
    @Override
    protected Void doInBackground() throws Exception {
        File [] files = ugent.bagger.helper.SwingUtils.chooseFiles(
            "Select file",
            null,
            JFileChooser.FILES_ONLY,
            true
        );
        int succeeded = 0;

        for(int i = 0;i<files.length;i++){
            File file = files[i];               
            try{
                Importer importer = ImporterFactory.createImporter(file);
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